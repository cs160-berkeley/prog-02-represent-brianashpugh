package represent.www.represent.api;

import android.app.Activity;
import android.content.Context;
import android.util.Log;

import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonParser;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.Reader;
import java.io.StringWriter;
import java.io.Writer;
import java.util.HashMap;
import java.util.Map;

import represent.www.represent.R;
import represent.www.represent.VotingData;

/**
 * Created by Brian on 3/6/16.
 */
public class VotingDataAPI extends Activity {

    Map<String, VotingData> countyToData;

    public VotingDataAPI(Context context) {
        InputStream stream = context.getResources().openRawResource(R.raw.election_county_2012);
        Writer writer = new StringWriter();
        char[] buffer = new char[1024];
        try {
            Log.d("api", "Loading voting data...");
            Reader reader = new BufferedReader(new InputStreamReader(stream, "UTF-8"));
            int n;
            while ((n = reader.read(buffer)) != -1) {
                writer.write(buffer, 0, n);
            }
            stream.close();
        }
        catch(IOException e) {
            Log.d("api", "Caught exception in VotingDataAPI: " + e.getMessage());
        }

        this.countyToData = new HashMap<>();

        String jsonString = writer.toString();
        JsonParser parser = new JsonParser();
        JsonArray counties = parser.parse(jsonString).getAsJsonArray();
        for (JsonElement county : counties) {
            Integer obamaVote = county.getAsJsonObject().get("obama-vote").getAsInt();
            Integer romneyVote = county.getAsJsonObject().get("romney-vote").getAsInt();
            Integer voteCount = obamaVote + romneyVote;
            VotingData data = new VotingData(
                    county.getAsJsonObject().get("state-postal").getAsString(),
                    county.getAsJsonObject().get("county-name").getAsString(),
                    county.getAsJsonObject().get("obama-percentage").getAsString(),
                    county.getAsJsonObject().get("romney-percentage").getAsString(),
                    voteCount.toString()
                    );
            this.countyToData.put(data.getCounty(), data);
        }
    }

    public VotingData getData(String county) {
        String countyWithoutCounty = county.replace(" County", "");
        String countyWithCounty = county + " County";
        VotingData data = this.countyToData.get(county);
        if (data == null) {
            data = this.countyToData.get(countyWithCounty);
        }
        if (data == null) {
            data = this.countyToData.get(countyWithoutCounty);
        }
        if (data == null) {
            Log.d("api", "Could not find voting data for county: " + county);
            data = new VotingData("", county, "", "", "No Information on");
        }
        return data;
    }
}
