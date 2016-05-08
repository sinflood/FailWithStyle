import java.util.*;
import java.io.*;
public class DSmall
{
	public static String[] strings;
	public static int n;
	public static int[] server;
	public static int maxSize;
	public static int count;
	
	public static void main(String[] args) throws Exception
	{
//		Scanner in = new Scanner(System.in);
		Scanner in = new Scanner(new File("d-small-attempt0.in"));
		PrintWriter out = new PrintWriter(new FileWriter(new File("dsmallattempt0.out")));
		
		int t = in.nextInt();
		for(int x = 0; x < t; x++)
		{
			int m = in.nextInt();
			n = in.nextInt();
			
			strings = new String[m];
			for(int y = 0; y < m; y++)
			{
				strings[y] = in.next();
			}
			
			server = new int[m];
			maxSize = 0;
			count = 0;
			
			subsets(0);
			
			out.println("Case #" + (x + 1) + ": " + maxSize + " " + count);
		}
		
		out.close();
	}
	
	public static void subsets(int index)
	{
		if(index == strings.length)
		{
			getSize();
		}
		else
		{
			for(int i = 0; i < n; i++)
			{
				server[index] = i;
				subsets(index + 1);
			}
		}
	}
	
	public static void getSize()
	{
		Trie[] tries = new Trie[n];
		for(int j = 0; j < tries.length; j++)
		{
			tries[j] = new Trie();
		}
		
		for(int i = 0; i < strings.length; i++)
		{
			tries[server[i]].add(strings[i]);
		}
		
		int current = 0;
		for(int k = 0; k < tries.length; k++)
		{
			if(tries[k].size == 1)
			{
				return;
			}
			
			current += tries[k].size;
		}
		
		if(current > maxSize)
		{
			maxSize = current;
			count = 1;
		}
		else if(current == maxSize)
		{
			count++;
		}
	}
	
	static class Trie
	{
		Node root;
		int size;
		
		public Trie()
		{
			root = new Node(' ');
			size = 1;
		}
		
		public void add(String str)
		{
			add(root, str, 0);
		}
		
		public void add(Node root, String str, int index)
		{
			if(index == str.length())
			{
				return;
			}
			
			for(Node child : root.children)
			{
				if(child.letter == str.charAt(index))
				{
					add(child, str, index + 1);
					return;
				}
			}
			
			Node newNode = new Node(str.charAt(index));
			root.children.add(newNode);
			size++;
			add(newNode, str, index + 1);
		}
	}
	
	static class Node
	{
		char letter;
		ArrayList<Node> children;
		
		public Node(char l)
		{
			letter = l;
			children = new ArrayList<Node>();
		}
	}
}
