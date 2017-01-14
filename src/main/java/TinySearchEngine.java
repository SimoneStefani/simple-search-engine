/**
 * TinySearchEngine.java
 *
 * Created by S. Stefani on 2016-11-24.
 */

import se.kth.id1020.TinySearchEngineBase;
import se.kth.id1020.util.Attributes;
import se.kth.id1020.util.Document;
import se.kth.id1020.util.Sentence;
import se.kth.id1020.util.Word;
import java.util.*;

public class TinySearchEngine implements TinySearchEngineBase {
    private HashMap<String, ArrayList<ResultDocument>> index;
    private HashMap<String, Integer> documentsLengths;
    private HashMap<String, ArrayList<ResultDocument>> cache;


    public TinySearchEngine() {
        this.index = new HashMap<String, ArrayList<ResultDocument>>();
        this.documentsLengths = new HashMap<String, Integer>();
        this.cache = new HashMap<String, ArrayList<ResultDocument>>();
    }

    public void preInserts() {
        System.out.println("Executing pre-insert...");
    }

    /**
     * Insert all the words of a sentence in the index.
     *
     * @param sentence is the current sentence
     * @param attributes contain the parent document of the sentence
     */
    public void insert(Sentence sentence, Attributes attributes) {
        for (Word word : sentence.getWords()) {
            // Add word to index if not in
            if (!index.containsKey(word.word)) {
                index.put(word.word, new ArrayList<ResultDocument>());
            }

            // Create new posting
            ArrayList<ResultDocument> postingList = index.get(word.word);
            ResultDocument newPosting = new ResultDocument(attributes.document, 1);

            int ind = Collections.binarySearch(postingList, newPosting);

            // Update posting if existent or add
            if (ind < 0) {
                postingList.add(-ind-1, newPosting);
            } else {
                postingList.get(ind).updatePosting();
            }
        }

        // Compute and store lengths of documents
        Integer sentenceLength = sentence.getWords().size();
        if (documentsLengths.containsKey(attributes.document.name)) {
            sentenceLength += documentsLengths.get(attributes.document.name);
        }
        documentsLengths.put(attributes.document.name, sentenceLength);
    }

    public void postInserts() {
        System.out.println("Executing post-insert...");
    }

    /**
     * Parse a user query and search for all the elements that satisfy such query.
     * Order the results according to the user input.
     *
     * @param s is the input query string
     * @return the list of docs that satisfy the query
     */
    public List<Document> search(String s) {
        // Parse query
        Query query = new Query(s);

        // Compute Array of result
        ArrayList<ResultDocument> result = runQuery(query.getParsedQuery());
        if (result == null) { return null; }

        // If sorting is specified use comparator to sort
        if (query.getProperty() != null && query.getProperty().equals("POPULARITY")) {
            Collections.sort(result, new ResultDocument.PopularityComparator(query.getDirection()));
        } else if (query.getProperty() != null && query.getProperty().equals("RELEVANCE")) {
            Collections.sort(result, new ResultDocument.RelevanceComparator(query.getDirection()));
        }

        // Convert into list of documents
        List<Document> documentList = new LinkedList<Document>();
        for (ResultDocument rd : result) { documentList.add(rd.getDocument()); }

        return documentList;
    }

    /**
     * Recursively analyse the query and compute the results considering the query operators.
     *
     * @param subQ is the sub-query object (result of the query parsing)
     * @return an array list of documents
     */
    private ArrayList<ResultDocument> runQuery(Subquery subQ) {
        if (subQ.rightTerm == null) {

            if (!index.containsKey(subQ.leftTerm)) return new ArrayList<ResultDocument>();
            ArrayList<ResultDocument> list = new ArrayList<ResultDocument>();
            for (ResultDocument value : index.get(subQ.leftTerm)) {
                ResultDocument newRD = new ResultDocument(value.getDocument(), value.getHits());
                newRD.computeRelevance(documentsLengths, index.get(subQ.leftTerm).size());
                list.add(newRD);
            }

            return list;
        }

        // Check if the query is cached
        if (cache.containsKey(subQ.orderedQuery)) {
            // System.out.println("Cache hit: " + subQ.toString());
            return cache.get(subQ.orderedQuery);
        }

        ArrayList<ResultDocument> leftResult = runQuery(subQ.leftTerm instanceof Subquery ? (Subquery) subQ.leftTerm : new Subquery(subQ.leftTerm));
        ArrayList<ResultDocument> rightResult = runQuery(subQ.rightTerm instanceof Subquery ? (Subquery) subQ.rightTerm : new Subquery(subQ.rightTerm));
        String operator = subQ.operator;

        // Run query operations (union, intersection, difference)
        ArrayList<ResultDocument> result;
        if (operator.equals("+")) {
            result = resultIntersection(leftResult, rightResult);
        } else if (operator.equals("|")) {
            result = resultUnion(leftResult, rightResult);
        } else {
            result = resultDifference(leftResult, rightResult);
        }

        // Cache the result
        cache.put(subQ.orderedQuery, result);
        // System.out.println("Add to cache: " + subQ.toString());

        return result;
    }

    // Compute intersection of two queries
    private ArrayList<ResultDocument> resultIntersection(ArrayList<ResultDocument> l, ArrayList<ResultDocument> r) {
        ArrayList<ResultDocument> result = new ArrayList<ResultDocument>();
        for (ResultDocument rd : l) {
            int ind = Collections.binarySearch(r, rd);
            if (ind >= 0) {
                result.add(merge(rd, r.get(ind)));
            }
        }

        return result;
    }

    // Compute union of two queries
    private ArrayList<ResultDocument> resultUnion(ArrayList<ResultDocument> l, ArrayList<ResultDocument> r) {
        ArrayList<ResultDocument> result = new ArrayList<ResultDocument>();
        result.addAll(l);
        for (ResultDocument rd : r) {
            int ind = Collections.binarySearch(result, rd);
            if (ind >= 0) {
                result.set(ind, merge(result.get(ind), rd));
            } else {
                result.add(-ind-1, rd);
            }
        }

        return result;
    }

    // Compute difference of two queries
    private ArrayList<ResultDocument> resultDifference(ArrayList<ResultDocument> l, ArrayList<ResultDocument> r) {
        ArrayList<ResultDocument> result = new ArrayList<ResultDocument>();

        for (ResultDocument rd : l) {
            if (!r.contains(rd)) { result.add(rd); }
        }

        return result;
    }

    private ResultDocument merge(ResultDocument u, ResultDocument v) {
        return new ResultDocument(u.getDocument(), u.getRelevance() + v.getRelevance());
    }

    /**
     * Output the infix version of the query string (useful to check correctness of parser)
     *
     * @param s is the user query string
     * @return the infix version of query
     */
    public String infix(String s) {
        Query query = new Query(s);
        String dir = query.getDirection() == 1 ? "asc" : "desc";
        return query.getParsedQuery().toString() + " orderby " + query.getProperty().toLowerCase() + " " + dir;
    }
}