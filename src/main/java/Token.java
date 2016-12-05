/**
 * Token.java
 *
 * Created by S. Stefani on 2016-12-27.
 */

import se.kth.id1020.util.Attributes;
import se.kth.id1020.util.Word;
import java.util.ArrayList;


public class Token implements Comparable<Token> {
    private String tokenName;
    private Word word;
    private Attributes attributes;
    private ArrayList<Posting> postingList;

    /**
     * Creates a complete token containing a Word and its Attributes.
     *
     * @param word is the word represented by the token
     * @param attributes are the attributes of the word
     */
    public Token(Word word, Attributes attributes) {
        this.tokenName = word.word;
        this.word = word;
        this.attributes = attributes;
        this.postingList = new ArrayList<Posting>();
    }

    /**
     * Creates a dummy token setting only the tokenName. Used for search an
     * comparison purposes.
     *
     * @param tokenName the word to search
     */
    public Token(String tokenName) {
        this.tokenName = tokenName;
    }

    /**
     * Get the string representation of the word encapsulated by the token.
     *
     * @return the word of the token
     */
    public String getTokenName() {
        return tokenName;
    }

    /**
     * Get the list of the postings (documents) that contain the token.
     *
     * @return a list of postings
     */
    public ArrayList<Posting> getPostingList() {
        return postingList;
    }

    /**
     * Add a posting to the current token. It dynamically search the correct insertion
     * point to keep an alphabetically ordered list of postings.
     *
     * @param posting is the posting to insert
     */
    public void addPosting(Posting posting) {
        BinarySearch bs = new BinarySearch();
        if (postingList.isEmpty()) {
            postingList.add(posting);
        } else {
            int postingPosition = bs.search(posting, postingList);

            if (postingPosition < 0) {
                postingList.add(posting);
            } else {
                postingList.get(postingPosition).addDoc(posting);
            }
        }
    }

    /**
     * Simple method to compare two tokens based on their tokenName (the represented word).
     * It uses the ordinary compareTo for strings.
     *
     * @param token is the token to compare to
     */
    public int compareTo(Token token) {
        return this.tokenName.compareTo(token.getTokenName());
    }
}
