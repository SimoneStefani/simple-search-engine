/**
 * Subquery.java
 *
 * Created by S. Stefani on 2017-01-06.
 */

public class Subquery implements Comparable<String> {
    public Comparable<String> leftTerm = null;
    public Comparable<String> rightTerm = null;
    public String operator;
    public String orderedQuery;

    public Subquery(Comparable<String> leftTerm) {
        this.leftTerm = leftTerm;
        this.orderedQuery = leftTerm.toString();
    }

    public Subquery(Comparable<String> leftTerm, String operator, Comparable<String> rightTerm) {

        this.leftTerm = leftTerm;
        this.operator = operator;
        this.rightTerm = rightTerm;
    }

    @Override
    public String toString() {
        if (rightTerm == null) return leftTerm.toString();

        return "(" + leftTerm.toString() + " " + operator + " " + rightTerm.toString() + ")";
    }

    public int compareTo(String o) {
        return this.toString().compareTo(o);
    }
}
