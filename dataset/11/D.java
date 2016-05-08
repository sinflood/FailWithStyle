import java.io.BufferedReader;
import java.io.FileReader;
import java.io.PrintWriter;
import java.util.Arrays;
import java.util.StringTokenizer;

public class D {
	static class Node {
		Node[] next;
		int size;

		public Node() {
			next = new Node[26];
			size = 0;
		}

		public void insert(char[] str) {
			Node current = this;
			current.size++;
			for (int i = 0; i < str.length; i++) {
				if (current.next[str[i] - 'A'] == null)
					current.next[str[i] - 'A'] = new Node();
				current = current.next[str[i] - 'A'];
				current.size++;
			}
		}

		public int count() {
			int result = 1;
			for (int i = 0; i < next.length; i++) {
				if (next[i] != null)
					result += next[i].count();
			}
			return result;
		}
	}

	static int[] ways = new int[10000];
	static char[][] strings;
	static int n;

	public static void partition(int position, int[] assignments) {
		if (position == strings.length) {
			Node[] tries = new Node[n];
			for (int i = 0; i < n; i++)
				tries[i] = new Node();
			for (int i = 0; i < assignments.length; i++)
				tries[assignments[i]].insert(strings[i]);
			for (int i = 0; i < tries.length; i++)
				if (tries[i].size == 0)
					return;
			int total = 0;
			for (int i = 0; i < tries.length; i++)
				total += tries[i].count();
			ways[total]++;
		} else {
			for (int i = 0; i < n; i++) {
				assignments[position] = i;
				partition(position + 1, assignments);
			}
		}
	}

	public static void main(String[] args) throws Exception {
		BufferedReader in = new BufferedReader(new FileReader("D.in"));
		PrintWriter out = new PrintWriter("D.out");
		StringTokenizer strtok = new StringTokenizer(in.readLine());
		int tc = Integer.parseInt(strtok.nextToken());
		for (int cc = 1; cc <= tc; cc++) {
			strtok = new StringTokenizer(in.readLine());
			int m = Integer.parseInt(strtok.nextToken());
			n = Integer.parseInt(strtok.nextToken());
			Arrays.fill(ways, 0);
			strings = new char[m][];
			for (int i = 0; i < m; i++)
				strings[i] = in.readLine().toCharArray();
			partition(0, new int[m]);
			for (int i = ways.length - 1; i >= 0; i--) {
				if (ways[i] != 0) {
					System.out.printf("Case #%d: %d %d\n", cc, i, ways[i]);
					out.printf("Case #%d: %d %d\n", cc, i, ways[i]);
					break;
				}
			}
		}
		out.close();
	}
}
