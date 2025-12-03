class HLD {

    private final int[] stk;

    private final int n, root;
    private final long defaultVal;

    private final int[] head, nxt, to;

    private final int[] fa, dep, sz, son, top, dfn;

    @FunctionalInterface
    interface Func1 {
        void applay(int l, int r, long v);
    }

    @FunctionalInterface
    interface Func2 {
        long applay(int l, int r);
    }

    @FunctionalInterface
    interface Func3 {
        long applay(long l, long r);
    }

    private final Func1 update;
    private final Func2 query;
    private final Func3 op;

    public HLD(int n, int root, int[] head, int[] nxt, int[] to, long defaultVal, Func1 update, Func2 query, Func3 op) {
        // 节点编号：1 ~ n
        stk = new int[n + 1];
        this.n = n;
        this.root = root;
        this.head = head;
        this.nxt = nxt;
        this.to = to;
        this.defaultVal = defaultVal;

        fa = new int[n + 1];
        dep = new int[n + 1];
        sz = new int[n + 1];
        son = new int[n + 1];
        top = new int[n + 1];
        dfn = new int[n + 1];

        init1(0, root);
        init2(0, root, root);

        this.update = update;
        this.query = query;
        this.op = op;
    }

    public void updatePath(int u, int v, long x) {
        while (top[u] != top[v]) {
            if (dep[top[u]] < dep[top[v]]) {
                u ^= v ^ (v = u);
            }
            update.applay(dfn[top[u]], dfn[u], x);
            u = fa[top[u]];
        }
        if (dep[u] > dep[v]) {
            u ^= v ^ (v = u);
        }
        update.applay(dfn[u], dfn[v], x);
    }

    public void updateTree(int u, long x) {
        update.applay(dfn[u], dfn[u] + sz[u] - 1, x);
    }

    public long queryPath(int u, int v) {
        long ans = defaultVal;
        while (top[u] != top[v]) {
            if (dep[top[u]] < dep[top[v]]) {
                u ^= v ^ (v = u);
            }
            ans = op.applay(ans, query.applay(dfn[top[u]], dfn[u]));
            u = fa[top[u]];
        }
        if (dep[u] > dep[v]) {
            u ^= v ^ (v = u);
        }
        ans = op.applay(ans, query.applay(dfn[u], dfn[v]));
        return ans;
    }

    public long queryTree(int u) {
        return op.applay(defaultVal, query.applay(dfn[u], dfn[u] + sz[u] - 1));
    }

    // 如严格需要按照 u -> v 的方向跳，则启用 prepare()

    // private final int[] path, p2;

    // public long queryPath(int u, int v) {
    //     prepare(u, v);
    //     long ans = defaultVal;
    //     while (true) {
    //         int l = u, r = path[u];
    //         ans = op.applay(ans, query.applay(l, r));
    //         u = r;
    //         if (u == v) {
    //             break;
    //         }
    //         u = p2[u];
    //     }
    //     return ans;
    // }
    
    // private void prepare(int u, int v) {
    //     while (top[u] != top[v]) {
    //         if (dep[top[u]] >= dep[top[v]]) {
    //             path[u] = top[u];
    //             p2[top[u]] = fa[top[u]];
    //             u = fa[top[u]];
    //         } else {
    //             path[top[v]] = v;
    //             p2[fa[top[v]]] = top[v];
    //             v = fa[top[v]];
    //         }
    //     }
    //     path[u] = v;
    // }

    public int lca(int u, int v) {
        while (top[u] != top[v]) {
            if (dep[top[u]] < dep[top[v]]) {
                u ^= v ^ (v = u);
            }
            u = fa[top[u]];
        }
        if (dep[u] > dep[v]) {
            u ^= v ^ (v = u);
        }
        return u;
    }

    // u != v 且 v 为 u 的祖先节点，找到 v 的子节点，该子节点为 u 的祖先节点
    public int lcason(int u, int v) {
        while (top[u] != top[v]) {
            if (fa[top[u]] == v) {
                return top[u];
            }
            u = fa[top[u]];
        }
        return son[v];
    }

    public int[] getDFNValsByValue(int[] val) {
        int[] ans = new int[n + 1];
        for (int i = 1; i <= n; i++) {
            ans[dfn[i]] = val[i];
        }
        return ans;
    }

    public int[] getDFNValsByWeight(int[] wt) {
        int[] ans = new int[n + 1];
        for (int e = 2; e < n; e += 2) {
            ans[Math.max(dfn[to[e]], dfn[to[e ^ 1]])] = wt[e];
        }
        return ans;
    }

    // --------- 以下为重链剖分预处理 -----------

    private void init1(int f, int u) {
        int z = 0;
        stk[++z] = u;
        dep[u] = 1;
        while (z > 0) {
            u = stk[z];
            f = fa[u];
            if (sz[u] == 0) {
                sz[u] = 1;
                for (int e = head[u], v; e != 0; e = nxt[e]) {
                    if (f != (v = to[e])) {
                        stk[++z] = v;
                        fa[v] = u;
                        dep[v] = dep[u] + 1;
                    }
                }
            } else {
                z--;
                for (int e = head[u], v; e != 0; e = nxt[e]) {
                    if (f != (v = to[e])) {
                        sz[u] += sz[v];
                        if (sz[son[u]] < sz[v]) {
                            son[u] = v;
                        }
                    }
                }
            }
        }
    }

    private void init2(int f, int u, int t) {
        int z = 0, no = 0;
        stk[++z] = u;
        top[u] = t;
        while (z > 0) {
            u = stk[z];
            f = fa[u];
            t = top[u];
            if (dfn[u] == 0) {
                dfn[u] = ++no;
                top[u] = t;
                for (int e = head[u], v; e != 0; e = nxt[e]) {
                    if ((v = to[e]) != f && v != son[u]) {
                        stk[++z] = v;
                        top[v] = v;
                    }
                }
                if (son[u] != 0) {
                    stk[++z] = son[u];
                    top[son[u]] = t;
                }
            } else {
                z--;
            }
        }
    }
}
