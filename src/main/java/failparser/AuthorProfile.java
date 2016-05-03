package failparser;

import java.io.File;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.apache.spark.mllib.linalg.Vector;
import org.apache.spark.mllib.linalg.Vectors;

public class AuthorProfile implements Serializable {
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	File sourceFile;
	Map<String, Double> featureMap;
	int author;
	
	 int trycnt = 0;
	 List<Integer> catchesList = new ArrayList<Integer>();
	 List<Integer> catchCommentSzList = new ArrayList<Integer>();
	 List<Integer> sizeList = new ArrayList<Integer>();
	 List<Integer> tryList = new ArrayList<Integer>();
	 List<Integer> depthList = new ArrayList<Integer>();
	 int noHandling = 0;
	 int handleRethrow = 0;
	 int handleReturn = 0;
	 int handlePrintStack = 0;
	 int handlePrintMsg = 0;
	 int handleExit = 0;
	 int fincnt = 0;
	
	 int ifcnt = 0;
	
	 int throwcnt = 0;
	 Set<String> exceptions = new HashSet<String>();
	
	 int methodThrows = 0;
	 
	 int LoC = 0;
	 
		public static double getAverage(List<Integer> vals){
			if (vals.size() == 0){
				return 0.0;
			}
			else{
				return getSum(vals)/vals.size();
			}
		}
		public static double getSum(List<Integer> vals){
			double tot = 0.0;
			for (int i : vals){
				tot += i;
			}
			return tot;
		}
		
	 public double getAvgCatches(){
		 return getAverage(this.catchesList);
	 }
	 
	 public double getAvgHandlingSize(){
		 return getAverage(this.sizeList);
	 }
	 
	 public double getErrorLoCRatio(){
		 return getSum(this.sizeList)/LoC;
	 }
	 
	 public Vector getVector(){
		Vector v = Vectors.dense(trycnt, exceptions.size(),
				 getAvgCatches(), getAvgHandlingSize(), getRatio(noHandling, trycnt),
				 ((double)methodThrows)/LoC, getRatio(handleRethrow, trycnt),
				  getRatio(handlePrintMsg, trycnt), getRatio(handlePrintStack, trycnt),
				 getRatio(handleExit, trycnt), getAverage(catchCommentSzList), getAverage(tryList));
		return v;
	 }
	 
	 public double getRatio(double num, double denom){
		 double ret = 0.0;
		 if (denom > 0){
			 ret = ((double)num)/denom;
		 }
		 return ret;
	 }
	 
	 public double if_exists(double num){
		 if (Double.isNaN(num) || num > 0){
			 return 1;
		 }
		 return 0;
	 }
	 
	 public void calcFeatureMap(){
		 if (featureMap == null){
			 featureMap = new HashMap<String, Double>();
		 }
		 featureMap.put("try_count", (double)trycnt);
		 featureMap.put("if_count", (double)ifcnt);
		 featureMap.put("exception_types", (double)exceptions.size());
		 featureMap.put("error_loc_ratio", getErrorLoCRatio());
		 featureMap.put("avg_catches", getAvgCatches());
		 featureMap.put("avg_size", getAvgHandlingSize());
		 featureMap.put("avg_try", getAverage(tryList));
		 featureMap.put("throw_count_ratio", ((double)throwcnt)/LoC);
		 featureMap.put("method_throw_ratio", ((double)methodThrows)/LoC);
		 featureMap.put("finally_ratio", if_exists(fincnt));
		 featureMap.put("rethrow_ratio", getRatio(handleRethrow, trycnt));
		 featureMap.put("return_ratio", getRatio(handleReturn, trycnt));
		 featureMap.put("print_msg_ratio", getRatio(handlePrintMsg, trycnt));
		 featureMap.put("print_stack_ratio", getRatio(handlePrintStack, trycnt));
		 featureMap.put("exit_ratio", getRatio(handleExit, trycnt));
		 featureMap.put("no_handle_ratio", getRatio(noHandling, trycnt));
		 featureMap.put("catch_comment_avg", getAverage(catchCommentSzList));
	 }
	
}
