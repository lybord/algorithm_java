class SegTree {
    private long[] mx;
    private final int low, high;

    public SegTree(int low, int high) {
        this.low = low;
        this.high = high;
        mx = new long[1 << (33 - Integer.numberOfLeadingZeros(high - low))];
    }
    
    public SegTree(int[] arr, int low, int high) {
        this.low = low;
        this.high = high;
        mx = new long[1 << (33 - Integer.numberOfLeadingZeros(high - low))];
        build(arr, low, high, 1);
    }

    public void init(int[] arr) {
        build(arr, low, high, 1);
    }

    private void build(int[] arr, int l, int r, int i) {
        if (l == r) {
            mx[i] = arr[l];
            return;
        }
        int m = (l + r) >> 1;
        build(arr, l, m, i << 1);
        build(arr, m + 1, r, i << 1 | 1);
        up(i);
    }

    private void up(int i) {
        mx[i] = Math.max(mx[i << 1], mx[i << 1 | 1]);
    }

    public void add(int o, long v) {
        add(o, v, low, high, 1);
    }

    private void add(int o, long v, int l, int r, int i) {
        if (l == r) {
            mx[i] += v;
            return;
        }
        int m = (l + r) >> 1;
        if (o <= m) {
            add(o, v, l, m, i << 1);
        } else {
            add(o, v, m + 1, r, i << 1 | 1);
        }
        up(i);
    }

    public void set(int o, long v) {
        set(o, v, low, high, 1);
    }

    private void set(int o, long v, int l, int r, int i) {
        if (l == r) {
            mx[i] = v;
            return;
        }
        int m = (l + r) >> 1;
        if (o <= m) {
            set(o, v, l, m, i << 1);
        } else {
            set(o, v, m + 1, r, i << 1 | 1);
        }
        up(i);
    }

    public long query(int l, int r) {
        return query(l, r, low, high, 1);
    }

    private long query(int L, int R, int l, int r, int i) {
        if (L <= l && r <= R) {
            return mx[i];
        }
        int m  = (l + r) >> 1;
        long ans = Long.MIN_VALUE;
        if (L <= m) {
            ans = Math.max(ans, query(L, R, l, m, i << 1));
        }
        if (R > m) {
            ans = Math.max(ans, query(L, R, m + 1, r, i << 1 | 1));
        }
        return ans;
    }

    public long query(int o) {
        return query(o, low, high, 1);
    }

    private long query(int o, int l, int r, int i) {
        if (l == r) {
            return mx[i];
        }
        int m  = (l + r) >> 1;
        if (o <= m) {
            return query(o, l, m, i << 1);
        } else {
            return query(o, m + 1, r, i << 1 | 1);
        }
    }

    // [L, R] 上大于等于 v 的最右侧下标
    public int right(int L, int R, long v) {
        return right(L, R, v, low, high, 1);
    }

    private int right(int L, int R, long v, int l, int r, int i) {
        if (l == r) {
            return mx[i] >= v ? l : -1;
        }
        int m = (l + r) >> 1;
        if (L <= l && r <= R) {
            return mx[i] < v ? -1 : mx[i << 1 | 1] >= v ? right(L, R, v, m + 1, r, i << 1 | 1) : right(L, R, v, l, m, i << 1);
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

    // [L, R] 上大于等于 v 的最左侧下标
    public int left(int L, int R, long v) {
        return left(L, R, v, low, high, 1);
    }

    private int left(int L, int R, long v, int l, int r, int i) {
        if (l == r) {
            return mx[i] >= v ? l : -1;
        }
        int m = (l + r) >> 1;
        if (L <= l && r <= R) {
            return mx[i] < v ? -1 : mx[i << 1] >= v ? left(L, R, v, l, m, i << 1) : left(L, R, v, m + 1, r, i << 1 | 1);
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
