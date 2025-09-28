public class Mat {

    // 矩阵快速幂
    long[][] pow(long[][] a, long b) {
        long[][] res = new long[a.length][a.length];
        for (int i = 0; i < a.length; i++) {
            res[i][i] = 1;
        }
        while (b != 0) {
            if ((b & 1) != 0) {
                res = mul(res, a);
            }
            a = mul(a, a);
            b >>= 1;
        }
        return res;
    }
    
    // 矩阵乘法
    long[][] mul(long[][] mat1, long[][] mat2) {
        int n = mat1.length, m = mat2[0].length;
        long[][] ans = new long[n][m];
        for (int i = 0; i < n; i++) {
            for (int j = 0; j < m; j++) {
                for (int k = 0; k < mat2.length; k++) {
                    ans[i][j] += mat1[i][k] * mat2[k][j];
                }
            }
        }
        return ans;
    }
}

// 32位整数
class MatrixQuery {
    private int n, m;
    private int[][] mat;
    private int[][] sum;
    private int[] lts, lbs;
    private int[][] rtt, rtb, rbt, rbb;

    public MatrixQuery(int[][] origin) {
        n = origin.length;
        m = origin[0].length;
        mat = new int[n][m];
        for (int i = 0; i < n; i++) {
            for (int j = 0; j < m; j++) {
                mat[i][j] = origin[i][j];
            }
        }
        sum = new int[n + 1][m + 1];
        for (int i = 0; i < n; i++) {
            for (int j = 0; j < m; j++) {
                sum[i + 1][j + 1] = sum[i + 1][j] + sum[i][j + 1] - sum[i][j] + mat[i][j];
            }
        }
        lts = new int[n + m];
        lbs = new int[n + m];
        for (int i = 0; i < n + m - 1; i++) {
            int x = Math.min(i, n - 1), y = i - x;
            lts[i + 1] = lts[i];
            while (x >= 0 && y < m) {
                lts[i + 1] += mat[x--][y++];
            }
            // y - x + (n - 1) = i
            x = Math.max(0, n - 1 - i);
            y = i + x - (n - 1);
            lbs[i + 1] = lbs[i];
            while (x < n && y < m) {
                lbs[i + 1] += mat[x++][y++];
            }
        }
        rtt = new int[n + 1][m + 2];
        rtb = new int[n + 2][m + 1];
        rbt = new int[n + 1][m + 1];
        rbb = new int[n + 2][m + 2];
        rtt = new int[n + 1][m + 2];
        rtb = new int[n + 2][m + 1];
        rbt = new int[n + 1][m + 1];
        rbb = new int[n + 2][m + 2];
        for (int i = 0; i < n; i++) {
            for (int j = 0; j < m; j++) {
                rtt[i + 1][j + 1] = rtt[i][j + 2] + mat[i][j];
                rbt[i + 1][j + 1] = rbt[i][j] + mat[i][j];
            }
        }
        for (int j = 0; j < m; j++) {
            for (int i = n - 1; i >= 0; i--) {
                rtb[i + 1][j + 1] = rtb[i + 2][j] + mat[i][j];
            }
        }
        for (int j = m - 1; j >= 0; j--) {
            for (int i = n - 1; i >= 0; i--) {
                rbb[i + 1][j + 1] = rbb[i + 2][j + 2] + mat[i][j];
            }
        }
        for (int i = 0; i < n; i++) {
            for (int j = 0; j < m; j++) {
                rtt[i + 1][j + 1] += rtt[i + 1][j];
                rtb[i + 1][j + 1] += rtb[i][j + 1];
                rbt[i + 1][j + 1] += rbt[i + 1][j];
                rbb[i + 1][j + 1] += rbb[i][j + 1];
            }
        }
    }

    public int queryManhattanArea(int x, int y, int d) {
        int l = Math.max(0, y - d), r = Math.min(m - 1, y + d), t = Math.max(0, x - d), b = Math.min(n - 1, x + d);
        int s = queryMat(t, l, b, r);
        if (t < x + y - l - d) {
            s -= queryTriLT(t, l, (x + y - l - d) - t - 1);
        }
        if (t < x + r - y - d) {
            s -= queryTriRT(t, r, (x + r - y - d) - t - 1);
        }
        if (b > d - (r - y) + x) {
            s -= queryTriRB(b, r, b - (d - (r - y) + x) - 1);
        }
        if (b > d - (y - l) + x) {
            s -= queryTriLB(b, l, b - (d - (y - l) + x) - 1);
        }
        return s;
    }

    public int queryTriLB(int x, int y, int l) {
        if (l == 0) {
            return mat[x][y];
        }
        return queryMat(x - l, y, x, y + l) - queryTriRT(x - l, y + l, l - 1);
    }

    public int queryTriRT(int x, int y, int l) {
        if (l == 0) {
            return mat[x][y];
        }
        int s = lbs[y - x + n] - lbs[y - x - l + (n - 1)];
        int sl = rbt[x][y] - rbt[x][Math.max(0, y - l - 1)];
        int sr = rbb[Math.min(x + l + 2, n)][y + 2] - rbb[x + 1][y + 2];
        return s - sl - sr;
    }

    public int queryTriRB(int x, int y, int l) {
        if (l == 0) {
            return mat[x][y];
        }
        return queryMat(x - l, y - l, x, y) - queryTriLT(x - l, y - l, l - 1);
    }

    public int queryTriLT(int x, int y, int l) {
        if (l == 0) {
            return mat[x][y];
        }
        int s = lts[x + y + l + 1] - lts[x + y];
        int sl = rtb[Math.min(n, x + l + 2)][y] - rtb[x + 1][y];
        int sr = rtt[x][Math.min(m, y + l + 2)] - rtt[x][y + 1];
        return s - sl - sr;
    }

    public int queryMat(int x1, int y1, int x2, int y2) {
        return sum[x2 + 1][y2 + 1] - sum[x2 + 1][y1] - sum[x1][y2 + 1] + sum[x1][y1];
    }
}

// 64位整数
class MatrixQuery2 {
    private int n, m;
    private long[][] mat;
    private long[][] sum;
    private long[] lts, lbs;
    private long[][] rtt, rtb, rbt, rbb;

    public MatrixQuery2(int[][] origin) {
        n = origin.length;
        m = origin[0].length;
        mat = new long[n][m];
        for (int i = 0; i < n; i++) {
            for (int j = 0; j < m; j++) {
                mat[i][j] = origin[i][j];
            }
        }
        sum = new long[n + 1][m + 1];
        for (int i = 0; i < n; i++) {
            for (int j = 0; j < m; j++) {
                sum[i + 1][j + 1] = sum[i + 1][j] + sum[i][j + 1] - sum[i][j] + mat[i][j];
            }
        }
        lts = new long[n + m];
        lbs = new long[n + m];
        for (int i = 0; i < n + m - 1; i++) {
            int x = Math.min(i, n - 1), y = i - x;
            lts[i + 1] = lts[i];
            while (x >= 0 && y < m) {
                lts[i + 1] += mat[x--][y++];
            }
            // y - x + (n - 1) = i
            x = Math.max(0, n - 1 - i);
            y = i + x - (n - 1);
            lbs[i + 1] = lbs[i];
            while (x < n && y < m) {
                lbs[i + 1] += mat[x++][y++];
            }
        }
        rtt = new long[n + 1][m + 2];
        rtb = new long[n + 2][m + 1];
        rbt = new long[n + 1][m + 1];
        rbb = new long[n + 2][m + 2];
        rtt = new long[n + 1][m + 2];
        rtb = new long[n + 2][m + 1];
        rbt = new long[n + 1][m + 1];
        rbb = new long[n + 2][m + 2];
        for (int i = 0; i < n; i++) {
            for (int j = 0; j < m; j++) {
                rtt[i + 1][j + 1] = rtt[i][j + 2] + mat[i][j];
                rbt[i + 1][j + 1] = rbt[i][j] + mat[i][j];
            }
        }
        for (int j = 0; j < m; j++) {
            for (int i = n - 1; i >= 0; i--) {
                rtb[i + 1][j + 1] = rtb[i + 2][j] + mat[i][j];
            }
        }
        for (int j = m - 1; j >= 0; j--) {
            for (int i = n - 1; i >= 0; i--) {
                rbb[i + 1][j + 1] = rbb[i + 2][j + 2] + mat[i][j];
            }
        }
        for (int i = 0; i < n; i++) {
            for (int j = 0; j < m; j++) {
                rtt[i + 1][j + 1] += rtt[i + 1][j];
                rtb[i + 1][j + 1] += rtb[i][j + 1];
                rbt[i + 1][j + 1] += rbt[i + 1][j];
                rbb[i + 1][j + 1] += rbb[i][j + 1];
            }
        }
    }

    public long queryManhattanArea(int x, int y, int d) {
        int l = Math.max(0, y - d), r = Math.min(m - 1, y + d), t = Math.max(0, x - d), b = Math.min(n - 1, x + d);
        long s = queryMat(t, l, b, r);
        if (t < x + y - l - d) {
            s -= queryTriLT(t, l, (x + y - l - d) - t - 1);
        }
        if (t < x + r - y - d) {
            s -= queryTriRT(t, r, (x + r - y - d) - t - 1);
        }
        if (b > d - (r - y) + x) {
            s -= queryTriRB(b, r, b - (d - (r - y) + x) - 1);
        }
        if (b > d - (y - l) + x) {
            s -= queryTriLB(b, l, b - (d - (y - l) + x) - 1);
        }
        return s;
    }

    public long queryTriLB(int x, int y, int l) {
        if (l == 0) {
            return mat[x][y];
        }
        return queryMat(x - l, y, x, y + l) - queryTriRT(x - l, y + l, l - 1);
    }

    public long queryTriRT(int x, int y, int l) {
        if (l == 0) {
            return mat[x][y];
        }
        long s = lbs[y - x + n] - lbs[y - x - l + (n - 1)];
        long sl = rbt[x][y] - rbt[x][Math.max(0, y - l - 1)];
        long sr = rbb[Math.min(x + l + 2, n)][y + 2] - rbb[x + 1][y + 2];
        return s - sl - sr;
    }

    public long queryTriRB(int x, int y, int l) {
        if (l == 0) {
            return mat[x][y];
        }
        return queryMat(x - l, y - l, x, y) - queryTriLT(x - l, y - l, l - 1);
    }

    public long queryTriLT(int x, int y, int l) {
        if (l == 0) {
            return mat[x][y];
        }
        long s = lts[x + y + l + 1] - lts[x + y];
        long sl = rtb[Math.min(n, x + l + 2)][y] - rtb[x + 1][y];
        long sr = rtt[x][Math.min(m, y + l + 2)] - rtt[x][y + 1];
        return s - sl - sr;
    }

    public long queryMat(int x1, int y1, int x2, int y2) {
        return sum[x2 + 1][y2 + 1] - sum[x2 + 1][y1] - sum[x1][y2 + 1] + sum[x1][y1];
    }
}
