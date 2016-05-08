package gcj2014.q;
import java.io.File;
import java.io.PrintWriter;
import java.text.SimpleDateFormat;
import java.util.Arrays;
import java.util.Date;
import java.util.Scanner;

public class DSmall {
	static String BASEPATH = "x:\\gcj\\";
	static boolean LARGE = false;
	static String INPATH = BASEPATH + DSmall.class.getSimpleName().charAt(0) + (LARGE ? "-large.in" : "-small-attempt2.in");
	static String OUTPATH = INPATH.substring(0, INPATH.length()-3) + new SimpleDateFormat("-HHmmss").format(new Date()) + ".out";
	
	static String INPUT = "";
	
	public void call()
	{
		int n = ni();
		double[] a = new double[n];
		for(int i = 0;i < n;i++)a[i] = in.nextDouble();
		double[] b = new double[n];
		for(int i = 0;i < n;i++)b[i] = in.nextDouble();
		Arrays.sort(a);
		Arrays.sort(b);
		
		int[] ord = new int[n];
		for(int i = 0;i < n;i++)ord[i] = i;
		int mdef = 0, mche = 0;
		do{
			int scdef = 0;
			boolean[] sed = new boolean[n];
			for(int j = 0;j < n;j++){
				boolean done = false;
				for(int i = 0;i < n;i++){
					if(a[ord[j]] < b[i] && !sed[i]){
						sed[i] = true;
						done = true;
						break;
					}
				}
				if(!done){
					for(int i = 0;i < n;i++){
						if(!sed[i]){
							sed[i] = true;
							scdef++;
							break;
						}
					}
				}
			}
			
			sed = new boolean[n];
			int scche = 0;
			for(int j = 0;j < n;j++){
				int maxi = -1;
				for(int i = n-1;i >= 0;i--){
					if(!sed[i]){
						maxi = i;
						break;
					}
				}
				int mini = -1;
				for(int i = 0;i < n;i++){
					if(!sed[i]){
						mini = i;
						break;
					}
				}
				if(a[ord[j]] > b[mini]){
					sed[mini] = true;
					scche++;
				}else if(a[ord[j]] < b[maxi]){
					sed[maxi] = true;
				}else{
					for(int i = 0;i < n;i++){
						if(!sed[i]){
							sed[i] = true;
							scche++;
							break;
						}
					}
				}
			}
			
			mdef = Math.max(mdef, scdef);
			mche = Math.max(mche, scche);
		}while(nextPermutation(ord));
		out.println(mche + " " + mdef);
	}
	
	public static boolean nextPermutation(int[] a)
	{
		int n = a.length;
		int i;
		for(i = n - 2;i >= 0 && a[i] > a[i+1];i--);
		if(i == -1)return false;
		int j;
		for(j = i + 1;j < n && a[i] < a[j];j++);
		int d = a[i]; a[i] = a[j - 1]; a[j - 1] = d;
		for(int p = i + 1, q = n - 1;p < q;p++,q--){
			d = a[p]; a[p] = a[q]; a[q] = d;
		}
		return true;
	}
	
	Scanner in;
	PrintWriter out;
	int cas;
	
	public DSmall(int cas, Scanner in, PrintWriter out)
	{
		this.cas = cas;
		this.in = in;
		this.out = out;
	}
	
	int ni() { return Integer.parseInt(in.next()); }
	long nl() { return Long.parseLong(in.next()); }
	double nd() { return Double.parseDouble(in.next()); }
	void tr(Object... o) { if(INPUT.length() != 0)System.out.println(Arrays.deepToString(o)); }
	
	public static void main(String[] args) throws Exception
	{
		long start = System.currentTimeMillis();
		boolean real = INPUT.isEmpty();
		
		if(real){
			System.out.println("INPATH : " + INPATH);
			System.out.println("OUTPATH : " + OUTPATH);
		}
		Scanner in = real ? new Scanner(new File(INPATH)) : new Scanner(INPUT);
		PrintWriter out = real ? new PrintWriter(new File(OUTPATH)) : new PrintWriter(System.out);
		int n = in.nextInt();
		in.nextLine();
		
		for(int i = 0;i < n;i++){
			out.printf("Case #%d: ", i+1);
			new DSmall(i+1, in, out).call();
			out.flush();
			if(real)System.err.println("case " + (i + 1) + " solved.\t");
		}
		
		long end = System.currentTimeMillis();
		System.out.println((end - start) + "ms");
		if(real){
			System.out.println("INPATH : " + INPATH);
			System.out.println("OUTPATH : " + OUTPATH);
		}
	}
}
