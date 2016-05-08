import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Scanner;
import java.util.TreeSet;

import static java.lang.Math.*;

public class ProblemD {
	String PROBLEM_ID = "problemD";

	enum TestType {
		EXAMPLE, SMALL, LARGE
	}

//	 TestType TYPE = TestType.EXAMPLE;
	 TestType TYPE = TestType.SMALL;
//	TestType TYPE = TestType.LARGE;

	public String getFileName() {
		String result = PROBLEM_ID + "_";
		switch (TYPE) {
		case EXAMPLE:
			result += "example";
			break;
		case SMALL:
			result += "small";
			break;
		case LARGE:
			result += "large";
			break;
		}
		return result;
	}

	public String getInFileName() {
		return getFileName() + ".in";
	}

	public String getOutFileName() {
		return getFileName() + ".out";
	}

	public static void main(String[] args) throws Exception {
		new ProblemD();
	}

	public ProblemD() throws Exception {
		BufferedReader in = new BufferedReader(new FileReader(getInFileName()));
		PrintWriter out = new PrintWriter(new BufferedWriter(new FileWriter(
				getOutFileName())));
		Scanner scan = new Scanner(in);
		int tests = scan.nextInt();
		for (int test = 0; test < tests; test++) {
			int m = scan.nextInt();
			int n = scan.nextInt();
			ArrayList<ArrayList<String>> parts = new ArrayList<ArrayList<String>>();
			for ( int i = 0; i < m; i++ ) {
				String s = scan.next();
				ArrayList<String> sParts = new ArrayList<String>();
				for ( int j = 1; j <= s.length(); j++) {
					sParts.add(s.substring(0, j));
				}
				parts.add(sParts);
			}
			int maxChoice = 1;
			ArrayList<TreeSet<String>> trees = new ArrayList<TreeSet<String>>();
			for ( int i = 0; i < n; i++) trees.add(new TreeSet<String>());
			for ( int i = 0; i < m; i++) maxChoice *= n;
			boolean[] filled = new boolean[n];
			int[] pick = new int[m];
			int bestSize = 0;
			int bestCount = 0;
			for ( int choice = 0; choice < maxChoice; choice++ ) {
				for ( int i = 0; i < n; i++) trees.get(i).clear();
				Arrays.fill(filled, false);
				int z = choice;
				for ( int i = 0; i < m; i++) {
					int p = z % n;
					z /= n;
					pick[i] = p;
					filled[p] = true;
				}
				boolean ok = true;
				for ( boolean a: filled ) ok &= a;
				if ( ok ) {
					for ( int i = 0; i < m; i++)  {
						TreeSet<String> subTree = trees.get(pick[i]);
						for ( String s: parts.get(i)) subTree.add(s);
					}
					int totalSize = 0;
					for ( TreeSet<String> subTree: trees ) {
						totalSize += 1 + subTree.size();
					}
					if ( totalSize > bestSize) {
						bestCount = 1;
						bestSize = totalSize;
					} else if ( totalSize == bestSize) {
						bestCount++;
					}
				}
			}
			String resultStr = String.format("Case #%d: %d %d", test + 1, bestSize, bestCount);
			// add answer here

			System.out.println(resultStr);
			out.println(resultStr);
		}
		out.close();
		System.out.println("*** in file =  " + getInFileName());
		System.out.println("*** out file = " + getOutFileName());
	}
}
