class Trie {
    private static final int LOW = 'a', HIGH = 'z', MAXN = 1000000, MAXT = MAXN * 2;
    private static final int[] nxt = new int[MAXT * (HIGH - LOW + 1) + 1];
    private static final int[] cnt = new int[MAXT + 1], end = new int[MAXT + 1];

    private static int no;

    private static int idx(int u, int i) {
        return u + i * MAXT;
    }

    public static void clear() {
        for (int i = 1; i <= no; i++) {
            cnt[i] = end[i] = 0;
            for (int j = HIGH - LOW; j >= 0; j--) {
                nxt[idx(i, j)] = 0;
            }
        }
        no = 0;
    }

    private final int root;

    public Trie() {
        root = ++no;
    }

    public void insert(String s) {
        insert(s.toCharArray());
    }

    public void insert(char[] str) {
        int u = root, i;
        for (char c : str) {
            i = idx(u, c - LOW);
            if (nxt[i] == 0) {
                nxt[i] = ++no;
            }
            u = nxt[i];
            cnt[u]++;
        }
        end[u]++;
    }

    public void delete(String s) {
        delete(s.toCharArray());
    }

    // char[] str 代表的值一定在 trie 中
    public void delete(char[] str) {
        int u = root, i;
        for (char c : str) {
            i = idx(u, c - LOW);
            u = nxt[i];
            if (--cnt[u] == 0) {
                nxt[i] = 0;
            }
        }
        end[u]--;
    }

    public boolean contains(String s) {
        return stringNumber(s) > 0;
    }

    public boolean contains(char[] str) {
        return stringNumber(str) > 0;
    }
    
    public int stringNumber(String s) {
        return stringNumber(s.toCharArray());
    }
    
    public int stringNumber(char[] str) {
        int u = root;
        for (char c : str) {
            u = nxt[idx(u, c - LOW)];
            if (u == 0) {
                return 0;
            }
        }
        return end[u];
    }

    public boolean isPrefix(String s) {
        return isPrefix(s.toCharArray());
    }

    public boolean isPrefix(char[] str) {
        int u = root;
        for (char c : str) {
            u = nxt[idx(u, c - LOW)];
            if (u == 0) {
                return false;
            }
        }
        return true;
    }

    public int prefixNumber(String s) {
        return prefixNumber(s.toCharArray());
    }

    public int prefixNumber(char[] str) {
        int u = root;
        for (char c : str) {
            u = nxt[idx(u, c - LOW)];
            if (u == 0) {
                return 0;
            }
        }
        return cnt[u];
    }
}