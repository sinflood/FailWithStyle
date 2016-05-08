import java.io.*;
import java.util.*;

public class Main {
    static int war(double[] naomi, double[] ken) {
        int N = naomi.length;
        int res = N;
        for (int i = 0, j = 0; i < N && j < N; ++i) {
            while (j < N && ken[j] < naomi[i]) {
                ++j;
            }
            if (j == N) {
                break;
            } else {
                --res;
                ++j;
            }
        }
        return res;
    }
    
    static int deceitfulWar(double[] naomi, double[] ken) {
        int N = naomi.length;
        int head = 0, tail = N - 1;
        int index = N - 1;
        int res = 0;
        while (tail >= head) {
            if (naomi[tail] > ken[index]) {
                --tail;
                ++res;
            } else {
                ++head;
            }
            --index;
        }
        return res;
    }
    
    public static void main(String[] args) throws Exception {
        Scanner scan = new Scanner(System.in);
        int taskCount = scan.nextInt();
        for (int taskIndex = 1; taskIndex <= taskCount; ++taskIndex) {
            int N = scan.nextInt();
            double[] naomi = new double[N];
            for (int i = 0; i < N; ++i) {
                naomi[i] = scan.nextDouble();
            }
            double[] ken = new double[N];
            for (int i = 0; i < N; ++i) {
                ken[i] = scan.nextDouble();
            }
            Arrays.sort(naomi);
            Arrays.sort(ken);
            System.out.println(String.format("Case #%d: %d %d", taskIndex, deceitfulWar(naomi, ken), war(naomi, ken)));
        }
    }
}