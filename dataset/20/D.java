package gcj2014.r2;
import java.io.File;
import java.io.PrintWriter;
import java.io.StringWriter;
import java.text.SimpleDateFormat;
import java.util.ArrayDeque;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.List;
import java.util.Queue;
import java.util.Scanner;
import java.util.concurrent.CompletionService;
import java.util.concurrent.ExecutorCompletionService;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class D implements Runnable {
	static final boolean LARGE = false;
	static final boolean PROD = true;
	static final int NTHREAD = 1;
	static String BASEPATH = "x:\\gcj\\";
//	static String BASEPATH = "/home/ec2-user/";
	
	static String INPATH = BASEPATH + D.class.getSimpleName().charAt(0) + (LARGE ? "-large.in" : "-small-attempt0.in");
//	static String INPATH = BASEPATH + TParallel.class.getSimpleName().charAt(0) + (LARGE ? "-large-practice.in" : "-small-practice.in");
	static String OUTPATH = INPATH.substring(0, INPATH.length()-3) + new SimpleDateFormat("-HHmmss").format(new Date()) + ".out";
	
	static String INPUT = "";
	int m, n;
	char[][] s;
	
	public void read() // not parallelized
	{
		m = ni();
		n = ni();
		s = new char[m][];
		for(int i = 0;i < m;i++){
			s[i] = in.next().toCharArray();
		}
	}
	
	public void process() // parallelized!
	{
		int[] gens = new int[1<<m];
		for(int i = 0;i < 1<<m;i++){
			TrieNew trie = new TrieNew();
			for(int j = 0;j < m;j++){
				if(i<<31-j<0)trie.add(s[j]);
			}
			gens[i] = trie.gen;
		}
		
		int[] a = new int[m];
		int max = -1, maxct = 0;
		inner:
		do{
			int[] ptns = new int[n];
			for(int i = 0;i < m;i++){
				ptns[a[i]] |= 1<<i;
			}
			for(int i = 0;i < n;i++){
				if(ptns[i] == 0)continue inner;
			}
			int s = 0;
			for(int ptn : ptns){
				s += gens[ptn];
			}
			if(s > max){
				max = s;
				maxct = 1;
			}else if(s == max){
				maxct++;
			}
		}while(inc(a, n));
		out.println(max + " " + maxct);
	}
	
	public static boolean inc(int[] a, int base)
	{
		int n = a.length;
		int i;
		for(i = n - 1;i >= 0 && a[i] == base - 1;i--);
		if(i == -1)return false;
		
		a[i]++;
		Arrays.fill(a, i + 1, n, 0);
		return true;
	}
	
	public static class TrieNew {
		public Node root = new Node((char)0, 0);
		public int gen = 1;
		
		public static class Node
		{
			public int id;
			public char c;
			public Node next, firstChild;
			public int hit = 0;
			
			public Node fail;
			
			public Node(char c, int id)
			{
				this.id = id;
				this.c = c;
			}
			
			public String toString(String indent)
			{
				StringBuilder sb = new StringBuilder();
				sb.append(indent + id + ":" + c);
				if(hit != 0)sb.append(" H:" + hit);
				if(fail != null)sb.append(" F:" + fail.id);
				sb.append("\n");
				for(Node c = firstChild;c != null; c = c.next){
					sb.append(c.toString(indent + "  "));
				}
				return sb.toString();
			}
		}
		
		public void add(char[] s)
		{
			Node cur = root;
			Node pre = null;
			for(char c : s){
				pre = cur; cur = cur.firstChild;
				if(cur == null){
					cur = pre.firstChild = new Node(c, gen++);
				}else{
					for(;cur != null && cur.c != c;pre = cur, cur = cur.next);
					if(cur == null)cur = pre.next = new Node(c, gen++);
				}
			}
			cur.hit++;
		}
		
		public void buildFailure()
		{
			root.fail = null;
			Queue<Node> q = new ArrayDeque<Node>();
			q.add(root);
			while(!q.isEmpty()){
				Node cur = q.poll();
				inner:
				for(Node ch = cur.firstChild;ch != null; ch = ch.next){
					q.add(ch);
					for(Node to = cur.fail; to != null; to = to.fail){
						for(Node lch = to.firstChild;lch != null; lch = lch.next){
							if(lch.c == ch.c){
								ch.fail = lch;
								ch.hit += lch.hit; // hit伝播
								continue inner;
							}
						}
					}
					ch.fail = root;
				}
			}
		}
		
		public Node next(Node cur, char c)
		{
			for(;cur != null;cur = cur.fail){
				for(Node ch = cur.firstChild;ch != null; ch = ch.next){
					if(ch.c == c)return ch;
				}
			}
			return root;
		}
		
		public void search(char[] q)
		{
			Node cur = root;
			outer:
			for(char c : q){
				for(;cur != null;cur = cur.fail){
					for(Node ch = cur.firstChild;ch != null; ch = ch.next){
						if(ch.c == c){
							// ch.hit
							cur = ch;
							continue outer;
						}
					}
				}
				cur = root;
			}
		}
		
		public int countHit(char[] q)
		{
			Node cur = root;
			int hit = 0;
			outer:
			for(char c : q){
				for(;cur != null;cur = cur.fail){
					for(Node ch = cur.firstChild;ch != null; ch = ch.next){
						if(ch.c == c){
							hit += ch.hit; // hitを使う
							cur = ch;
							continue outer;
						}
					}
				}
				cur = root;
			}
			return hit;
		}
		
		public Node[] toArray()
		{
			Queue<Node> q = new ArrayDeque<Node>();
			q.add(root);
			Node[] ret = new Node[gen];
			while(!q.isEmpty()){
				Node cur = q.poll();
				ret[cur.id] = cur;
				if(cur.next != null)q.add(cur.next);
				if(cur.firstChild != null)q.add(cur.firstChild);
			}
			return ret;
		}
		
		public String toString()
		{
			return root.toString("");
		}
	}
	
	public static void preprocess()
	{
	}
	
	Scanner in;
	PrintWriter out;
	StringWriter sw;
	int cas;
	static List<Status> running = new ArrayList<Status>();
	
	@Override
	public void run()
	{
		long S = System.nanoTime();
		// register
		synchronized(running){
			Status st = new Status();
			st.id = cas;
			st.S = S;
			running.add(st);
		}
		process();
		// deregister
		synchronized(running){
			for(Status st : running){
				if(st.id == cas){
					running.remove(st);
					break;
				}
			}
		}
		long G = System.nanoTime();
		
		if(PROD){
			System.err.println("case " + cas + " solved. [" + (G-S)/1000000 + "ms]");
			synchronized(running){
				StringBuilder sb = new StringBuilder("running : ");
				for(Status st : running){
					sb.append(st.id + ":" + (G-st.S)/1000000 + "ms, ");
				}
				System.err.println(sb);
			}
		}
	}
	
	private static class Status {
		public int id;
		public long S;
	}
	
	public D(int cas, Scanner in)
	{
		this.cas = cas;
		this.in = in;
		this.sw = new StringWriter();
		this.out = new PrintWriter(this.sw);
	}
	
	private int ni() { return Integer.parseInt(in.next()); }
	private long nl() { return Long.parseLong(in.next()); }
	private int[] na(int n) { int[] a = new int[n]; for(int i = 0;i < n;i++)a[i] = ni(); return a; }
	private double nd() { return Double.parseDouble(in.next()); }
	private void tr(Object... o) { if(!PROD)System.out.println(Arrays.deepToString(o)); }
	
	public static void main(String[] args) throws Exception
	{
		long start = System.nanoTime();
		
		ExecutorService es = Executors.newFixedThreadPool(NTHREAD);
		CompletionService<D> cs = new ExecutorCompletionService<D>(es);
		
		if(PROD){
			System.out.println("INPATH : " + INPATH);
			System.out.println("OUTPATH : " + OUTPATH);
		}
		Scanner in = PROD ? new Scanner(new File(INPATH)) : new Scanner(INPUT);
		PrintWriter out = PROD ? new PrintWriter(new File(OUTPATH)) : new PrintWriter(System.out);
		int n = in.nextInt();
		in.nextLine();
		
		preprocess();
		for(int i = 0;i < n;i++){
			D runner = new D(i+1, in);
			runner.read();
			cs.submit(runner, runner);
		}
		es.shutdown();
		String[] outs = new String[n];
		for(int i = 0;i < n;i++){
			D runner = cs.take().get(); // not ordered
			runner.out.flush();
			runner.out.close();
			outs[runner.cas-1] = runner.sw.toString();
		}
		for(int i = 0;i < n;i++){
			out.printf("Case #%d: ", i+1);
			out.append(outs[i]);
			out.flush();
		}
		
		long end = System.nanoTime();
		System.out.println((end - start)/1000000 + "ms");
		if(PROD){
			System.out.println("INPATH : " + INPATH);
			System.out.println("OUTPATH : " + OUTPATH);
		}
	}
}
