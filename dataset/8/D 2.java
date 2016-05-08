package Qualifications;

import java.util.Arrays;
import java.util.Scanner;

public class D {
    static int inf = 10;

    public static int war(double[] A, double[] B) {
        int p = 0;
        boolean[] used = new boolean[A.length];
        for (int i = B.length - 1; i >= 0; i--) {
            double last = inf;
            int index = -1;
            for (int j = 0; j < B.length; j++)
                if (B[j] > A[i] && A[j] < last && !used[j]) {
                    last = A[j];
                    index = j;
                }
            if (index == -1) {
                for (int j = 0; j < B.length; j++)
                    if (!used[j]) {
                        used[j] = true;
                        p++;
                        break;
                    }
            } else {
                used[index] = true;
            }
        }
        return p;
    }

    public static int dWar(double[] A, double[] B) {
        int p = 0;
        boolean[] usedA = new boolean[A.length];
        boolean[] usedB = new boolean[B.length];
        for (int i = B.length - 1; i >= 0;) {
            if (usedA[i]) {
                i--;
                continue;
            }
            double last = inf;
            int index = -1;
            for (int j = 0; j < B.length; j++)
                if (B[j] > A[i] && A[j] < last && !usedB[j]) {
                    last = A[j];
                    index = j;
                }
            if (index == -1) {
                for (int j = 0; j < B.length; j++)
                    if (!usedB[j]) {
                        usedB[j] = true;
                        index = j;
                        p++;
                        break;
                    }
                for (int j = 0; j < A.length; j++)
                    if (A[j] > B[index] && !usedA[j]) {
                        usedA[j] = true;
                        break;
                    }
            } else {
                usedB[index] = true;
                for (int j = 0; j < A.length; j++)
                    if (!usedA[j]) {
                        usedA[j] = true;
                        break;
                    }
            }
        }
        return p;
    }

    public static void main(String[] args) {
        Scanner in = new Scanner(System.in);
        int t = in.nextInt();
        for (int _ = 1; _ <= t; _++) {
            int n = in.nextInt();
            double[] A = new double[n];
            double[] B = new double[n];
            for (int i = 0; i < n; i++)
                A[i] = in.nextDouble();
            for (int i = 0; i < n; i++)
                B[i] = in.nextDouble();
            Arrays.sort(A);
            Arrays.sort(B);
            System.out.println("Case #" + _ + ": " + dWar(A, B) + " "
                    + war(A, B));
        }
    }
}
