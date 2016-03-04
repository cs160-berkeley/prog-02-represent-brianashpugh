package represent.www.represent;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

/**
 * Created by Brian on 3/3/16.
 */
class Representative {
    String title;
    String name;
    String party;

    Representative(String title, String name, String party) {
        this.title = title;
        this.name = name;
        this.party = party;
    }

    static Representative fromBytes(byte[] bytes) {
        String combined = bytes.toString();
        List<String> split = Arrays.asList(combined.split("$$$"));
        return new Representative(split.get(0), split.get(1), split.get(2));
    }
}
