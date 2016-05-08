import java.io.File;
import java.io.PrintWriter;
import java.util.Scanner;
import java.util.TreeSet;

public class D {
	public static void main(String[] args) throws Exception {
		String fname = "D-large";
		Scanner in = new Scanner(new File(fname + ".in"));
		PrintWriter out = new PrintWriter(fname + ".out");
		int tc = in.nextInt();
		for (int cc = 1; cc <= tc; cc++) {
			int n = in.nextInt();
			TreeSet<Double> A = new TreeSet<>();
			TreeSet<Double> B = new TreeSet<>();
			for (int j = 0; j < n; j++)
				A.add(in.nextDouble());
			for (int j = 0; j < n; j++)
				B.add(in.nextDouble());
			TreeSet<Double> C = (TreeSet<Double>) B.clone();
			int cheat = 0;
			for (double x : A) {
				if (x > B.first()) {
					cheat++;
					B.pollFirst();
				} else {
					B.pollLast();
				}
			}
			int fair = 0;
			for (double x : A) {
				Double y = C.ceiling(x);
				if (y != null) {
					C.remove(y);
				} else {
					C.pollFirst();
					fair++;
				}
			}
			System.out.printf("Case #%d: %d %d\n", cc, cheat, fair);
			out.printf("Case #%d: %d %d\n", cc, cheat, fair);
		}
		out.close();
	}
}
