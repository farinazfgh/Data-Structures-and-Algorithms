package trie;


public class TrieUnitTest {

    public void whenEmptyTrie_thenNoElements() {
        Trie trie = new Trie();

        System.out.println(trie.isEmpty());
    }

    public void givenATrie_whenAddingElements_thenTrieNotEmpty() {
        Trie trie = createExampleTrie();

        System.out.println(trie.isEmpty());
    }

    public void givenATrie_whenAddingElements_thenTrieHasThoseElements() {
        Trie trie = createExampleTrie();

        System.out.println(trie.containsNode("3"));
        System.out.println(trie.containsNode("vida"));

        System.out.println(trie.containsNode("Programming"));
        System.out.println(trie.containsNode("is"));
        System.out.println(trie.containsNode("a"));
        System.out.println(trie.containsNode("way"));
        System.out.println(trie.containsNode("of"));
        System.out.println(trie.containsNode("life"));
    }


    public void givenATrie_whenLookingForNonExistingElement_thenReturnsFalse() {
        Trie trie = createExampleTrie();

        System.out.println(trie.containsNode("99"));
    }


    public void givenATrie_whenDeletingElements_thenTreeDoesNotContainThoseElements() {

        Trie trie = createExampleTrie();

        System.out.println(trie.containsNode("Programming"));
        trie.delete("Programming");
        System.out.println(trie.containsNode("Programming"));
    }


    public void givenATrie_whenDeletingOverlappingElements_thenDontDeleteSubElement() {

        Trie trie1 = new Trie();

        trie1.insert("pie");
        trie1.insert("pies");

        trie1.delete("pies");

        System.out.println(trie1.containsNode("pie"));
    }

    private static Trie createExampleTrie() {
        Trie trie = new Trie();

        trie.insert("Programming");
        trie.insert("is");
        trie.insert("a");
        trie.insert("way");
        trie.insert("of");
        trie.insert("life");

        return trie;
    }

    public static void main(String[] args) {
        Trie trie = createExampleTrie();
    }
}
