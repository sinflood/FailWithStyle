package failparser;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

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

	        try {
	            // parse the file
	            cu = JavaParser.parse(in);
	        } catch (ParseException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			} finally {
	            try {
					in.close();
				} catch (IOException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
	        }
		} catch (FileNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
        		//"/Users/cdarmetk/Documents/workspace/FailingWithStyle/src/main/java/failparser/TestClassBasic.java");

		if (cu != null){
			res.LoC = cu.getEndLine() - cu.getBeginLine();
		}
		else{
			System.out.println("CU NULL: " +res.sourceFile.getPath());
		}
		
        //Counter ctr = new Counter();
        // visit and print the methods names
        fv = new FeatureExtractorVisitor(res, cu);
        fv.visit(cu, null);
        System.out.println("-----------------------------");
        System.out.println("LoC:" + (cu.getEndLine() - cu.getBeginLine())); //x
        System.out.println("Error _Handling_ LoC:" + getSum(res.sizeList)); //x
        System.out.println("Error handling to LoC ratio:" + (getSum(res.sizeList)/(cu.getEndLine() - cu.getBeginLine()))); //
        System.out.println("Trys:" + res.trycnt); //
        System.out.println("Avg handling LoC Size:" + getAverage(res.sizeList)); //
        System.out.println("Avg Catches:" + getAverage(res.catchesList)); //
        System.out.println("Avg Catch comments:" + getAverage(res.catchCommentSzList));
        System.out.println("depth:" + res.depthList); //x
        System.out.println("Avg depth:" + getAverage(res.depthList)); //x
        System.out.println("Finallys:" + res.fincnt);//
        System.out.println("rethrow:" + res.handleRethrow);//
        System.out.println("return:" + res.handleReturn);//
        System.out.println("printmsg:" + res.handlePrintMsg);//
        System.out.println("printstack:" + res.handlePrintStack);//
        System.out.println("exit:" + res.handleExit);//
        System.out.println("Doesn't Handle error:" + res.noHandling);//
        System.out.println("Ifs:" + res.ifcnt); //
        System.out.println("Throws:" + res.throwcnt); //
        System.out.println("Method Throws:" + res.methodThrows); //
        System.out.println("Exception types:" + res.exceptions.toString()); //
        System.out.println("-----------------------------");
        
        
        
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
            // here you can access the attributes of the method.
            // this method will be called for all methods in this 
            // CompilationUnit, including inner class methods
        	prof.trycnt++;
        	Node parent = n.getParentNode();
        	int depth = 0;
        	while (parent != null){
        		//System.out.println("parent:" + parent.getClass());
        		//if (parent.getClass() != CompilationUnit){
        	    depth++;
        		//}
        		parent = parent.getParentNode();
        	}
        	prof.depthList.add(depth);
        	//System.out.println("Line:" + n.toStringWithoutComments());
        	int catches = n.getCatchs().size();
        	prof.catchesList.add(catches);
        	for (CatchClause c : n.getCatchs()){
        		c.getAllContainedComments().size();
        		prof.catchCommentSzList.add(c.getAllContainedComments().size());
        		/*System.out.println("catchblock len:" + (c.getEndLine() - c.getBeginLine()));
        		System.out.println("except:" + c.getExcept());
        		System.out.println("exceptcl:" + c.getExcept().getClass());*/
        		MultiTypeParameter e = c.getExcept();
        		/*System.out.println("id:" + e.getId());
        		System.out.println("mod:" + e.getModifiers());
        		System.out.println("type:" + e.getTypes());*/
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
        				//System.out.println("cLine:" + s.toStringWithoutComments());
        				//System.out.println(s.getClass());
        				if (s instanceof ThrowStmt){
        					prof.handleRethrow++;
        				}
        				else if (s instanceof ReturnStmt){
        					prof.handleReturn++;
        				}
        				else if (s instanceof ExpressionStmt){
        					//system.exit, printStackTrace, system.out.print
        					ExpressionStmt es = (ExpressionStmt)s;
        					Expression exp = es.getExpression();
        					if (exp instanceof MethodCallExpr){
        						MethodCallExpr mexp = (MethodCallExpr) exp;
            					//System.out.println("mexp:" + mexp.getName());
            					//System.out.println("mexp expr:" + mexp.getNameExpr());
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
        			//System.out.println("nohandling:" + c.toString());
        			prof.noHandling++;
        		}
        		//System.out.println("catchblock:" + c.getCatchBlock());
        		//System.out.println("catchblockcl:" + c.getCatchBlock().getClass());
        	}
        	
        	
        	

        	
        	BlockStmt fin = n.getFinallyBlock();
        	if (fin != null){
        		//System.out.println("Fin:" + fin.toStringWithoutComments());
        		prof.fincnt++;
        	}
        	/*
        	n.getChildrenNodes();
        	n.getThrows();
            System.out.println(n.getName());
            BlockStmt body = n.getBody();
            for (Statement s : body.getStmts()){
            	if (s instanceof TryStmt){
            		
            	}
            	System.out.println("Line:" + s.toStringWithoutComments());
            	System.out.println("Type:" + s.getClass().toString());
            	
            }*/
            //System.out.println(n.getBody());
            super.visit(n, arg);
        }
        @Override
        public void visit(IfStmt n, Object arg) {

        	
        	Expression cond = n.getCondition();
        	Statement th = n.getThenStmt();
        	Statement el = n.getElseStmt();
        	
        	/*System.out.println("Line:" + n.toStringWithoutComments());
        	System.out.println("if class:" + cond.getClass());
        	System.out.println("else:" + el.toString());
        	System.out.println("then:" + th.toString());*/
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
        	//System.out.println("Line:" + n.toStringWithoutComments());
        	Expression exp = n.getExpr();
        	if (exp instanceof ObjectCreationExpr){
        		ObjectCreationExpr o = (ObjectCreationExpr) exp;
        		prof.exceptions.add(o.getType().toStringWithoutComments());
        		/*System.out.println("args:" + o.getArgs());
        		System.out.println("type:" + o.getType());
        		System.out.println("scope:" + o.getScope());
        		System.out.println("type args:" + o.getTypeArgs());*/
        	}
        	//System.out.println("class:" + n.getExpr().getClass());
            super.visit(n, arg);
        }
        
        @Override
        public void visit(MethodDeclaration n, Object arg) {
            // here you can access the attributes of the method.
            // this method will be called for all methods in this 
            // CompilationUnit, including inner class methods
            List<NameExpr> thr = n.getThrows();
            if (thr != null && thr.size() > 0){
            	prof.methodThrows++;
            }
            for (NameExpr ex : thr){
            	prof.exceptions.add(ex.getName());
            }
            //System.out.println(n.getBody());
            super.visit(n, arg);
        }
    }
        
        /**
         * Simple visitor implementation for visiting MethodDeclaration nodes. 
         */
        @SuppressWarnings("rawtypes")
    	private static class MethodVisitor extends VoidVisitorAdapter {

            @Override
            public void visit(MethodDeclaration n, Object arg) {
                // here you can access the attributes of the method.
                // this method will be called for all methods in this 
                // CompilationUnit, including inner class methods
            	n.getChildrenNodes();
            	n.getThrows();
                //System.out.println(n.getName());
                BlockStmt body = n.getBody();
                for (Statement s : body.getStmts()){
                	if (s instanceof TryStmt){
                		
                	}
                	//System.out.println("Line:" + s.toStringWithoutComments());
                	//System.out.println("Type:" + s.getClass().toString());
                	
                }
                //System.out.println(n.getBody());
                super.visit(n, arg);
            }
       }
}