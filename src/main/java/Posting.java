/**
 * Posting.java
 *
 * Created by S. Stefani on 2016-11-27.
 */

import se.kth.id1020.util.Attributes;
import se.kth.id1020.util.Document;
import se.kth.id1020.util.Word;

public class Posting {
    private String name;
    private String word;
    private Document document;
    private int popularity;
    private int hits;

    public Posting(Word word, Attributes attributes) {
        this.name = attributes.document.name;
        this.word = word.word;
        this.document = attributes.document;
        this.popularity = attributes.document.popularity;
        this.hits = 1;
    }

    public String getName() {
        return name;
    }

    public String getWord() {
        return word;
    }

    public Document getDocument() {
        return document;
    }

    public int getPopularity() {
        return popularity;
    }

    public int getHits() {
        return hits;
    }

    public void updatePosting() {
        this.hits++;
    }
}
