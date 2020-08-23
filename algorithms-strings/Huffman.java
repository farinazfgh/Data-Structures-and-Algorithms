/******************************************************************************
 *  Execution:    java Huffman - < tinytinyTale.txt   (compress)
 *  Execution:    java Huffman + < tinytinyTale.txt   (expand)
 *
 *  Compress or expand a binary input stream using the Huffman algorithm.
 *
 *  % java Huffman - < abra.txt | java BinaryDump 60
 *  010100000100101000100010010000110100001101010100101010000100
 *  000000000000000000000000000110001111100101101000111110010100
 *  120 bits
 *
 *  % java Huffman - < abra.txt | java Huffman +
 *  ABRACADABRA!
 *
 ******************************************************************************/


import java.util.PriorityQueue;

/**
 * The Huffman class provides static methods for compressing and expanding a binary input using Huffman codes over the 8-bit extended ASCII alphabet.
 */
public class Huffman {

    // Radix extended ASCII
    private static final int R = 256;

    // Do not instantiate.
    private Huffman() {
    }

    // Huffman trie node
    private static class TrieNode implements Comparable<TrieNode> {
        private final char ch;
        private final int freq;
        private final TrieNode left;
        private final TrieNode right;

        TrieNode(char ch, int freq, TrieNode left, TrieNode right) {
            this.ch = ch;
            this.freq = freq;
            this.left = left;
            this.right = right;
        }

        private boolean isLeaf() {
            assert ((left == null) && (right == null)) || ((left != null) && (right != null));
            return (left == null) && (right == null);
        }

        public int compareTo(TrieNode that) {

            return this.freq - that.freq;
        }
    }

    /**
     * Reads a sequence of 8-bit bytes from standard input; compresses them
     * using Huffman codes with an 8-bit alphabet; and writes the results
     * to standard output.
     */
    public static void compress() {
        String s = BinaryStdIn.readString();
        char[] input = s.toCharArray();

        // tabulate frequency counts
        int[] freq = new int[R];
        for (int i = 0; i < input.length; i++)
            freq[input[i]]++;

        // build Huffman trie
        TrieNode root = buildHuffmanTrie(freq);

        // build code table
        String[] st = new String[R];
        buildCode(st, root, "");

        // print trie for decoder
        writeTrie(root);

        // print number of bytes in original uncompressed message
        BinaryStdOut.write(input.length);

        for (char c : input) {
            String code = st[c];
            for (int j = 0; j < code.length(); j++) {
                if (code.charAt(j) == '0') {
                    BinaryStdOut.write(false);
                } else if (code.charAt(j) == '1') {
                    BinaryStdOut.write(true);
                } else throw new IllegalStateException("Illegal state");
            }
        }

        BinaryStdOut.close();
    }

    private static TrieNode buildHuffmanTrie(int[] freq) {

        // initialze priority queue with singleton trees
        PriorityQueue<TrieNode> pq = new PriorityQueue<TrieNode>();
        for (char c = 0; c < R; c++)
            if (freq[c] > 0)
                pq.offer(new TrieNode(c, freq[c], null, null));

        // merge two smallest trees
        while (pq.size() > 1) {
            TrieNode left = pq.poll();
            TrieNode right = pq.poll();
            TrieNode parent = new TrieNode('\0', left.freq + right.freq, left, right);
            pq.offer(parent);
        }
        return pq.poll();
    }


    // write bitstring-encoded trie to standard output. Write preorder traversal of trie; mark leaf and internal nodes with a bit.
    private static void writeTrie(TrieNode x) {
        if (x.isLeaf()) {
            BinaryStdOut.write(true);
            BinaryStdOut.write(x.ch, 8);
            return;
        }
        BinaryStdOut.write(false);//not at leaf simply write 0
        writeTrie(x.left);
        writeTrie(x.right);
    }

    // make a lookup table from symbols and their encodings
    private static void buildCode(String[] st, TrieNode x, String s) {
        if (!x.isLeaf()) {
            buildCode(st, x.left, s + '0');
            buildCode(st, x.right, s + '1');
        } else {
            st[x.ch] = s;
        }
    }

    /**
     * Reads a sequence of bits that represents a Huffman-compressed message from
     * standard input; expands them; and writes the results to standard output.
     */
    public static void expand() {

        // read in Huffman trie from input stream
        TrieNode root = readTrie();

        // number of bytes to write
        int length = BinaryStdIn.readInt();

        // decode using the Huffman trie
        for (int i = 0; i < length; i++) {
            TrieNode x = root;
            while (!x.isLeaf()) {//expand codeword for ith char
                boolean bit = BinaryStdIn.readBoolean();
                if (bit) x = x.right;
                else x = x.left;
            }
            BinaryStdOut.write(x.ch, 8);
        }
        BinaryStdOut.close();
    }


    private static TrieNode readTrie() {
        boolean isLeaf = BinaryStdIn.readBoolean();
        if (isLeaf) {
            return new TrieNode(BinaryStdIn.readChar(), -1, null, null);
        } else {//'\0 not used for internal nodes
            return new TrieNode('\0', -1, readTrie(), readTrie());//read left and right recursively
        }
    }


    public static void main(String[] args) {
        if (args[0].equals("-")) compress();
        else if (args[0].equals("+")) expand();
        else throw new IllegalArgumentException("Illegal command line argument");
    }
}


