/**
 * Query.java
 *
 * Created by S. Stefani on 2016-12-02.
 */

import edu.princeton.cs.algs4.Stack;

public class Query {
    private Subquery parsedQuery;
    private String property;
    private int direction;

    public Query(String queryString) {
        parseQuery(queryString);
    }

    private void parseQuery(String str) {
        String[] parts = str.split("orderby");
        String[] elements = parts[0].split("\\s+");

        // Use two-stack algorithm to parse prefix notation
        Stack<Comparable<String>> terms = new Stack<Comparable<String>>();
        Stack<Comparable<String>> helper = new Stack<Comparable<String>>();

        for (String el : elements) { terms.push(el); }
        while (!terms.isEmpty()) {
            Comparable<String> term = terms.pop();
            String operands = "+|-";
            if (operands.contains(term.toString())) {
                Comparable<String> leftSide = helper.pop();
                Comparable<String> rightSide = helper.pop();
                helper.push(new Subquery(leftSide, term.toString(), rightSide));
            } else {
                helper.push(term);
            }
        }

        Comparable<String> resultQuery = helper.pop();
        parsedQuery = resultQuery instanceof String ? new Subquery(resultQuery) : (Subquery) resultQuery;

        if (parts.length < 2) {
            return;
        }

        // Parse sorting properties
        if (parts[1].contains("relevance")) {
            property = "RELEVANCE";
        } else if (parts[1].contains("popularity")) {
            property = "POPULARITY";
        }

        if (parts[1].contains("asc")) {
            direction = 1;
        } else if (parts[1].contains("desc")) {
            direction = -1;
        }
    }

    public Subquery getParsedQuery() {
        return parsedQuery;
    }

    public String getProperty() {
        return property;
    }

    public int getDirection() {
        return direction;
    }
}
