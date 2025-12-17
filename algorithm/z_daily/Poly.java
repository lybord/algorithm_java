class Poly {
    private static final int MOD, g, ig, gpow[], igpow[];
    static {
        MOD = 998244353;
        g = 3;
        ig = (int) pow(g, MOD - 2);
        gpow = new int[30];
        igpow = new int[30];
        for (int i = 1; i <= 23; i++) {
            gpow[i] = (int) pow(g, (MOD - 1) / (1 << i));
            igpow[i] = (int) pow(ig, (MOD - 1) / (1 << i));
        }
    }

    private Poly() {}

    // 给定数列的前 m 项 a，返回符合 a 的最短常系数齐次线性递推式的系数 coef（模 MOD 意义下）
    // 设 coef 长为 k，当 n >= k 时，有递推式：
    // f(n) = coef[0]*f(n-1) + coef[1]*f(n-2) + ... + coef[k-1]*f(n-k)
    // 初始值 f(n) = a[n] (0 <= n < k)
    // 时间复杂度 O(m^2)，其中 m 是 a 的长度
    public static int[] berlekampMassey(int[] a) {
        int[] coef = new int[0];
        int[] preC = new int[0];
        int preI = -1;
        int preD = 0;

        for (int i = 0; i < a.length; i++) {
            // d = a[i] - 递推式算出来的值
            long d = a[i];
            for (int j = 0; j < coef.length; j++) {
                d = (d - (long) coef[j] * a[i - 1 - j]) % MOD;
            }
            if (d == 0) { // 递推式正确
                continue;
            }

            // 首次算错，初始化 coef 为 i+1 个 0
            if (preI < 0) {
                coef = new int[i + 1];
                preI = i;
                preD = (int) d;
                continue;
            }

            int bias = i - preI;
            int oldLen = coef.length;
            int newLen = bias + preC.length;
            int[] tmp = null;
            if (newLen > oldLen) { // 递推式变长了
                tmp = coef.clone();
                coef = java.util.Arrays.copyOf(coef, newLen);
            }

            // 历史错误为 preD = a[preI] - sum_j preC[j]*a[preI-1-j]
            // 现在 a[i] = sum_j coef[j]*a[i-1-j] + d
            // 联立得 a[i] = sum_j coef[j]*a[i-1-j] + d/preD * (a[preI] - sum_j preC[j]*a[preI-1-j])
            // 其中 a[preI] 的系数 d/preD 位于当前（i）的 bias-1 = i-preI-1 处
            long delta = d * pow(preD, MOD - 2) % MOD;
            coef[bias - 1] = (int) ((coef[bias - 1] + delta) % MOD);
            for (int j = 0; j < preC.length; j++) {
                coef[bias + j] = (int) ((coef[bias + j] - delta * preC[j]) % MOD);
            }

            if (newLen > oldLen) {
                preC = tmp;
                preI = i;
                preD = (int) d;
            }
        }

        for (int i = 0; i < coef.length; i++) {
            coef[i] = (coef[i] + MOD) % MOD;
        }

        // 计算完后，可能 coef 的末尾有 0，这些 0 不能去掉
        // 比如数列 (1,2,4,2,4,2,4,...) 的系数为 [0,1,0]，表示 f(n) = 0*f(n-1) + 1*f(n-2) + 0*f(n-3) = f(n-2)   (n >= 3)
        // 如果把末尾的 0 去掉，变成 [0,1]，就表示 f(n) = 0*f(n-1) + f(n-2) = f(n-2)   (n >= 2)
        // 看上去一样，但按照这个式子算出来的数列是错误的 (1,2,1,2,1,2,...)

        return coef;
    }

    // Bostan–Mori 返回 [x^m] P/Q
    public static int bostanMori(int[] P, int[] Q, long m) {
        while (m > 0) {
            // Q(-x)
            int[] Qm = new int[Q.length];
            for (int i = 0; i < Q.length; i++) {
                Qm[i] = (int) ((i & 1) == 0 ? Q[i] : (MOD - Q[i]) % MOD);
            }

            int[] A = mul(P, Qm);
            int[] B = mul(Q, Qm);

            // 取奇偶
            int na = (A.length + 1) / 2;
            int nb = (B.length + 1) / 2;
            int[] P2 = new int[na];
            int[] Q2 = new int[nb];

            if ((m & 1) == 0) {
                for (int i = 0; i < na; i++) P2[i] = A[i * 2];
            } else {
                for (int i = 0; i < na; i++) {
                    int idx = i * 2 + 1;
                    if (idx < A.length) P2[i] = A[idx];
                }
            }
            for (int i = 0; i < nb; i++) Q2[i] = B[i * 2];

            P = P2;
            Q = Q2;
            m >>= 1;
        }
        return (int) (P[0] * inv(Q[0]) % MOD);
    }

    // 计算 P/Q 的 前 k 项： f[0..k-1] , k = deg Q
    public static int[] preK(int[] P, int[] Q) {
        int k = Q.length - 1;
        int invQ0 = (int) inv(Q[0]);
        int[] f = new int[k];
    
        for (int n = 0; n < k; n++) {
            long val = (n < P.length ? P[n] : 0);
            for (int i = 1; i <= n && i < Q.length; i++) {
                val = (val - (long) Q[i] * f[n - i]) % MOD;
            }
            f[n] = (int) (val * invQ0 % MOD);
            if (f[n] < 0) f[n] += MOD;
        }
        return f;
    }

     // 构造递推系数： f_n = c[0] f_{n-1} + ... + c[k-1] f_{n-k}  , k = deg Q
    public static int[] coef(int[] Q) {
        int k = Q.length - 1;
        int[] coef = new int[k];
        long invQ0 = inv(Q[0]);
        for (int i = 1; i <= k; i++) {
            coef[i - 1] = (int) (invQ0 *(MOD - Q[i]) % MOD);
        }
        return coef;
    }

    // 给定常系数齐次线性递推式 f(n) = coef[k-1] * f(n-1) + ... + coef[0] * f(n-k)
    // 以及初始值 f(i) (0 <= i < k)
    // 返回 f(n) % MOD，其中参数 n 从 0 开始
    // 注意 coef 的顺序
    // 时间复杂度 O(k^2 log n)，其中 k 是 coef 的长度
    public static int kitamasa(int[] coef, int[] f, long n) {
        if (n < f.length) {
            return f[(int) n] % MOD;
        }

        int k = coef.length;
        // 特判 k = 0, 1 的情况
        if (k == 0) {
            return 0;
        }
        if (k == 1) {
            return (int) ((long) f[0] * pow(coef[0], n) % MOD);
        }

        // 计算 resC，以表出 f(n) = resC[k-1] * a[k-1] + ... + resC[0] * a[0]
        int[] resC = new int[k];
        int[] c = new int[k];
        resC[0] = c[1] = 1;
        for (; n > 0; n /= 2) {
            if (n % 2 > 0) {
                resC = compose(coef, c, resC);
            }
            // 由于会修改 compose 的第三个参数，这里把 c 复制一份再传入
            c = compose(coef, c, c.clone());
        }

        long ans = 0;
        for (int i = 0; i < k; i++) {
            ans = (ans + (long) resC[i] * f[i]) % MOD;
        }

        return (int) ((ans + MOD) % MOD); // 保证返回值非负
    }

    // 已知 f(n) 的各项系数为 a，f(m) 的各项系数为 b
    // 计算并返回 f(n+m) 的各项系数 c
    private static int[] compose(int[] coef, int[] a, int[] b) {
        int k = a.length;
        int[] c = new int[k];
        for (int v : a) {
            for (int j = 0; j < k; j++) {
                c[j] = (int) ((c[j] + (long) v * b[j]) % MOD);
            }
            // 原地计算下一组系数，比如已知 f(4) 的各项系数，现在要计算 f(5) 的各项系数
            // 倒序遍历，避免提前覆盖旧值
            long bk1 = b[k - 1];
            for (int i = k - 1; i > 0; i--) {
                b[i] = (int) ((b[i - 1] + bk1 * coef[i]) % MOD);
            }
            b[0] = (int) (bk1 * coef[0] % MOD);
        }
        return c;
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
        ntt(r, A, N, false);
        ntt(r, B, N, false);
        for (int i = 0; i < N; i++) {
            A[i] = A[i] * B[i] % MOD;
        }
        ntt(r, A, N, true);
        int[] ans = new int[n + m + 1];
        long iN = pow(N, MOD - 2);
        for (int i = 0; i <= n + m; i++) {
            ans[i] = (int) (A[i] * iN % MOD);
        }
        return ans;
    }

    private static void ntt(int[] r, long[] A, int n, boolean invert) {
        for (int i = 0; i < n; i++) {
            if (i < r[i]) {
                A[i] ^= A[r[i]] ^ (A[r[i]] = A[i]);
            }
        }
        for (int m = 2, t = 1; m <= n; m <<= 1, t++) {
            long g1 = invert ? igpow[t] : gpow[t];
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

    private static long inv(long a) {
        return pow(a, MOD - 2);
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
