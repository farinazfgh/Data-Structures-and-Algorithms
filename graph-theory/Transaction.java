

import util.StdOut;

import java.util.Arrays;
import java.util.Comparator;
import java.util.Date;

public class Transaction implements Comparable<Transaction> {
    private final String customer;
    private final Date date;
    private final double amount;


    public Transaction(String customer, Date date, double amount) {
        if (Double.isNaN(amount) || Double.isInfinite(amount))
            throw new IllegalArgumentException("Amount cannot be NaN or infinite");
        this.customer = customer;
        this.date = date;
        this.amount = amount;
    }


    public Transaction(String transaction) {
        String[] a = transaction.split("\\s+");
        customer = a[0];
        date = new Date(a[1]);
        amount = Double.parseDouble(a[2]);
        if (Double.isNaN(amount) || Double.isInfinite(amount))
            throw new IllegalArgumentException("Amount cannot be NaN or infinite");
    }


    public String getCustomer() {
        return customer;
    }


    public Date getDate() {
        return date;
    }


    public double getAmount() {
        return amount;
    }

    @Override
    public String toString() {
        return String.format("%-10s %10s %8.2f", customer, date, amount);
    }


    public int compareTo(Transaction that) {
        return Double.compare(this.amount, that.amount);
    }


    @Override
    public boolean equals(Object other) {
        if (other == this) return true;
        if (other == null) return false;
        if (other.getClass() != this.getClass()) return false;
        Transaction that = (Transaction) other;
        return (this.amount == that.amount) && (this.customer.equals(that.customer))
                && (this.date.equals(that.date));
    }



    public int hashCode() {
        int hash = 1;
        hash = 31 * hash + customer.hashCode();
        hash = 31 * hash + date.hashCode();
        hash = 31 * hash + ((Double) amount).hashCode();
        return hash;
        // return Objects.hash(customer, data, amount);
    }


    public static class compareByCustomerName implements Comparator<Transaction> {

        @Override
        public int compare(Transaction v, Transaction w) {
            return v.customer.compareTo(w.customer);
        }
    }

    public static class compareByDate implements Comparator<Transaction> {

        @Override
        public int compare(Transaction v, Transaction w) {
            return v.date.compareTo(w.date);
        }
    }

    public static class comparebyAmount implements Comparator<Transaction> {

        @Override
        public int compare(Transaction v, Transaction w) {
            return Double.compare(v.amount, w.amount);
        }
    }



    public static void main(String[] args) {
        Transaction[] a = new Transaction[4];
        a[0] = new Transaction("Turing   6/17/1990  644.08");
        a[1] = new Transaction("Tarjan   3/26/2002 4121.85");
        a[2] = new Transaction("Knuth    6/14/1999  288.34");
        a[3] = new Transaction("Dijkstra 8/22/2007 2678.40");

        StdOut.println("Unsorted");
        for (Transaction transaction : a) StdOut.println(transaction);
        StdOut.println();

        StdOut.println("Sort by date");
        Arrays.sort(a, new compareByDate());
        for (Transaction transaction : a) StdOut.println(transaction);
        StdOut.println();

        StdOut.println("Sort by customer");
        Arrays.sort(a, new compareByCustomerName());
        for (Transaction transaction : a) StdOut.println(transaction);
        StdOut.println();

        StdOut.println("Sort by amount");
        Arrays.sort(a, new comparebyAmount());
        for (Transaction transaction : a) StdOut.println(transaction);
        StdOut.println();
    }

}
