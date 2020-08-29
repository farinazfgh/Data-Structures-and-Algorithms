import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.*;

public class Trie {
    static class FastScanner {
        StringTokenizer tok = new StringTokenizer("");
        BufferedReader in;

        FastScanner() {
            in = new BufferedReader(new InputStreamReader(System.in));
        }

        String next() throws IOException {
            while (!tok.hasMoreElements())
                tok = new StringTokenizer(in.readLine());
            return tok.nextToken();
        }

        int nextInt() throws IOException {
            return Integer.parseInt(next());
        }
    }
    /*
TrieConstruction(Patterns)
Trie ← a graph consisting of a single node root
for each string Pattern in Patterns:
currentNode ← root
for 𝑖 from 0 to |Pattern| − 1:
currentSymbol ← Pattern[𝑖]
if there is an outgoing edge from currentNode with label currentSymbol:
currentNode ← ending node of this edge
else:
add a new node newNode to Trie
add a new edge from currentNode to newNode with label currentSymbol
currentNode ← newNode
return Trie
     */

    List<Map<Character, Integer>> buildTrie(String[] patterns) {
        List<Map<Character, Integer>> trie = new ArrayList<>();
        Map<Character, Integer> root = new HashMap<>();
        trie.add(root);
        for (String pattern : patterns) {
            Map<Character, Integer> currentNode = root;
            for (int i = 0; i < pattern.length(); i++) {
                Character currentSymbol = pattern.charAt(i);
                Set<Character> neighbours = currentNode.keySet();
                if (neighbours.contains(currentSymbol)) {
                    currentNode = trie.get(currentNode.get(currentSymbol));
                } else {
                    Map<Character, Integer> newNode = new HashMap<>();
                    trie.add(newNode);
                    currentNode.put(currentSymbol, trie.size() - 1);
                    currentNode = newNode;
                }

            }
        }
        return trie;
    }

    static public void main(String[] args) throws IOException {

        new Trie().run();
    }

    public void print(List<Map<Character, Integer>> trie) {
        for (int i = 0; i < trie.size(); ++i) {
            Map<Character, Integer> node = trie.get(i);
            for (Map.Entry<Character, Integer> entry : node.entrySet()) {
                System.out.println(i + "->" + entry.getValue() + ":" + entry.getKey());
            }
        }
    }

    public void run() throws IOException {
        FastScanner scanner = new FastScanner();
        int patternsCount = scanner.nextInt();
        String[] patterns = new String[patternsCount];
        for (int i = 0; i < patternsCount; ++i) {
            patterns[i] = scanner.next();
        }
        List<Map<Character, Integer>> trie = buildTrie(patterns);
        print(trie);
    }
}
