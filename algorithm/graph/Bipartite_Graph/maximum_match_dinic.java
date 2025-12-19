package algorithm.graph.Bipartite_Graph;
import static algorithm.zz.U.*;

import java.util.*;

/**
 * 测试链接：https://www.luogu.com.cn/problem/P3386
 * 二分图最大匹配（dinic algorithm）
 */
public class maximum_match_dinic {

    void solve() {
        int n = ni(), m = ni(), e = ni();
        MF mf = new MF(n, m);
        for (int i = 0; i < e; i++) {
            int u = ni(), v = ni();
            mf.addBiEdge(u, v);
        }
        println(mf.maxFlow());
    }

}

class MF {
    private final int INF = 0x3f3f3f3f;
    private int n, m, s, t, z;
    private int[] head, nxt, to, cap, es, dep, que;

    public MF(int n, int m) {
        this.n = n;
        this.m = m;
        s = n + m + 1;
        t = s + 1;
        z = 2;
        head = new int[n + m + 3];
        int tot = (n + m + 2) << 2;
        nxt = new int[tot];
        to = new int[tot];
        cap = new int[tot];
        for (int i = 1; i <= n; i++) {
            addEdge(s, i, 1);
            addEdge(i, s, 0);
        }
        for (int i = 1 + n; i <= m + n; i++) {
            addEdge(i, t, 1);
            addEdge(t, i, 0);
        }
        es = new int[n + m + 3];
        dep = new int[n + m + 3];
        que = new int[n + m + 2];
    }

    public void addBiEdge(int u, int v) {
        if (z + 1 <= nxt.length) {
            int len = nxt.length << 1;
            nxt = java.util.Arrays.copyOf(nxt, len);
            to = java.util.Arrays.copyOf(to, len);
            cap = java.util.Arrays.copyOf(cap, len);
        }
        v += n;
        addEdge(u, v, 1);
        addEdge(v, u, 0);
    }

    private void addEdge(int u, int v, int w) {
        nxt[z] = head[u];
        head[u] = z;
        cap[z] = w;
        to[z++] = v;
    }

    public int maxFlow() {
        int ans = 0;
        while (bfs()) {
            ans += flow(s, INF);
        }
        return ans;
    }

    public int[] match() {
        int[] ans = new int[n + 1];
        for (int i = 1; i <= n; i++) {
            for (int e = head[i]; e != 0; e = nxt[e]) {
                if (cap[e] == 0) {
                    ans[i] = to[e] - n;
                    break;
                }
            }
        }
        return ans;
    }

    private int flow(int u, int r) {
        if (u == t) {
            return r;
        }
        int ans = 0;
        int f;
        for (int e = es[u], v; e != 0 && r > 0; es[u] = e, e = nxt[e]) {
            v = to[e];
            if (cap[e] > 0 && dep[v] == dep[u] + 1) {
                f = flow(v, Math.min(r, cap[e]));
                if (f == 0) {
                    dep[v] = 0;
                }
                r -= f;
                cap[e] -= f;
                cap[e ^ 1] += f;
                ans += f;
            }
        }
        return ans;
    }

    private boolean bfs() {
        Arrays.fill(dep, 0);
        que[0] = s;
        dep[s] = 1;
        es[s] = head[s];
        for (int l = 0, r = 0; l <= r;) {
            int u = que[l++];
            for (int e = head[u], v; e != 0; e = nxt[e]) {
                v = to[e];
                if (dep[v] == 0 && cap[e] > 0) {
                    es[v] = head[v];
                    dep[v] = dep[u] + 1;
                    if (v == t) {
                        return true;
                    }
                    que[++r] = v;
                }
            }
        }
        return false;
    }
}
