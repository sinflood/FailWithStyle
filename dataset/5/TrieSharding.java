import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.PrintStream;
import java.util.HashSet;
import java.util.Scanner;
import java.util.Set;


public class TrieSharding {
	
	static int pow(int a, int p) {
		int r = 1;
		for (int i = 0; i < p; i++) r *= a;
		return r;
	}
	
	public static void main(String[] args) {
		try {
			Scanner in = new Scanner(new BufferedReader(new FileReader(new File("D-small-attempt0.in")), 256 << 10));
			PrintStream out = new PrintStream(new File("output.txt"));

			int testsNumber = in.nextInt();
			test:
			for (int testId = 1; testId <= testsNumber; testId++) {
				int n = in.nextInt();
				int k = in.nextInt();
				String[] s = new String[n];
				for (int i = 0; i < n; i++) {
					s[i] = in.next();
				}
				int max = 0;
				int count = 0;
				for (int i = 0, l = pow(k, n); i < l; i++) {
					@SuppressWarnings("unchecked")
					Set<String>[] servers = new HashSet[k];
					for (int j = 0; j < k; j++) servers[j] = new HashSet<String>();
					int c = i;
					for (int j = 0; j < n; j++) {
						int si = c % k;
						c /= k;
						for (int q = 0; q <= s[j].length(); q++) servers[si].add(s[j].substring(0, q));
					}
					int nc = 0;
					for (int j = 0; j < k; j++) nc += servers[j].size();
					if (nc > max) {
						max = nc;
						count = 1;
					}
					else if (nc == max) {
						count++;
					}
				}
				out.append("Case #" + testId + ": " + max + " " + count + "\n");
			}
			in.close();
			out.close();
		}
		catch (RuntimeException rte) {
			throw rte;
		}
		catch (Exception e) {
			System.err.println("Error:" + e.getMessage());
		}
	}

}
