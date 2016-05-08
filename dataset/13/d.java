import java.util.*;
public class d {
public static void main(String[] args)
{
	Scanner input = new Scanner(System.in);
	int T = input.nextInt();
	for(int t = 0; t<T; t++)
	{
		System.out.printf("Case #%d: ", t+1);
		m = input.nextInt(); n = input.nextInt();
		data = new String[m];
		for(int i = 0; i<m; i++) data[i] = input.next();
		max = ways = 0;
		go(new int[m], 0);
		System.out.println(max+" "+ways);
	}
}
static int m, n;
static String[] data;
static int max, ways;
static void go(int[] assign, int at)
{
	if(at == m)
	{
		HashSet<String>[] sets = new HashSet[n];
		for(int i = 0; i<n; i++) sets[i] = new HashSet<String>();
		for(int i = 0; i<m; i++)
		{
			for(int j = 0; j<=data[i].length(); j++)
				sets[assign[i]].add(data[i].substring(0, j));
		}
		int tot = 0;
		for(int i = 0; i<n; i++) tot += sets[i].size();
		if(tot > max)
		{
			max = tot;
			ways = 1;
		}
		else if(tot == max) ways++;
		return;
	}
	for(int i = 0; i<n; i++)
	{
		assign[at] = i;
		go(assign, at+1);
	}
}
}
