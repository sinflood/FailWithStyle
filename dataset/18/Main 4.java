import java.io.*;
import java.util.*;
import java.lang.reflect.*;


public class Main {

	static long CURRENT_TIME_NANO = System.nanoTime();

	public static void main(String[] args) throws Exception {

		int tests = next();

		for (int test = 1; test <= tests; test++) {
			int m = next();
			int n = next();
			String[] s = new String[m];
			for (int i = 0; i < m; i++) s[i] = nextline();
			
			int[] kol = new int[100];

			for (int mask = 0; mask < (1 << (2*m)); mask++) {
				int[] ser = new int[m];
				for (int i = 0; i < m; i++) ser[i] = (mask >> (2*i)) & 3;
				int[] count = new int[4];
				for (int i = 0; i < m; i++) count[ser[i]]++;
				boolean flag = true;
				for (int i = 0; i < n; i++) if (count[i] == 0) flag = false;
				for (int i = n; i < 4; i++) if (count[i] != 0) flag = false;
				if (!flag) continue;

				TreeSet<String>[] set = new TreeSet[n];
				for (int i = 0; i < n; i++) set[i] = new TreeSet<String>();

				for (int i = 0; i < m; i++)
					for (int j = 0; j <= s[i].length(); j++) set[ser[i]].add(s[i].substring(0, j));

				int sum = 0;
				for (int i = 0; i < n; i++) sum += set[i].size();
				kol[sum]++;

			}

			for (int i = 99; i >= 0; i--) if (kol[i] > 0) {
				out.println("Case #" + test + ": " + i + " " + kol[i]);
				break;
			}
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