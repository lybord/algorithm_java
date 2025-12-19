package algorithm.graph.network_flow;
import static algorithm.zz.U.*;

import java.util.*;

/**
 * 最小费用最大流
 * 测试链接：https://www.luogu.com.cn/problem/P3381
 */
public class CostFlow {

    void solve() {
        int n = ni(), m = ni(), s = ni(), t = ni();
        MCMF mcmf = new MCMF(n, m, s, t);
        while (m-- > 0) {
            int u = ni(), v = ni(), w = ni(), c = ni();
            mcmf.addBiEdge(u, v, w, c);
        }
        print(mcmf.maxFlow());
        print(' ');
        println(mcmf.cost());
    }

}

class MCMF {
    private final long INF = 0x3f3f3f3f3f3f3f3fL;
    private int n, s, t, z;
    private int[] head, nxt, to, es;
    private int qn, que[];
    private long[] cap, cost;
    private boolean[] vis;
    private long[] dis;
    private long res;

    public MCMF(int n, int s, int t) {
        this.n = n;
        this.s = s;
        this.t = t;
        z = 2;
        head = new int[n + 1];
        nxt = new int[n << 2];
        to = new int[n << 2];
        es = new int[n + 1];
        qn = 1 << (32 - Integer.numberOfLeadingZeros(n - 1));
        que = new int[qn--];
        cap = new long[n << 2];
        cost = new long[n << 2];
        vis = new boolean[n + 1];
        dis = new long[n + 1];
    }

    public void addBiEdge(int u, int v, long w, long c) {
        if (z + 1 >= nxt.length) {
            int len = nxt.length << 1;
            nxt = java.util.Arrays.copyOf(nxt, len);
            to = java.util.Arrays.copyOf(to, len);
            cap = java.util.Arrays.copyOf(cap, len);
            cost = java.util.Arrays.copyOf(cost, len);
        }
        addEdge(u, v, w, c);
        addEdge(v, u, 0, -c);
    }

    private void addEdge(int u, int v, long w, long c) {
        nxt[z] = head[u];
        head[u] = z;
        cap[z] = w;
        cost[z] = c;
        to[z++] = v;
    }

    public long maxFlow() {
        res = 0;
        long ans = 0, x;
        while (spfa()) {
            System.arraycopy(head, 1, es, 1, n);
            do {
                x = flow(s, INF);
                ans += x;
            } while (x > 0);
        }
        return ans;
    }

    public long cost() {
        return res;
    }

    private long flow(int u, long r) {
        if (u == t) {
            return r;
        }
        vis[u] = true;
        long f, ans = 0;
        for (int e = es[u], v; e != 0 && r > 0; es[u] = e, e = nxt[e]) {
            v = to[e];
            if (!vis[v] && cap[e] > 0 && dis[v] == dis[u] + cost[e]) {
                f = flow(v, Math.min(r, cap[e]));
                if (f > 0) {
                    res += f * cost[e];
                    cap[e] -= f;
                    cap[e ^ 1] += f;
                    r -= f;
                    ans += f;
                }
            }
        }
        vis[u] = false;
        return ans;
    }

    private boolean spfa() {
        Arrays.fill(dis, INF);
        que[0] = s;
        dis[s] = 0;
        vis[s] = true;
        for (int l = 0, r = 0; l <= r;) {
            int u = que[l++ & qn];
            vis[u] = false;
            for (int e = head[u], v; e != 0; e = nxt[e]) {
                v = to[e];
                if (cap[e] > 0 && dis[v] > dis[u] + cost[e]) {
                    dis[v] = dis[u] + cost[e];
                    if (!vis[v]) {
                        vis[v] = true;
                        que[++r & qn] = v;
                    }
                }
            }
        }
        return dis[t] != INF;
    }
}
