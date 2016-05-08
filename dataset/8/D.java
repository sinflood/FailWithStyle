package Round2;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;

public class D {
    static int n, m;
    static String[] A;
    static int[] index;
    static int worst;
    static int count;

    public static void rec(int x) {
        if (x == n) {
            int[] occ = new int[m];
            for (int i = 0; i < n; i++)
                occ[index[i]]++;
            for (int i = 0; i < m; i++)
                if (occ[i] == 0)
                    return;
            Trie[] tries = new Trie[m];
            for (int i = 0; i < m; i++)
                tries[i] = new Trie();
            for (int i = 0; i < n; i++)
                tries[index[i]].addWord(A[i]);
            int res = 0;
            for (int i = 0; i < m; i++)
                res += countNodes(tries[i]);
            if (res > worst) {
                worst = res;
                count = 1;
            } else if (res == worst)
                count++;
        } else
            for (int i = 0; i < m; i++) {
                index[x] = i;
                rec(x + 1);
            }
    }

    private static int countNodes(Trie trie) {
        if (trie == null)
            return 0;
        int ans = 1;
        for (int i = 0; i < trie.next.length; i++)
            ans += countNodes(trie.next[i]);
        return ans;
    }

    public static void main(String[] args) throws NumberFormatException,
            IOException {
        BufferedReader in = new BufferedReader(new InputStreamReader(System.in));
        int t = Integer.parseInt(in.readLine());
        for (int _ = 1; _ <= t; _++) {
            String[] S = in.readLine().split(" ");
            n = Integer.parseInt(S[0]);
            m = Integer.parseInt(S[1]);
            A = new String[n];
            worst = 0;
            count = 0;
            index = new int[n];
            for (int i = 0; i < n; i++)
                A[i] = in.readLine().toLowerCase();
            rec(0);
            System.out.println("Case #" + _ + ": " + worst + " " + count);
        }
    }
}

class Trie {
    boolean wordEnd;
    Trie[] next;
    static final int INF = (int) 1e9;

    public Trie() {
        wordEnd = false;
        next = new Trie[26];
    }

    public void addWord(String s) {
        addWord(s, 0);
    }

    private void addWord(String s, int index) {
        if (index == s.length())
            wordEnd = true;
        else {
            int edge = s.charAt(index) - 'a';
            if (next[edge] == null)
                next[edge] = new Trie();
            next[edge].addWord(s, index + 1);
        }
    }

    public boolean search(String s) {
        return search(s, 0);
    }

    private boolean search(String s, int index) {
        if (index == s.length())
            return wordEnd;
        else {
            int edge = s.charAt(index) - 'a';
            if (next[edge] == null)
                return false;
            else
                return next[edge].search(s, index + 1);
        }
    }

    public String spellCorrect(String s, int mistakes) {
        myPair res = spellCorrect(s, mistakes, 0);
        if (res.score >= 0)
            return res.s;
        else
            return null;
    }

    private myPair spellCorrect(String s, int mistakes, int index) {
        if (mistakes < 0)
            return new myPair("", -INF);
        if (index == s.length())
            if (wordEnd)
                return new myPair("", mistakes);
            else
                return new myPair("", -INF);
        myPair best = new myPair("", -INF);
        // insert
        for (int i = 0; i < 26; i++)
            if (next[i] != null) {
                myPair temp = next[i].spellCorrect(s, mistakes - 1, index);
                if (temp.compareTo(best) > 0) {
                    best = temp;
                    best.s = (char) (i + 'a') + best.s;
                }
            }

        // delete
        myPair temp = spellCorrect(s, mistakes - 1, index + 1);
        if (temp.compareTo(best) > 0) {
            best = temp;
        }

        // edit
        for (int i = 0; i < 26; i++) {
            int sub = i == (s.charAt(index) - 'a') ? 0 : 1;
            if (next[i] != null) {
                temp = next[i].spellCorrect(s, mistakes - sub, index + 1);
                if (temp.compareTo(best) > 0) {
                    best = temp;
                    best.s = (char) (i + 'a') + best.s;
                }
            }
        }
        return best;
    }

    public static void main(String[] args) {
        Trie T = new Trie();
        T.addWord("ten");
        T.addWord("ted");
        System.out.println(T.search("ten"));
        System.out.println(T.search("te"));
        System.out.println(T.spellCorrect("tex", 1));
        System.out.println(T.spellCorrect("tex", 0));
        System.out.println(T.spellCorrect("tdx", 2));
        System.out.println(T.spellCorrect("hen", 1));
        System.out.println(T.spellCorrect("hen", 2));
    }
}

class myPair implements Comparable<myPair> {
    String s;
    int score;

    public myPair(String s, int score) {
        this.s = s;
        this.score = score;
    }

    @Override
    public int compareTo(myPair o) {
        return score - o.score;
    }
}