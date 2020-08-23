/******************************************************************************
 *  The following features are not supported:
 *    - The + operator
 *    - Multiway or
 *    - Metacharacters in the text
 *    - Character classes.
 *
 ******************************************************************************/


import java.util.ArrayList;
import java.util.List;
import java.util.Stack;

/**
 * The NFA class provides a data type for:
 * creating a nondeterministic finite state automaton (NFA) from a regular expression
 * and testing whether a given string is matched by that regular expression.
 * It supports the following operations:
 * concatenation,
 * closure,
 * binary or, and parentheses.
 * <p>
 * This implementation builds the NFA using a digraph and a stack and simulates the NFA using digraph search .
 * The constructor takes time proportional to m, where m is the number of characters in the regular expression.
 * The recognizes method takes time proportional to m n, where n is the number of characters in the text.
 */
public class NFA {

    private final Digraph graph;     // digraph of epsilon transitions
    private final String regexp;     // regular expression
    private final int regexpNumberOfChars;       // number of characters in regular expression


    public NFA(String regexp) {
        this.regexp = regexp;
        regexpNumberOfChars = regexp.length();
        Stack<Integer> operations = new Stack<>();
        graph = new Digraph(regexpNumberOfChars + 1);
        for (int i = 0; i < regexpNumberOfChars; i++) {
            int lp = i;
            if (regexp.charAt(i) == '(' || regexp.charAt(i) == '|')
                operations.push(i);
            else if (regexp.charAt(i) == ')') {
                int or = operations.pop();

                // 2-way or operator
                if (regexp.charAt(or) == '|') {
                    lp = operations.pop();
                    graph.addEdge(lp, or + 1);
                    graph.addEdge(or, i);
                } else if (regexp.charAt(or) == '(')
                    lp = or;
                else assert false;
            }

            // closure operator (uses 1-character lookahead)
            if (i < regexpNumberOfChars - 1 && regexp.charAt(i + 1) == '*') {
                graph.addEdge(lp, i + 1);
                graph.addEdge(i + 1, lp);
            }
            if (regexp.charAt(i) == '(' || regexp.charAt(i) == '*' || regexp.charAt(i) == ')')
                graph.addEdge(i, i + 1);
        }
        if (operations.size() != 0)
            throw new IllegalArgumentException("Invalid regular expression");
    }


    public boolean matches(String txt) {
        DirectedDFS dfs = new DirectedDFS(graph, 0);//all the states you can reach from state 0
        List<Integer> pc = new ArrayList<>();//set of all possible states that an NFA could be in (program counter)
        for (int v = 0; v < graph.getNumberofVertices(); v++)
            if (dfs.isVisited(v)) pc.add(v);

        // Compute possible NFA states for txt[i+1]
        for (int i = 0; i < txt.length(); i++) {
            if (txt.charAt(i) == '*' || txt.charAt(i) == '|' || txt.charAt(i) == '(' || txt.charAt(i) == ')')
                throw new IllegalArgumentException("text contains the metacharacter '" + txt.charAt(i) + "'");

            List<Integer> match = new ArrayList<>();
            //o now, we build another DFS which gives us marks all the states that you could reach by starting with any of the states in match
            for (int v : pc) {
                if (v == regexpNumberOfChars) continue;//did we reach the accept state?
                if ((regexp.charAt(v) == txt.charAt(i)) || regexp.charAt(v) == '.')//'.' is the dont care here
                    match.add(v + 1);//if we have a match add the next state
            }
            dfs = new DirectedDFS(graph, match);
            pc = new ArrayList<>();
            for (int v = 0; v < graph.getNumberofVertices(); v++)
                if (dfs.isVisited(v)) pc.add(v);

            // optimization if no states reachable
            if (pc.size() == 0) return false;
        }

        // check for accept state
        for (int v : pc)
            if (v == regexpNumberOfChars) return true;
        return false;
    }

    private Digraph buildEpsilonTransitionDigraph(char[] re) {

        Digraph G = new Digraph(regexpNumberOfChars + 1);
        Stack<Integer> ops = new Stack<Integer>();
        for (int i = 0; i < regexpNumberOfChars; i++) {
            int lp = i;
            if (re[i] == '(' || re[i] == '|') ops.push(i);
            else if (re[i] == ')') {
                int or = ops.pop();
                if (re[or] == '|') {
                    lp = ops.pop();
                    G.addEdge(lp, or + 1);
                    G.addEdge(or, i);
                } else lp = or;
            }
            if (i < regexpNumberOfChars - 1 && re[i + 1] == '*') {
                G.addEdge(lp, i + 1);
                G.addEdge(i + 1, lp);
            }
            if (re[i] == '(' || re[i] == '*' || re[i] == ')')
                G.addEdge(i, i + 1);
        }
        return G;
    }

    public static void main(String[] args) {
        /*
        "(A*B|AC)D" AAAABD
        "(A*B|AC)D" AAAAC
        "(a|(bc)*d)*" abcbcd
        "(a|(bc)*d)*" abcbcbcdaaaabcbcdaaaddd
        */
        String regexp = "(" + args[0] + ")";
        String txt = args[1];
        NFA nfa = new NFA(regexp);
        System.out.println(nfa.matches(txt));
    }
}

