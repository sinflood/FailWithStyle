package failparser;

import java.io.FileInputStream;
import java.io.FileNotFoundException;

public class TestClassBasic {

	public static String changeString(String input) throws FileNotFoundException{
		try {
			FileInputStream in = new FileInputStream("test2.java");
		} catch (FileNotFoundException e) {
			// TODO Auto-generated catch block

		}
		try {
			FileInputStream in2 = new FileInputStream("test3.java");
		} catch (FileNotFoundException e) {
			// TODO Auto-generated catch block
			return null;
		}
		if (input != null){
			return input;
		}
		else{
			throw new FileNotFoundException("something");
		}
		
		
		
	}
	
	public static String printThis() throws FileNotFoundException{
		
		return "newString!";
	}
	public static void main(String[] args){
		String message = "message";
		
		String out = "out";
		try {
			out = changeString(message);
		} catch (Exception e1) {
			// TODO Auto-generated catch block
			System.out.println(e1.getMessage());
			System.exit(-1);
		}
		
		try {
			FileInputStream in = new FileInputStream("test.java");
		} catch (FileNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (Exception e2) {
			throw new Error(e2);
		}
		
		
		System.out.println(out);
		
	}
}
