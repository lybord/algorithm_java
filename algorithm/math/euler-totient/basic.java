/**
 * 欧拉函数
 */
public class basic {
    
    // 线性筛求欧拉函数
    int[] getPhi(int n) {
        boolean[] isp = new boolean[n + 1];
        int[] prime = new int[n + 1];
        for (int i = 2; i <= n; i++) {
            isp[i] = true;
        }
        int[] phi = new int[n + 1];
        phi[1] = 1;
        for (int i = 2, j = 0, v; i <= n; i++) {
            if (isp[i]) {
                prime[j++] = i;
                phi[i] = i - 1;
            }
            for (int p : prime) {
                v = i * p;
                if (v > n) {
                    break;
                }
                isp[v] = false;
                if (i % p == 0) {
                    phi[i] = p * phi[i];
                    break;
                }
                phi[i] = phi[i] * (p - 1);
            }
        }
        return phi;
    }
    
}
