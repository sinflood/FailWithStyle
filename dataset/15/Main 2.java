import java.io.InputStreamReader;
import java.io.IOException;
import java.util.InputMismatchException;
import java.io.PrintStream;
import java.io.BufferedReader;
import java.io.OutputStream;
import java.io.PrintWriter;
import java.io.Reader;
import java.io.Writer;
import java.io.InputStream;

/**
 * Built using CHelper plug-in
 * Actual solution is at the top
 * @author Niyaz Nigmatullin
 */
public class Main {
	public static void main(String[] args) {
		InputStream inputStream = System.in;
		OutputStream outputStream = System.out;
		FastScanner in = new FastScanner(inputStream);
		FastPrinter out = new FastPrinter(outputStream);
		MinesweeperMaster solver = new MinesweeperMaster();
		int testCount = Integer.parseInt(in.next());
		for (int i = 1; i <= testCount; i++)
			solver.solve(i, in, out);
		out.close();
	}
}

class MinesweeperMaster {

    static boolean[][] field;
    static int[][] countMines;
    static boolean[][] was;
    static int[] q;
    static int ci;
    static int cj;
    static int n;
    static int m;

    static boolean check() {
        for (int i = 0; i < n; i++) {
            for (int j = 0; j < m; j++) {
                was[i][j] = false;
                countMines[i][j] = 0;
            }
        }
        for (int i = 0; i < n; i++) {
            for (int j = 0; j < m; j++) {
                if (!field[i][j]) continue;
                for (int dx = -1; dx <= 1; dx++) {
                    for (int dy = -1; dy <= 1; dy++) {
                        int x = i + dx;
                        int y = j + dy;
                        if (x < 0 || y < 0 || x >= n || y >= m) continue;
                        countMines[x][y]++;
                    }
                }
                was[i][j] = true;
            }
        }
        ci = cj = -1;
        all:
        for (int i = 0; i < n; i++) {
            for (int j = 0; j < m; j++) {
                if (countMines[i][j] == 0) {
                    ci = i;
                    cj = j;
                    break all;
                }
            }
        }
        if (ci < 0) {
            all:
            for (int i = 0; i < n; i++) {
                for (int j = 0; j < m; j++) {
                    if (!field[i][j]) {
                        ci = i;
                        cj = j;
                        break all;
                    }
                }
            }
        }
        int head = 0;
        int tail = 0;
        q[tail++] = ci * m + cj;
        was[ci][cj] = true;
        while (head < tail) {
            int v = q[head++];
            int cx = v / m;
            int cy = v % m;
            if (countMines[cx][cy] == 0) {
                for (int dx = -1; dx <= 1; dx++) {
                    for (int dy = -1; dy <= 1; dy++) {
                        int nx = cx + dx;
                        int ny = cy + dy;
                        if (nx < 0 || ny < 0 || nx >= n || ny >= m) continue;
                        if (!was[nx][ny]) {
                            q[tail++] = nx * m + ny;
                            was[nx][ny] = true;
                        }
                    }
                }
            }
        }
        for (int i = 0; i < n; i++) {
            for (int j = 0; j < m; j++) {
                if (!was[i][j]) {
                    return false;
                }
            }
        }
        return true;
    }

    public void solve(int testNumber, FastScanner in, FastPrinter out) {
        System.err.println("[" + testNumber + "]");
        out.println("Case #" + testNumber + ":");
        n = in.nextInt();
        m = in.nextInt();
        int mines = in.nextInt();
        if (n > 5 || m > 5) {
            out.println("Bad input!!!!!");
            return;
        }
        field = new boolean[n][m];
        countMines = new int[n][m];
        was = new boolean[n][m];
        q = new int[n * m];
        for (int mask = 0; mask < 1 << n * m; mask++) {
            if (Integer.bitCount(mask) != mines) {
                continue;
            }
            for (int x = 0; x < n; x++) {
                for (int y = 0; y < m; y++) {
                    int v = x * m + y;
                    field[x][y] = ((mask >> v) & 1) == 1;
                }
            }
            if (check()) {
                char[][] ans = new char[n][m];
                for (int x = 0; x < n; x++) {
                    for (int y = 0; y < m; y++) {
                        if (x == ci && y == cj) {
                            ans[x][y] = 'c';
                        } else if (field[x][y]) {
                            ans[x][y] = '*';
                        } else {
                            ans[x][y] = '.';
                        }
                    }
                }
                for (char[] e : ans) {
                    out.println(e);
                }
                return;
            }
        }
        out.println("Impossible");
    }
}

class FastScanner extends BufferedReader {

    public FastScanner(InputStream is) {
        super(new InputStreamReader(is));
    }

    public int read() {
        try {
            int ret = super.read();
//            if (isEOF && ret < 0) {
//                throw new InputMismatchException();
//            }
//            isEOF = ret == -1;
            return ret;
        } catch (IOException e) {
            throw new InputMismatchException();
        }
    }

    public String next() {
        StringBuilder sb = new StringBuilder();
        int c = read();
        while (isWhiteSpace(c)) {
            c = read();
        }
        if (c < 0) {
            return null;
        }
        while (c >= 0 && !isWhiteSpace(c)) {
            sb.appendCodePoint(c);
            c = read();
        }
        return sb.toString();
    }

    static boolean isWhiteSpace(int c) {
        return c >= 0 && c <= 32;
    }

    public int nextInt() {
        int c = read();
        while (isWhiteSpace(c)) {
            c = read();
        }
        int sgn = 1;
        if (c == '-') {
            sgn = -1;
            c = read();
        }
        int ret = 0;
        while (c >= 0 && !isWhiteSpace(c)) {
            if (c < '0' || c > '9') {
                throw new NumberFormatException("digit expected " + (char) c
                        + " found");
            }
            ret = ret * 10 + c - '0';
            c = read();
        }
        return ret * sgn;
    }

    public String readLine() {
        try {
            return super.readLine();
        } catch (IOException e) {
            return null;
        }
    }

    }

class FastPrinter extends PrintWriter {

    public FastPrinter(OutputStream out) {
        super(out);
    }

    public FastPrinter(Writer out) {
        super(out);
    }


}

