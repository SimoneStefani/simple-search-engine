/**
 * Subquery.java
 *
 * Created by S. Stefani on 2017-01-06.
 */

public class Subquery implements Comparable<String> {
    private Comparable<String> leftTerm;
    private Comparable<String> rightTerm;
    private String operator;

    public Subquery(Comparable<String> leftTerm) {
        this.leftTerm = leftTerm;
    }

    public Subquery(Comparable<String> leftTerm, String operator, Comparable<String> rightTerm) {

        // If operator is commutative order terms ascending
        if ((operator.equals("|") || operator.equals("+")) && leftTerm.compareTo(rightTerm.toString()) >= 1) {
            Comparable<String> t = leftTerm;
            leftTerm = rightTerm;
            rightTerm = t;
        }

        this.leftTerm = leftTerm;
        this.operator = operator;
        this.rightTerm = rightTerm;
    }

    public int compareTo(String o) {
        return this.toString().compareTo(o);
    }
}
