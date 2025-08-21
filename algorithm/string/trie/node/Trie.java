public class Trie {

    class Node {
        int end;
        int cnt;
        Node[] nxt = new Node[26];
    }

    Node root;

    void insert(String word) {
        Node cur = root;
        for (char c : word.toCharArray()) {
            int j = c - 'a';
            if (cur.nxt[j] == null) {
                cur.nxt[j] = new Node();
            }
            cur = cur.nxt[j];
            cur.cnt++;
        }
        cur.end++;
    }

    // 字符串在前缀树中
    void delete(String word) {
        Node cur = root;
        for (char c : word.toCharArray()) {
            int j = c - 'a';
            if (--cur.nxt[j].cnt == 0) {
                cur.nxt[j] = null;
                return;
            }
            cur = cur.nxt[j];
        }
        cur.end--;
    }

    boolean search(String word) {
        Node cur = root;
        for (char c : word.toCharArray()) {
            cur = cur.nxt[c - 'a'];
            if (cur == null) {
                return false;
            }
        }
        return cur.end > 0;
    }

    int prefixNumber(String pre) {
        Node cur = root;
        for (char c : pre.toCharArray()) {
            cur = cur.nxt[c - 'a'];
            if (cur == null) {
                return 0;
            }
        }
        return cur.cnt;
    }
}