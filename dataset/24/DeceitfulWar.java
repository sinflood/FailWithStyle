
import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.PrintWriter;
import java.util.Arrays;
import java.util.Scanner;
import java.util.TreeSet;

public class DeceitfulWar {
	String PROBLEM_ID = "problemD";

	enum TestType {
		EXAMPLE, SMALL, LARGE
	}

//	 TestType TYPE = TestType.EXAMPLE;
//	 TestType TYPE = TestType.SMALL;
	TestType TYPE = TestType.LARGE;

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
		new DeceitfulWar();
	}

	public DeceitfulWar() throws Exception {
		BufferedReader in = new BufferedReader(new FileReader(getInFileName()));
		PrintWriter out = new PrintWriter(new BufferedWriter(new FileWriter(
				getOutFileName())));
		Scanner scan = new Scanner(in);
		int tests = scan.nextInt();
		for (int test = 0; test < tests; test++) {
			int n = scan.nextInt();
			double[] naomi = new double[n];
			double[] ken = new double[n];
			for ( int i = 0; i < n; i++ ) naomi[i] = scan.nextDouble();
			for ( int i = 0; i < n; i++ ) ken[i] = scan.nextDouble();
			Arrays.sort(naomi);
			Arrays.sort(ken);
//			System.out.println(Arrays.toString(naomi));
//			System.out.println(Arrays.toString(ken));
			int baseScore = 0;
			TreeSet<Double> values = new TreeSet<Double>();
			for ( int j = 0; j < n; j++) values.add(ken[j]);
			for ( int j = 0; j < n; j++) {
				Double z = values.ceiling(naomi[j]);
				if ( z == null ) {
					baseScore++;
					values.pollFirst();							
				} else {
					values.remove(z);
				}
			}
			int fakeScore = 0;
			for ( int fake = 0; fake <= n; fake++) {
				if ( fake == 0 || naomi[fake-1] < ken[n-fake]) {
					int score2 = 0;
					int index = fake;
					for ( int i = 0; i < n - fake; i++) {
						while ( index < n && naomi[index] < ken[i] ) index++;
						if ( index < n) {
							score2++;
							index++;
						}
					}
//					System.out.printf("fake = %d score = %d\n", fake, score2);
//					if ( score != score2) System.out.printf("score old %d new %d\n", score, score2);
					fakeScore = Math.max(fakeScore, score2);
				}
			}
			fakeScore = Math.max(fakeScore, baseScore);
			String resultStr = String.format("Case #%d: %d %d", test + 1, fakeScore, baseScore);
			// add answer here

			System.out.println(resultStr);
			out.println(resultStr);
		}
		out.close();
		System.out.println("*** in file =  " + getInFileName());
		System.out.println("*** out file = " + getOutFileName());
	}
}

