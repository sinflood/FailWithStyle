package Rnd1B;

import java.util.Scanner;

public class B {
    static String a, b, k;
    static long DP[][][][];

    public static long get(int index, int a_s, int b_s, int k_s) {
        if (index == 32)
            return 1;

        if (DP[index][a_s][b_s][k_s] != -1)
            return DP[index][a_s][b_s][k_s];
        long ans = 0;
        for (int i = 0; i < 2; i++)
            for (int j = 0; j < 2; j++) {
                if ((i > (a.charAt(index) - '0') && a_s == 0)
                        || (j > (b.charAt(index) - '0') && b_s == 0)
                        || ((i & j) > (k.charAt(index) - '0') && k_s == 0))
                    continue;
                int temp_as = (a_s == 1 || (i < (a.charAt(index) - '0'))) ? 1
                        : 0;
                int temp_bs = (b_s == 1 || (j < (b.charAt(index) - '0'))) ? 1
                        : 0;
                int temp_ks = (k_s == 1 || ((i & j) < (k.charAt(index) - '0'))) ? 1
                        : 0;
                ans += get(index + 1, temp_as, temp_bs, temp_ks);
            }
        return DP[index][a_s][b_s][k_s] = ans;
    }

    public static void main(String[] args) {
        Scanner in = new Scanner(System.in);
        int t = in.nextInt();
        for (int _ = 1; _ <= t; _++) {
            DP = new long[32][2][2][2];
            for (int i = 0; i < 32; i++)
                for (int j = 0; j < 2; j++)
                    for (int k = 0; k < 2; k++)
                        DP[i][j][k][0] = DP[i][j][k][1] = -1;
            a = extend(Integer.toBinaryString(in.nextInt() - 1));
            b = extend(Integer.toBinaryString(in.nextInt() - 1));
            k = extend(Integer.toBinaryString(in.nextInt() - 1));
            System.out.println("Case #" + _ + ": " + get(0, 0, 0, 0));
        }
    }

    private static String extend(String binaryString) {
        while (binaryString.length() < 32) {
            binaryString = "0" + binaryString;
        }
        return binaryString;
    }
}
