/**
 * Created by S. Stefani on 2016-12-02.
 */

import se.kth.id1020.util.Attributes;
import se.kth.id1020.util.Word;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;


public class Token implements Comparable<Token> {
    private String tokenName;
    private Word word;
    private Attributes attributes;
    private List<Posting> postingList;

    public Token(Word word, Attributes attributes) {
        this.tokenName = word.word;
        this.word = word;
        this.attributes = attributes;
        this.postingList = new ArrayList<Posting>();
    }

    public Token(String tokenName) {
        this.tokenName = tokenName;
    }

    public String getTokenName() {
        return tokenName;
    }

    public List<Posting> getPostingList() {
        return postingList;
    }

    public void addPosting(Posting posting) {
        if (postingList.isEmpty()) {
            postingList.add(posting);
        } else {
            int postingPosition = Collections.binarySearch(postingList, posting);

            if (postingPosition < 0) {
                postingList.add(posting);
            } else {
                postingList.get(postingPosition).addDoc(posting);
            }
        }
    }

    public int compareTo(Token token) {
        return this.tokenName.compareTo(token.getTokenName());
    }
}
