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
    private HashMap<String, HashMap<String, Posting>> index;
    private HashMap<String, Integer> documentsLengths;
    private HashMap<String, ArrayList<ResultDocument>> cache;

    public TinySearchEngine() {
        this.index = new HashMap<String, HashMap<String, Posting>>();
        this.documentsLengths = new HashMap<String, Integer>();
        this.cache = new HashMap<String, ArrayList<ResultDocument>>();
    }

    public void preInserts() {
        System.out.println("Executing pre-insert...");
    }

    public void insert(Sentence sentence, Attributes attributes) {
        for (Word word : sentence.getWords()) {
            // Add word to index if not in
            if (!index.containsKey(word.word)) {
                index.put(word.word, new HashMap<String, Posting>());
            }

            // Create new posting
            HashMap<String, Posting> postingList = index.get(word.word);
            Posting newPosting = new Posting(word, attributes);

            // Update posting if existent or add
            if (postingList.containsKey(newPosting.getName())) {
                postingList.get(newPosting.getName()).updatePosting();
            } else {
                postingList.put(newPosting.getName(), new Posting(word, attributes));
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

    public List<Document> search(String s) {
        // Parse query
        Query query = new Query(s);

        // Compute Array of result
        // TODO: commutativity
        ArrayList<ResultDocument> result = runQuery(query.getParsedQuery());

        if (result == null) {
            return null;
        }

        // If sorting is specified use comparator to sort
        if (query.getProperty() != null && query.getProperty().equals("POPULARITY")) {
            Collections.sort(result, new ResultDocument.PopularityComparator(query.getDirection()));
        } else if (query.getProperty() != null && query.getProperty().equals("RELEVANCE")) {
            Collections.sort(result, new ResultDocument.RelevanceComparator(query.getDirection()));
        }

        // Convert into list of docs
        List<Document> documentList = new LinkedList<Document>();
        for (ResultDocument rd : result) { documentList.add(rd.getDocument()); }


        for (ResultDocument re: result) {
            // System.out.println(re.getDocument().name + " - hits: " + re.getHits() + " - pop: " + re.getPopularity() + " - rel: " + re.getRelevance());
        }
        return documentList;
    }

    private ArrayList<ResultDocument> runQuery(Subquery subQ) {
        if (subQ.rightTerm == null) {

            if (!index.containsKey(subQ.leftTerm)) return new ArrayList<ResultDocument>();
            HashMap<String, Posting> temp = index.get(subQ.leftTerm);
            ArrayList<ResultDocument> list = new ArrayList<ResultDocument>();
            for (Posting value : temp.values()) {
                ResultDocument newRD = new ResultDocument(value.getDocument(), value.getHits());
                newRD.computeRelevance(documentsLengths, temp.size());
                list.add(newRD);
            }

            Collections.sort(list);

            return list;
        }

        if (cache.containsKey(subQ.orderedQuery)) {
            System.out.println("Cache hit: " + subQ.toString());
            return cache.get(subQ.orderedQuery);
        }

        ArrayList<ResultDocument> leftResult = runQuery(subQ.leftTerm instanceof Subquery ? (Subquery) subQ.leftTerm : new Subquery(subQ.leftTerm));
        ArrayList<ResultDocument> rightResult = runQuery(subQ.rightTerm instanceof Subquery ? (Subquery) subQ.rightTerm : new Subquery(subQ.rightTerm));
        String operator = subQ.operator;

        ArrayList<ResultDocument> result;
        if (operator.equals("+")) {
            result = resultIntersection(leftResult, rightResult);
        } else if (operator.equals("|")) {
            result = resultUnion(leftResult, rightResult);
        } else {
            result = resultDifference(leftResult, rightResult);
        }

        cache.put(subQ.orderedQuery, result);
        System.out.println("Add to cache: " + subQ.toString());

        return result;
    }

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

    public String infix(String s) {
        Query query = new Query(s);

        return query.getParsedQuery().toString();
    }
}