import java.util.*;
import java.io.*;
public class B
{
	public static int[] hit;
	public static int[] gold;
	public static int[] diane;
	public static int[] tower;
	public static int[][] memo;
	
	public static void main(String[] args) throws Exception
	{
//		Scanner in = new Scanner(System.in);
		Scanner in = new Scanner(new File("b-large.in"));
		PrintWriter out = new PrintWriter(new FileWriter(new File("blarge.out")));
		
		int t = in.nextInt();
		for(int x = 0; x < t; x++)
		{
			int p = in.nextInt();
			int q = in.nextInt();
			int n = in.nextInt();
			
			hit = new int[n];
			gold = new int[n];
			diane = new int[n];
			tower = new int[n];
			for(int y = 0; y < n; y++)
			{
				hit[y] = in.nextInt();
				gold[y] = in.nextInt();
				diane[y] = ((hit[y] - 1) % q) / p + 1;
				tower[y] = (hit[y] - 1) / q + 1;
			}
			
			memo = new int[n][10 * n + 100];
			for(int z = 0; z < memo.length; z++)
			{
				Arrays.fill(memo[z], -1);
			}
			
			int result = fight(0, 1);
			
			out.println("Case #" + (x + 1) + ": " + result);
		}
		
		out.close();
	}
	
	public static int fight(int index, int ahead)
	{
		if(index == hit.length)
		{
			return 0;
		}
		else if(memo[index][ahead] != -1)
		{
			return memo[index][ahead];
		}
		else
		{
			int best = fight(index + 1, ahead + tower[index]);
			
			if(diane[index] < tower[index] + ahead)
			{
				best = Math.max(best, gold[index] + fight(index + 1, ahead + tower[index] - diane[index] - 1));
			}
			
			return memo[index][ahead] = best;
		}
	}
}
