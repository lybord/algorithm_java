class StringHash {
    private static final int MAXN = 1000000, MOD1, MOD2, BASE1, BASE2;
    private static final int[] POW_BASE1 = new int[MAXN + 1], POW_BASE2 = new int[MAXN + 1];
    static {
        o: for (int i = 1000000001 + ((int) (250000000 * Math.random()) << 1);; i += 2) {
            for (int j = 3; j * j <= i; j += 2) {
                if (i % j == 0) {
                    continue o;
                }
            }
            MOD1 = i;
            break;
        }
        o: for (int i = 1500000001 + ((int) (250000000 * Math.random()) << 1);; i += 2) {
            for (int j = 3; j * j <= i; j += 2) {
                if (i % j == 0) {
                    continue o;
                }
            }
            MOD2 = i;
            break;
        }
        BASE1 = (int) (1e7 + 9e7 * Math.random());
        BASE2 = (int) (1e8 + 9e8 * Math.random());
        POW_BASE1[0] = POW_BASE2[0] = 1;
        for (int i = 0; i < MAXN; i++) {
            POW_BASE1[i + 1] = (int) ((long) POW_BASE1[i] * BASE1 % MOD1);
            POW_BASE2[i + 1] = (int) ((long) POW_BASE2[i] * BASE2 % MOD2);
        }
    }

    private final int N;
    private final int[] PRE_HASH1, PRE_HASH2;

    public StringHash(String s) {
        N = s.length();
        PRE_HASH1 = new int[N + 1];
        PRE_HASH2 = new int[N + 1];
        for (int i = 0, v; i < N; i++) {
            v = s.charAt(i);
            PRE_HASH1[i + 1] = (int) (((long) PRE_HASH1[i] * BASE1 + v) % MOD1);
            PRE_HASH2[i + 1] = (int) (((long) PRE_HASH2[i] * BASE2 + v) % MOD2);
        }
    }

    public StringHash(char[] str) {
        N = str.length;
        PRE_HASH1 = new int[N + 1];
        PRE_HASH2 = new int[N + 1];
        for (int i = 0; i < N; i++) {
            PRE_HASH1[i + 1] = (int) (((long) PRE_HASH1[i] * BASE1 + str[i]) % MOD1);
            PRE_HASH2[i + 1] = (int) (((long) PRE_HASH2[i] * BASE2 + str[i]) % MOD2);
        }
    }

    public StringHash(int[] arr) {
        N = arr.length;
        PRE_HASH1 = new int[N + 1];
        PRE_HASH2 = new int[N + 1];
        for (int i = 0; i < N; i++) {
            PRE_HASH1[i + 1] = (int) (((long) PRE_HASH1[i] * BASE1 + arr[i]) % MOD1);
            PRE_HASH2[i + 1] = (int) (((long) PRE_HASH2[i] * BASE2 + arr[i]) % MOD2);
        }
    }

    public long hash(int l, int r) {// s[l:r]
        long h1 = (PRE_HASH1[r + 1] - (long) PRE_HASH1[l] * POW_BASE1[r - l + 1]) % MOD1;
        long h2 = (PRE_HASH2[r + 1] - (long) PRE_HASH2[l] * POW_BASE2[r - l + 1]) % MOD2;
        if (h1 < 0) {
            h1 += MOD1;
        }
        if (h2 < 0) {
            h2 += MOD2;
        }
        return h1 << 32 | h2;
    }

    public boolean same(int l1, int r1, int l2, int r2) {
        return ((long) PRE_HASH1[l2] * POW_BASE1[r2 - l2 + 1] - (long) PRE_HASH1[l1] * POW_BASE1[r1 - l1 + 1] + PRE_HASH1[r1 + 1] - PRE_HASH1[r2 + 1]) % MOD1 == 0
        && ((long) PRE_HASH2[l2] * POW_BASE2[r2 - l2 + 1] - (long) PRE_HASH2[l1] * POW_BASE2[r1 - l1 + 1] + PRE_HASH2[r1 + 1] - PRE_HASH2[r2 + 1]) % MOD2 == 0;
    }
}
