/**
 * Created by S. Stefani on 2016-12-02.
 */

import se.kth.id1020.util.Attributes;
import se.kth.id1020.util.Word;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;


public class Token implements Comparable<Token> {
    private Word word;
    private Attributes attributes;
    private List<Posting> postingList;

    public Token(Word word, Attributes attributes) {
        this.word = word;
        this.attributes = attributes;
        this.postingList = new ArrayList<Posting>();
    }

    public Word getWord() {
        return word;
    }

    public void setWord(Word word) {
        this.word = word;
    }

    public Attributes getAttributes() {
        return attributes;
    }

    public void setAttributes(Attributes attributes) {
        this.attributes = attributes;
    }

    public void addPosting(Posting posting) {
        if (postingList.isEmpty()) {
            postingList.add(posting);
        } else {
            int postingPosition = Collections.binarySearch(postingList, posting);

            if (postingPosition < 0) {
                postingList.add(posting);
            } else {
                postingList.get(postingPosition).incrementOccurrence(posting);
            }
        }
    }

    public int compareTo(Token token) {
        return this.word.word.compareTo(token.getWord().word);
    }
}
