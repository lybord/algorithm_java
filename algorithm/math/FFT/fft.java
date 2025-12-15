package algorithm.math.FFT;
import static algorithm.zz.U.*;
import java.util.*;

/**
 * 多项式乘法
 * 测试链接：https://www.luogu.com.cn/problem/P3803
 */
public class fft {        

    void solve() {
        int n = ni(), m = ni();
        int tot = 1 << (32 - Integer.numberOfLeadingZeros(n + m));
        Complex[] A = new Complex[tot], B = new Complex[tot];
        for (int i = 0; i <= n; i++) {
            A[i] = new Complex(ni(), 0);
        }
        for (int i = 0; i <= m; i++) {
            B[i] = new Complex(ni(), 0);
        }
        for (int i = n + 1; i < tot; i++) {
            A[i] = Complex.ZERO;
        }
        for (int i = m + 1; i < tot; i++) {
            B[i] = Complex.ZERO;
        }
        FFT(A, tot, 1);
        FFT(B, tot, 1);
        for (int i = 0; i < tot; i++) {
            A[i] = A[i].mul(B[i]);
        }
        FFT(A, tot, -1);
        for (int i = 0; i <= n + m; i++) {
            print((int) (A[i].x / tot + 0.5) + " ");
        }
        writeln();
    }

    // 迭代版 + 位逆序变换（蝴蝶变换）
    void FFT(Complex[] A, int n, int op) {
        int[] r = new int[n];
        for (int i = 0; i < n; i++) {
            r[i] = r[i >> 1] >> 1 | ((i & 1) == 0 ? 0 : n >> 1);
        }
        for (int i = 0; i < n; i++) {
            if (i < r[i]) {
                Complex t = A[i];
                A[i] = A[r[i]];
                A[r[i]] = t;
            }
        }
        for (int m = 2; m <= n; m <<= 1) {
            Complex w1 = new Complex(Math.cos(Math.PI * 2 / m), Math.sin(Math.PI * 2 / m) * op);
            for (int j = 0; j < n; j += m) {
                Complex wk = Complex.ONE;
                for (int i = j; i < j + m / 2; i++, wk = wk.mul(w1)) {
                    Complex a = A[i], b = A[i + m / 2].mul(wk);
                    A[i] = a.add(b);
                    A[i + m / 2] = a.sub(b);
                }
            }
        }
    }

    // 递归版
    // void FFT(Complex[] A, int n, int op) {
    //     if (n == 1) {
    //         return;
    //     }
    //     Complex[] A1 = new Complex[n / 2], A2 = new Complex[n / 2];
    //     for (int i = 0; i < n / 2; i++) {
    //         A1[i] = A[i * 2];
    //         A2[i] = A[i * 2 + 1];
    //     }
    //     FFT(A1, n / 2, op);
    //     FFT(A2, n / 2, op);
    //     Complex w1 = new Complex(Math.cos(Math.PI * 2 / n), Math.sin(Math.PI * 2 / n) * op), wk = Complex.ONE;
    //     for (int i = 0; i < n / 2; i++, wk = wk.mul(w1)) {
    //         Complex tmp = A2[i].mul(wk);
    //         A[i] = A1[i].add(tmp);
    //         A[i + n / 2] = A1[i].sub(tmp);
    //     }
    // }

}

// 复数类
class Complex {
    static final Complex ZERO = new Complex(0, 0), ONE = new Complex(1, 0);
    double x, y;

    public Complex(double x, double y) {
        this.x = x;
        this.y = y;
    }

    public Complex add(Complex o) {
        return new Complex(x + o.x, y + o.y);
    }

    public Complex sub(Complex o) {
        return new Complex(x - o.x, y - o.y);
    }

    public Complex mul(Complex o) {
        return new Complex(x * o.x - y * o.y, x * o.y + y * o.x);
    }

}

// FFT 封装类
class FFT {

    public static long[] mul(int[] a, int[] b) {
        int n = a.length - 1, m = b.length - 1;
        int N = 1 << (32 - Integer.numberOfLeadingZeros(n + m));
        int[] r = new int[N];
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
        fft(r, Ax, Ay, N, 1);
        fft(r, Bx, By, N, 1);
        for (int i = 0; i < N; i++) {
            double x = Ax[i] * Bx[i] - Ay[i] * By[i];
            double y = Ax[i] * By[i] + Ay[i] * Bx[i];
            Ax[i] = x;
            Ay[i] = y;
        }
        fft(r, Ax, Ay, N, -1);
        long[] ans = new long[n + m + 1];
        for (int i = 0; i <= n + m; i++) {
            ans[i] = (long) (Ax[i] / N + (Ax[i] >= 0 ? 0.5 : -0.5));
        }
        return ans;
    }

    private static void fft(int[] r, double[] Ax, double[] Ay, int n, int op) {
        for (int i = 0; i < n; i++) {
            if (i < r[i]) {
                double t = Ax[i];
                Ax[i] = Ax[r[i]];
                Ax[r[i]] = t;
                t = Ay[i];
                Ay[i] = Ay[r[i]];
                Ay[r[i]] = t;
            }
        }
        for (int m = 2; m <= n; m <<= 1) {
            double w1x = Math.cos(Math.PI * 2 / m);
            double w1y = Math.sin(Math.PI * 2 / m) * op;
            for (int k = 0; k < n; k += m) {
                double wkx = 1;
                double wky = 0;
                for (int i = k, j = k + m / 2; i < k + m / 2; i++, j++) {
                    double ax = Ax[i];
                    double ay = Ay[i];
                    double bx = Ax[j] * wkx - Ay[j] * wky;
                    double by = Ax[j] * wky + Ay[j] * wkx;
                    Ax[i] = ax + bx;
                    Ay[i] = ay + by;
                    Ax[j] = ax - bx;
                    Ay[j] = ay - by;
                    double x = w1x * wkx - w1y * wky;
                    double y = w1x * wky + w1y * wkx;
                    wkx = x;
                    wky = y;
                }
            }
        }
    }
}