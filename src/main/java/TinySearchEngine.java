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

import javax.print.Doc;
import java.util.*;

public class TinySearchEngine implements TinySearchEngineBase {
    private HashMap<Word, HashMap<String, Posting>> index;
    private HashMap<String, Integer> documentsLengths;

    public TinySearchEngine() {
        this.index = new HashMap<Word, HashMap<String, Posting>>();
        this.documentsLengths = new HashMap<String, Integer>();
    }

    public void preInserts() {
        System.out.println("Executing pre-insert...");
    }

    public void insert(Sentence sentence, Attributes attributes) {
        for (Word word : sentence.getWords()) {
            // Add word to index if not in
            if (!index.containsKey(word)) {
                index.put(word, new HashMap<String, Posting>());
            }

            // Create new posting
            HashMap<String, Posting> postingList = index.get(word);
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
        // TODO: complete query, implement caching (commutative)
        ArrayList<ResultDocument> result = runQuery(query.getParsedQuery());

        // TODO: set comparators
        Comparator cmp;

        // Sort according to strategy
        Collections.sort(result, cmp);

        // Convert into list of docs
        List<Document> documentList = new LinkedList<Document>();
        for (ResultDocument rd : result) { documentList.add(rd.getDocument()); }

        return documentList;
    }

    private ArrayList<ResultDocument> runQuery(Subquery subQ) {
        if (subQ.rightTerm == null) {
            HashMap<String, Posting> temp = index.get(subQ.leftTerm);
            ArrayList<ResultDocument> list = new ArrayList<ResultDocument>();
            for (Posting value : temp.values()) {
                list.add(new ResultDocument(value.getDocument(), value.getHits()));
            }
            return list;
        }
        ArrayList<ResultDocument> leftResult = runQuery((Subquery) subQ.leftTerm);
        ArrayList<ResultDocument> rightResult = runQuery((Subquery) subQ.rightTerm);
        String operator = subQ.operator;

        if (operator.equals("+")) {
            return resultIntersection(leftResult, rightResult);
        } else if (operator.equals("|")) {
            return resultUnion(leftResult, rightResult);
        } else {
            return resultDifference(leftResult, rightResult);
        }
    }

    private ArrayList<ResultDocument> resultIntersection(ArrayList<ResultDocument> l, ArrayList<ResultDocument> r) {

    }

    private ArrayList<ResultDocument> resultUnion(ArrayList<ResultDocument> l, ArrayList<ResultDocument> r) {

    }

    private ArrayList<ResultDocument> resultDifference(ArrayList<ResultDocument> l, ArrayList<ResultDocument> r) {

    }

    public String infix(String s) {
        // TODO: Add infix test
        return null;
    }
}
