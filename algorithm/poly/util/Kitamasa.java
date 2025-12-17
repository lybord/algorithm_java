public class Kitamasa {

    int MOD = 998244353;

    // 给定常系数齐次线性递推式 f(n) = coef[k-1] * f(n-1) + ... + coef[0] * f(n-k)
    // 以及初始值 f(i) (0 <= i < k)
    // 返回 f(n) % MOD，其中参数 n 从 0 开始
    // 注意 coef 的顺序
    // 时间复杂度 O(k^2 log n)，其中 k 是 coef 的长度
    private int kitamasa(int[] coef, int[] f, long n) {
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
    private int[] compose(int[] coef, int[] a, int[] b) {
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

    private int pow(long x, long n) {
        long res = 1;
        for (; n > 0; n /= 2) {
            if (n % 2 > 0) {
                res = res * x % MOD;
            }
            x = x * x % MOD;
        }
        return (int) res;
    }

    private void reverse(int[] a) {
        for (int i = 0, j = a.length - 1; i < j; i++, j--) {
            int tmp = a[i];
            a[i] = a[j];
            a[j] = tmp;
        }
    }
    
}
