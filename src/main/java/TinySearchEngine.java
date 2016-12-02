/**
 * TinySearchEngine.java
 *
 * Created by S. Stefani on 2016-11-24.
 */

import se.kth.id1020.TinySearchEngineBase;
import se.kth.id1020.util.Attributes;
import se.kth.id1020.util.Document;
import se.kth.id1020.util.Word;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class TinySearchEngine implements TinySearchEngineBase {
    public List<Token> index = new ArrayList<Token>();

    public void insert(Word word, Attributes attributes) {
        Token token = new Token(word, attributes);

        int tokenPosition = Collections.binarySearch(index, token);

        if (tokenPosition < 0) {
            token.addPosting(new Posting(word, attributes));
            index.add(~tokenPosition, token);
        } else {
            token = index.get(tokenPosition);
            token.addPosting(new Posting(word, attributes));
        }
    }

    public List<Document> search(String s) {
        String[] query = s.split("\\s+");
        int length = query.length;
        List<Document> resultsList = new ArrayList<Document>();


    }
}
