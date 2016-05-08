import java.util.*;
import java.io.*;

public class triesharding {
  private static BufferedReader in;
  private static PrintWriter out;
  public static final String NAME = "D-small-attempt2";
  public static int max, count, N, M;
  public static String[] s;

  private static void main2() throws IOException {
    StringTokenizer st = new StringTokenizer(in.readLine());
    M = Integer.parseInt(st.nextToken());
    N = Integer.parseInt(st.nextToken());
    s = new String[M];
    for (int i = 0; i < M; i++)
      s[i] = in.readLine();
    max = 0;
    count = 0;
    dfs(N, 0, 0);
    System.out.println (N + " " + M);
    for (int i = 0; i < M; i++) System.out.println (s[i]);
    out.println (max + " " + count);
  }
  
  private static void dfs(int machinesLeft, int mask, int c) {
    if (machinesLeft == 0) {
      if (mask == ((1 << M) - 1)) {
        if (c > max) {
          max = c;
          count = 1;
        } else if (c == max) {
          count++;
        }
      }
      return;
    }
    dfs2(machinesLeft, 0, mask, c, new Trie());
  }
  
  private static void dfs2(int machinesLeft, int index, int mask, int c, Trie cur) {
    if (index == M) {
      if (cur.count > 1)
        dfs(machinesLeft - 1, mask, c + cur.count);
      return;
    }
    dfs2(machinesLeft, index + 1, mask, c, cur);
    if ((mask & (1 << index)) == 0) {
      Trie next = cur.copy();
      next.insertString(s[index]);
      dfs2(machinesLeft, index + 1, mask | (1 << index), c, next);
    }
  }
  
  static class Trie {
    public Trie[] next;
    public int count;
    public Trie () {
      next = new Trie[26];
      count = 1;
    }
    
    public void insertString(String s) {
      if (s.length() == 0) return;
      int idx = s.charAt(0) - 'A';
      if (next[idx] == null) next[idx] = new Trie();
      next[idx].insertString(s.substring(1));
      
      count = 1;
      for (int i = 0; i < 26; i++)
        if (next[i] != null)
          count += next[i].count;
    }
    
    public Trie copy() {
      Trie ret = new Trie();
      for (int i = 0; i < 26; i++) {
        if (next[i] != null)
          ret.next[i] = next[i].copy();
      }
      ret.count = this.count;
      return ret;
    }
  }

  public static void main(String[] args) throws IOException {
    in = new BufferedReader(new FileReader (new File (NAME + ".in")));
    out = new PrintWriter(new BufferedWriter(new FileWriter(NAME + ".out")));

    int numCases = Integer.parseInt(in.readLine());
    for (int test = 1; test <= numCases; test++) {
      out.print("Case #" + test + ": ");
      main2();
    }

    out.close();
    System.exit(0);
  }
}
