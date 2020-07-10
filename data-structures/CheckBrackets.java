import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.Stack;

class Bracket {
    Bracket(char type, int position) {
        this.type = type;
        this.position = position;
    }

    boolean Match(char c) {
/*
        if (this.type == '[' && c == ']')
            return true;
        if (this.type == '{' && c == '}')
            return true;
        if (this.type == '(' && c == ')')
            return true;

 */
        int sub = Math.abs(this.type - c);
        return (sub == 1 || sub == 2);
    }

    char type;
    int position;
}

class CheckBrackets {
    public static void main(String[] args) throws IOException {
        InputStreamReader input_stream = new InputStreamReader(System.in);
        BufferedReader reader = new BufferedReader(input_stream);
        String text = reader.readLine();

        Stack<Bracket> openingBracketsStack = new Stack<>();
        for (int position = 0; position < text.length(); ++position) {
            char next = text.charAt(position);

            if (next == '(' || next == '[' || next == '{') {
                openingBracketsStack.push(new Bracket(next, position));
                continue;
            }

            if (next == ')' || next == ']' || next == '}') {
                if (openingBracketsStack.empty()) {
                    System.out.println(position + 1);
                    return;
                }
                Bracket openBracket = openingBracketsStack.pop();
                if (!openBracket.Match(next)) {
                    System.out.println(position + 1);
                    return;
                }
            }
        }
        if (!openingBracketsStack.empty()) {
            System.out.println(openingBracketsStack.pop().position + 1);
            return;
        }
        System.out.println("Success");
    }
}
