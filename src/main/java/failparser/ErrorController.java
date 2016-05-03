package failparser;

import java.io.File;
import java.io.FileFilter;
import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;

import org.apache.commons.io.FileUtils;
import org.apache.commons.io.filefilter.FileFilterUtils;
import org.apache.commons.io.filefilter.TrueFileFilter;
import org.apache.commons.lang3.ArrayUtils;
import org.apache.commons.math3.stat.descriptive.moment.Mean;
import org.apache.commons.math3.stat.descriptive.moment.Variance;
import org.apache.commons.math3.stat.inference.OneWayAnova;
import org.apache.log4j.LogManager;
import org.apache.log4j.Logger;

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
	static boolean doANOVA = false;
	static boolean doClassification = true;
	static Logger logger = Logger.getLogger(ErrorController.class);
	
	public static void main(String[] args) {
		logger.info("--------------------------------------------");
		logger.info("Starting Run");
		
		
		//get input directory
		String dirPath = null;
		
		if (args.length > 0) {	
			dirPath = args[0];
		}
		else{
	        System.err.println("First argument must be directory.");
	        System.exit(1);
	    }
		
		//initialize Spark
		String appName = "Failing With Style";
		String master = "local";
		SparkConf conf = new SparkConf().setAppName(appName).setMaster(master);
		JavaSparkContext sc = new JavaSparkContext(conf);
		
		//Get directory object and compute # of authors
		File dir = new File(dirPath);		
		int author_count  = dir.listFiles(new FileFilter() {
		    public boolean accept(File f) {
		        return f.isDirectory();
		    }
		}).length;
		
		//Find all the Java files (recursively) in the input directory. These are the samples.
		Collection<File> filescol = FileUtils.listFiles(dir,
                FileFilterUtils.suffixFileFilter(".java"), TrueFileFilter.INSTANCE);
		List<File> files = new ArrayList<File>(filescol);
		logger.info("Total samples: " + files.size());
		//Convert to Spark RDD
		JavaRDD<File> samples = sc.parallelize(files);
		
		//Create author profiles for each sample
		JavaRDD<AuthorProfile> res = samples.map(new Function<File, AuthorProfile>(){
			public AuthorProfile call(File sample){
				logger.info("-----------------------------");
				FeatureExtractor mp = new FeatureExtractor();
				AuthorProfile res = mp.extractFeatures(sample);
				if (res != null){
					//Get Author ID (from parent directory name)
					try{	
						res.author = Integer.parseInt(res.sourceFile.getParentFile().getName());
					} catch (Exception e){
						e.printStackTrace();
					}
					res.calcFeatureMap();
				}
				logger.info("-----------------------------");
				return res;
			}
		});
		
		List<AuthorProfile> profList = res.collect();

		//Run Analysis of Variance
		if (doANOVA){
			//What categories to analyze
			String[] categories = {"try_count", "if_count", "error_loc_ratio",
								   "avg_catches", "avg_size", "exception_types", "throw_count_ratio",
								   "method_throw_ratio", "finally_ratio","rethrow_ratio", "return_ratio",
								   "print_msg_ratio", "print_stack_ratio", "exit_ratio", "no_handle_ratio",
								   "catch_comment_avg", "avg_try"};
			//String[] categories = {"if_count"};
			for (String cat : categories){
				//create a new map, with author -> double array
				Map<Integer,List<Double>> data = new HashMap<Integer,List<Double>>();
				for (AuthorProfile ap : profList){
					if (!data.containsKey(ap.author)){
						data.put(ap.author, new ArrayList<Double>());
					}
					data.get(ap.author).add(ap.featureMap.get(cat));
				}

				//convert to collection of double[]
				List<double[]> parsed = new ArrayList<double[]>();
				for (List<Double> d : data.values()){
					parsed.add(ArrayUtils.toPrimitive(d.toArray(new Double[d.size()])));
				}
				
				//run ANOVA
				OneWayAnova owa = new OneWayAnova();
				double fval = owa.anovaFValue(parsed);
				logger.info("FVal for " + cat + ": " + fval);
				double pval = owa.anovaPValue(parsed);
				logger.info("PVal for " + cat + ": " + pval);
				//compute mean and variance for each author
				/*for (int i = 0; i<parsed.size(); i++){
					double[] auth = parsed.get(i);
					Mean m = new Mean();
					double avg = m.evaluate(auth);
					Variance v = new Variance();
					double var = v.evaluate(auth, avg);
					//System.out.println(i + ", " + avg + ", " + var);
					System.out.println(var);
				}*/
			}
		}
		
		//run training/classification
		if (doClassification){
			// convert to LabelledPoint RDD for LogisticRegression
			JavaRDD<LabeledPoint> inp = res.map(new Function<AuthorProfile, LabeledPoint>(){
				public LabeledPoint call(AuthorProfile prof){
					LabeledPoint lp = new LabeledPoint(prof.author, prof.getVector());
					return lp;
				}
			});
			
			// Adapted from Spark tutorial:
			// http://spark.apache.org/docs/latest/mllib-linear-methods.html#logistic-regression
			
			// Split initial RDD into two... [90% training data, 10% testing data].
			JavaRDD<LabeledPoint>[] splits = inp.randomSplit(new double[] {0.9, 0.1}, 11L);
			JavaRDD<LabeledPoint> training = splits[0].cache();
			JavaRDD<LabeledPoint> test = splits[1];
			
			// Train the model
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
		    logger.info("author count = " + author_count);
		    logger.info("Precision = " + precision);
		    logger.info("Recall = " + metrics.recall());			
			
			
		}
		sc.close();
		logger.info("--------------------------------------------");
	}

}
