public class basic {

    int MOD = 998244353, inv2 = (MOD + 1) / 2;

    // op 为 1 则是正变换，否则为 -1 是逆变换
    void or(long[] a, int op) {
        int n = a.length;
        for (int m = 2, d = 1; m <= n; m <<= 1, d <<= 1) {
            for (int i = 0; i < n; i += m) {
                for (int j = 0; j < d; j++) {
                    a[i + j + d] = (a[i + j + d] + a[i + j] * op + MOD) % MOD;
                }
            }
        }
        
    }

    // op 为 1 则是正变换，否则为 -1 是逆变换
    void and(long[] a, int op) {
        int n = a.length;
        for (int m = 2, d = 1; m <= n; m <<= 1, d <<= 1) {
            for (int i = 0; i < n; i += m) {
                for (int j = 0; j < d; j++) {
                    a[i + j] = (a[i + j] + a[i + j + d] * op + MOD) % MOD;
                }
            }
        }
    }

    // op 为 1 则是正变换，否则为 1/2 是逆变换
    void xor(long[] a, int op) {
        int n = a.length;
        for (int m = 2, d = 1; m <= n; m <<= 1, d <<= 1) {
            for (int i = 0; i < n; i += m) {
                for (int j = 0; j < d; j++) {
                    long x = a[i + j], y = a[i + j + d];
                    a[i + j] = (x + y) * op % MOD;
                    a[i + j + d] = (x - y + MOD) * op % MOD;
                }
            }
        }
    }


}
