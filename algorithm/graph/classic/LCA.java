class LCA {
    private final int[] head, nxt, to, stk, log, st[];
    final int[] dfn, fa, dep;

    public LCA(int root, int[] head, int[] nxt, int[] to) {
        this.head = head;
        this.nxt = nxt;
        this.to = to;

        int n = head.length - 1;
        stk = new int[n + 1];
        dfn = new int[n + 1];
        fa = new int[n + 1];
        dep = new int[n + 1];
        
        log = new int[n + 1];
        log[0] = -1;
        for (int i = 1; i <= n; i++) {
            log[i] = log[i >> 1] + 1;
        }
        int h = log[n] + 1;
        st = new int[h][n + 1];

        dfs(0, root);
        for (int i = 1; i < h; i++) {
            for (int j = 1; j + (1 << (i - 1)) <= n; j++) {
                int u = st[i - 1][j], v = st[i - 1][j + (1 << (i - 1))];
                st[i][j] = dep[u] < dep[v] ? u : v;
            }
        }
    }

    public int get(int u, int v) {
        if (u == v) {
            return u;
        }
        int l = dfn[u], r = dfn[v];
        if (l > r) {
            l ^= r ^ (r = l);
        }
        l++;
        int g = log[r - l + 1];
        u = st[g][l];
        v = st[g][r - (1 << g) + 1];
        return dep[u] < dep[v] ? u : v;
    }

    private void dfs(int f, int u) {
        int z = 0, no = 0;
        stk[++z] = u;
        fa[u] = f;
        while (z > 0) {
            u = stk[z];
            f = fa[u];
            if (dfn[u] == 0) {
                dfn[u] = ++no;
                dep[u] = dep[f] + 1;
                st[0][no] = f;
                for (int e = head[u], v; e != 0; e = nxt[e]) {
                    if (f != (v = to[e])) {
                        stk[++z] = v;
                        fa[v] = u;
                    }
                }
            } else {
                z--;
            }
        }
    }
}