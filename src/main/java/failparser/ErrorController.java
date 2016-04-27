package failparser;

import java.io.File;
import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.commons.io.FileUtils;
import org.apache.commons.io.filefilter.FileFilterUtils;
import org.apache.commons.io.filefilter.TrueFileFilter;
import org.apache.commons.lang3.ArrayUtils;
import org.apache.commons.math3.stat.inference.OneWayAnova;

import failparser.FeatureExtractor.FeatureExtractorVisitor;
import scala.Tuple2;

import org.apache.spark.api.java.JavaSparkContext;
import org.apache.spark.api.java.function.Function;
import org.apache.spark.api.java.JavaRDD;
import org.apache.spark.SparkConf;
import org.apache.spark.SparkContext;
import org.apache.spark.mllib.classification.LogisticRegressionModel;
import org.apache.spark.mllib.classification.LogisticRegressionWithLBFGS;
import org.apache.spark.mllib.evaluation.MulticlassMetrics;
import org.apache.spark.mllib.linalg.Vectors;
import org.apache.spark.mllib.regression.LabeledPoint;

import breeze.optimize.flow.LPMaxFlow;

public class ErrorController {
	static final boolean train = true;
	static boolean doANOVA = true;
	static boolean doClassification = true;
	//static String dir = "/Users/cdarmetk/columbia/ASE/project/dataset_sm"
	
	public static void main(String[] args) {
		
		
		//get input directory
		String dirPath = null;
		
		if (args.length > 0) {	
			dirPath = args[0];
		}
		else{
	        System.err.println("First argument must be directory.");
	        System.exit(1);
	    }
		//get options
		String appName = "Failing With Style";
		String master = "local";
		SparkConf conf = new SparkConf().setAppName(appName).setMaster(master);
		JavaSparkContext sc = new JavaSparkContext(conf);
		//JavaSparkContext jsc = new JavaSparkContext(conf);
		
		//FileUtils.toFile(new URL(dir))
		
		File dir = new File(dirPath);
		int author_count = dir.list().length;
		
		Collection<File> filescol = FileUtils.listFiles(dir,
                FileFilterUtils.suffixFileFilter(".java"), TrueFileFilter.INSTANCE);
		List<File> files = new ArrayList<File>(filescol);
		JavaRDD<File> samples = sc.parallelize(files);
		
		JavaRDD<AuthorProfile> res = samples.map(new Function<File, AuthorProfile>(){
			public AuthorProfile call(File sample){
				//extract features on dirs
				FeatureExtractor mp = new FeatureExtractor();
				AuthorProfile res = mp.extractFeatures(sample);
				if (res != null){
					if (train){
						try{
							res.author = Integer.parseInt(res.sourceFile.getParentFile().getName());
							System.out.println("Author:"+res.author);
						} catch (Exception e){
							e.printStackTrace();
						}
					}
					res.calcFeatureVector();
				}
				return res;
			}
		});
		
		List<AuthorProfile> profList = res.collect();

		
		//run training/classification
		if (doANOVA){
			//for each category
			String[] categories = {"try_count", "if_count", "extype_count"};
			for (String cat : categories){
				//create a new map, with author -> double array
				Map<Integer,List<Double>> data = new HashMap<Integer,List<Double>>();
				for (AuthorProfile ap : profList){
					if (!data.containsKey(ap.author)){
						data.put(ap.author, new ArrayList<Double>());
					}
					data.get(ap.author).add(ap.featureVector.get(cat));
				}

				//convert to collection of double[]
				data.values();
				
				Collection<double[]> parsed = new ArrayList<double[]>();
				
				for (List<Double> d : data.values()){
					parsed.add(ArrayUtils.toPrimitive(d.toArray(new Double[d.size()])));
				}
				//run ANOVA
				OneWayAnova owa = new OneWayAnova();
				double fval = owa.anovaFValue(parsed);
				System.out.println("FVal for " + cat + ": " + fval);
				double pval = owa.anovaPValue(parsed);
				System.out.println("PVal for " + cat + ": " + pval);
			}
		}
		if (doClassification){
			JavaRDD<LabeledPoint> inp = res.map(new Function<AuthorProfile, LabeledPoint>(){
				public LabeledPoint call(AuthorProfile prof){
					LabeledPoint lp = new LabeledPoint(prof.author, Vectors.dense(prof.trycnt, prof.ifcnt, prof.exceptions.size()));
					return lp;
				}
			});
			
			// Split initial RDD into two... [90% training data, 10% testing data].
			JavaRDD<LabeledPoint>[] splits = inp.randomSplit(new double[] {0.9, 0.1}, 11L);
			JavaRDD<LabeledPoint> training = splits[0].cache();
			JavaRDD<LabeledPoint> test = splits[1];
			
			final LogisticRegressionModel model = new LogisticRegressionWithLBFGS()
				      .setNumClasses(author_count)
				      .run(training.rdd());
			
			// Compute raw scores on the test set.
		    JavaRDD<Tuple2<Object, Object>> predictionAndLabels = test.map(
		      new Function<LabeledPoint, Tuple2<Object, Object>>() {
		        public Tuple2<Object, Object> call(LabeledPoint p) {
		          Double prediction = model.predict(p.features());
		          return new Tuple2<Object, Object>(prediction, p.label());
		        }
		      }
		    );

		    // Get evaluation metrics.
		    MulticlassMetrics metrics = new MulticlassMetrics(predictionAndLabels.rdd());
		    double precision = metrics.precision();
		    System.out.println("Precision = " + precision);

		    // Save and load model
		    //model.save(sc, "myModelPath");
		    //LogisticRegressionModel sameModel = LogisticRegressionModel.load(sc, "myModelPath");
			
			
			
		}
		sc.close();
		//output results
	}

}
