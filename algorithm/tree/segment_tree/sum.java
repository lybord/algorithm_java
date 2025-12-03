class SegTree {
    private long[] sum;
    private final int low, high;

    public SegTree(int low, int high) {
        this.low = low;
        this.high = high;
        sum = new long[1 << (33 - Integer.numberOfLeadingZeros(high - low))];
    }

    public SegTree(int[] arr, int low, int high) {
        this.low = low;
        this.high = high;
        sum = new long[1 << (33 - Integer.numberOfLeadingZeros(high - low))];
        build(arr, low, high, 1);
    }

    public void init(int[] arr) {
        build(arr, low, high, 1);
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

    public void add(int o, long v) {
        add(o, v, low, high, 1);
    }

    private void add(int o, long v, int l, int r, int i) {
        if (l == r) {
            sum[i] += v;
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
            sum[i] = v;
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
            return sum[i];
        }
        int m  = (l + r) >> 1;
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
        return query(o, low, high, 1);
    }

    private long query(int o, int l, int r, int i) {
        if (l == r) {
            return sum[i];
        }
        int m  = (l + r) >> 1;
        if (o <= m) {
            return query(o, l, m, i << 1);
        } else {
            return query(o, m + 1, r, i << 1 | 1);
        }
    }
}
