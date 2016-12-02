import java.util.ArrayList;
import java.util.List;

/**
 * Created by S. Stefani on 2016-12-02.
 */
public class Query {
    private String property;
    private boolean direction;
    private List<String> words;

    public Query() {
        this.words = new ArrayList<String>();
    }

    public String getProperty() {
        return property;
    }

    public boolean getDirection() {
        return direction;
    }

    public List<String> getWords() {
        return words;
    }

    public void parseQuery(String queryInput) {
        String[] query = queryInput.split("\\s+");
        int queryLength = query.length;

        for (int i = 0; i < queryLength; i++) {
            if (query[i].equals("orderby")) {
                if (i + 1 < queryLength) {
                    if (query[i + 1].equals("count") || query[i + 1].equals("popularity") || query[i + 1].equals("occurrence")) {
                        this.property = query[i + 1];
                    } else {
                        System.out.println("Choose a property among count, popularity and occurrence");
                        break;
                    }
                }
                if (i + 2 < queryLength) {
                    if (query[i + 2].equals("asc")) {
                        this.direction = true;
                    } else if (query[i + 2].equals("desc")) {
                        this.direction = false;
                    } else {
                        System.out.println("Choose a direction between asc and desc");
                        break;
                    }
                }
                break;
            } else {
                words.add(query[i]);
            }
        }
    }


}
