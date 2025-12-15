import static algorithm.zz.U.*;
/**
 * 卢卡斯定理
 */
public class basic {

    int n, m, p;
    long[] F, IF;

    void solve() {
        n = ni(); m = ni(); p = ni();
        F = new long[p]; IF = new long[p];
        F[0] = IF[0] = 1;
        for (int i = 1; i < p; i++) {
            F[i] = F[i - 1] * i % p;
        }
        IF[p - 1] = p - 1;
        for (int i = p - 2; i >= 1; i--) {
            IF[i] = IF[i + 1] * (i + 1) % p;
        }
        println(lucas(n + m, n));
    }

    long lucas(int n, int m) {
        return m == 0 ? 1 : lucas(n / p, m / p) * comb(n % p, m % p) % p;
    }

    long comb(int n, int m) {
        return n < m ? 0 : F[n] * IF[n - m] % p * IF[m] % p;
    }

}
