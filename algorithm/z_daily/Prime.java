class Prime {
    private static final int[] x32 = {2, 7, 61}, x78 = {2, 3, 5, 7, 11, 13, 17, 19, 23, 29, 31, 37};
    private static final int N = 1000000;
    private static boolean[] isp;
    private static int[] mf;
    private static int[] num;
    private static int[][] factory;

    private static final long i31 = 1L << 31;
    private static java.util.Random rnd = new java.util.Random();
    private static long[] fac = new long[64];
    private static int fi;

    static {
        isp = new boolean[N + 1];
        mf = new int[N + 1];
        num = new int[N / 2 + 1];
        int j = 0;
        for (int i = 2, v; i <= N; i++) {
            if (mf[i] == 0) {
                mf[i] = i;
                isp[i] = true;
                num[j++] = i;
            }
            for (int p : num) {
                v = p * i;
                if (v > N) {
                    break;
                }
                mf[v] = p;
                if (i % p == 0) {
                    break;
                }
            }
        }
        num = java.util.Arrays.copyOf(num, j);
    }

    public static boolean isP(int n) {
        return n <= N ? isp[n] : MillerRabin(n);
    }

    public static boolean isP(long n) {
        return n < i31 ? isP((int) n) : MillerRabin(n);
    }
    
    public static int[] primes() {
        return num;
    }

    public static int[] factories(int n) {
        if (n <= N) {
            if (factory == null) {
                int[] len = new int[N + 1];
                for (int i = 1; i <= N; i++) {
                    for (int j = i; j <= N; j += i) {
                        len[j]++;
                    }
                }
                factory = new int[N + 1][];
                for (int i = 1; i <= N; i++) {
                    factory[i] = new int[len[i]];
                }
                for (int i = N; i >= 1; i--) {
                    for (int j = i; j <= N; j += i) {
                        factory[j][--len[j]] = i;
                    }
                }
            }
            return factory[n];
        }
        long[] a = factories((long) n);
        int[] res = new int[a.length];
        for (int i = 0; i < a.length; i++) {
            res[i] = (int) a[i];
        }
        return res;
    }

    public static long[] factories(long n) {
        long[] a = pFactories(n);
        long[] tmp = new long[a.length];
        int z = 0;
        for (int i = 0, j = i; i < a.length; i = j) {
            while (j < a.length && a[i] == a[j]) {
                j++;
            }
            int len = j - i;
            long v = a[i], c = 1;
            while (len >= c) {
                tmp[z++] = v;
                len -= c;
                c <<= 1;
                v *= v;
            }
            if (len > 0) {
                v = 1;
                while (len-- > 0) {
                    v *= a[i];
                }
                tmp[z++] = v;
            }
        }
        long[] res = new long[1 << z];
        java.util.Arrays.fill(res, 1);
        for (int s = 0; s < 1 << z; s++) {
            for (int i = 0; i < z; i++) {
                if ((s >> i & 1) != 0) {
                    res[s] *= tmp[i];
                }
            }
        }
        java.util.Arrays.sort(res);
        z = 1;
        for (int i = 1; i < res.length; i++) {
            if (res[i - 1] != res[i]) {
                res[z++] = res[i];
            }
        }
        return java.util.Arrays.copyOf(res, z);
    }

    public static int minf(int n) {
        return (int) minf((long) n);
    }

    public static long minf(long n) {
        if (n <= N) {
            return mf[(int) n];
        }
        fi = 0;
        dfs(n);
        long ans = n;
        for (int i = 0; i < fi; i++) {
            ans = Math.min(ans, fac[i]);
        }
        return ans;
    }

    public static int maxf(int n) {
        return (int) maxf((long) n);
    }

    public static long maxf(long n) {
        fi = 0;
        dfs(n);
        long ans = 0;
        for (int i = 0; i < fi; i++) {
            ans = Math.max(ans, fac[i]);
        }
        return ans;
    }

    public static int[] pFactories(int n) {
        fi = 0;
        dfs(n);
        int[] ans = new int[fi];
        for (int i = 0; i < fi; i++) {
            ans[i] = (int) fac[i];
        }
        java.util.Arrays.sort(ans);
        return ans;
    }

    public static long[] pFactories(long n) {
        fi = 0;
        dfs(n);
        long[] ans = new long[fi];
        for (int i = 0; i < fi; i++) {
            ans[i] = fac[i];
        }
        java.util.Arrays.sort(ans);
        return ans;
    }
    
    private static void dfs(int n) {
        if (n < 2) {
            return;
        }
        if (isP(n)) {
            fac[fi++] = n;
            return;
        }
        int d;
        do {
            d = PollardRho(n);
        } while (d == n);
        dfs(d);
        dfs(n / d);
    }

    private static void dfs(long n) {
        if (n < i31) {
            dfs((int) n);
            return;
        }
        if (isP(n)) {
            fac[fi++] = n;
            return;
        }
        long d;
        do {
            d = PollardRho(n);
        } while (d == n);
        dfs(d);
        dfs(n / d);
    }

    private static boolean MillerRabin(int n) {
        int a = n - 1, b = Integer.numberOfTrailingZeros(a);
        a >>= b;
        for (int x : x32) {
            if (!check(n, a, b, x)) {
                return false;
            }
        }
        return true;
    }

    private static boolean MillerRabin(long n) {
        long a = n - 1;
        int b = Long.numberOfTrailingZeros(a);
        a >>= b;
        for (int s : x78) {
            if (!check2(n, a, b, s)) {
                return false;
            }
        }
        return true;
    }

    private static boolean check(long n, long a, int b, int x) {
        long v = pow(x, a, n);
        if (v == 1) {
            return true;
        }
        int j = 1;
        while (j <= b) {
            if (v == n - 1) {
                return true;
            }
            v = v * v % n;
            j++;
        }
        return false;
    }
    
    private static boolean check2(long n, long a, int b, int x) {
        long v = pow2(x, a, n);
        if (v == 1) {
            return true;
        }
        int j = 1;
        while (j <= b) {
            if (v == n - 1) {
                return true;
            }
            v = mul(v, v, n);
            j++;
        }
        return false;
    }
    
    private static int PollardRho(int n) {
        if (n <= N) {
            return mf[n];
        }
        long s = 0, t = 0, c = rnd.nextInt(n - 1) + 1;
        int stp = 0, goal = 1;
        long val = 1;
        long d;
        for (goal = 1; ; goal <<= 1, s = t, val = 1) {
            for (stp = 1; stp <= goal; stp++) {
                t = f(t, c, n);
                val = val * Math.abs(t - s) % n;
                if (stp % 127 == 0 && (d = gcd(val, n)) > 1) {
                    return (int) d;
                }
            }
            if ((d = gcd(val, n)) > 1) {
                return (int) d;
            }
        }
    }
    
    private static long PollardRho(long n) {
        if (n < i31) {
            return PollardRho((int) n);
        }
        long s = 0, t = 0, c = rnd.nextLong(n - 1) + 1;
        int stp = 0, goal = 1;
        long val = 1;
        long d;
        for (goal = 1; ; goal <<= 1, s = t, val = 1) {
            for (stp = 1; stp <= goal; stp++) {
                t = f2(t, c, n);
                val = mul(val, Math.abs(t - s), n);
                if (stp % 127 == 0 && (d = gcd(val, n)) > 1) {
                    return d;
                }
            }
            if ((d = gcd(val, n)) > 1) {
                return d;
            }
        }
    }
    
    private static long f(long x, long c, long m) {
        return (x * x + c) % m;
    }
    private static long f2(long x, long c, long m) {
        return (mul(x, x, m) + c) % m;
    }
    
    private static long gcd(long a, long b) {
        while (b != 0) {
            b = a % (a = b);
        }
        return a;
    }

    private static long pow(long a, long b, long m) {
        long res = 1;
        while (b != 0) {
            if ((b & 1) != 0) {
                res = res * a % m;
            }
            a = a * a % m;
            b >>= 1;
        }
        return res;
    }

    private static long pow2(long a, long b, long m) {
        long res = 1;
        while (b != 0) {
            if ((b & 1) != 0) {
                res = mul(res, a, m);
            }
            a = mul(a, a, m);
            b >>= 1;
        }
        return res;
    }
    
    private static long mul(long a, long b, long m) {
        long high = Math.multiplyHigh(a, b), low = a * b;
        high %= m;
        long f = -1;
        for (int i = 63; i >= 0;) {
            int h = Math.min(Long.numberOfLeadingZeros(high) - 1, i + 1);
            high = (high << h | ((f & low) >>> (i + 1 - h))) % m;
            f >>>= h;
            i -= h;
        }
        return high;
    }
}
