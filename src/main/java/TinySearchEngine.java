/**
 * TinySearchEngine.java
 *
 * Created by S. Stefani on 2016-11-24.
 */

import se.kth.id1020.TinySearchEngineBase;
import se.kth.id1020.util.Attributes;
import se.kth.id1020.util.Document;
import se.kth.id1020.util.Word;
import java.util.*;

public class TinySearchEngine implements TinySearchEngineBase {
    public ArrayList<Token> index = new ArrayList<Token>();
    public BinarySearch bs = new BinarySearch();

    public void insert(Word word, Attributes attributes) {
        Token token = new Token(word, attributes);

        int tokenPosition = bs.search(token, index);

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

        ArrayList<Posting> postings = union(newQuery);

        if (postings == null) { return null; }

        sort(postings, newQuery.getComparator());

        List<Document> resultList = new ArrayList<Document>();
        for (Posting pst : postings) {
            resultList.add(pst.getDocument());
        }

        return resultList;
    }

    private void sort(List<Posting> list, Posting.PostingComparator cmp) {

        int r = list.size() - 2;
        boolean swapped = true;

        while (r >= 0 && swapped) {
            swapped = false;
            for (int i = 0; i <= r; i++) {
                if (cmp.compare(list.get(i), list.get(i + 1)) > 0) {
                    swapped = true;
                    Collections.swap(list, i, i + 1);
                }
            }
            r--;
        }
    }

    private ArrayList<Posting> fetchFromIndex(String word) {
        Token token = new Token(word);
        int tokenPosition = bs.search(token, index);
        if (tokenPosition < 0)
            return null;
        else {
            return index.get(tokenPosition).getPostingList();

        }
    }

    private ArrayList<Posting> union(Query newQuery) {
        ArrayList<Posting> postings = new ArrayList<Posting>();
        if (fetchFromIndex(newQuery.getWords().get(0)) == null) {
            postings = null;
        } else {
            postings.addAll(fetchFromIndex(newQuery.getWords().get(0)));
        }

        int wordsNum = newQuery.getWords().size();
        while (wordsNum > 1) {
            ArrayList<Posting> unionList;
            unionList = fetchFromIndex(newQuery.getWords().get(wordsNum - 1));
            if (postings == null) {
                postings = unionList;
            }
            for (Posting posting : unionList) {
                int index = bs.search(posting, postings);
                if (index < 0) {
                    postings.add(~index, posting);
                }
            }
            wordsNum--;
        }
        return postings;
    }
}
