/**
 * Query.java
 *
 * Created by S. Stefani on 2016-12-02.
 */

import java.util.ArrayList;
import java.util.List;

public class Query {
    private List<String> words;
    private Posting.PostingComparator cmp;

    public Query() {
        this.words = new ArrayList<String>();
        this.cmp = new Posting.PostingComparator("popularity", true);
    }

    /**
     * Get the searched words.
     *
     * @return an array of words
     */
    public List<String> getWords() {
        return words;
    }

    /**
     * Get a comparator with specific comparison criteria based on the query.
     * Default: popularity, ascending.
     *
     * @return a posting comparator
     */
    public Posting.PostingComparator getComparator() {
        return cmp;
    }

    /**
     * Parse a query string and extract the searched words together with the optional
     * arguments property and direction
     *
     * @param queryInput is the query string
     */
    public void parseQuery(String queryInput) {
        String[] query = queryInput.split("\\s+");
        int queryLength = query.length;

        for (int i = 0; i < queryLength; i++) {
            if (query[i].equals("orderby")) {
                if (i + 1 < queryLength) {
                    if (query[i + 1].equals("count") || query[i + 1].equals("popularity") || query[i + 1].equals("occurrence")) {
                        this.cmp.setProperty(query[i + 1]);
                    } else {
                        System.out.println("Choose a property among count, popularity and occurrence");
                        break;
                    }
                }
                if (i + 2 < queryLength) {
                    if (query[i + 2].equals("asc")) {
                        this.cmp.setDirection(true);
                    } else if (query[i + 2].equals("desc")) {
                        this.cmp.setDirection(false);
                    } else {
                        System.out.println("Choose a direction between asc and desc");
                        break;
                    }
                }
                break;
            } else {
                words.add(query[i]);
            }
        }
    }
}
