import static algorithm.zz.U.*;

import java.util.*;

/**
 * 高精度乘法
 * 测试链接：https://www.luogu.com.cn/problem/P1919
 */

public class A_times_B {    

    void solve() {
        char[] s1 = ns().toCharArray(), s2 = ns().toCharArray();
        int n = s1.length, m = s2.length;
        int[] a = new int[n], b = new int[m];
        for (int i = 0, j = n - 1; i < n; i++, j--) {
            a[i] = s1[j] - '0';
        }
        for (int i = 0, j = m - 1; i < m; i++, j--) {
            b[i] = s2[j] - '0';
        }
        int[] res = new FFT().mul(a, b);
        int[] ans = new int[n + m];
        for (int i = 0, p = 0; i < res.length || p > 0; i++) {
            p += i < res.length ? res[i] : 0;
            ans[i] = p % 10;
            p /= 10;
        }
        int i = n + m - 1;
        while (ans[i] == 0) {
            i--;
        }
        while (i >= 0) {
            print(ans[i--]);
        }
        writeln();
    }

}

class FFT {
    private double x, y, t, w1x, w1y, wkx, wky, ax, ay, bx, by;
    private int[] r;

    public int[] mul(int[] a, int[] b) {
        int n = a.length - 1, m = b.length - 1;
        int N = 1 << (32 - Integer.numberOfLeadingZeros(n + m));
        r = new int[N];
        for (int i = 0; i < N; i++) {
            r[i] = r[i >> 1] >> 1 | ((i & 1) == 0 ? 0 : N >> 1);
        }
        double[] Ax = new double[N], Ay = new double[N], Bx = new double[N], By = new double[N];
        for (int i = 0; i <= n; i++) {
            Ax[i] = a[i];
        }
        for (int i = 0; i <= m; i++) {
            Bx[i] = b[i];
        }
        fft(Ax, Ay, N, 1);
        fft(Bx, By, N, 1);
        for (int i = 0; i < N; i++) {
            x = Ax[i] * Bx[i] - Ay[i] * By[i];
            y = Ax[i] * By[i] + Ay[i] * Bx[i];
            Ax[i] = x;
            Ay[i] = y;
        }
        fft(Ax, Ay, N, -1);
        int[] ans = new int[n + m + 1];
        for (int i = 0; i <= n + m; i++) {
            ans[i] = (int) (Ax[i] / N + 0.5);
        }
        return ans;
    }

    private void fft(double[] Ax, double[] Ay, int n, int op) {
        for (int i = 0; i < n; i++) {
            if (i < r[i]) {
                t = Ax[i];
                Ax[i] = Ax[r[i]];
                Ax[r[i]] = t;
                t = Ay[i];
                Ay[i] = Ay[r[i]];
                Ay[r[i]] = t;
            }
        }
        for (int m = 2; m <= n; m <<= 1) {
            w1x = Math.cos(Math.PI * 2 / m);
            w1y = Math.sin(Math.PI * 2 / m) * op;
            for (int k = 0; k < n; k += m) {
                wkx = 1;
                wky = 0;
                for (int i = k, j = k + m / 2; i < k + m / 2; i++, j++) {
                    ax = Ax[i];
                    ay = Ay[i];
                    bx = Ax[j] * wkx - Ay[j] * wky;
                    by = Ax[j] * wky + Ay[j] * wkx;
                    Ax[i] = ax + bx;
                    Ay[i] = ay + by;
                    Ax[j] = ax - bx;
                    Ay[j] = ay - by;
                    x = w1x * wkx - w1y * wky;
                    y = w1x * wky + w1y * wkx;
                    wkx = x;
                    wky = y;
                }
            }
        }
    }
}