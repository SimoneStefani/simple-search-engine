/**
 * TinySearchEngine.java
 * <p>
 * Created by S. Stefani on 2016-11-24.
 */

import se.kth.id1020.TinySearchEngineBase;
import se.kth.id1020.util.Attributes;
import se.kth.id1020.util.Document;
import se.kth.id1020.util.Word;

import java.util.*;

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

    public List<Document> search(String search) {
        Query newQuery = new Query();
        newQuery.parseQuery(search);

        List<Posting> postings = fetchFromIndex(newQuery.getWords().get(0));
        int wordsNum = newQuery.getWords().size();

        System.out.println(newQuery.getWords());
        while (wordsNum > 1) {
            List<Posting> unionList = fetchFromIndex(newQuery.getWords().get(wordsNum - 1));
            if (postings == null) { postings = unionList; }
            for (Posting posting : unionList) {
                int index = Collections.binarySearch(postings, posting);
                if (index < 0) { postings.add(~index, posting); }
            }
            wordsNum--;
        }

        System.out.println(postings);

        sort(postings, newQuery.getProperty(), newQuery.getDirection());
        if (postings == null) { return null; }

        List<Document> resultList = new ArrayList<Document>();
        for (Posting pst : postings) {
            resultList.add(pst.document);
        }

        return resultList;
    }

    public void sort(List<Posting> list, String property, boolean direction) {
        Posting.PostingComparator cmp = new Posting.PostingComparator(property, direction);

        int r = list.size() - 2;
        boolean swapped = true;

        while (r >= 0 && swapped) {
            swapped = false;
            for (int i = 0; i <= r; i++) {
                if (cmp.greaterThan(list.get(i), list.get(i + 1))) {
                    swapped = true;
                    Collections.swap(list, i, i + 1);
                }
            }
            r--;
        }
    }


    private List<Posting> fetchFromIndex(String word) {
        Token token = new Token(word);
        int tokenPosition = Collections.binarySearch(index, token);
        if (tokenPosition < 0)
            return null;
        else {
            return index.get(tokenPosition).getPostingList();

        }
    }
}
