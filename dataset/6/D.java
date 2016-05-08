package round2;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class D {
    public static void main(String[] args) throws FileNotFoundException {
        Kattio io;

//        io = new Kattio(System.in, System.out);
//        io = new Kattio(new FileInputStream("round2/D-sample.in"), System.out);
        io = new Kattio(new FileInputStream("round2/D-small-0.in"), new FileOutputStream("round2/D-small-0.out"));
//        io = new Kattio(new FileInputStream("round2/D-large-0.in"), new FileOutputStream("round2/D-large-0.out"));

        int cases = io.getInt();
        for (int i = 1; i <= cases; i++) {
            io.print("Case #" + i + ": ");
            new D().solve(io);
        }
        io.close();
    }

    int noServers;
    String[] words;
    int[] server;
    List<List<String>> w;

    private void solve(Kattio io) {
        int noWords = io.getInt();
        noServers = io.getInt();
        words = new String[noWords];
        for (int i = 0; i < noWords; i++) {
            words[i] = io.getWord();
        }
        server = new int[noWords];
        Arrays.sort(words);
        w = new ArrayList<>();
        for (int i = 0; i < noServers; i++) {
            w.add(new ArrayList<String>());
        }
        worst = 0; worstCnt = 0;
        go(0);
        io.println(worst + " " + worstCnt);
    }

    private int worst, worstCnt;

    private void go(int cur) {
        if (cur == words.length) {
            int count = noServers;
            for (int i = 0; i < noServers; i++) {
                if (w.get(i).size() == 0) return;
                String last = "";
                for (String s : w.get(i)) {
                    int j = 0;
                    while (j < last.length() && j < s.length() && last.charAt(j) == s.charAt(j)) {
                        j++;
                    }
                    count += s.length() - j;
                    last = s;
                }
            }
            if (count > worst) {
                worst = count;
                worstCnt = 1;
            } else if (count == worst)
                worstCnt++;
            return;
        }
        for (int i = 0; i < noServers; i++) {
            List<String> strings = w.get(i);
            strings.add(words[cur]);
            go(cur + 1);
            strings.remove(strings.size() - 1);
        }
    }
}
