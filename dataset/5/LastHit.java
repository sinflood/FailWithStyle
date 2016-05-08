package google.codejam;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.util.Arrays;
import java.util.Scanner;

public class LastHit {
	public static void main(String[] args) {
		try {
			Scanner in = new Scanner(new BufferedReader(new FileReader(new File("B-large.in")), 256 << 10));
			BufferedWriter out = new BufferedWriter(new FileWriter(new File("output.txt")), 256 << 10);

			int testsNumber = in.nextInt();
			for (int testId = 1; testId <= testsNumber; testId++) {
				int d = in.nextInt(), t = in.nextInt(), n = in.nextInt();
				int[] health = new int[n];
				int[] gold = new int[n];
				for (int i = 0; i < n; i++) {
					health[i] = in.nextInt();
					gold[i] = in.nextInt();
				}
				long[][] maxgold = new long[n+1][20*n+1];
				for (int i = 0; i <= n; i++) Arrays.fill(maxgold[i], -1);
				maxgold[0][1] = 0;
				for (int i = 0; i < n; i++) {
					for (int stacks = 0; stacks <= 20*n; stacks++) 
					if (maxgold[i][stacks] >= 0) {
						int towerHits = health[i] / t;
						int towerRem = health[i] % t;
						if (towerRem == 0) {
							towerHits--;
							towerRem = t;
						}
						int dianaHits = towerRem / d;
						int dianaRem =  towerRem % d;
						if (dianaRem > 0) dianaHits++;
						maxgold[i+1][stacks+towerHits+1] = Math.max(maxgold[i+1][stacks+towerHits+1], maxgold[i][stacks]);
						if (stacks+towerHits-dianaHits >= 0) {
							maxgold[i+1][stacks+towerHits-dianaHits] = Math.max(maxgold[i+1][stacks+towerHits-dianaHits] , maxgold[i][stacks] + gold[i]);
						}
					}
				}
				long max = 0;
				for (int i = 0; i <= 20*n; i++) max = Math.max(max, maxgold[n][i]);
				out.append("Case #" + testId + ": " + max);
				out.append("\n");
			}
			in.close();
			out.close();
		}
		catch (Exception e) {
			System.err.println("Error:" + e.getMessage());
		}
	}
}
