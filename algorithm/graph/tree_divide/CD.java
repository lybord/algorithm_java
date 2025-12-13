int[] head, nxt, to; boolean[] vis;

void cd(java.util.function.IntConsumer calc) {
    int[] mainStk = new int[head.length], stk = new int[head.length], fa = new int[head.length], sz = new int[head.length];
    java.util.function.IntBinaryOperator getCentroid = (f, u) -> {
        fa[u] = f;
        sz[u] = 0;
        stk[1] = u;
        for (int z = 1; z > 0;) {
            u = stk[z];
            f = fa[u];
            if (sz[u] == 0) {
                sz[u] = 1;
                for (int e = head[u], v; e != 0; e = nxt[e]) {
                    if (f != (v = to[e]) && !vis[v]) {
                        stk[++z] = v;
                        fa[v] = u;
                        sz[v] = 0;
                    }
                }
            } else {
                z--;
                for (int e = head[u], v; e != 0; e = nxt[e]) {
                    if (f != (v = to[e]) && !vis[v]) {
                        sz[u] += sz[v];
                    }
                }
            }
        }
        int half = sz[u] >> 1;
        boolean find = false;
        while (!find) {
            find = true;
            for (int e = head[u], v; e != 0; e = nxt[e]) {
                if (f != (v = to[e]) && !vis[v] && sz[v] > half) {
                    f = u;
                    u = v;
                    find = false;
                    break;
                }
            }
        }
        return u;
    };
    int u = getCentroid.applyAsInt(0, 1);
    mainStk[1] = u;
    for (int z = 1; z > 0;) {
        u = mainStk[z];
        if (!vis[u]) {
            vis[u] = true;
            calc.accept(u);
            for (int e = head[u], v; e != 0; e = nxt[e]) {
                if (!vis[v = to[e]]) {
                    mainStk[++z] = getCentroid.applyAsInt(u, v);
                }
            }
        } else {
            z--;
        }
    }
}