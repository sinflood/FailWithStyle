import java.io.*;
import java.util.*;

public class Main {
    static final long MOD = 1000000007;
    static int M, N;
    static String[] arr;
    static long max, count;
    
    static int cal(String[] prefix, int len) {
        //System.out.println(Arrays.toString(prefix) + " " + len);
        HashSet<String> check = new HashSet<String>();
        check.add("");
        for (int index = 0; index < len; ++index) {
            String s = prefix[index];
            for (int i = 1; i <= s.length(); ++i) {
                check.add(s.substring(0, i));
            }
        }
        //System.out.println(Arrays.toString(Arrays.copyOfRange(prefix, 0, len)) + " " + check.size());
        return check.size();
    }
    
    static void search(String[] record, boolean[] used, int setIndex, int strIndex, int arrIndex, int left, int node) {
        if (setIndex == N) {
            if (left == 0) {
                if (node > max) {
                    max = node;
                    count = 1;
                } else if (node == max) {
                    ++count;
                }
            }
            return;
        }
        if (strIndex == 0) {
            for (int i = 0; i < M; ++i) {
                if (!used[i]) {
                    used[i] = true;
                    String[] create = new String[M];
                    create[strIndex] = arr[i];
                    search(create, used, setIndex, 1, i + 1, left - 1, node);
                    used[i] = false;
                    break;
                }
            }
        } else {
            search(record, used, setIndex + 1, 0, arrIndex, left, node + cal(record, strIndex));
            if (left >= N - setIndex) {
                for (int i = arrIndex; i < M; ++i) {
                    if (!used[i]) {
                        used[i] = true;
                        record[strIndex] = arr[i];
                        //System.out.println("B : " + Arrays.toString(Arrays.copyOfRange(record, 0, strIndex + 1)));
                        search(record, used, setIndex, strIndex + 1, i + 1, left - 1, node);
                        used[i] = false;
                    }
                }
            }
        }
    }
    
    static void solve() {
        String[] record = new String[M];
        boolean[] used = new boolean[M];
        search(record, used, 0, 0, 0, M, 0);
    }
    
    public static void main(String[] args) throws Exception {
        FastScanner scan = new FastScanner(System.in);
        int taskCount = scan.nextInt();
        for (int taskIndex = 1; taskIndex <= taskCount; ++taskIndex) {
            M = scan.nextInt();
            N = scan.nextInt();
            arr = new String[M];
            for (int i = 0; i < M; ++i) {
                arr[i] = scan.next();
            }
            max = -1;
            count = -1;
            solve();
            for (int i = N; i >= 2; --i) {
                count = (count * i) % MOD;
            }
            System.out.println(String.format("Case #%d: %d %d", taskIndex, max, count));
        }
    }
}

class FastScanner {
    BufferedReader in;
    StringTokenizer tok;

    public FastScanner(InputStream in) {
        this.in = new BufferedReader(new InputStreamReader(in));
        tok = new StringTokenizer("");
    }

    public String tryReadNextLine() {
        try {
            return in.readLine();
        } catch (Exception e) {
            throw new InputMismatchException();
        }
    }

    public String nextToken() {
        while (!tok.hasMoreTokens()) {
            tok = new StringTokenizer(next());
        }
        return tok.nextToken();
    }

    public String next() {
        String newLine = tryReadNextLine();
        if (newLine == null)
            throw new InputMismatchException();
        return newLine;
    }

    public int nextInt() {
        return Integer.parseInt(nextToken());
    }
    
    public double nextDouble() {
        return Double.parseDouble(nextToken());
    }

    public long nextLong() {
        return Long.parseLong(nextToken());
    }

}