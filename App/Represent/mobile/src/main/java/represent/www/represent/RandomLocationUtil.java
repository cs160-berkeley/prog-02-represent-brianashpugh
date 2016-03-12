package represent.www.represent;

import android.content.Context;
import android.location.Location;
import android.util.Log;

import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.Reader;
import java.io.StringWriter;
import java.io.Writer;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ThreadLocalRandom;

/**
 * Created by Brian on 3/10/16.
 */
public class RandomLocationUtil {

    JsonArray locations;
    Context context;

    public RandomLocationUtil(Context context) {
        this.context = context;
        String result = rawResourceToString(R.raw.county_lat_longs);
        JsonParser parser = new JsonParser();
        locations = parser.parse(result).getAsJsonArray();
        Log.d("phone", "Random location util loaded...");
    }

    public Location getRandomLocation() {
        int randomIndex = ThreadLocalRandom.current().nextInt(0, locations.size());
        JsonObject obj = locations.get(randomIndex).getAsJsonObject();
        Location location = new Location("random");
        location.setLatitude(obj.get("lat").getAsDouble());
        location.setLongitude(obj.get("lng").getAsDouble());
        return location;
    }

    public String rawResourceToString(int id) {
        InputStream stream = context.getResources().openRawResource(id);
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
            Log.d("api", "Caught exception in RandomLocationUtil: " + e.getMessage());
        }
        return writer.toString();
    }

}
