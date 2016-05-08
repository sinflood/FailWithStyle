import java.io.*;
import java.util.*;
import java.lang.reflect.*;


public class Main {

	static long CURRENT_TIME_NANO = System.nanoTime();

	public static void main(String[] args) throws Exception {

		int tests = next();

		for (int test = 1; test <= tests; test++) {
			int p = next();
			int q = next();
			int n = next();

			int[] h = new int[n];
			int[] g = new int[n];

			for (int i = 0; i < n; i++) {
				h[i] = next();
				g[i] = next();
			}

			int[] a = new int[n];
			int[] b = new int[n];
			int[] c = new int[n];

			for (int i = 0; i < n; i++) {
				c[i] = (h[i] + q - 1)/q;
				if (p < q) {
					while (h[i] > 0 && ((h[i] % q > p) || (h[i] % q == 0))) {
						a[i]++;
						h[i] -= p;
					}
					if (h[i] > 0) {
						b[i] = h[i] / q;
						a[i]++;
					}
				} else {
					while (h[i] - q > 0) {
						h[i] -= q;
						b[i]++;
					}
					a[i] = 1;
				}
			}
			
			int m = 1002;
			int[][] max = new int[n + 1][m];
			int inf = 1000000000;
			for (int[] mm : max) Arrays.fill(mm, -inf);
			max[0][1] = 0;
			for (int i = 0; i < n; i++) {
				for (int j = 0; j < m; j++) if (max[i][j] >= 0) {
					max[i + 1][j + c[i]] = Math.max(max[i + 1][j + c[i]], max[i][j]);
					if (j + b[i] - a[i] >= 0) max[i + 1][j + b[i] - a[i]] = Math.max(max[i + 1][j + b[i] - a[i]], max[i][j] + g[i]);
				}
			}

			int answ = 0;
			for (int i = 0; i < m; i++) answ = Math.max(answ, max[n][i]);
			
			out.println("Case #" + test + ": " + answ);
		}

		out.close();
	}
	
	static void printtime() {System.out.println((System.nanoTime() - CURRENT_TIME_NANO)*1e-9);}
	static void nexttime() {printtime(); CURRENT_TIME_NANO = System.nanoTime();}
	static PrintWriter out = new PrintWriter(System.out);
	
	static BufferedReader bufferedreader = new BufferedReader(new InputStreamReader(System.in));
	static StringTokenizer in = new StringTokenizer("");

	static String nextToken() throws Exception {
		if (!in.hasMoreTokens()) in = new StringTokenizer(bufferedreader.readLine());
		return in.nextToken();
	}

	static int next()  throws Exception {return Integer.parseInt(nextToken());};
	static int[] next(int n) throws Exception {
		int[] x = new int[n];
		for (int i = 0; i < n; i++) x[i] = next();
		return x;
	}
	static int[][] next(int n, int m) throws Exception {
		int[][] x = new int[n][];
		for (int i = 0; i < n; i++) x[i] = next(m);
		return x;
	}

	static long nextl() throws Exception {return Long.parseLong(nextToken());};
	static long[] nextl(int n) throws Exception {
		long[] x = new long[n];
		for (int i = 0; i < n; i++) x[i] = nextl();
		return x;
	}
	static long[][] nextl(int n, int m) throws Exception {
		long[][] x = new long[n][];
		for (int i = 0; i < n; i++) x[i] = nextl(m);
		return x;
	}

	static double nextd() throws Exception {return Double.parseDouble(nextToken());};
	static double[] nextd(int n) throws Exception {
		double[] x = new double[n];
		for (int i = 0; i < n; i++) x[i] = nextd();
		return x;
	}
	static double[][] nextd(int n, int m) throws Exception {
		double[][] x = new double[n][];
		for (int i = 0; i < n; i++) x[i] = nextd(m);
		return x;
	}

	static String nextline() throws Exception {
		in = new StringTokenizer("");
		return bufferedreader.readLine();
	}

	static void sout(long x) {System.out.println(x);}
	static void sout(String s) {System.out.println(s);}
	static void sout(int[] x) {for (int xx : x) System.out.print(xx + " "); System.out.println();}
	static void sout(long[] x) {for (long xx : x) System.out.print(xx + " "); System.out.println();}
	static void sout(int[][] x) {for (int[] xx : x) sout(xx); System.out.println();}
	static void sout(long[][] x) {for (long[] xx : x) sout(xx); System.out.println();}


}