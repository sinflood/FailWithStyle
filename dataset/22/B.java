import static java.lang.Math.*;
import static java.util.Arrays.*;
import java.io.*;
import java.util.*;

public class B {
	Scanner sc = new Scanner(System.in);
	
	double C, F, X;
	
	void read() {
		C = sc.nextDouble();
		F = sc.nextDouble();
		X = sc.nextDouble();
	}
	
	void solve() {
		double min = X / 2.0, crt = 0, speed = 2.0;
		while (crt <= min) {
			double t = C / speed;
			crt += t;
			speed += F;
			min = Math.min(min, crt + X / speed);
		}
		System.out.printf("%.7f%n", min);
	}
	
	void run() {
		int caseN = sc.nextInt();
		for (int caseID = 1; caseID <= caseN; caseID++) {
			read();
			System.out.printf("Case #%d: ", caseID);
			solve();
			System.out.flush();
		}
	}
	
	void debug(Object...os) {
		System.err.println(deepToString(os));
	}
	
	public static void main(String[] args) {
		try {
			System.setIn(new BufferedInputStream(new FileInputStream(args.length > 0 ? args[0] : (B.class.getName() + ".in"))));
		} catch (Exception e) {
		}
		new B().run();
	}
}
