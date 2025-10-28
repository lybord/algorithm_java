public class KMP {
    
    public int[] kmp(String s, String t) {
        char[] str = s.toCharArray(), tar = t.toCharArray();
        int n = str.length, m = tar.length, x = 0, y = 0;
        int[] next = next(tar);
        int[] ans = new int[n];
        int z = 0;
        while (x < n) {
            if (str[x] == tar[y]) {
                x++;
                y++;
                if (y == m) {
                    ans[z++] = x - m;
                    y = next[y];
                }
            } else if (y == 0) {
                x++;
            } else {
                y = next[y];
            }
        }
        return java.util.Arrays.copyOf(ans, z);
    }

    public int[] next(char[] str) {
        int n = str.length;
        int[] next = new int[n + 1];
        next[0] = -1;
        for (int i = 2, j = 0; i <= n; i++) {
            while (j >= 0 && str[i - 1] != str[j]) {
                j = next[j];
            }
            next[i] = ++j;
        }
        return next;
    }
}
