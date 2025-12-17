public class BostanMori {

    static int MOD = 998244353;
    
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

}
