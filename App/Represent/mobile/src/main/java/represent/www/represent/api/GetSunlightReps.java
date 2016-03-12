package represent.www.represent.api;

import android.location.Location;
import android.os.AsyncTask;
import android.util.Log;

import com.google.gson.Gson;
import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonParser;
import com.google.gson.reflect.TypeToken;

import java.io.BufferedInputStream;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.lang.reflect.Type;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;

import represent.www.represent.RepDatabase;
import represent.www.represent.models.Representative;
import represent.www.represent.models.SunlightRep;

/**
 * Created by Brian on 3/4/16.
 */
public class GetSunlightReps extends AsyncTask<String, String, List<SunlightRep>> {

    private final String API_KEY = "e46a3b64064f4d1cadcbb0d9ab8a59c5";
    public static final String RESPONSE_TYPE_ZIP = "ZIP";
    public static final String RESPONSE_TYPE_LOCATION = "LOCATION";
    private static final String LOCATE_LEGISLATORS_URL = "http://congress.api.sunlightfoundation.com/legislators/locate";
    private static final String COMMITTEES_URL = "http://congress.api.sunlightfoundation.com/committees";
    private static final String BILLS_URL = "http://congress.api.sunlightfoundation.com/bills";
    RepDatabase repDatabase;
    String responseType;
    Location location;
    String zipCode;

    public GetSunlightReps(RepDatabase repDatabase, String responseType, Location location, String zipCode) {
        this.repDatabase = repDatabase;
        this.responseType = responseType;
        this.location = location;
        this.zipCode = zipCode;
    }

    public void getRepresentatives(String zipCode) {
        this.responseType = RESPONSE_TYPE_ZIP;
        this.zipCode = zipCode;
        String legislatorsUrl = LOCATE_LEGISLATORS_URL + "?zip=" + zipCode;
        this.execute(legislatorsUrl);
    }

    public void getRepresentatives(Location location) {
        this.responseType = RESPONSE_TYPE_LOCATION;
        this.location = location;
        String latitude = ((Double) location.getLatitude()).toString();
        String longitude = ((Double) location.getLongitude()).toString();
        String legislatorsUrl = LOCATE_LEGISLATORS_URL + "?latitude=" + latitude + "&longitude=" + longitude;
        this.execute(legislatorsUrl);
    }

    @Override
    protected List<SunlightRep> doInBackground(String... params) {
        Log.d("api", "Executing task to get Sunlight Reps...");
        String legislatorsUrl = params[0];
        String result = hitUrl(legislatorsUrl);
        JsonParser parser = new JsonParser();
        List<SunlightRep> reps = new ArrayList<>();
        JsonArray results = parser.parse(result).getAsJsonObject().get("results").getAsJsonArray();
        Gson gson = new Gson();
        for (JsonElement element : results) {
            reps.add(gson.fromJson(element.getAsJsonObject(), SunlightRep.class));
        }

        for (SunlightRep rep : reps) {
            String bills = hitUrl(BILLS_URL + "?sponsor_id=" + rep.getId() + "&apikey=" + API_KEY);
            Log.d("api", "Getting bills for url: " + BILLS_URL + "?sponsor_id=" + rep.getId() + "&apikey=" + API_KEY);
            String committees = hitUrl(COMMITTEES_URL + "?member_ids=" + rep.getId() + "&apikey=" + API_KEY);
            Log.d("api", "Getting committees for url: " + COMMITTEES_URL + "?member_ids=" + rep.getId() + "&apikey=" + API_KEY);
            List<String> billNames = new ArrayList<>();
            List<String> billDates = new ArrayList<>();
            JsonArray jsonBills = parser.parse(bills).getAsJsonObject().get("results").getAsJsonArray();
            try {
                for (JsonElement jsonBill : jsonBills) {
                    String name;
                    JsonElement shortName = jsonBill.getAsJsonObject().get("short_title");
                    if (!shortName.isJsonNull()) {
                        name = shortName.getAsString();
                    }
                    else{
                        name = jsonBill.getAsJsonObject().get("official_title").getAsString();
                    }
                    billNames.add(name);
                    billDates.add(jsonBill.getAsJsonObject().get("last_action_at").getAsString());
                }
                List<String> committeeNames = new ArrayList<>();
                JsonArray jsonCommittees = parser.parse(committees).getAsJsonObject().get("results").getAsJsonArray();
                for (JsonElement jsonCommittee : jsonCommittees) {
                    committeeNames.add(jsonCommittee.getAsJsonObject().get("name").getAsString());
                }
                rep.setBills(billNames);
                rep.setBillDates(billDates);
                rep.setCommittees(committeeNames);
            }
            catch (UnsupportedOperationException e) {
                Log.d("api", "Problem parsing Json: " + bills + "\n" + committees);
                e.printStackTrace();
            }
        }
        Log.d("api", "Sunlight API returned " + reps.size() + " reps...");
        return reps;
    }

    private String hitUrl(String baseUrl) {
        try {
            URL url = new URL(baseUrl + "&apikey=" + API_KEY);
            HttpURLConnection urlConnection = (HttpURLConnection) url.openConnection();
            //Log.d("api", "URL connection opened...");
            InputStream in = new BufferedInputStream(urlConnection.getInputStream());
            BufferedReader reader = new BufferedReader(new InputStreamReader(in));
            String line;
            StringBuilder resultBuilder = new StringBuilder();
            while ((line = reader.readLine()) != null) {
                resultBuilder.append(line);
            }
            return resultBuilder.toString();
        }
        catch (MalformedURLException e) {
            Log.d("api", "Malformed URL: " + e.getMessage());
            return "";
        }
        catch (IOException e) {
            Log.d("api", "Problem with API call: " + e.getMessage());
            return "";
        }
    }

    class Result<T> {
        List<T> results;
    }

    @Override
    protected void onPostExecute(List<SunlightRep> sunlightReps) {
        super.onPostExecute(sunlightReps);

        this.repDatabase.processSunlightResponse(sunlightReps, responseType, zipCode, location);
    }
}
