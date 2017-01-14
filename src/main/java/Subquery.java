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

    /**
     * Construct a sub-query formed only by one element.
     *
     * @param leftTerm is the term in the sub-query.
     */
    public Subquery(Comparable<String> leftTerm) {
        this.leftTerm = leftTerm;
        this.orderedQuery = leftTerm.toString();
    }

    /**
     * Construct a sub-query formed by a triplet of elements.
     *
     * @param leftTerm is the left-hand term in the sub-query
     * @param operator is the element that connects two terms of the sub-query
     * @param rightTerm is the right-hand term in the sub-query
     */
    public Subquery(Comparable<String> leftTerm, String operator, Comparable<String> rightTerm) {

        this.leftTerm = leftTerm;
        this.operator = operator;
        this.rightTerm = rightTerm;
    }

    /**
     * Generate a string with the infix notation of the sub-query.
     *
     * @return infix sub-query
     */
    @Override
    public String toString() {
        if (rightTerm == null) return leftTerm.toString();

        return "(" + leftTerm.toString() + " " + operator + " " + rightTerm.toString() + ")";
    }

    /**
     * Simple comparator for two sub-queries.
     *
     * @param o is the sub-query to compare to
     * @return usual comparator integer result (-1/0/1)
     */
    public int compareTo(String o) {
        return this.toString().compareTo(o);
    }
}
