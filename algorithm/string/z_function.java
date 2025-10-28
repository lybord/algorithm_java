public class z_function {
    
    int[] z_func(char[] str) {
        int n = str.length;
        int[] z = new int[n];
//        z[0] = n;
        for (int i = 1, l = 0, r = 0; i < n; i++) {
            z[i] = Math.max(Math.min(z[i - l], r - i + 1), 0);
            while (i + z[i] < n && str[z[i]] == str[i + z[i]]) {
                l = i;
                r = i + z[i];
                z[i]++;
            }
        }
        return z;
    }
}
