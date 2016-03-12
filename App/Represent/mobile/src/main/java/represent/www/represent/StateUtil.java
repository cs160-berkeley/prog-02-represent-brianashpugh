package represent.www.represent;

import android.util.Log;

import java.util.HashMap;
import java.util.Map;

/**
 * Created by Brian on 3/9/16.
 */
public class StateUtil {

    Map<String, String> americanAbbrToFull;
    Map<String, String> americanFullToAbbr;

    public StateUtil() {
        americanAbbrToFull = new HashMap<>();
        americanAbbrToFull.put("AL", "Alabama");
        americanAbbrToFull.put("AK", "Alaska");
        americanAbbrToFull.put("AZ", "Arizona");
        americanAbbrToFull.put("AR", "Arkansas");
        americanAbbrToFull.put("CA", "California");
        americanAbbrToFull.put("CO", "Colorado");
        americanAbbrToFull.put("CT", "Connecticut");
        americanAbbrToFull.put("DE", "Delaware");
        americanAbbrToFull.put("DC", "District of Columbia");
        americanAbbrToFull.put("FL", "Florida");
        americanAbbrToFull.put("GA", "Georgia");
        americanAbbrToFull.put("HI", "Hawaii");
        americanAbbrToFull.put("ID", "Idaho");
        americanAbbrToFull.put("IL", "Illinois");
        americanAbbrToFull.put("IN", "Indiana");
        americanAbbrToFull.put("IA", "Iowa");
        americanAbbrToFull.put("KS", "Kansas");
        americanAbbrToFull.put("KY", "Kentucky");
        americanAbbrToFull.put("LA", "Louisiana");
        americanAbbrToFull.put("ME", "Maine");
        americanAbbrToFull.put("MD", "Maryland");
        americanAbbrToFull.put("MA", "Massachusetts");
        americanAbbrToFull.put("MI", "Michigan");
        americanAbbrToFull.put("MN", "Minnesota");
        americanAbbrToFull.put("MS", "Mississippi");
        americanAbbrToFull.put("MO", "Missouri");
        americanAbbrToFull.put("MT", "Montana");
        americanAbbrToFull.put("NE", "Nebraska");
        americanAbbrToFull.put("NV", "Nevada");
        americanAbbrToFull.put("NH", "New Hampshire");
        americanAbbrToFull.put("NJ", "New Jersey");
        americanAbbrToFull.put("NM", "New Mexico");
        americanAbbrToFull.put("NY", "New York");
        americanAbbrToFull.put("NC", "North Carolina");
        americanAbbrToFull.put("ND", "North Dakota");
        americanAbbrToFull.put("OH", "Ohio");
        americanAbbrToFull.put("OK", "Oklahoma");
        americanAbbrToFull.put("OR", "Oregon");
        americanAbbrToFull.put("PA", "Pennsylvania");
        americanAbbrToFull.put("PR", "Puerto Rico");
        americanAbbrToFull.put("RI", "Rhode Island");
        americanAbbrToFull.put("SC", "South Carolina");
        americanAbbrToFull.put("SD", "South Dakota");
        americanAbbrToFull.put("TN", "Tennessee");
        americanAbbrToFull.put("TX", "Texas");
        americanAbbrToFull.put("VI", "U.S. Virgin Islands");
        americanAbbrToFull.put("UT", "Utah");
        americanAbbrToFull.put("VT", "Vermont");
        americanAbbrToFull.put("VA", "Virginia");
        americanAbbrToFull.put("WA", "Washington");
        americanAbbrToFull.put("WV", "West Virginia");
        americanAbbrToFull.put("WI", "Wisconsin");
        americanAbbrToFull.put("WY", "Wyoming");

        this.americanFullToAbbr = new HashMap<>();
        for (String key : americanAbbrToFull.keySet()) {
            americanFullToAbbr.put(americanAbbrToFull.get(key), key);
        }
    }

    public String getStateName(String abbrev) {
        return americanAbbrToFull.get(abbrev);
    }

    public String getStateAbbrev(String name) {
        Log.d("phone", "state abbrev for " + name + " is " + americanFullToAbbr.get(name));
        return americanFullToAbbr.get(name);
    }
}
