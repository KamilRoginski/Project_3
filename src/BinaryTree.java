// Name: Kamil Roginski
// Project: CMSC 315 Project 3
// Date: 22 APR 2025
// Description: Immutable binary tree with parsing, BST/balance checks, and indented output.


import java.util.*;

public final class BinaryTree {
    private final Node root;

    // Node is a private nested class
    private static class Node {
        final int value;
        final Node left, right;
        Node(int val, Node l, Node r) {
            this.value = val;
            this.left = l;
            this.right = r;
        }
    }

    /** Parses a tree from its preorder string, e.g. "(53 (28 (11 * *) (41 * *)) (83 (67 * *) *))" */
    public BinaryTree(String repr) throws InvalidSyntaxException {
        Parser p = new Parser(repr);
        this.root = p.parseTree();
        p.skipWhitespace();
        if (!p.atEnd()) {
            throw new InvalidSyntaxException("Extra characters at the end");
        }
    }

    /** Builds a balanced BST out of the given list of values (unsorted OK). */
    public BinaryTree(List<Integer> values) {
        List<Integer> sorted = new ArrayList<>(values);
        Collections.sort(sorted);
        this.root = buildBalanced(sorted, 0, sorted.size() - 1);
    }

    /** Returns an indented string representation (3 spaces per level). */
    public String toIndentedString() {
        StringBuilder sb = new StringBuilder();
        buildIndent(root, 0, sb);
        return sb.toString();
    }

    /** True if this tree is a binary search tree. */
    public boolean isBST() {
        return checkBST(root, Long.MIN_VALUE, Long.MAX_VALUE);
    }

    /** True if this tree is height‐balanced (every node’s subtrees differ by ≤1). */
    public boolean isBalanced() {
        return checkBalanced(root).balanced;
    }

    /** Returns the height (max depth) of this tree, where a single node has height 0. */
    public int getHeight() {
        return height(root);
    }

    /** Returns the tree’s values in sorted order (in‐order). */
    public List<Integer> getValues() {
        List<Integer> out = new ArrayList<>();
        inorder(root, out);
        return out;
    }

    // ───── Private helpers ─────────────────────────────────────────────────────

    private static Node buildBalanced(List<Integer> a, int lo, int hi) {
        if (lo > hi) return null;
        int mid = (lo + hi) / 2;
        Node left = buildBalanced(a, lo, mid - 1);
        Node right = buildBalanced(a, mid + 1, hi);
        return new Node(a.get(mid), left, right);
    }

    private void buildIndent(Node n, int depth, StringBuilder sb) {
        if (n == null) return;
        sb.append("   ".repeat(depth)).append(n.value).append("\n");
        buildIndent(n.left,  depth + 1, sb);
        buildIndent(n.right, depth + 1, sb);
    }

    private boolean checkBST(Node n, long min, long max) {
        if (n == null) return true;
        if (n.value <= min || n.value >= max) return false;
        return checkBST(n.left,  min, n.value)
                && checkBST(n.right, n.value, max);
    }

    private static class BalInfo {
        final boolean balanced;
        final int height;
        BalInfo(boolean b, int h) { balanced = b; height = h; }
    }

    private BalInfo checkBalanced(Node n) {
        if (n == null) return new BalInfo(true, -1);
        BalInfo L = checkBalanced(n.left);
        BalInfo R = checkBalanced(n.right);
        boolean b = L.balanced && R.balanced && Math.abs(L.height - R.height) <= 1;
        int h = Math.max(L.height, R.height) + 1;
        return new BalInfo(b, h);
    }

    private int height(Node n) {
        return n == null ? -1 : Math.max(height(n.left), height(n.right)) + 1;
    }

    private void inorder(Node n, List<Integer> out) {
        if (n == null) return;
        inorder(n.left,  out);
        out.add(n.value);
        inorder(n.right, out);
    }

    // ───── Parser for the preorder string ────────────────────────────────────────
    private static class Parser {
        private final String s;
        private int pos = 0;
        Parser(String str) { s = str; }

        Node parseTree() throws InvalidSyntaxException {
            skipWhitespace();
            if (atEnd()) {
                throw new InvalidSyntaxException("Incomplete tree");
            }
            if (s.charAt(pos) == '*') {
                pos++;
                return null;
            }
            if (s.charAt(pos) != '(') {
                throw new InvalidSyntaxException("Missing left parenthesis");
            }
            pos++; // consume '('
            skipWhitespace();

            // parse integer
            int start = pos;
            boolean neg = false;
            if (pos < s.length() && s.charAt(pos) == '-') {
                neg = true; pos++;
            }
            if (pos >= s.length() || !Character.isDigit(s.charAt(pos))) {
                throw new InvalidSyntaxException("Data is not an integer");
            }
            while (pos < s.length() && Character.isDigit(s.charAt(pos))) pos++;
            int val;
            try {
                val = Integer.parseInt(s.substring(start, pos));
            } catch (NumberFormatException e) {
                throw new InvalidSyntaxException("Data is not an integer");
            }

            // parse subtrees
            Node left  = parseTree();
            Node right = parseTree();
            skipWhitespace();
            if (pos >= s.length() || s.charAt(pos) != ')') {
                throw new InvalidSyntaxException("Missing right parenthesis");
            }
            pos++;
            return new Node(val, left, right);
        }

        void skipWhitespace() {
            while (pos < s.length() && Character.isWhitespace(s.charAt(pos))) pos++;
        }

        boolean atEnd() {
            skipWhitespace();
            return pos >= s.length();
        }
    }
}


