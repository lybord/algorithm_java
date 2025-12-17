class NTT {

    /**
     *  常用原根 g       模数 p              分解 p                  最大长度
     *      3         469762049           7 * 2^26 + 1                 2^26
     *      3         998244353           119 * 2^23 + 1               2^23
     *      3         2281701377          17 * 2^27 + 1                2^27
     */

     private static final long MOD, g, ig, gpow[], igpow[];

     static {
        MOD = 998244353;
        g = 3;
        ig = pow(g, MOD - 2);
        gpow = new long[30];
        igpow = new long[30];
        for (int i = 1; i <= 23; i++) {
            gpow[i] = pow(g, (MOD - 1) / (1 << i));
            igpow[i] = pow(ig, (MOD - 1) / (1 << i));
        }
     }

     public static int[] mul(int[] a, int[] b) {
        int n = a.length - 1, m = b.length - 1;
        int N = 1 << (32 - Integer.numberOfLeadingZeros(n + m));
        int[] r = new int[N];
        for (int i = 0; i < N; i++) {
            r[i] = r[i >> 1] >> 1 | ((i & 1) == 0 ? 0 : N >> 1);
        }
        long[] A = new long[N], B = new long[N];
        for (int i = 0; i <= n; i++) {
            A[i] = a[i];
        }
        for (int i = 0; i <= m; i++) {
            B[i] = b[i];
        }
        ntt(r, A, N, 1);
        ntt(r, B, N, 1);
        for (int i = 0; i < N; i++) {
            A[i] = A[i] * B[i] % MOD;
        }
        ntt(r, A, N, -1);
        int[] ans = new int[n + m + 1];
        long iN = pow(N, MOD - 2);
        for (int i = 0; i <= n + m; i++) {
            ans[i] = (int) (A[i] * iN % MOD);
        }
        return ans;
    }

    private static void ntt(int[] r, long[] A, int n, int op) {
        for (int i = 0; i < n; i++) {
            if (i < r[i]) {
                A[i] ^= A[r[i]] ^ (A[r[i]] = A[i]);
            }
        }
        for (int m = 2, t = 1; m <= n; m <<= 1, t++) {
            long g1 = op == 1 ? gpow[t] : igpow[t];
            for (int k = 0; k < n; k += m) {
                long gk = 1;
                for (int i = k, j = k + (m >> 1); i < k + (m >> 1); i++, j++) {
                    long x = A[i], y = A[j] * gk % MOD;
                    A[i] = (x + y) % MOD;
                    A[j] = (x - y + MOD) % MOD;
                    gk = gk * g1 % MOD;
                }
            }
        }
    }

    private static long pow(long a, long b) {
        long res = 1;
        while (b != 0) {
            if ((b & 1) != 0) {
                res = res * a % MOD;
            }
            a = a * a % MOD;
            b >>= 1;
        }
        return res;
    }
    
}
