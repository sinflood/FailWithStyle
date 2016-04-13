package failparser;

import java.io.FileInputStream;
import java.io.FileNotFoundException;

public class TestClassBasic {

	public static String changeString(String input) throws FileNotFoundException{
		if (1 == 1){
			return input;
		}
		else{
			throw new FileNotFoundException("something");
		}
	}
	public static void main(String[] args){
		String message = "message";
		
		String out = "out";
		try {
			out = changeString(message);
		} catch (FileNotFoundException e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		}
		
		try {
			FileInputStream in = new FileInputStream("test.java");
		} catch (FileNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		
		System.out.println(out);
		
	}
}
