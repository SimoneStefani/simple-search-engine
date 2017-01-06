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

import java.util.HashMap;
import java.util.List;

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
        Query query = new Query(s);

        return null;
    }

    public String infix(String s) {
        return null;
    }
}
