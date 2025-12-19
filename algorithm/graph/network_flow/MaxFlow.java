package algorithm.graph.network_flow;
import static algorithm.zz.U.*;

import java.util.*;

/**
 * 网络最大流
 * 测试链接：https://www.luogu.com.cn/problem/P3376
 */

public class MaxFlow {

    void solve() {
        int n = ni(), m = ni(), s = ni(), t = ni();
        MF mf = new MF(n, s, t);
        for (int i = 0; i < m; i++) {
            int u = ni(), v = ni(), w = ni();
            mf.addBiEdge(u, v, w);
        }
        println(mf.maxFlow());
    }

}

class MF {
    private final long INF = 0x3f3f3f3f3f3f3f3fL;
    private int n, s, t, z;
    private int[] head, nxt, to, es, dep, que;
    private long[] cap;

    public MF(int n, int s, int t) {
        this.n = n;
        this.s = s;
        this.t = t;
        z = 2;
        head = new int[n + 1];
        nxt = new int[n << 2];
        to = new int[n << 2];
        cap = new long[n << 2];
        es = new int[n + 1];
        dep = new int[n + 1];
        que = new int[n];
    }

    public void addBiEdge(int u, int v, long w) {
        if (z + 1 >= nxt.length) {
            int len = nxt.length << 1;
            nxt = java.util.Arrays.copyOf(nxt, len);
            to = java.util.Arrays.copyOf(to, len);
            cap = java.util.Arrays.copyOf(cap, len);
        }
        addEdge(u, v, w);
        addEdge(v, u, 0);
    }

    private void addEdge(int u, int v, long w) {
        nxt[z] = head[u];
        head[u] = z;
        cap[z] = w;
        to[z++] = v;
    }

    public long maxFlow() {
        long ans = 0;
        while (bfs()) {
            ans += flow(s, INF);
        }
        return ans;
    }

    private long flow(int u, long r) {
        if (u == t) {
            return r;
        }
        long ans = 0;
        long f;
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
