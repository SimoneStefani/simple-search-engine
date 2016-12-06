/**
 * Posting.java
 *
 * Created by S. Stefani on 2016-11-27.
 */

import se.kth.id1020.util.Attributes;
import se.kth.id1020.util.Document;
import se.kth.id1020.util.Word;
import java.util.Comparator;

public class Posting implements Comparable<Posting> {
    private String postingName;
    private Document document;
    private int occurrence;
    private int popularity;
    private int hits;

    public Posting(Word word, Attributes attributes) {
        this.postingName = attributes.document.name;
        this.document = attributes.document;
        this.popularity = attributes.document.popularity;
        this.occurrence = attributes.occurrence;
        this.hits = 1;
    }

    /**
     * When a document with an existing posting is added to the index we
     * increment the number of hits (the number of times a word appears in the same
     * document and we update the occurrence (after how many words it appears).
     *
     * @param posting is the posting we are adding the word to
     */
    public void addDoc(Posting posting) {
        if (posting.getOccurrence() < this.occurrence) {
            this.occurrence = posting.getOccurrence();
        }
        this.hits++;
    }

    /**
     * Get the occurrence of a word in a specific document.
     *
     * @return the occurrence
     */
    public int getOccurrence() {
        return occurrence;
    }

    /**
     * The posting name encapsulates in a simple way the name of the document
     * referred by the posting.
     *
     * @return the posting name
     */
    public String getPostingName() {
        return postingName;
    }

    /**
     * The number of times a word appear in a document.
     *
     * @return the number of hits
     */
    public int getHits() {
        return hits;
    }

    /**
     * The document referred by the posting.
     *
     * @return the document
     */
    public Document getDocument() {
        return document;
    }

    /**
     * Simple way of comparing postings based on the posting name (alphabetic order).
     *
     * @param posting is the posting we want to compare to
     * @return
     */
    public int compareTo(Posting posting) {
        return this.postingName.compareTo(posting.getPostingName());
    }

    /**
     * A more advanced comparator. It compares the postings base on popularity, occurrence and relevance
     * considering both ascending and descending order.
     */
    public static class PostingComparator implements Comparator<Posting> {
        private String property;
        private boolean direction;

        public PostingComparator(String property, boolean direction) {
            this.property = property;
            this.direction = direction;
        }

        public void setProperty(String property) {
            this.property = property;
        }

        public void setDirection(boolean direction) {
            this.direction = direction;
        }

        public int compare(Posting pst1, Posting pst2) {
            if (property.equals("popularity"))
                return byPopularity(pst1, pst2);
            else if (property.equals("count"))
                return byRelevance(pst1, pst2);
            else if (property.equals("occurrence"))
                return byOccurrence(pst1, pst2);
            else
                return 0;
        }

        private int byOccurrence(Posting pst1, Posting pst2) {
            int diff = pst1.occurrence - pst2.occurrence;
            diff = direction ? diff : -diff;

            if (diff < 0) { return -1; }
            else if (diff > 0) { return 1; }
            else { return 0; }
        }

        private int byPopularity(Posting pst1, Posting pst2) {
            int diff = pst1.popularity - pst2.popularity;
            diff = direction ? diff : -diff;

            if (diff < 0) { return -1; }
            else if (diff > 0) { return 1; }
            else { return 0; }
        }

        private int byRelevance(Posting pst1, Posting pst2) {
            int diff = pst1.getHits() - pst2.getHits();
            diff = direction ? diff : -diff;

            if (pst1.getPostingName().equals(pst2.getPostingName())) { return 0; }
            if (diff > 0) { return 1; }
            else if (diff < 0) { return -1; }
            else { return 1; }
        }
    }
}
