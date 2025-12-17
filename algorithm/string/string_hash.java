class StringHash {
    private static final int MOD1, MOD2;
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
    }

    private final int N, BASE1, BASE2;
    private final int[] POW_BASE1, POW_BASE2;
    private final int[] PRE_HASH1, PRE_HASH2;

    public StringHash(String s) {
        N = s.length();
        BASE1 = (int) (8e8 + 1e8 * Math.random());
        BASE2 = (int) (8e8 + 1e8 * Math.random());
        POW_BASE1 = new int[N + 1];
        POW_BASE2 = new int[N + 1];
        PRE_HASH1 = new int[N + 1];
        PRE_HASH2 = new int[N + 1];
        POW_BASE1[0] = POW_BASE2[0] = 1;
        for (int i = 0, v; i < N; i++) {
            v = s.charAt(i);
            POW_BASE1[i + 1] = (int) ((long) POW_BASE1[i] * BASE1 % MOD1);
            POW_BASE2[i + 1] = (int) ((long) POW_BASE2[i] * BASE2 % MOD2);
            PRE_HASH1[i + 1] = (int) (((long) PRE_HASH1[i] * BASE1 + v) % MOD1);
            PRE_HASH2[i + 1] = (int) (((long) PRE_HASH2[i] * BASE2 + v) % MOD2);
        }
    }

    public long hash1(int l, int r) {
        return (PRE_HASH1[r + 1] - (long) PRE_HASH1[l] * POW_BASE1[r - l + 1] % MOD1 + MOD1) % MOD1;
    }

    public long hash2(int l, int r) {
        return (PRE_HASH2[r + 1] - (long) PRE_HASH2[l] * POW_BASE2[r - l + 1] % MOD2 + MOD2) % MOD2;
    }

    public long hash(int l, int r) {// s[l:r]
        return hash1(l, r) << 32 | hash2(l, r);
    }

    public boolean same(int l1, int r1, int l2, int r2) {
        return hash(l1, r1) == hash(l2, r2);
    }
}