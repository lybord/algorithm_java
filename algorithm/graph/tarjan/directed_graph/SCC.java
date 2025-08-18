class TwoSat {
    // range [1, n]
    // if i is true, then i + n must be false
    // if i is false, then i + n must be true

    private int[] stack;
    private int[] head, nxt, to;
    private int[] dfn, low, stk, scc;
    private boolean[] vis;
    private int n, z, ts, top, no;

    public TwoSat(int n) {
        this.n = n;
        z = 1;
        int m = n << 1 | 1;
        stack = new int[m];
        head = new int[m];
        nxt = new int[m];
        to = new int[m];
        dfn = new int[m];
        low = new int[m];
        stk = new int[m];
        scc = new int[m];
        vis = new boolean[m];
    }

    public void addEdge(int u, int v) {
        if (z == nxt.length) {
            nxt = java.util.Arrays.copyOf(nxt, z << 1);
            to = java.util.Arrays.copyOf(to, z << 1);
        }
        nxt[z] = head[u];
        head[u] = z;
        to[z++] = v;
    }

    public boolean[] match() {
        for (int u = 1; u <= n << 1; u++) {
            if (dfn[u] == 0) {
                tarjan(u);
            }
        }
        for (int i = 1; i <= n; i++) {
            if (scc[i] == scc[i + n]) {
                return null;
            }
        }
        boolean[] ans = new boolean[n + 1];
        for (int i = 1; i <= n; i++) {
            ans[i] = scc[i] < scc[i + n];
        }
        return ans;
    }

    private void tarjan(int u) {
        int z = 1;
        stack[1] = u;
        while (z > 0) {
            u = stack[z];
            if (dfn[u] == 0) {
                dfn[u] = low[u] = ++ts;
                stk[++top] = u;
                vis[u] = true;
                for (int e = head[u], v; e != 0; e = head[u]) {
                    v = to[e];
                    if (dfn[v] == 0) {
                        stack[++z] = v;
                        break;
                    }
                    if (vis[v]) {
                        low[u] = Math.min(low[u], dfn[v]);
                    }
                    head[u] = nxt[e];
                }
            } else {
                int e = head[u];
                if (e != 0) {
                    low[u] = Math.min(low[u], low[to[e]]);
                    head[u] = nxt[e];
                    e = head[u];
                    for (int v; e != 0; e = head[u]) {
                        v = to[e];
                        if (dfn[v] == 0) {
                            stack[++z] = v;
                            break;
                        }
                        if (vis[v]) {
                            low[u] = Math.min(low[u], dfn[v]);
                        }
                        head[u] = nxt[e];
                    }
                }
                if (e == 0) {
                    z--;
                    if (dfn[u] == low[u]) {
                        no++;
                        int o;
                        do {
                            o = stk[top--];
                            vis[o] = false;
                            scc[o] = no;
                        } while (o != u);
                    }
                }
            }
        }
    }

    public void clear() {
        Arrays.fill(head, 0);
        Arrays.fill(dfn, 0);
        z = 1;
        ts = no = 0;
    }
}
