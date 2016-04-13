package failparser;

import java.io.FileInputStream;
import java.util.ArrayList;
import java.util.List;

import com.github.javaparser.JavaParser;
import com.github.javaparser.ast.CompilationUnit;
import com.github.javaparser.ast.body.MethodDeclaration;
import com.github.javaparser.ast.expr.Expression;
import com.github.javaparser.ast.expr.ObjectCreationExpr;
import com.github.javaparser.ast.stmt.BlockStmt;
import com.github.javaparser.ast.stmt.IfStmt;
import com.github.javaparser.ast.stmt.Statement;
import com.github.javaparser.ast.stmt.ThrowStmt;
import com.github.javaparser.ast.stmt.TryStmt;
import com.github.javaparser.ast.visitor.VoidVisitorAdapter;

public class MethodPrinter {

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
        TryVisitor tr = new TryVisitor();
        tr.visit(cu, null);
        System.out.println("Trys:" + TryVisitor.trycnt);
        System.out.println("Avg Size:" + TryVisitor.getAverageTrySize());
        System.out.println("Avg Catches:" + TryVisitor.getAverageCatchCount());
        System.out.println("Finallys:" + TryVisitor.fincnt);
        System.out.println("Ifs:" + TryVisitor.ifcnt);
        System.out.println("Throws:" + TryVisitor.throwcnt);
    }


    @SuppressWarnings("rawtypes")
	private static class TryVisitor extends VoidVisitorAdapter {
    	static int trycnt = 0;
		static List<Integer> catchesList = new ArrayList<Integer>();
		static List<Integer> sizeList = new ArrayList<Integer>();
		static int fincnt = 0;
		
		static int ifcnt = 0;
		
		static int throwcnt = 0;

		
		
		public static double getAverageTrySize(){
			double tot = 0.0;
			for (int i : sizeList){
				tot += i;
			}
			return tot/sizeList.size();			
		}
		public static double getAverageCatchCount(){
			double tot = 0.0;
			for (int i : catchesList){
				tot += i;
			}
			return tot/catchesList.size();			
		}
		
        @Override
        public void visit(TryStmt n, Object arg) {
            // here you can access the attributes of the method.
            // this method will be called for all methods in this 
            // CompilationUnit, including inner class methods
        	trycnt++;
        	System.out.println("Line:" + n.toStringWithoutComments());
        	int catches = n.getCatchs().size();
        	catchesList.add(catches);
        	sizeList.add(n.getEndLine() - n.getBeginLine());
        	
        	BlockStmt fin = n.getFinallyBlock();
        	if (fin != null){
        		System.out.println("Fin:" + fin.toStringWithoutComments());
        		fincnt++;
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
        	ifcnt++;
        	System.out.println("Line:" + n.toStringWithoutComments());
            super.visit(n, arg);
        }
        @Override
        public void visit(ThrowStmt n, Object arg) {
        	throwcnt++;
        	System.out.println("Line:" + n.toStringWithoutComments());
        	Expression exp = n.getExpr();
        	if (exp instanceof ObjectCreationExpr){
        		ObjectCreationExpr o = (ObjectCreationExpr) exp;
        		System.out.println("args:" + o.getArgs());
        		System.out.println("type:" + o.getType());
        		System.out.println("scope:" + o.getScope());
        		System.out.println("type args:" + o.getTypeArgs());
        	}
        	System.out.println("class:" + n.getExpr().getClass());
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
                System.out.println(n.getName());
                BlockStmt body = n.getBody();
                for (Statement s : body.getStmts()){
                	if (s instanceof TryStmt){
                		
                	}
                	System.out.println("Line:" + s.toStringWithoutComments());
                	System.out.println("Type:" + s.getClass().toString());
                	
                }
                //System.out.println(n.getBody());
                super.visit(n, arg);
            }
       }
}