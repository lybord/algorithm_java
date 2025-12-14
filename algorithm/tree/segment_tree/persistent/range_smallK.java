class SegTree {
    private int[] root, cnt, lc, rc;
    private int[] val;
    private long[] sum;
    private int N, no;

    public SegTree(int[] a) {
        int n = a.length;
        val = a.clone();
        java.util.Arrays.sort(val);
        N = 1;
        for (int i = 1; i < n; i++) {
            if (val[i - 1] != val[i]) {
                val[N++] = val[i];
            }
        }
        // 以上为值域离散化，离散化后值域为 [0, N-1] ，此时 val[i] 为 i 的实际值

        // 总共 n 个版本
        // 每个版本的生成占用空间为： N 的不含前导零的二进制表示长度 + 1
        // 此外，还有一个 0 代表的初始版本，只占用一个空间
        int tot = n * (33 - Integer.numberOfLeadingZeros(N - 1)) + 1;

        root = new int[n];// root[i] 表示 a[:i] 生成的权值线段树的根节点编号
        lc = new int[tot];// lc[i] ：i 节点左儿子编号
        rc = new int[tot];// rc[i] ：i 节点右儿子编号
        cnt = new int[tot];
        sum = new long[tot];

        // a[:i] 的权值线段树 基于 a[:i-1] 的权值线段树 来生成
        root[0] = insert(java.util.Arrays.binarySearch(val, 0, N, a[0]), 0, N - 1, 0);
        for (int i = 1; i < n; i++) {
            root[i] = insert(java.util.Arrays.binarySearch(val, 0, N, a[i]), 0, N - 1, root[i - 1]);
        }
    }

    private int insert(int o, int l, int r, int i) {
        int u = ++no;
        cnt[u] = cnt[i] + 1;
        sum[u] = sum[i] + val[o];
        if (l == r) {
            return u;
        }
        int m = (l + r) >> 1;
        if (o <= m) {
            lc[u] = insert(o, l, m, lc[i]);
            rc[u] = rc[i];
        } else {
            lc[u] = lc[i];
            rc[u] = insert(o, m + 1, r, rc[i]);
        }
        return u;
    }

    // 求 a[l:r] 中的第 k 小值
    public int smallK(int l, int r, int k) {
        return smallK(k, 0, N - 1, l == 0 ? 0 : root[l - 1], root[r]);
    }

    private int smallK(int k, int l, int r, int i, int j) {
        if (l == r) {
            return val[l];
        }
        int m = (l + r) >> 1;
        int t = cnt[lc[j]] - cnt[lc[i]];
        if (t < k) {
            return smallK(k - t, m + 1, r, rc[i], rc[j]);
        } else {
            return smallK(k, l, m, lc[i], lc[j]);
        }
    }

    // 求 a[l:r] 中的前 k 小累加和
    public long smallKSum(int l, int r, int k) {
        return smallKSum(k, 0, N - 1, l == 0 ? 0 : root[l - 1], root[r]);
    }

    private long smallKSum(int k, int l, int r, int i, int j) {
        if (l == r) {
            return (long) val[l] * k;
        }
        int m = (l + r) >> 1;
        int t = cnt[lc[j]] - cnt[lc[i]];
        if (t < k) {
            return sum[lc[j]] - sum[lc[i]] + smallKSum(k - t, m + 1, r, rc[i], rc[j]);
        } else {
            return smallKSum(k, l, m, lc[i], lc[j]);
        }
    }

    // 查询 a[l:r] 中 v 的数量
    public int count(int l, int r, int v) {
        v = java.util.Arrays.binarySearch(val, 0, N, v);
        return v < 0 ? 0 : count(v, 0, N - 1, l == 0 ? 0 : root[l - 1], root[r]);
    }

    private int count(int o, int l, int r, int i, int j) {
        if (l == r) {
            return cnt[j] - cnt[i];
        }
        int m = (l + r) >> 1;
        if (o <= m) {
            return count(o, l, m, lc[i], lc[j]);
        } else {
            return count(o, m + 1, r, rc[i], rc[j]);
        }
    }

    // 查询 a[l:r] 中 小于 v 的数量
    public int countSmall(int l, int r, int v) {
        return countSmall(v, 0, N - 1, l == 0 ? 0 : root[l - 1], root[r]);
    }

    private int countSmall(int o, int l, int r, int i, int j) {
        if (l == r) {
            return val[l] < o ? cnt[j] - cnt[i] : 0;
        }
        int m = (l + r) >> 1;
        if (o > val[m]) {
            return cnt[lc[j]] - cnt[lc[i]] + countSmall(o, m + 1, r, rc[i], rc[j]);
        } else {
            return countSmall(o, l, m, lc[i], lc[j]);
        }
    }

    // 查询 a[l:r] 中 大于 v 的数量
    public int countMore(int l, int r, int v) {
        return countMore(v, 0, N - 1, l == 0 ? 0 : root[l - 1], root[r]);
    }

    private int countMore(int o, int l, int r, int i, int j) {
        if (l == r) {
            return val[l] > o ? cnt[j] - cnt[i] : 0;
        }
        int m = (l + r) >> 1;
        if (o >= val[m]) {
            return countMore(o, m + 1, r, rc[i], rc[j]);
        } else {
            return cnt[rc[j]] - cnt[rc[i]] + countMore(o, l, m, lc[i], lc[j]);
        }
    }

    // 查询 a[l:r] 中值在 [vl, vr] 上的元素数量
    public int countRange(int l, int r, int vl, int vr) {
        vl = Math.max(vl, val[0]);
        vr = Math.min(vr, val[N - 1]);
        return vl > vr ? 0 : countRange(vl, vr, 0, N - 1, l == 0 ? 0 : root[l - 1], root[r]);
    }

    private int countRange(int L, int R, int l, int r, int i, int j) {
        if (L <= val[l] && val[r] <= R) {
            return cnt[j] - cnt[i];
        }
        int m = (l + r) >> 1;
        int ans = 0;
        if (L <= val[m]) {
            ans += countRange(L, R, l, m, lc[i], lc[j]);
        }
        if (R > val[m]) {
            ans += countRange(L, R, m + 1, r, rc[i], rc[j]);
        }
        return ans;
    }
}
