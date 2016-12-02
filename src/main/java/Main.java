/**
 * Main.java
 *
 * Created by S. Stefani on 2016-11-24.
 */

import se.kth.id1020.Driver;
import se.kth.id1020.TinySearchEngineBase;

public class Main {
    public static void main(String[] args) throws Exception {
        TinySearchEngineBase searchEngine = new TinySearchEngine();
        Driver.run(searchEngine);
    }
}
