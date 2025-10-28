public class Manacher {
    
    // 若s[i..j]为回文串，则  (j - i + 1) < pArr[i + j + 1]
    int[] pArr(char[] s) {
        char[] str = new char[s.length * 2 + 1];
        str[0] = '#';
        int z = 1;
        for (char b : s) {
            str[z++] = b;
            str[z++] = '#';
        }
        int n = str.length;
        int[] pArr = new int[n];
        for (int i = 0, r = 0, c = 0; i < n; i++) {
            pArr[i] = i >= r ? 1 : Math.min(r - i, pArr[c * 2 - i]);
            while (i - pArr[i] >= 0 && i + pArr[i] < n && str[i - pArr[i]] == str[i + pArr[i]]) {
                pArr[i]++;
            }
            if (i + pArr[i] > r) {
                c = i;
                r = i + pArr[i];
            }
        }
        return pArr;
    }

}
