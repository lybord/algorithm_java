public class BerlekampMassey {

    int MOD = 998244353;
    
    // 给定数列的前 m 项 a，返回符合 a 的最短常系数齐次线性递推式的系数 coef（模 MOD 意义下）
    // 设 coef 长为 k，当 n >= k 时，有递推式：
    // f(n) = coef[0]*f(n-1) + coef[1]*f(n-2) + ... + coef[k-1]*f(n-k)
    // 初始值 f(n) = a[n] (0 <= n < k)
    // 时间复杂度 O(m^2)，其中 m 是 a 的长度
    private int[] berlekampMassey(int[] a) {
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

        // 计算完后，可能 coef 的末尾有 0，这些 0 不能去掉
        // 比如数列 (1,2,4,2,4,2,4,...) 的系数为 [0,1,0]，表示 f(n) = 0*f(n-1) + 1*f(n-2) + 0*f(n-3) = f(n-2)   (n >= 3)
        // 如果把末尾的 0 去掉，变成 [0,1]，就表示 f(n) = 0*f(n-1) + f(n-2) = f(n-2)   (n >= 2)
        // 看上去一样，但按照这个式子算出来的数列是错误的 (1,2,1,2,1,2,...)

        return coef;
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

}
