import java.util.*;
import java.io.*;

public class lasthit {
  private static Reader in;
  private static PrintWriter out;
  public static final String NAME = "B-large";

  private static void main2() throws IOException {
    int P = in.nextInt(), Q = in.nextInt(), N = in.nextInt();
    int[] weight = new int[N];
    int[] val = new int[N];
    for (int i = 0; i < N; i++) {
      weight[i] = in.nextInt();
      val[i] = in.nextInt();
    }
    int MAX = 11     * N;
    int[] best = new int[MAX];
    Arrays.fill (best, -1 << 29);
    best[0] = 0;
    best[1] = 0;
    for (int i = 0; i < N; i++) {
      int[] temp = new int[MAX];
      System.arraycopy (best, 0, temp, 0, N);
      // don't last hit this
      int hits = weight[i] / Q;
      int rem = weight[i] % Q;
      if (rem > 0) hits++;
      int ahits = hits;
      if (rem > 0) hits--;
      // now choose to lasthit this monster
      if (rem == 0) {
        rem += Q;
        hits--;
      }
      int myhits = rem / P;
      if (rem % P > 0) myhits++;
      int dhits = myhits - hits;
      
      for (int j = 0; j < MAX; j++) {
        temp[j] = Math.max (j >= ahits ? best[j - ahits] : -1 << 29, j + dhits >= 0 && j + dhits < MAX ? best[j + myhits - hits] + val[i] : -1 << 29);
      }
//      System.out.println (Arrays.toString(temp));
      
      best = temp;
    }
    
    int max = 0;
    for (int i = 0; i < MAX; i++) max = Math.max (max, best[i]);
    
    out.println (max);
//    System.out.println (P + " " + Q + " " + N);
//    System.out.println(Arrays.toString(val));
//    System.out.println(Arrays.toString(weight));
//    System.out.println();
  }
  
  public static void main(String[] args) throws IOException {
    in = new Reader(NAME + ".in");
    out = new PrintWriter(new BufferedWriter(new FileWriter(NAME + ".out")));

    int numCases = in.nextInt();
    for (int test = 1; test <= numCases; test++) {
      out.print("Case #" + test + ": ");
      main2();
    }

    out.close();
    System.exit(0);
  }

  /** Faster input **/
  static class Reader {
    final private int BUFFER_SIZE = 1 << 16;
    private DataInputStream din;
    private byte[] buffer;
    private int bufferPointer, bytesRead;

    public Reader() {
      din = new DataInputStream(System.in);
      buffer = new byte[BUFFER_SIZE];
      bufferPointer = bytesRead = 0;
    }

    public Reader(String file_name) throws IOException {
      din = new DataInputStream(new FileInputStream(file_name));
      buffer = new byte[BUFFER_SIZE];
      bufferPointer = bytesRead = 0;
    }

    public String readLine() throws IOException {
      byte[] buf = new byte[1024];
      int cnt = 0, c;
      while ((c = read()) != -1) {
        if (c == '\n')
          break;
        buf[cnt++] = (byte) c;
      }
      return new String(buf, 0, cnt);
    }

    public int nextInt() throws IOException {
      int ret = 0;
      byte c = read();
      while (c <= ' ')
        c = read();
      boolean neg = (c == '-');
      if (neg)
        c = read();
      do {
        ret = ret * 10 + c - '0';
      } while ((c = read()) >= '0' && c <= '9');
      if (neg)
        return -ret;
      else
        return ret;
    }

    public long nextLong() throws IOException {
      long ret = 0;
      byte c = read();
      while (c <= ' ')
        c = read();
      boolean neg = (c == '-');
      if (neg)
        c = read();
      do {
        ret = ret * 10 + c - '0';
      } while ((c = read()) >= '0' && c <= '9');
      if (neg)
        return -ret;
      else
        return ret;
    }

    public double nextDouble() throws IOException {
      double ret = 0, div = 1;
      byte c = read();
      while (c <= ' ')
        c = read();
      boolean neg = (c == '-');
      if (neg)
        c = read();
      do {
        ret = ret * 10 + c - '0';
      } while ((c = read()) >= '0' && c <= '9');
      if (c == '.') {
        while ((c = read()) >= '0' && c <= '9')
          ret += (c - '0') / (div *= 10);
      }
      if (neg)
        return -ret;
      else
        return ret;
    }

    public char nextChar() throws IOException {
      char ret = 0;
      byte c = read();
      while (c <= ' ')
        c = read();
      return (char) c;
    }

    private void fillBuffer() throws IOException {
      bytesRead = din.read(buffer, bufferPointer = 0, BUFFER_SIZE);
      if (bytesRead == -1)
        buffer[0] = -1;
    }

    private byte read() throws IOException {
      if (bufferPointer == bytesRead)
        fillBuffer();
      return buffer[bufferPointer++];
    }

    public void close() throws IOException {
      if (din == null)
        return;
      din.close();
    }
  }
}
