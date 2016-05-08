import java.util.List;
import java.io.InputStreamReader;
import java.io.IOException;
import java.util.Locale;
import java.util.ArrayList;
import java.util.Set;
import java.io.BufferedReader;
import java.io.OutputStream;
import java.io.FileOutputStream;
import java.io.PrintWriter;
import java.io.FileInputStream;
import java.io.File;
import java.util.TreeSet;
import java.util.StringTokenizer;
import java.io.FilenameFilter;
import java.io.InputStream;

/**
 * Built using CHelper plug-in
 * Actual solution is at the top
 */
public class Main {
	public static void main(String[] args) {
		Locale.setDefault(Locale.US);
		InputStream inputStream;
		try {
			final String regex = "D-(small|large).*[.]in";
			File directory = new File(".");
			File[] candidates = directory.listFiles(new FilenameFilter() {
				public boolean accept(File dir, String name) {
					return name.matches(regex);
				}
			});
			File toRun = null;
			for (File candidate : candidates) {
				if (toRun == null || candidate.lastModified() > toRun.lastModified())
					toRun = candidate;
			}
			inputStream = new FileInputStream(toRun);
		} catch (IOException e) {
			throw new RuntimeException(e);
		}
		OutputStream outputStream;
		try {
			outputStream = new FileOutputStream("d.out");
		} catch (IOException e) {
			throw new RuntimeException(e);
		}
		InputReader in = new InputReader(inputStream);
		PrintWriter out = new PrintWriter(outputStream);
		TaskD solver = new TaskD();
		int testCount = Integer.parseInt(in.next());
		for (int i = 1; i <= testCount; i++)
			solver.solve(i, in, out);
		out.close();
	}
}

class TaskD {
    public void solve(int testNumber, InputReader in, PrintWriter out) {
        int strings = in.nextInt();
        int servers = in.nextInt();
        String[] s = new String[strings];
        for (int i = 0; i < strings; i++) {
            s[i] = in.nextToken();
        }
        int[] shard = new int[strings];
        maxNodes = -1;
        goAllShards(s, servers, shard, 0);
        out.println("Case #"+testNumber+": "+maxNodes+" "+waysMaxNodes);
    }

    private void goAllShards(String[] s, int servers, int[] shard, int cur) {
        if (cur == s.length) {
            doTheDirtyWork(s, shard, servers);
            return;
        }
        for (int i = 0; i < servers; i++) {
            shard[cur] = i;
            goAllShards(s, servers, shard, cur + 1);
        }
    }

    int maxNodes;
    int waysMaxNodes;

    private void doTheDirtyWork(String[] s, int[] shard, int servers) {
        List<String>[] lists = new List[servers];
        for (int i = 0; i < servers; i++) {
            lists[i] = new ArrayList<>();
        }
        for (int i = 0; i < s.length; i++) {
            lists[shard[i]].add(s[i]);
        }
        int nodes = 0;
        for (List<String> list : lists) {
            nodes += calcNodes(list);
        }
        if (nodes > maxNodes) {
            maxNodes = nodes;
            waysMaxNodes = 0;
        }
        if (nodes == maxNodes) {
            waysMaxNodes++;
        }
    }

    private int calcNodes(List<String> list) {
        Set<String> nodes = new TreeSet<>();
        for (String s : list) {
            for (int i = 0; i <= s.length(); i++) {
                nodes.add(s.substring(0, i));
            }
        }
        return nodes.size();
    }
}

class InputReader {
    BufferedReader br;
    StringTokenizer st;

    public InputReader(InputStream stream) {
        br = new BufferedReader(new InputStreamReader(stream));
    }

    public String next() {
        return nextToken();
    }

    public String nextToken() {
        while (st == null || !st.hasMoreTokens()) {
            String line = null;
            try {
                line = br.readLine();
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
            if (line == null) {
                return null;
            }
            st = new StringTokenizer(line);
        }
        return st.nextToken();
    }

    public int nextInt() {
        return Integer.parseInt(nextToken());
    }

}

