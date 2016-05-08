
import java.io.File;
import java.io.FileNotFoundException;
import java.io.PrintWriter;
import java.util.Arrays;
import java.util.Scanner;

public class D {

    private static final long MOD = (long) (1e9 + 7);
    private int n;
    private long[][] c;

    public static void main(String[] args) throws FileNotFoundException {
        Scanner in = new Scanner(new File(D.class.getSimpleName() + ".in"));
        PrintWriter out = new PrintWriter(new File(D.class.getSimpleName() + ".out"));
        int T = in.nextInt();
        for (int i = 0; i < T; i++) {
            String s = "Case #" + (i + 1) + ": " + new D().solve(in);
            out.println(s);
            System.out.println(s);
        }
        out.close();
    }

    private String solve(Scanner in) {
        int m = in.nextInt();
        n = in.nextInt();
        Node root = new Node();
        for (int i = 0; i < m; i++) {
            String s = in.next();
            Node cur = root;
            for (int j = 0; j < s.length(); j++) {
                char c = s.charAt(j);
                if (cur.ch[c] == null) {
                    cur.ch[c] = new Node();
                }
                cur = cur.ch[c];
            }
            cur.terminal = true;
        }
        c = new long[n + 1][n + 1];
        for (int i = 0; i <= n; i++) {
            c[i][0] = 1;
            c[i][i] = 1;
            for (int j = 1; j < i; j++) {
                c[i][j] = c[i - 1][j - 1] + c[i - 1][j];
                if (c[i][j] > MOD) c[i][j] -= MOD;
            }
        }
        dfs(root);
        return "" + root.s + " " + root.w;
    }

    private void dfs(Node node) {
        node.r = 0;
        if (node.terminal) {
            node.r = 1;
        }
        long[] ww = new long[n + 1];
        long[] ww2 = new long[n + 1];
        ww[node.r] = 1;
        for (Node child : node.ch) {
            if (child != null) {
                dfs(child);
                node.s += child.s;
                node.r += child.r;
                Arrays.fill(ww2, 0);
                for (int r1 = 0; r1 <= n; r1++)
                    if (ww[r1] > 0) {
                        for (int nr = r1; nr <= r1 + child.r && nr <= n; nr++) {
                            int r2 = child.r;
                            if (r2 > nr) continue;
                            long w1 = ww[r1];
                            long w2 = child.w;
                            int cc = r1 + r2 - nr;
                            long w = w1 * w2;
                            w %= MOD;
                            w *= c[nr][cc];
                            w %= MOD;
                            w *= c[nr - cc][r1 - cc];
                            w %= MOD;
                            ww2[nr] += w;
                        }
                    }
                long[] t = ww; ww = ww2; ww2 = t;
            }
        }
        node.r = Math.min(node.r, n);
//        System.out.println(node.r + " " + Arrays.toString(ww));
        node.w = ww[node.r];
        node.s += node.r;
    }

    class Node {
        Node[] ch = new Node[256];
        boolean terminal;
        int r;
        long s;
        long w;
    }
}