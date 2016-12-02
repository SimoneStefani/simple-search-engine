/**
 * Created by S. Stefani on 2016-11-27.
 */

import se.kth.id1020.util.Attributes;
import se.kth.id1020.util.Document;
import se.kth.id1020.util.Word;
import java.util.Comparator;

public class Posting implements Comparable<Posting> {
    public String postingName;
    public Document document;
    public int occurrence;
    public int popularity;

    public Posting(Word word, Attributes attributes) {
        this.postingName = attributes.document.name;
        this.document = attributes.document;
        this.popularity = attributes.document.popularity;
        this.occurrence = attributes.occurrence;
    }

    public void incrementOccurrence(Posting posting) {
        if (posting.getOccurrence() < this.occurrence) {
            this.occurrence = posting.getOccurrence();
        }
    }

    public int getOccurrence() {
        return occurrence;
    }

    public String getPostingName() {
        return postingName;
    }

    public int compareTo(Posting posting) {
        return this.postingName.compareTo(posting.getPostingName());
    }

    public static class PostingComparator implements Comparator<Posting> {
        private String property = "popularity";
        private boolean direction = true;

        public PostingComparator(String property, boolean direction) {
            this.property = property;
            this.direction = direction;
        }

        public int compare(Posting pst1, Posting pst2) {
            if (property.equals("popularity"))
                return byPopularity(pst1, pst2);
            else if (property.equals("occurrence"))
                return byOccurrence(pst1, pst2);
            else
                return 0;
        }

        public boolean greaterThan(Posting pst1, Posting pst2) {
            return compare(pst1, pst2) > 0;
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
    }
}
