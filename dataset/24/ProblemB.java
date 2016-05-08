import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.PrintWriter;
import java.util.HashMap;
import java.util.Scanner;

import static java.lang.Math.*;

public class ProblemB {
	String PROBLEM_ID = "problemB";

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
		new ProblemB();
	}

	public ProblemB() throws Exception {
		BufferedReader in = new BufferedReader(new FileReader(getInFileName()));
		PrintWriter out = new PrintWriter(new BufferedWriter(new FileWriter(
				getOutFileName())));
		Scanner scan = new Scanner(in);
		int tests = scan.nextInt();
		for (int test = 0; test < tests; test++) {
			diana = scan.nextInt();
			tower = scan.nextInt();
			n = scan.nextInt();
			hp = new int[n];
			gold = new int[n];
			for ( int i = 0; i < n; i++ ) {
				hp[i] = scan.nextInt();
				gold[i] = scan.nextInt();
			}
			memo = new HashMap<Long, Integer>();
			int answer = go(0, hp[0], 0, 0);
//			System.out.println("number of states "+memo.size());
			String resultStr = String.format("Case #%d: %d", test + 1, answer);
			// add answer here

			System.out.println(resultStr);
			out.println(resultStr);
		}
		out.close();
		System.out.println("*** in file =  " + getInFileName());
		System.out.println("*** out file = " + getOutFileName());
	}
	
	int diana, tower, n;
	int[] hp, gold;
	HashMap<Long, Integer> memo;
	public int go(int index, int life, int shots, int turn) {
		if ( index == n ) return 0;
		long id = ((index*1000L + life)*10000 + shots)*2 + turn;
		if ( memo.containsKey(id)) return memo.get(id);
		int result = 0;
		int nextFullHP = index + 1 < n ? hp[index+1] : 0;
		int nextTurn = 1 - turn;
		if ( turn == 1 ) {
			// tower kills
			if ( life > tower ) result = go(index, life - tower, shots, nextTurn);
			else result = go(index+1, nextFullHP, shots, nextTurn);
			// diana spent shots on it
			if ( shots*diana >= life ) {
				int need = 1+(life-1)/diana;
				int kill = gold[index] + go(index+1, nextFullHP,shots-need, turn);
				result = max(result, kill);
			}
		} else {
			// diana's turn
			// skip
			result = go(index, life, shots+1, nextTurn);
			// regular shot
			int regular = 0;
			if ( life > diana ) regular = go(index, life - diana, shots, nextTurn);
			else regular = gold[index] + go(index+1, nextFullHP, shots, nextTurn);
			result = max(result, regular);
			int kill = 0;
			if ( (shots+1)*diana >= life ) {
				int need = 1+(life-1)/diana;
				kill = gold[index] + go(index+1, nextFullHP, shots - (need-1), nextTurn);
			}
			result = max(result, kill);
		}
		memo.put(id, result);
		return result;
	}
}
