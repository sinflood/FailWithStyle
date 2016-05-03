package failparser;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import org.apache.log4j.LogManager;
import org.apache.log4j.Logger;

import com.github.javaparser.JavaParser;
import com.github.javaparser.ParseException;
import com.github.javaparser.ast.CompilationUnit;
import com.github.javaparser.ast.Node;
import com.github.javaparser.ast.body.MethodDeclaration;
import com.github.javaparser.ast.body.MultiTypeParameter;
import com.github.javaparser.ast.expr.BinaryExpr;
import com.github.javaparser.ast.expr.Expression;
import com.github.javaparser.ast.expr.MethodCallExpr;
import com.github.javaparser.ast.expr.NameExpr;
import com.github.javaparser.ast.expr.NullLiteralExpr;
import com.github.javaparser.ast.expr.ObjectCreationExpr;
import com.github.javaparser.ast.stmt.BlockStmt;
import com.github.javaparser.ast.stmt.CatchClause;
import com.github.javaparser.ast.stmt.ExpressionStmt;
import com.github.javaparser.ast.stmt.IfStmt;
import com.github.javaparser.ast.stmt.ReturnStmt;
import com.github.javaparser.ast.stmt.Statement;
import com.github.javaparser.ast.stmt.ThrowStmt;
import com.github.javaparser.ast.stmt.TryStmt;
import com.github.javaparser.ast.type.Type;
import com.github.javaparser.ast.visitor.VoidVisitorAdapter;

public class FeatureExtractor {
	
	public FeatureExtractorVisitor fv;
	
	static Logger logger = Logger.getLogger(FeatureExtractor.class);
	
	public static double getAverage(List<Integer> vals){
		return getSum(vals)/vals.size();
	}
	public static double getSum(List<Integer> vals){
		double tot = 0.0;
		for (int i : vals){
			tot += i;
		}
		return tot;
	}
	
	public AuthorProfile extractFeatures(File featureFile){
		// creates an input stream for the file to be parsed
		AuthorProfile res = new AuthorProfile();
		res.sourceFile = featureFile;
        FileInputStream in;
        CompilationUnit cu = null;
		try {
			in = new FileInputStream(featureFile);
			
            // parse the file
	        cu = JavaParser.parse(in);
			in.close();
	        
		} catch (Exception e) {
			e.printStackTrace();
		}
		
		if (cu != null){
			//record total LoC
			res.LoC = cu.getEndLine() - cu.getBeginLine();
		}
		else{
			logger.warn("CU is NULL: " + res.sourceFile.getPath());
		}
		
        fv = new FeatureExtractorVisitor(res, cu);
        fv.visit(cu, null);
        /*logger.info("LoC:" + (cu.getEndLine() - cu.getBeginLine())); //x
        logger.info("Error _Handling_ LoC:" + getSum(res.sizeList)); //x
        logger.info("Error handling to LoC ratio:" + (getSum(res.sizeList)/(cu.getEndLine() - cu.getBeginLine()))); //*/
        logger.info("Trys:" + res.trycnt); //
        /*logger.info("Avg handling LoC Size:" + getAverage(res.sizeList)); //
        logger.info("Avg Catches:" + getAverage(res.catchesList)); //
        logger.info("Avg Catch comments:" + getAverage(res.catchCommentSzList));
        logger.info("depth:" + res.depthList); //x
        logger.info("Avg depth:" + getAverage(res.depthList)); //x
        logger.info("Finallys:" + res.fincnt);//
        logger.info("rethrow:" + res.handleRethrow);//
        logger.info("return:" + res.handleReturn);//
        logger.info("printmsg:" + res.handlePrintMsg);//
        logger.info("printstack:" + res.handlePrintStack);//
        logger.info("exit:" + res.handleExit);//
        logger.info("Doesn't Handle error:" + res.noHandling);//
        logger.info("Ifs:" + res.ifcnt); //
        logger.info("Throws:" + res.throwcnt); //
        logger.info("Method Throws:" + res.methodThrows); //
        logger.info("Exception types:" + res.exceptions.toString()); //*/    

        return res;
	}
	
    
	public static void main(String[] args) throws Exception {
        // creates an input stream for the file to be parsed
        FileInputStream in = new FileInputStream("/Users/cdarmetk/Documents/workspace/FailingWithStyle/src/main/java/failparser/TestClassBasic.java");

        CompilationUnit cu;
        try {
            // parse the file
            cu = JavaParser.parse(in);
        } finally {
            in.close();
        }

        //Counter ctr = new Counter();
        // visit and print the methods names
        AuthorProfile prof = new AuthorProfile();
        
        prof.LoC = cu.getEndLine() - cu.getBeginLine();
        FeatureExtractorVisitor tr = new FeatureExtractorVisitor(prof, cu);
        tr.visit(cu, null);
        System.out.println("LoC:" + (cu.getEndLine() - cu.getBeginLine()));
        System.out.println("Error _Handling_ LoC:" + getSum(prof.sizeList));
        System.out.println("Error handling to LoC ratio:" + (getSum(prof.sizeList)/(cu.getEndLine() - cu.getBeginLine())));
        System.out.println("Trys:" + prof.trycnt);
        System.out.println("Avg handling LoC Size:" + getAverage(prof.sizeList));
        System.out.println("Avg Catches:" + getAverage(prof.catchesList));
        System.out.println("Avg Catch comments:" + getAverage(prof.catchCommentSzList));
        System.out.println("depth:" + prof.depthList);
        System.out.println("Avg depth:" + getAverage(prof.depthList));
        System.out.println("Finallys:" + prof.fincnt);
        System.out.println("rethrow:" + prof.handleRethrow);
        System.out.println("return:" + prof.handleReturn);
        System.out.println("printmsg:" + prof.handlePrintMsg);
        System.out.println("printstack:" + prof.handlePrintStack);
        System.out.println("exit:" + prof.handleExit);
        System.out.println("Doesn't Handle error:" + prof.noHandling);
        System.out.println("Ifs:" + prof.ifcnt);
        System.out.println("Throws:" + prof.throwcnt);
        System.out.println("Method Throws:" + prof.methodThrows);
        System.out.println("Exception types:" + prof.exceptions.toString());
    }


    @SuppressWarnings("rawtypes")
	public static class FeatureExtractorVisitor extends VoidVisitorAdapter {

		
		AuthorProfile prof;
		CompilationUnit cu;
		
		public FeatureExtractorVisitor(AuthorProfile ap, CompilationUnit c){
			prof = ap;
			cu = c;
		}

		
        @Override
        public void visit(TryStmt n, Object arg) {
        	prof.trycnt++;
        	Node parent = n.getParentNode();
        	int depth = 0;
        	while (parent != null){
        	    depth++;
        		parent = parent.getParentNode();
        	}
        	prof.depthList.add(depth);
        	int catches = n.getCatchs().size();
        	prof.catchesList.add(catches);
        	BlockStmt tryblock = n.getTryBlock();
        	if (tryblock != null){
        		prof.tryList.add(tryblock.getEndLine() -tryblock.getBeginLine());
        	}
        	for (CatchClause c : n.getCatchs()){
        		c.getAllContainedComments().size();
        		prof.catchCommentSzList.add(c.getAllContainedComments().size());
        		MultiTypeParameter e = c.getExcept();
        		for (Type t : e.getTypes()){
        			prof.exceptions.add(t.toStringWithoutComments());
        		}
        		
        		prof.sizeList.add(c.getCatchBlock().getEndLine() - c.getCatchBlock().getBeginLine());
        		List<Statement> sts = c.getCatchBlock().getStmts();
        		if (sts != null){
        			if (sts.size() == 0){
        				prof.noHandling++;
        			}
        			for (Statement s : sts){
        				if (s instanceof ThrowStmt){
        					prof.handleRethrow++;
        				}
        				else if (s instanceof ReturnStmt){
        					prof.handleReturn++;
        				}
        				else if (s instanceof ExpressionStmt){
        					ExpressionStmt es = (ExpressionStmt)s;
        					Expression exp = es.getExpression();
        					if (exp instanceof MethodCallExpr){
        						MethodCallExpr mexp = (MethodCallExpr) exp;
            					if (mexp.getName().equals("println")){
            						prof.handlePrintMsg++;
            					}
            					else if (mexp.getName().equals("printStackTrace")){
            						prof.handlePrintStack++;
            					}
            					else if (mexp.getName().equals("exit")){
            						prof.handleExit++;
            					}
        					}

        				}

        			}
        		}
        		else{
        			prof.noHandling++;
        		}
        	}
        	
        	BlockStmt fin = n.getFinallyBlock();
        	if (fin != null){
        		prof.fincnt++;
        	}
            super.visit(n, arg);
        }
        @Override
        public void visit(IfStmt n, Object arg) {

        	
        	Expression cond = n.getCondition();
        	Statement th = n.getThenStmt();
        	Statement el = n.getElseStmt();

        	if (cond instanceof BinaryExpr){
        		BinaryExpr bcond = (BinaryExpr) cond;
        		if (bcond.getRight() instanceof NullLiteralExpr || bcond.getLeft() instanceof NullLiteralExpr){
        			prof.ifcnt++;
        			if (bcond.getOperator() == BinaryExpr.Operator.notEquals){
        				// means 'else' section is error handling
        				if (n.getElseStmt() != null){
        					prof.sizeList.add(n.getElseStmt().getEndLine() - n.getElseStmt().getBeginLine());
        				}
        				else{
        					prof.sizeList.add(0);
        					//TODO also add this to error handling style for not handled
        				}
        				
        				
        			}
        			else if(bcond.getOperator() == BinaryExpr.Operator.equals){
        				//means 'then' section is error handling
        				prof.sizeList.add(n.getThenStmt().getEndLine() - n.getThenStmt().getBeginLine());
        			}
        		}
        	}
        	
        	
            super.visit(n, arg);
        }
        @Override
        public void visit(ThrowStmt n, Object arg) {
        	prof.throwcnt++;
        	Expression exp = n.getExpr();
        	if (exp instanceof ObjectCreationExpr){
        		ObjectCreationExpr o = (ObjectCreationExpr) exp;
        		prof.exceptions.add(o.getType().toStringWithoutComments());
        	}
            super.visit(n, arg);
        }
        
        @Override
        public void visit(MethodDeclaration n, Object arg) {
            List<NameExpr> thr = n.getThrows();
            if (thr != null && thr.size() > 0){
            	prof.methodThrows++;
            }
            for (NameExpr ex : thr){
            	prof.exceptions.add(ex.getName());
            }
            super.visit(n, arg);
        }
    }
}