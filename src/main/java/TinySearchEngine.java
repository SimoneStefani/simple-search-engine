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

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class TinySearchEngine implements TinySearchEngineBase {
    public HashMap<Word, ArrayList<Posting>> index = new HashMap<Word, ArrayList<Posting>>();
    public HashMap<String, Integer> documentsLenghts = new HashMap<String, Integer>();

    public void preInserts() {

    }

    public void insert(Sentence sentence, Attributes attributes) {
        for (Word word : sentence.getWords()) {
            if (!index.containsKey(word)) {
                index.put(word, new ArrayList<Posting>());
            }

            ArrayList<Posting> postingList = index.get(word);
            Posting newPosting = new Posting(word, attributes);
            if (postingList.contains(newPosting)) {
                postingList.get(postingList.indexOf(newPosting)).addDoc(newPosting);
            } else {
                postingList.add(new Posting(word, attributes));
            }

        }

        Integer sentencelength = sentence.getWords().size();
        if (documentsLenghts.containsKey(attributes.document.name)) {
            sentencelength += documentsLenghts.get(attributes.document.name);
        }
        documentsLenghts.put(attributes.document.name, sentencelength);
    }

    public void postInserts() {
        // foreach posting
    }

    public List<Document> search(String s) {
        return null;
    }

    public String infix(String s) {
        return null;
    }
}
