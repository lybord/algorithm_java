/**
 * 莫比乌斯函数
 */
public class Basic {

    // 线性筛求莫比乌斯函数
    int[] getMu(int n) {
        boolean[] isp = new boolean[n + 1];
        int[] prime = new int[n + 1];
        for (int i = 2; i <= n; i++) {
            isp[i] = true;
        }
        int[] mu = new int[n + 1];
        mu[1] = 1;
        for (int i = 2, j = 0, v; i <= n; i++) {
            if (isp[i]) {
                prime[j++] = i;
                mu[i] = -1;
            }
            for (int k = 0, p; k < j; k++) {
                p = prime[k];
                v = i * p;
                if (v > n) {
                    break;
                }
                isp[v] = false;
                if (i % p == 0) {
                    break;
                }
                mu[v] = -mu[i];
            }
        }
        return mu;
    }
    
}
