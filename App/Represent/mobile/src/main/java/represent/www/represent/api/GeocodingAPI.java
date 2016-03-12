package represent.www.represent.api;

import android.location.Location;
import android.os.AsyncTask;
import android.util.Log;

import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;

import java.io.BufferedInputStream;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;

import represent.www.represent.GeolocationResponder;

/**
 * Created by Brian on 3/6/16.
 */
public class GeocodingAPI extends AsyncTask<String, String, String> {

    final String LAT_LONG_KEY = "latlong";
    final String ZIP_CODE_KEY = "zipcode";
    private final String API_KEY = "AIzaSyCdmql0_jpwP0CskZTo4jt8Ii14VeE_exY";
    private final GeolocationResponder responder;

    public GeocodingAPI(GeolocationResponder responder) {
        this.responder = responder;
    }

    public void getCountyForLocation(Location location) {
        String latitude = new Float(location.getLatitude()).toString();
        String longitude = new Float(location.getLongitude()).toString();
        this.execute(LAT_LONG_KEY, latitude, longitude);
    }

    public void getCountyForZipCode(String zipCode) {
        this.execute(ZIP_CODE_KEY, zipCode);
    }

    String truncateCountyName(String name) {
        String result = name;
        if (name.contains("County")) {
            result = name.replace("County", "");
            result = result.trim();
        }
        return result;
    }

    @Override
    protected String doInBackground(String... params) {
        String req_params = "";
        if (params[0] == LAT_LONG_KEY) {
            Log.d("phone", "Getting geolocation for location...");
            req_params = "latlng=" + params[1] + "," + params[2] + "&key=" + API_KEY;
        }
        else if (params[0] == ZIP_CODE_KEY) {
            Log.d("phone", "Getting geolocation for zip code: " + params[1]);
            req_params = "address=" + params[1] + "&components=country:us&key=" + API_KEY;
        }

        InputStream in;
        String result = "";

        try {
            URL url = new URL("https://maps.googleapis.com/maps/api/geocode/json?" + req_params);
            Log.d("api", "Sending GET to " + url.toString());
            HttpURLConnection urlConnection = (HttpURLConnection) url.openConnection();
            in = new BufferedInputStream(urlConnection.getInputStream());
            BufferedReader reader = new BufferedReader(new InputStreamReader(in));
            String line;
            StringBuilder resultBuilder = new StringBuilder();
            while((line = reader.readLine()) != null) {
                resultBuilder.append(line);
            }
            result = resultBuilder.toString();
        }
        catch(IOException e) {
            Log.d("api", "Problem with API call: " + e.getMessage());
        }

        return result;
    }

    @Override
    protected void onPostExecute(String response) {
        super.onPostExecute(response);

        List<String> counties = new ArrayList<String>();
        List<String> states = new ArrayList<String>();

        JsonParser parser = new JsonParser();
        JsonObject responseJson = parser.parse(response).getAsJsonObject();
        Log.d("api", "Received Geocoding response...");
        JsonArray results = responseJson.getAsJsonArray("results");
        for (JsonElement result : results) {
            JsonArray addressComponents = result.getAsJsonObject().getAsJsonArray("address_components");
            for (JsonElement addressComponent : addressComponents) {
                JsonArray types = addressComponent.getAsJsonObject().getAsJsonArray("types");
                for (JsonElement type : types) {
                    if (type.toString().replaceAll("^\"|\"$", "").equals("administrative_area_level_2")) {
                        counties.add(truncateCountyName(addressComponent.getAsJsonObject().get("long_name").getAsString()));
                    }
                    if (type.toString().replaceAll("^\"|\"$", "").equals("administrative_area_level_1")) {
                        states.add(addressComponent.getAsJsonObject().get("long_name").getAsString());
                    }
                }
            }
        }

        responder.onGeolocationFetched(counties, states);
    }
}
