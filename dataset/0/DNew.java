import java.io.*;
import java.util.*;

public class DNew {
	public static void main(String[] args) {
		new DNew().run();
	}

	BufferedReader br;
	StringTokenizer in;
	PrintWriter out;

	public String nextToken() throws IOException {
		while (in == null || !in.hasMoreTokens()) {
			in = new StringTokenizer(br.readLine());
		}
		return in.nextToken();
	}

	public int nextInt() throws IOException {
		return Integer.parseInt(nextToken());
	}

	public void solve() throws IOException {
		int p = nextInt();
		int q = nextInt();
		int n = nextInt();
		int[] h = new int[n + 1];
		int[] g = new int[n];

		for (int i = 0; i < n; i++) {
			h[i] = nextInt();
			g[i] = nextInt();
		}

		int max_skip_on_turn = (200 + q - 1) / q;
		int max_skip = max_skip_on_turn * n + n;

		long[][][][] ans = new long[n + 1][2][201][max_skip + 1];
		for (int i = 0; i < ans.length; i++) {
			for (int j = 0; j < 2; j++) {
				for (int k = 0; k <= 200; k++) {
					Arrays.fill(ans[i][j][k], -1);
					Arrays.fill(ans[i][j][k], -1);
				}
			}
		}

		ans[0][0][h[0]][0] = 0;
		for (int monsters = 0; monsters < n; monsters++) {
			for (int hh = h[monsters]; hh > 0; hh--) {
				for (int skipped = 0; skipped <= max_skip; skipped++) {
					for (int turn = 0; turn < 2; turn++) {
						if (ans[monsters][turn][hh][skipped] == -1)
							continue;

						// skip
						if (turn == 0) {
							ans[monsters][1][hh][skipped + 1] = Math.max(
									ans[monsters][1][hh][skipped + 1],
									ans[monsters][0][hh][skipped]);
						}

						// reuse
						if (skipped > 0 && turn == 1) {
							if (hh > p) {
								ans[monsters][1][hh - p][skipped - 1] = Math
										.max(ans[monsters][1][hh - p][skipped - 1],
												ans[monsters][1][hh][skipped]);
							} else {
								ans[monsters + 1][1][h[monsters + 1]][skipped - 1] = Math
										.max(ans[monsters + 1][1][h[monsters + 1]][skipped - 1],
												ans[monsters][1][hh][skipped]
														+ g[monsters]);
							}
						}

						// normal_turn
						if (turn == 0) {
							if (hh > p) {
								ans[monsters][1][hh - p][skipped] = Math.max(
										ans[monsters][1][hh - p][skipped],
										ans[monsters][0][hh][skipped]);
							} else {
								ans[monsters + 1][1][h[monsters + 1]][skipped] = Math
										.max(ans[monsters + 1][1][h[monsters + 1]][skipped],
												ans[monsters][0][hh][skipped]
														+ g[monsters]);
							}
						} else {
							if (hh > q) {
								ans[monsters][0][hh - q][skipped] = Math.max(
										ans[monsters][0][hh - q][skipped],
										ans[monsters][1][hh][skipped]);
							} else {
								ans[monsters + 1][0][h[monsters + 1]][skipped] = Math
										.max(ans[monsters + 1][0][h[monsters + 1]][skipped],
												ans[monsters][1][hh][skipped]);
							}
						}
					}
				}
			}
		}

		long max = 0;
		for (int turn = 0; turn < 2; turn++) {
			for (int skipped = 0; skipped <= max_skip; skipped++)
				max = Math.max(max, ans[n][turn][0][skipped]);
		}

		out.println(max);
	}

	public void run() {
		try {
			br = new BufferedReader(new FileReader("input.txt"));
			out = new PrintWriter("output.txt");

			int t = nextInt();
			for (int i = 0; i < t; i++) {
				out.print(String.format("Case #%d: ", i + 1));
				solve();
				System.err.println("Ready " + (i + 1));
			}

			out.close();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
}
