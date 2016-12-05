/**
 * BinarySearch.java
 *
 * Created by S. Stefani on 2016-12-05.
 */

import java.util.ArrayList;

public class BinarySearch<T extends Comparable> {

    public int search(T key, ArrayList<T> a) {
        return search(key, a, 0, a.size());
    }

    private int search(T key, ArrayList<T> a, int lo, int hi) {
        // possible key indices in [lo, hi)
        if (hi <= lo) return -(lo + 1);
        int mid = lo + (hi - lo) / 2;
        int cmp = a.get(mid).compareTo(key);
        if      (cmp > 0) return search(key, a, lo, mid);
        else if (cmp < 0) return search(key, a, mid+1, hi);
        else              return mid;
    }
}
