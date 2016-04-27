package failparser;

import java.io.File;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

public class AuthorProfile implements Serializable {
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	File sourceFile;
	Map<String, Double> featureVector;
	int author;
	
	 int trycnt = 0;
	 List<Integer> catchesList = new ArrayList<Integer>();
	 List<Integer> catchCommentSzList = new ArrayList<Integer>();
	 List<Integer> sizeList = new ArrayList<Integer>();
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
	 
	 
	 public void calcFeatureVector(){
		 if (featureVector == null){
			 featureVector = new HashMap<String, Double>();
		 }
		 featureVector.put("try_count", (double)trycnt);
		 featureVector.put("if_count", (double)ifcnt);
		 featureVector.put("extype_count", (double)exceptions.size());
	 }
	
}
