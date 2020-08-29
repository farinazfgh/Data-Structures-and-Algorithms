import java.util.Scanner;

public class Change {
    /*
Task. The goal in this problem is to find the minimum number of coins needed to change the input value
(an integer) into coins with denominations 1, 5, and 10.
Input Format. The input consists of a single integer 𝑚.
Constraints. 1 ≤ 𝑚 ≤ 103.
Output Format. Output the minimum number of coins with denominations 1, 5, 10 that changes 𝑚.
     */
    private static int getChange(int m) {
        int noCoins = 0;
        //write your code here
        noCoins += getDivision(m, 10);
        noCoins += getDivision((m % 10), 5);;
        noCoins += ((m % 10) % 5);

        return noCoins;
    }

    static int getDivision(int m, int n) {
        return (m / n);
    }

    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);
        int m = scanner.nextInt();
        System.out.println(getChange(m));

    }
}