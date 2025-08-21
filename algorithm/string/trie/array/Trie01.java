class Trie01 {
    private static final int H = 30, MAXN = 500000, MAXT = MAXN * (H + 2);
    private static final int[] nxt = new int[MAXT * 2 + 1], cnt = new int[MAXT + 1];

    private static int no;

    private static int idx(int u, int i) {
        return u + i * MAXT;
    }

    public static void clear() {
        for (int i = 1; i <= no; i++) {
            nxt[i] = nxt[i + MAXT] = cnt[i] = 0;
        }
        no = 0;
    }

    private final int root;

    public Trie01() {
        root = ++no;
    }

    public void insert(int v) {
        int u = root;
        for (int i = H, j; i >= 0; i--) {
            j = idx(u, v >> i & 1);
            if (nxt[j] == 0) {
                nxt[j] = ++no;
            }
            u = nxt[j];
            cnt[u]++;
        }
    }

    // v 代表的值一定在 trie 中
    public void delete(int v) {
        int u = root;
        for (int i = H; i >= 0; i--) {
            u = nxt[idx(u, v >> i & 1)];
            cnt[u]--;
        }
    }

    public boolean contains(int v) {
        return valNumber(v) > 0;
    }

    public int valNumber(int v) {
        int u = root;
        for (int i = H; i >= 0; i--) {
            u = nxt[idx(u, v >> i & 1)];
            if (u == 0) {
                return 0;
            }
        }
        return cnt[u];
    }

    public int maxXor(int v) {
        int u = root;
        int ans = 0;
        for (int i = H, j; i >= 0; i--) {
            j = idx(u, (v >> i & 1) ^ 1);
            if (nxt[j] != 0) {
                ans |= 1 << i;
                u = nxt[j];
            } else {
                u = nxt[idx(u, v >> i & 1)];
            }
        }
        return ans;
    }

    public int minXor(int v) {
        int u = root;
        int ans = 0;
        for (int i = H, j; i >= 0; i--) {
            j = idx(u, v >> i & 1);
            if (nxt[j] != 0) {
                u = nxt[j];
            } else {
                ans |= 1 << i;
                u = nxt[idx(u, (v >> i & 1) ^ 1)];
            }
        }
        return ans;
    }
}