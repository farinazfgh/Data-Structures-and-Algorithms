import java.util.Scanner;

class EditDistance {
    public static int editDistance1(String string1, String string2) {
        int[][] numberTable = new int[string1.length() + 1][string2.length() + 1];
        for (int j = 0; j < string1.length(); j++) numberTable[0][j] = j;
        for (int i = 0; i < string2.length(); i++) numberTable[i][0] = i;

        char[] s1 = string1.toCharArray();
        char[] s2 = string2.toCharArray();
        int i;
        int j = 1;
        for (i = 1; i < string1.length() + 1; i++)
            for (j = 1; j < string2.length() + 1; j++) {
                if (s1[i - 1] == s2[j - 1]) {
                    numberTable[i][j] = numberTable[i - 1][j - 1];
                } else {
                    numberTable[i][j] = Math.min(Math.min(numberTable[i - 1][j], numberTable[i][j - 1]), numberTable[i - 1][j - 1]) + 1;
                }
            }

        System.out.println(numberTable[i - 1][j - 1]);
        return numberTable[i - 1][j - 1];
    }

    public static int editDistance(String a, String b) {
        a = a.toLowerCase();
        b = b.toLowerCase();
        // i == 0
        int[] costs = new int[b.length() + 1];
        for (int j = 0; j < costs.length; j++)
            costs[j] = j;
        for (int i = 1; i <= a.length(); i++) {
            // j == 0; nw = lev(i - 1, j)
            costs[0] = i;
            int nw = i - 1;
            for (int j = 1; j <= b.length(); j++) {
                int cj = Math.min(1 + Math.min(costs[j], costs[j - 1]), a.charAt(i - 1) == b.charAt(j - 1) ? nw : nw + 1);
                nw = costs[j];
                costs[j] = cj;
            }
        }
        return costs[b.length()];
    }

    public static void main(String[] args) {
        Scanner scan = new Scanner(System.in);

        String s = scan.next();
        String t = scan.next();

        System.out.println(editDistance(s, t));
    }

}
