/**
 * ResultDocument.java
 *
 * Created by S. Stefani on 2017-01-06.
 */

import se.kth.id1020.util.Document;
import java.util.HashMap;

public class ResultDocument {
    private int hits;
    private double relevance;
    private Document document;
    private int popularity;

    public ResultDocument(Document document, int hits) {
        this.document = document;
        this.hits = hits;
        this.popularity = document.popularity;
    }

    public ResultDocument(int hits, double relevance, Document document) {
        this.hits = hits;
        this.relevance = relevance;
        this.document = document;
    }

    public void computeRelevance(HashMap<String, Integer> documentsLengths, int relevantDocs) {
        relevance = tf(documentsLengths.get(document.name)) * idf(documentsLengths.size(), relevantDocs);
    }

    public int getHits() {
        return hits;
    }

    public double getRelevance() {
        return relevance;
    }

    public int getPopularity() {
        return popularity;
    }

    private double tf(int totalTerms) {
        return (double) this.hits / totalTerms;
    }

    private double idf(int totalDocs, int relevantDocs) {
        return Math.log10(totalDocs / relevantDocs);
    }
}
