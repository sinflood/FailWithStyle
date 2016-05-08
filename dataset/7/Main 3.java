import java.io.InputStreamReader;
import java.io.IOException;
import java.util.Set;
import java.io.BufferedReader;
import java.io.OutputStream;
import java.io.FileOutputStream;
import java.io.PrintWriter;
import java.io.FileInputStream;
import java.io.File;
import java.util.StringTokenizer;
import java.io.FilenameFilter;
import java.util.HashSet;
import java.io.InputStream;

/**
 * Built using CHelper plug-in
 * Actual solution is at the top
 */
public class Main {
	public static void main(String[] args) {
		InputStream inputStream;
		try {
			final String regex = "D-(small|large).*[.]in";
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
			outputStream = new FileOutputStream("d.out");
		} catch (IOException e) {
			throw new RuntimeException(e);
		}
		InputReader in = new InputReader(inputStream);
		PrintWriter out = new PrintWriter(outputStream);
		D solver = new D();
		int testCount = Integer.parseInt(in.next());
		for (int i = 1; i <= testCount; i++)
			solver.solve(i, in, out);
		out.close();
	}
}

class D {
	public void solve(int testNumber, InputReader in, PrintWriter out) {
		int m = in.nextInt();
		int n = in.nextInt();
		String[] s = new String[m];
		for (int i = 0; i < m; i++) {
			s[i] = in.next();
		}

		int res1 = 0;
		int res2 = 0;

		if (n == 1) {
			int mask1 = (1 << m) - 1;
			int size = solve(m, s, mask1);
			if (res1 < size) {
				res1 = size;
				res2 = 1;
			} else if (res1 == size) {
				++res2;
			}
		}

		if (n == 2) {
			for (int mask1 = 0; mask1 < 1 << m; mask1++) {
				int mask2 = ((1 << m) - 1) ^ mask1;
				int size = solve(m, s, mask1) + solve(m, s, mask2);
				if (res1 < size) {
					res1 = size;
					res2 = 1;
				} else if (res1 == size) {
					++res2;
				}
			}
		}

		if (n == 3) {
			for (int mask1 = 0; mask1 < 1 << m; mask1++) {
				for (int mask2 = 0; mask2 < 1 << m; mask2++) {
					if ((mask1 & mask2) > 0) continue;
					int mask3 = ((1 << m) - 1) ^ mask1 ^ mask2;
					int size = solve(m, s, mask1) + solve(m, s, mask2) + solve(m, s, mask3);
					if (res1 < size) {
						res1 = size;
						res2 = 1;
					} else if (res1 == size) {
						++res2;
					}
				}
			}
		}

		if (n == 4) {
			for (int mask1 = 0; mask1 < 1 << m; mask1++) {
				for (int mask2 = 0; mask2 < 1 << m; mask2++) {
					if ((mask1 & mask2) > 0) continue;
					for (int mask3 = 0; mask3 < 1 << m; mask3++) {
						if ((mask1 & mask3) > 0) continue;
						if ((mask2 & mask3) > 0) continue;
						int mask4 = ((1 << m) - 1) ^ mask1 ^ mask2 ^ mask3;
						int size = solve(m, s, mask1) + solve(m, s, mask2) + solve(m, s, mask3) + solve(m, s, mask4);
						if (res1 < size) {
							res1 = size;
							res2 = 1;
						} else if (res1 == size) {
							++res2;
						}
					}
				}
			}
		}

		out.println("Case #" + testNumber + ": " + res1 + " " + res2);
	}

	static int solve(int m, String[] s, int mask) {
		Set<String> set = new HashSet<>();
		for (int i = 0; i < m; i++) {
			if ((mask & (1 << i)) != 0) {
				for (int j = 0; j <= s[i].length(); j++) {
					set.add(s[i].substring(0, j));
				}
			}
		}
		return set.size();
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

