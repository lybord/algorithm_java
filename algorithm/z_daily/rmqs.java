class RMQ {
    private static final int MAXN = 1000000;
    private static final int[] log = new int[MAXN + 1];
    static {
        log[0] = -1;
        for (int i = 1; i <= MAXN; i++) {
            log[i] = log[i >> 1] + 1;
        }
    }

    private final int[][] st;

    private final java.util.function.IntBinaryOperator op;

    public RMQ(int[] a, java.util.function.IntBinaryOperator op) {
        this.op = op;
        int n = a.length, m = log[n] + 1;
        st = new int[m][n];
        System.arraycopy(a, 0, st[0], 0, n);
        for (int j = 1; j < m; j++) {
            for (int i = 0; i + (1 << j) - 1 < n; i++) {
                st[j][i] = op.applyAsInt(st[j - 1][i], st[j - 1][i + (1 << (j - 1))]);
            }
        }
    }
    
    public int query(int l, int r) {
        int g = log[r - l + 1];
        return op.applyAsInt(st[g][l], st[g][r - (1 << g) + 1]);
    }
}

class RMQ2 {
    private static final int MAXN = 1000000;
    private static final int[] log = new int[MAXN + 1];
    static {
        log[0] = -1;
        for (int i = 1; i <= MAXN; i++) {
            log[i] = log[i >> 1] + 1;
        }
    }
    
    private final long[][] st;

    private final java.util.function.LongBinaryOperator op;

    public RMQ2(long[] a, java.util.function.LongBinaryOperator op) {
        this.op = op;
        int n = a.length, m = log[n] + 1;
        st = new long[m][n];
        System.arraycopy(a, 0, st[0], 0, n);
        for (int j = 1; j < m; j++) {
            for (int i = 0; i + (1 << j) - 1 < n; i++) {
                st[j][i] = op.applyAsLong(st[j - 1][i], st[j - 1][i + (1 << (j - 1))]);
            }
        }
    }
    
    public long query(int l, int r) {
        int g = log[r - l + 1];
        return op.applyAsLong(st[g][l], st[g][r - (1 << g) + 1]);
    }
}
