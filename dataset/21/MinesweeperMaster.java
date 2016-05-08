import java.io.*;
import java.util.*;

public class MinesweeperMaster {

	FastScanner in;
	PrintWriter out;

	void solve() {
		int r = in.nextInt(), c = in.nextInt(), m = in.nextInt();

		if (m + 1 == r * c) {
			for (int i = 0; i < r; i++) {
				for (int j = 0; j < c; j++) {
					if (i + j == 0) {
						out.print('c');
					} else {
						out.print('*');
					}
				}
				out.println();
			}
			return;
		}

		int[][] field = new int[r + 2][c + 2];
		int[][] deg = new int[r + 2][c + 2];
		boolean[][] vis = new boolean[r + 2][c + 2];

		int[] queue = new int[r * c * 2];
		for (int mask = 0; mask < (1 << (r * c)); mask++) {
			int tmp = mask;
			if (Integer.bitCount(mask) != m) {
				continue;
			}
			for (int i = 0; i < field.length; i++) {
				Arrays.fill(field[i], 1);
				Arrays.fill(deg[i], 0);
				Arrays.fill(vis[i], false);
			}
			for (int i = 1; i <= r; i++) {
				for (int j = 1; j <= c; j++) {
					field[i][j] = tmp & 1;
					tmp >>= 1;
					if (field[i][j] == 1) {
						for (int dx = -1; dx <= 1; dx++) {
							for (int dy = -1; dy <= 1; dy++) {
								deg[i + dx][j + dy]++;
							}
						}
					}
				}
			}
			int head = 0, tail = 0;

			int startX = -1, startY = -1;
			for (int i = 1; i <= r; i++) {
				for (int j = 1; j <= c; j++) {
					if (field[i][j] == 0 && deg[i][j] == 0) {
						if (startX == -1) {
							vis[i][j] = true;
							startX = i;
							startY = j;
							queue[tail++] = i;
							queue[tail++] = j;
						}
					}
				}
			}

			while (head < tail) {
				int curX = queue[head++], curY = queue[head++];

				for (int dx = -1; dx <= 1; dx++) {
					for (int dy = -1; dy <= 1; dy++) {
						if (field[curX + dx][curY + dy] == 0 && !vis[curX + dx][curY + dy]) {
							vis[curX + dx][curY + dy] = true;
							if (deg[curX + dx][curY + dy] == 0) {
								queue[tail++] = curX + dx;
								queue[tail++] = curY + dy;
							}
						}
					}
				}
			}
			boolean ok = true;
			for (int i = 1; i <= r; i++) {
				for (int j = 1; j <= c; j++) {
					if (field[i][j] == 0 && !vis[i][j]) {
						ok = false;
						break;
					}
				}
			}
			if (ok) {
				for (int i=  1; i <= r; i++) {
					for (int j = 1; j <= c; j++) {
						if (field[i][j] == 1) {
							out.print('*');
						} else if (startX == i && startY == j) {
							out.print('c');
						} else {
							out.print('.');
						}
					}
					out.println();
				}
				return;
			}
		}

		out.println("Impossible");
		System.err.println("Impossible: " + r + " " + c + " " + m);
	}

	void run() {
		try {
			in = new FastScanner("input.txt");
			out = new PrintWriter("output.txt");
			int T = in.nextInt();
			for (int i = 1; i <= T; i++) {
				long time = System.currentTimeMillis();
				out.printf("Case #%d:\n", i);
				solve();
//				System.err.println("Test #" + i + " done in " + (System.currentTimeMillis() - time)
//						+ " ms");
			}
			out.close();
		} catch (Exception e) {
			e.printStackTrace();
			System.exit(1);
		}
	}

	class FastScanner {
		BufferedReader br;
		StringTokenizer st;

		public FastScanner(String s) {
			try {
				br = new BufferedReader(new FileReader(s));
			} catch (FileNotFoundException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}

		String nextToken() {
			while (st == null || !st.hasMoreElements()) {
				try {
					st = new StringTokenizer(br.readLine());
				} catch (IOException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			}
			return st.nextToken();
		}

		int nextInt() {
			return Integer.parseInt(nextToken());
		}

		long nextLong() {
			return Long.parseLong(nextToken());
		}

		double nextDouble() {
			return Double.parseDouble(nextToken());
		}
	}

	public static void main(String[] args) {
		new MinesweeperMaster().run();
	}
}
