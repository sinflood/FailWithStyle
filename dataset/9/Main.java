import java.io.*;

import java.awt.geom.Point2D;
import java.text.*;
import java.math.*;
import java.util.*;

public class Main implements Runnable {

	final String problem = "C";
	// final String filename = problem + "-sample";

	// final String filename = problem + "-small-attempt0";
	final String filename = problem + "-small-attempt3";
	// final String filename= problem+"-large";

	int[][] field;
	int R, C;

	int[][] cnt;

	int[] dx = new int[] { 1, 1, 0, -1, -1, -1, 0, 1 };
	int[] dy = new int[] { 0, 1, 1, 1, 0, -1, -1, -1 };

	public void rec(int x, int num) throws Exception {
		if (num == 0) {
			test();
			return;
		}
		field[x / C][x % C] = -1;
		rec(x + 1, num - 1);
		field[x / C][x % C] = 0;
		if (R * C - x > num)
			rec(x + 1, num);
	}

	public void test() throws Exception {
		int x0 = -1, y0 = -1, x1 = -1, y1 = -1;
		for (int i = 0; i < R; i++)
			for (int j = 0; j < C; j++) {
				if (field[i][j] == -1)
					cnt[i][j] = -1;
				else {
					cnt[i][j] = 0;
					for (int k = 0; k < 8; k++) {
						int x = i + dx[k];
						int y = j + dy[k];
						if (x >= 0 && y >= 0 && x < R && y < C
								&& field[x][y] == -1) {
							cnt[i][j]++;
						}
					}
					if (cnt[i][j] == 0) {
						x0 = i;
						y0 = j;
					} else {
						x1 = i;
						y1 = j;
					}
				}
			}
		if (x0 == -1) {
			x0 = x1;
			y0 = y1;
		}
		dfs(x0, y0);
		for (int i = 0; i < R; i++)
			for (int j = 0; j < C; j++) {
				if (cnt[i][j] > 0) {
					return;
				}
			}
		cnt[x0][y0] = -3;
		throw new Exception("test");
	}

	public void dfs(int x, int y) {
		if (x >= 0 && y >= 0 && x < R && y < C) {
			if (cnt[x][y] > 0)
				cnt[x][y] = -2;
			if (cnt[x][y] == 0) {
				cnt[x][y] = -2;
				for (int k = 0; k < 8; k++)
					dfs(x + dx[k], y + dy[k]);
			}
		}
	}

	public void solve() throws Exception {
		R = iread();
		C = iread();
		int M = iread();
		field = new int[R][C];
		cnt = new int[R][C];
		try {
			rec(0, M);
			out.write("\nImpossible");
		} catch (Exception e) {
			for (int i = 0; i < R; i++) {
				out.write("\n");
				for (int j = 0; j < C; j++)
					switch (cnt[i][j]) {
					case -1:
						out.write("*");
						break;
					case -2:
						out.write(".");
						break;
					case -3:
						out.write("c");
						break;
					}
			}
		}
	}

	public void solve_gcj() throws Exception {
		int tests = iread();
		for (int test = 1; test <= tests; test++) {
			out.write("Case #" + test + ":");
			solve();
			out.write("\n");
		}
	}

	public void run() {
		try {
			// in = new BufferedReader(new InputStreamReader(System.in));
			// out = new BufferedWriter(new OutputStreamWriter(System.out));
			in = new BufferedReader(new FileReader(filename + ".in"));
			out = new BufferedWriter(new FileWriter(filename + ".out"));
			solve_gcj();
			out.flush();
		} catch (Exception e) {
			e.printStackTrace();
			System.exit(1);
		}
	}

	public int iread() throws Exception {
		return Integer.parseInt(readword());
	}

	public double dread() throws Exception {
		return Double.parseDouble(readword());
	}

	public long lread() throws Exception {
		return Long.parseLong(readword());
	}

	BufferedReader in;

	BufferedWriter out;

	public String readword() throws IOException {
		StringBuilder b = new StringBuilder();
		int c;
		c = in.read();
		while (c >= 0 && c <= ' ')
			c = in.read();
		if (c < 0)
			return "";
		while (c > ' ') {
			b.append((char) c);
			c = in.read();
		}
		return b.toString();
	}

	public static void main(String[] args) {
		try {
			Locale.setDefault(Locale.US);
		} catch (Exception e) {

		}
		new Thread(new Main()).start();
		// new Thread(null, new Main(), "1", 1<<25).start();
	}
}