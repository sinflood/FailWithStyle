import java.io.InputStreamReader;
import java.io.IOException;
import java.util.Arrays;
import java.util.Locale;
import java.io.BufferedReader;
import java.io.OutputStream;
import java.io.FileOutputStream;
import java.io.PrintWriter;
import java.io.FileInputStream;
import java.io.File;
import java.util.StringTokenizer;
import java.io.FilenameFilter;
import java.io.InputStream;

/**
 * Built using CHelper plug-in
 * Actual solution is at the top
 */
public class Main {
	public static void main(String[] args) {
		Locale.setDefault(Locale.US);
		InputStream inputStream;
		try {
			final String regex = "B-(small|large).*[.]in";
			File directory = new File(".");
			File[] candidates = directory.listFiles(new FilenameFilter() {
				public boolean accept(File dir, String name) {
					return name.matches(regex);
				}
			});
			File toRun = null;
			for (File candidate : candidates) {
				if (toRun == null || candidate.lastModified() > toRun.lastModified())
					toRun = candidate;
			}
			inputStream = new FileInputStream(toRun);
		} catch (IOException e) {
			throw new RuntimeException(e);
		}
		OutputStream outputStream;
		try {
			outputStream = new FileOutputStream("b.out");
		} catch (IOException e) {
			throw new RuntimeException(e);
		}
		InputReader in = new InputReader(inputStream);
		PrintWriter out = new PrintWriter(outputStream);
		TaskB solver = new TaskB();
		int testCount = Integer.parseInt(in.next());
		for (int i = 1; i <= testCount; i++)
			solver.solve(i, in, out);
		out.close();
	}
}

class TaskB {
	public void solve(int testNumber, InputReader in, PrintWriter out) {
		int p = in.nextInt();
		int q = in.nextInt();
		int n = in.nextInt();
		int[] h = new int[n];
		int[] g = new int[n];
		int[] shotsD = new int[n];
		int[] shotsT = new int[n];
		for (int i = 0; i < n; i++) {
			h[i] = in.nextInt();
			g[i] = in.nextInt();
			shotsT[i] = (h[i] - 1) / q;
			shotsD[i] = (h[i] - shotsT[i] * q + p - 1) / p;
		}

		int MAXN = 2048;
		int[][] dp = new int[n + 1][MAXN];
		Arrays.fill(dp[0], -1);
		dp[0][1] = 0;
		relax(dp[0]);
		for (int i = 0; i < n; i++) {
			System.arraycopy(dp[i], 0, dp[i + 1], 0, MAXN);
			for (int j = 0; j < MAXN; j++) {
				int balance = (h[i] + q - 1) / q;
				if (dp[i][j] != -1 && j + balance >= 0 && j + balance < MAXN) {
					dp[i + 1][j + balance] = Math.max(dp[i + 1][j + balance], dp[i][j]);
				}
			}
			relax(dp[i + 1]);
			for (int j = 0; j < MAXN; j++) {
				int balance = shotsT[i] - shotsD[i];
				if (dp[i][j] != -1 && j + balance >= 0 && j + balance < MAXN) {
					dp[i + 1][j + balance] = Math.max(dp[i + 1][j + balance], dp[i][j] + g[i]);
				}
			}
			relax(dp[i + 1]);
		}

		int res = 0;
		for (int v : dp[n]) {
			res = Math.max(res, v);
		}
		out.println("Case #" + testNumber + ": " + res);
	}

	static void relax(int[] x) {
		for (int i = x.length - 1; i > 0; i--) {
			x[i - 1] = Math.max(x[i - 1], x[i]);
		}
	}
}

class InputReader {
	public BufferedReader reader;
	public StringTokenizer tokenizer;

	public InputReader(InputStream stream) {
		reader = new BufferedReader(new InputStreamReader(stream));
	}

	public String next() {
		while (tokenizer == null || !tokenizer.hasMoreTokens()) {
			try {
				tokenizer = new StringTokenizer(reader.readLine());
			} catch (IOException e) {
				throw new RuntimeException(e);
			}
		}
		return tokenizer.nextToken();
	}

	public int nextInt() {
		return Integer.parseInt(next());
	}
}

