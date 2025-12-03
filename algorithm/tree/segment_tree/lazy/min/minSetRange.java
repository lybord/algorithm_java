class SegTree {
    private long[] mn;
    private long[] lazy;
    private final int low, high;

    public SegTree(int low, int high) {
        this.low = low;
        this.high = high;
        int tot = 1 << (33 - Integer.numberOfLeadingZeros(high - low));
        mn = new long[tot];
        lazy = new long[tot];
        java.util.Arrays.fill(lazy, Long.MIN_VALUE);
    }

    public SegTree(int[] arr, int low, int high) {
        this.low = low;
        this.high = high;
        int tot = 1 << (33 - Integer.numberOfLeadingZeros(high - low));
        mn = new long[tot];
        lazy = new long[tot];
        java.util.Arrays.fill(lazy, Long.MIN_VALUE);
        build(arr, low, high, 1);
    }

    public void init(int[] arr) {
        build(arr, low, high, 1);
    }

    private void build(int[] arr, int l, int r, int i) {
        if (l == r) {
            mn[i] = arr[l];
            return;
        }
        int m = (l + r) >> 1;
        build(arr, l, m, i << 1);
        build(arr, m + 1, r, i << 1 | 1);
        up(i);
    }

    private void up(int i) {
        mn[i] = Math.min(mn[i << 1], mn[i << 1 | 1]);
    }

    private void down(int i) {
        if (lazy[i] != Long.MIN_VALUE) {
            lazy[i << 1] = lazy[i];
            lazy[i << 1 | 1] = lazy[i];
            mn[i << 1] = lazy[i];
            mn[i << 1 | 1] = lazy[i];
            lazy[i] = Long.MIN_VALUE;
        }
    }

    public void set(int l, int r, long v) {
        set(l, r, v, low, high, 1);
    }

    private void set(int L, int R, long v, int l, int r, int i) {
        if (L <= l && r <= R) {
            lazy[i] = v;
            mn[i] = v;
            return;
        }
        int m = (l + r) >> 1;
        down(i);
        if (L <= m) {
            set(L, R, v, l, m, i << 1);
        }
        if (R > m) {
            set(L, R, v, m + 1, r, i << 1 | 1);
        }
        up(i);
    }

    public long query(int l, int r) {
        return query(l, r, low, high, 1);
    }

    private long query(int L, int R, int l, int r, int i) {
        if (L <= l && r <= R) {
            return mn[i];
        }
        int m  = (l + r) >> 1;
        down(i);
        long ans = Long.MAX_VALUE;
        if (L <= m) {
            ans = Math.min(ans, query(L, R, l, m, i << 1));
        }
        if (R > m) {
            ans = Math.min(ans, query(L, R, m + 1, r, i << 1 | 1));
        }
        return ans;
    }

    public long query(int o) {
        return query(o, low, high, 1);
    }

    private long query(int o, int l, int r, int i) {
        if (l == r) {
            return mn[i];
        }
        int m  = (l + r) >> 1;
        down(i);
        if (o <= m) {
            return query(o, l, m, i << 1);
        } else {
            return query(o, m + 1, r, i << 1 | 1);
        }
    }

    // [L, R] 上小于等于 v 的最右侧下标
    public int right(int L, int R, long v) {
        return right(L, R, v, low, high, 1);
    }

    private int right(int L, int R, long v, int l, int r, int i) {
        if (l == r) {
            return mn[i] <= v ? l : -1;
        }
        int m = (l + r) >> 1;
        down(i);
        if (L <= l && r <= R) {
            return mn[i] > v ? -1 : mn[i << 1 | 1] <= v ? right(L, R, v, m + 1, r, i << 1 | 1) : right(L, R, v, l, m, i << 1);
        }
        int ans = -1;
        if (R > m) {
            ans = right(L, R, v, m + 1, r, i << 1 | 1);
        }
        if (ans == -1 && L <= m) {
            ans = right(L, R, v, l, m, i << 1);
        }
        return ans;
    }

    // [L, R] 上小于等于 v 的最左侧下标
    public int left(int L, int R, long v) {
        return left(L, R, v, low, high, 1);
    }

    private int left(int L, int R, long v, int l, int r, int i) {
        if (l == r) {
            return mn[i] <= v ? l : -1;
        }
        int m = (l + r) >> 1;
        down(i);
        if (L <= l && r <= R) {
            return mn[i] > v ? -1 : mn[i << 1] <= v ? left(L, R, v, l, m, i << 1) : left(L, R, v, m + 1, r, i << 1 | 1);
        }
        int ans = -1;
        if (L <= m) {
            ans = left(L, R, v, l, m, i << 1);
        }
        if (ans == -1 && R > m) {
            ans = left(L, R, v, m + 1, r, i << 1 | 1);
        }
        return ans;
    }
}
