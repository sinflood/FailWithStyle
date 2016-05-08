import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;

public class B_N {
    static String[] cars;
    static int n;
    static int ans = 0;
    static int[] order;

    public static void rec(int index) {
        if (index == n) {
            boolean[] visited = new boolean[26];
            for (int i = 0; i < n; i++) {
                if (i > 0) {
                    if (cars[order[i - 1]]
                            .charAt(cars[order[i - 1]].length() - 1) != cars[order[i]]
                            .charAt(0)
                            && visited[cars[order[i]].charAt(0) - 'a']) {
                        return;
                    }
                }
                for (int j = 1; j < cars[order[i]].length(); j++) {
                    if (cars[order[i]].charAt(j) != cars[order[i]].charAt(j - 1)) {
                        if (visited[cars[order[i]].charAt(j) - 'a'])
                            return;
                        visited[cars[order[i]].charAt(j - 1) - 'a'] = true;
                    }
                }
                visited[cars[order[i]].charAt(cars[order[i]].length() - 1) - 'a'] = true;
            }
            ans++;
        } else {
            for (int j = index; j < n; j++) {
                int temp = order[j];
                order[j] = order[index];
                order[index] = temp;
                rec(index + 1);
                temp = order[j];
                order[j] = order[index];
                order[index] = temp;
            }
        }
    }

    public static void main(String[] args) throws NumberFormatException,
            IOException {
        BufferedReader in = new BufferedReader(new InputStreamReader(System.in));
        int t = Integer.parseInt(in.readLine());
        for (int _ = 1; _ <= t; _++) {
            n = Integer.parseInt(in.readLine());
            cars = in.readLine().split(" ");
            ans = 0;
            order = new int[n];
            for (int i = 0; i < n; i++)
                order[i] = i;
            rec(0);
            System.out.println("Case #" + _ + ": " + ans);
        }
    }
}
