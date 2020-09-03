import java.util.HashMap;
import java.util.Map;

class Trie_ {
    static class TrieNode {
        private final Map<Character, TrieNode> children = new HashMap<>();
        private boolean endOfWord;

        Map<Character, TrieNode> getChildren() {
            return children;
        }

        boolean isEndOfWord() {
            return endOfWord;
        }

        void setEndOfWord(boolean endOfWord) {
            this.endOfWord = endOfWord;
        }

        public String toString() {
            return "{" + "->" + children + '}';
        }
    }

    private TrieNode root;

    Trie_() {

        root = new TrieNode();
    }

    void insert(String word) {
        TrieNode current = root;

        for (char l : word.toCharArray()) {
            current = current.getChildren().computeIfAbsent(l, c -> new TrieNode());
        }
        current.setEndOfWord(true);
    }

    boolean delete(String word) {
        return delete(root, word, 0);
    }

    boolean containsNode(String word) {
        TrieNode current = root;

        for (int i = 0; i < word.length(); i++) {
            char ch = word.charAt(i);
            TrieNode node = current.getChildren().get(ch);
            if (node == null) {
                return false;
            }
            current = node;
        }
        return current.isEndOfWord();
    }

    boolean isEmpty() {
        return root == null;
    }

    private boolean delete(TrieNode current, String word, int index) {
        if (index == word.length()) {
            if (!current.isEndOfWord()) {
                return false;
            }
            current.setEndOfWord(false);
            return current.getChildren().isEmpty();
        }
        char ch = word.charAt(index);
        TrieNode node = current.getChildren().get(ch);
        if (node == null) {
            return false;
        }
        boolean shouldDeleteCurrentNode = delete(node, word, index + 1) && !node.isEndOfWord();

        if (shouldDeleteCurrentNode) {
            current.getChildren().remove(ch);
            return current.getChildren().isEmpty();
        }
        return false;
    }

    public static void whenEmptyTrie_thenNoElements() {
        Trie_ trie = new Trie_();

        System.out.println(trie.isEmpty());
    }




    public static void givenATrie_whenAddingElements_thenTrieHasThoseElements() {
        Trie_ trie = createExampleTrie();

        System.out.println(trie.containsNode("3"));
        System.out.println(trie.containsNode("vida"));

        System.out.println(trie.containsNode("Programming"));
        System.out.println(trie.containsNode("is"));
        System.out.println(trie.containsNode("a"));
        System.out.println(trie.containsNode("way"));
        System.out.println(trie.containsNode("of"));
        System.out.println(trie.containsNode("life"));
    }


    public static void givenATrie_whenLookingForNonExistingElement_thenReturnsFalse() {
        Trie_ trie = createExampleTrie();

        System.out.println(trie.containsNode("99"));
    }


    public static void givenATrie_whenDeletingElements_thenTreeDoesNotContainThoseElements() {

        Trie_ trie = createExampleTrie();

        System.out.println(trie.containsNode("Programming"));
        trie.delete("Programming");
        System.out.println(trie.containsNode("Programming"));
    }


    public static void givenATrie_whenDeletingOverlappingElements_thenDontDeleteSubElement() {

        Trie_ trie1 = new Trie_();

        trie1.insert("pie");
        trie1.insert("pies");

        trie1.delete("pies");

        System.out.println(trie1.containsNode("pie"));
    }

    private static Trie_ createExampleTrie() {
        Trie_ trie = new Trie_();

        trie.insert("Programming");
        trie.insert("is");
        trie.insert("a");
        trie.insert("way");
        trie.insert("of");
        trie.insert("life");
        trie.insert("lifE");

        return trie;
    }

    public static void main(String[] args) {
       /* Trie_ trie = createExampleTrie();
        System.out.println(trie);
        whenEmptyTrie_thenNoElements();
        givenATrie_whenAddingElements_thenTrieHasThoseElements();
        givenATrie_whenLookingForNonExistingElement_thenReturnsFalse();
        givenATrie_whenDeletingElements_thenTreeDoesNotContainThoseElements();*/
       givenATrie_whenDeletingOverlappingElements_thenDontDeleteSubElement();
    }

    @Override
    public String toString() {
        return "Trie{ " + root + '}';
    }
}
