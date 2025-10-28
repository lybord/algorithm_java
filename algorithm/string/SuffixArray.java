class SuffixArray {// DC3

    public int[] sa;// sa[i] 表示排名为 i 的后缀

    public int[] rk;// rk[i] 表示 i 号后缀的排名

    public int[] height;// height[i] 表示 sa[i] 和 sa[i - 1] 的 最长公共前缀（lcp）

    private int[] log;// 2^log[i] 表示最接近但不超过 i 的 2 的某次方

    private int[][] st;// 查询两个后缀的最长公共前缀（lcp）

    private char[] str;// 原始字符串
    
    private int[] arr;// 原始数组

    public SuffixArray(char[] str) {
        this.str = str;
        int mn = Integer.MAX_VALUE, mx = Integer.MIN_VALUE;
        for (char c : str) {
            mn = Math.min(mn, c);
            mx = Math.max(mx, c);
        }
        mn--;
        int n = str.length;
        int[] nums = new int[n];
        for (int i = 0; i < n; i++) {
            nums[i] = str[i] - mn;
        }
        mx -= mn;
        init(nums, mx);
    }

    public SuffixArray(int[] arr) {
        this.arr = arr;
        int n = arr.length;
        int[] val = arr.clone();
        java.util.Arrays.sort(val);
        int m = 1;
        for (int i = 1; i < n; i++) {
            if (val[i - 1] != val[i]) {
                val[m++] = val[i];
            }
        }
        int[] nums = new int[n];
        for (int i = 0; i < n; i++) {
            nums[i] = java.util.Arrays.binarySearch(val, 0, m, arr[i]) + 1;
        }
        init(nums, m);
    }

    private void init(int[] nums, int mx) {
        if (nums.length == 1) {
            sa = new int[1];
            rk = new int[1];
        } else {
            sa = sa(nums, mx);
            rk = rank();
        }
        height = height(nums);
        st = rmq();
    }

    private int[] sa(int[] nums, int max) {
        int n = nums.length;
        int[] arr = new int[n + 3];
        for (int i = 0; i < n; i++) {
            arr[i] = nums[i];
        }
        return skew(arr, n, max);
    }

    private int[] skew(int[] nums, int n, int K) {
        int n0 = (n + 2) / 3, n1 = (n + 1) / 3, n2 = n / 3, n02 = n0 + n2;
        int[] s12 = new int[n02 + 3], sa12 = new int[n02 + 3];
        for (int i = 0, j = 0; i < n + (n0 - n1); ++i) {
            if (0 != i % 3) {
                s12[j++] = i;
            }
        }
        radixPass(nums, s12, sa12, 2, n02, K);
        radixPass(nums, sa12, s12, 1, n02, K);
        radixPass(nums, s12, sa12, 0, n02, K);
        int name = 0, c0 = -1, c1 = -1, c2 = -1;
        for (int i = 0; i < n02; ++i) {
            if (c0 != nums[sa12[i]] || c1 != nums[sa12[i] + 1] || c2 != nums[sa12[i] + 2]) {
                name++;
                c0 = nums[sa12[i]];
                c1 = nums[sa12[i] + 1];
                c2 = nums[sa12[i] + 2];
            }
            if (1 == sa12[i] % 3) {
                s12[sa12[i] / 3] = name;
            } else {
                s12[sa12[i] / 3 + n0] = name;
            }
        }
        if (name < n02) {
            sa12 = skew(s12, n02, name);
            for (int i = 0; i < n02; i++) {
                s12[sa12[i]] = i + 1;
            }
        } else {
            for (int i = 0; i < n02; i++) {
                sa12[s12[i] - 1] = i;
            }
        }
        int[] s0 = new int[n0], sa0 = new int[n0];
        for (int i = 0, j = 0; i < n02; i++) {
            if (sa12[i] < n0) {
                s0[j++] = 3 * sa12[i];
            }
        }
        radixPass(nums, s0, sa0, 0, n0, K);
        int[] sa = new int[n];
        for (int p = 0, t = n0 - n1, k = 0; k < n; k++) {
            int i = sa12[t] < n0 ? sa12[t] * 3 + 1 : (sa12[t] - n0) * 3 + 2;
            int j = sa0[p];
            if (sa12[t] < n0 ? leq(nums[i], s12[sa12[t] + n0], nums[j], s12[j / 3])
                    : leq(nums[i], nums[i + 1], s12[sa12[t] - n0 + 1], nums[j], nums[j + 1], s12[j / 3 + n0])) {
                sa[k] = i;
                t++;
                if (t == n02) {
                    for (k++; p < n0; p++, k++) {
                        sa[k] = sa0[p];
                    }
                }
            } else {
                sa[k] = j;
                p++;
                if (p == n0) {
                    for (k++; t < n02; t++, k++) {
                        sa[k] = sa12[t] < n0 ? sa12[t] * 3 + 1 : (sa12[t] - n0) * 3 + 2;
                    }
                }
            }
        }
        return sa;
    }

    private void radixPass(int[] nums, int[] input, int[] output, int offset, int n, int k) {
        int[] cnt = new int[k + 1];
        for (int i = 0; i < n; ++i) {
            cnt[nums[input[i] + offset]]++;
        }
        for (int i = 0, sum = 0; i < cnt.length; ++i) {
            int t = cnt[i];
            cnt[i] = sum;
            sum += t;
        }
        for (int i = 0; i < n; ++i) {
            output[cnt[nums[input[i] + offset]]++] = input[i];
        }
    }

    private boolean leq(int a1, int a2, int b1, int b2) {
        return a1 < b1 || (a1 == b1 && a2 <= b2);
    }

    private boolean leq(int a1, int a2, int a3, int b1, int b2, int b3) {
        return a1 < b1 || (a1 == b1 && leq(a2, a3, b2, b3));
    }

    private int[] rank() {
        int n = sa.length;
        int[] ans = new int[n];
        for (int i = 0; i < n; i++) {
            ans[sa[i]] = i;
        }
        return ans;
    }

    private int[] height(int[] s) {
        int n = s.length;
        int[] ans = new int[n];
        for (int i = 0, k = 0; i < n; ++i) {
            if (rk[i] != 0) {
                if (k > 0) {
                    --k;
                }
                int j = sa[rk[i] - 1];
                while (i + k < n && j + k < n && s[i + k] == s[j + k]) {
                    ++k;
                }
                ans[rk[i]] = k;
            }
        }
        return ans;
    }

    private int[][] rmq() {
        int n = sa.length, m = 32 - Integer.numberOfLeadingZeros(n);
        log = new int[n + 1];
        for (int i = 2; i <= n; i++) {
            log[i] = log[i >> 1] + 1;
        }
        int[][] st = new int[n][m];
        for (int i = 0; i < n; i++) {
            st[i][0] = height[i];
        }
        for (int j = 1; j < m; j++) {
            for (int i = 0; i + (1 << j) - 1 < n; i++) {
                st[i][j] = Math.min(st[i][j - 1], st[i + (1 << (j - 1))][j - 1]);
            }
        }
        return st;
    }

    public int lcp(int i, int j) {
        if (i == j) {
            return sa.length - sa[rk[i]];
        }
        i = rk[i];
        j = rk[j];
        if (i > j) {
            i ^= j ^ (j = i);
        }
        i++;
        int g = log[j - i + 1];
        return Math.min(st[i][g], st[j - (1 << g) + 1][g]);
    }

    public int[] saRange(char[] tar) {
        int l = 0, r = sa.length - 1, m;
        int left = -1, right = -1;
        while (l <= r) {
            m = (l + r) >> 1;
            int d = cmp(tar, sa[m]);
            if (d == 0) {
                left = m;
                r = m - 1;
            } else if (d < 0) {
                r = m - 1;
            } else {
                l = m + 1;
            }
        }
        if (left == -1) {
            return new int[]{-1, -1};
        }
        l = 0;
        r = sa.length - 1;
        while (l <= r) {
            m = (l + r) >> 1;
            int d = cmp(tar, sa[m]);
            if (d == 0) {
                right = m;
                l = m + 1;
            } else if (d < 0) {
                r = m - 1;
            } else {
                l = m + 1;
            }
        }
        return new int[]{left, right};
    }

    public int[] saRange(int[] tar) {
        int l = 0, r = sa.length - 1, m;
        int left = -1, right = -1;
        while (l <= r) {
            m = (l + r) >> 1;
            int d = cmp(tar, sa[m]);
            if (d == 0) {
                left = m;
                r = m - 1;
            } else if (d < 0) {
                r = m - 1;
            } else {
                l = m + 1;
            }
        }
        if (left == -1) {
            return new int[]{-1, -1};
        }
        l = 0;
        r = sa.length - 1;
        while (l <= r) {
            m = (l + r) >> 1;
            int d = cmp(tar, sa[m]);
            if (d == 0) {
                right = m;
                l = m + 1;
            } else if (d < 0) {
                r = m - 1;
            } else {
                l = m + 1;
            }
        }
        return new int[]{left, right};
    }
    
    private int cmp(char[] tar, int idx) {
        int j = idx;
        for (int i = 0; i < tar.length; i++, j++) {
            if (j == str.length) {
                return 1;
            }
            if (tar[i] < str[j]) {
                return -1;
            } else if (tar[i] > str[j]) {
                return 1;
            }
        }
        return 0;
    }

    private int cmp(int[] tar, int idx) {
        int j = idx;
        for (int i = 0; i < tar.length; i++, j++) {
            if (j == arr.length) {
                return 1;
            }
            if (tar[i] < arr[j]) {
                return -1;
            } else if (tar[i] > arr[j]) {
                return 1;
            }
        }
        return 0;
    }
}
