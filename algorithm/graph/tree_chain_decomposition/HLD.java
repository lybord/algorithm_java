package algorithm.graph.tree_chain_decomposition;
import static algorithm.zz.U.*;

/**
 * 重链剖分（模板）
 */

public class HLD {

    int[] stk = new int[1000001];

    int[] head, nxt, to, val;
    int[] fa, dep, sz, son, top, dfn;

    SegTree tree;

    void solve() {
        int n = ni(), q = ni();
        head = new int[n + 1]; nxt = new int[n << 1]; to = new int[n << 1]; val = new int[n + 1];
        fa = new int[n + 1]; dep = new int[n + 1]; sz = new int[n + 1]; son = new int[n + 1]; top = new int[n + 1]; dfn = new int[n + 1];
        for (int i = 1; i <= n; i++) {
            val[i] = ni();
        }
        for (int i = 1, j = 2; i < n; i++) {
            int u = ni(), v = ni();
            nxt[j] = head[u]; head[u] = j; to[j++] = v;
            nxt[j] = head[v]; head[v] = j; to[j++] = u;
        }
        init1(0, 1);
        init2(0, 1, 1);

        int[] a = new int[n + 1];
        for (int i = 1; i <= n; i++) {
            a[dfn[i]] = val[i];
        }
        tree = new SegTree(a);

        int x, y, z;
        while (q-- > 0) {
            switch (ni()) {
                case 1 -> {
                    x = ni(); y = ni(); z = ni();
                    add(x, y, z);
                }
                case 2 -> {
                    x = ni(); y = ni();
                    println(query(x, y));
                }
                case 3 -> {
                    x = ni(); z = ni();
                    tree.add(dfn[x], dfn[x] + sz[x] - 1, z);
                }
                default -> {
                    x = ni();
                    println(tree.query(dfn[x], dfn[x] + sz[x] - 1));
                }
            }
        }
    }

    

    // --------- 以下为重链剖分更新 + 查询 -----------

    void add(int u, int v, long x) {
        while (top[u] != top[v]) {
            if (dep[top[u]] < dep[top[v]]) {
                u ^= v ^ (v = u);
            }
            tree.add(dfn[top[u]], dfn[u], x);
            u = fa[top[u]];
        }
        if (dep[u] > dep[v]) {
            u ^= v ^ (v = u);
        }
        tree.add(dfn[u], dfn[v], x);
    }

    long query(int u, int v) {
        long ans = 0;
        while (top[u] != top[v]) {
            if (dep[top[u]] < dep[top[v]]) {
                u ^= v ^ (v = u);
            }
            ans += tree.query(dfn[top[u]], dfn[u]);
            u = fa[top[u]];
        }
        if (dep[u] > dep[v]) {
            u ^= v ^ (v = u);
        }
        ans += tree.query(dfn[u], dfn[v]);
        return ans;
    }

     // u != v 且 v 为 u 的祖先节点，找到 v 的子节点，该子节点为 u 的祖先节点
     int lcason(int u, int v) {
        while (top[u] != top[v]) {
            if (fa[top[u]] == v) {
                return top[u];
            }
            u = fa[top[u]];
        }
        return son[v];
    }

    // 如严格需要按照 u -> v 的方向跳，则启用 prepare()

    // int query(int u, int v) {
    //     prepare(u, v);
    
    //     while (true) {
    //         l = u;
    //         r = path[u];
           
    //         ...query...
    
    //         u = r;
    //         if (u == v) {
    //             break;
    //         }
    //         u = p2[u];
    //     }
        
    // }
    
    // void prepare(int u, int v) {
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


    // --------- 以下为重链剖分预处理 -----------

    void init2(int f, int u, int t) {
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

    void init1(int f, int u) {
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

}

class SegTree {
    private long[] sum;
    private long[] lazy;
    private int N;

    public SegTree(int len) {
        N = len;
        int tot = 1 << (33 - Integer.numberOfLeadingZeros(N - 1));
        sum = new long[tot];
        lazy = new long[tot];
    }

    public SegTree(int[] arr) {
        N = arr.length - 1;
        int tot = 1 << (33 - Integer.numberOfLeadingZeros(N - 1));
        sum = new long[tot];
        lazy = new long[tot];
        build(arr, 1, N, 1);
    }

    private void build(int[] arr, int l, int r, int i) {
        if (l == r) {
            sum[i] = arr[l];
            return;
        }
        int m = (l + r) >> 1;
        build(arr, l, m, i << 1);
        build(arr, m + 1, r, i << 1 | 1);
        up(i);
    }

    private void up(int i) {
        sum[i] = sum[i << 1] + sum[i << 1 | 1];
    }

    private void down(int i, int ln, int rn) {
        if (lazy[i] != 0) {
            lazy[i << 1] += lazy[i];
            lazy[i << 1 | 1] += lazy[i];
            sum[i << 1] += lazy[i] * ln;
            sum[i << 1 | 1] += lazy[i] * rn;
            lazy[i] = 0;
        }
    }

    public void add(int l, int r, long v) {
        add(l, r, v, 1, N, 1);
    }

    private void add(int L, int R, long v, int l, int r, int i) {
        if (L <= l && r <= R) {
            lazy[i] += v;
            sum[i] += v * (r - l + 1);
            return;
        }
        int m = (l + r) >> 1;
        down(i, m - l + 1, r - m);
        if (L <= m) {
            add(L, R, v, l, m, i << 1);
        }
        if (R > m) {
            add(L, R, v, m + 1, r, i << 1 | 1);
        }
        up(i);
    }

    public long query(int l, int r) {
        return query(l, r, 1, N, 1);
    }

    private long query(int L, int R, int l, int r, int i) {
        if (L <= l && r <= R) {
            return sum[i];
        }
        int m  = (l + r) >> 1;
        down(i, m - l + 1, r - m);
        long ans = 0;
        if (L <= m) {
            ans += query(L, R, l, m, i << 1);
        }
        if (R > m) {
            ans += query(L, R, m + 1, r, i << 1 | 1);
        }
        return ans;
    }

    public long query(int o) {
        return query(o, 1, N, 1);
    }

    private long query(int o, int l, int r, int i) {
        if (l == r) {
            return sum[i];
        }
        int m  = (l + r) >> 1;
        down(i, m - l + 1, r - m);
        if (o <= m) {
            return query(o, l, m, i << 1);
        } else {
            return query(o, m + 1, r, i << 1 | 1);
        }
    }
}