package represent.www.represent;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.location.Location;
import android.util.Log;

import com.twitter.sdk.android.core.TwitterSession;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import represent.www.represent.api.GetSunlightReps;
import represent.www.represent.api.RepsResponder;
import represent.www.represent.models.SunlightRep;
import represent.www.represent.api.TwitterAPI;
import represent.www.represent.api.TwitterResponse;
import represent.www.represent.models.HouseRepresentative;
import represent.www.represent.models.Representative;
import represent.www.represent.models.Senator;

/**
 * Created by Brian on 2/29/16.
 */
public class RepDatabase {

    private static final String QUERY_TYPE_LOCATION = "LOCATION";
    private static final String QUERY_TYPE_ZIP = "ZIP";
    private static RepDatabase instance;
    private final Context context;
    Map<String, Representative> repMap;
    Map<String, List<String>> zipToRepIds;
    Map<String, List<Senator>> senatorsByState;
    GetSunlightReps getSunlightReps;
    RepsResponder repsResponder;
    private String latestQueryType;
    private String lastZipCode;
    private Location lastLocation;
    private TwitterAPI twitterApi;
    private List<String> currentRepIds;
    int twitterResponsesRemaining;

    public static RepDatabase getInstance(Context context, TwitterSession twitterSession, RepsResponder repsResponder) {
        if (instance == null) {
            instance = new RepDatabase(context, twitterSession, repsResponder);
            return instance;
        }
        if (repsResponder != null) {
            instance.repsResponder = repsResponder;
        }
        if (context != null && twitterSession != null) {
            instance.twitterApi = new TwitterAPI(twitterSession, context, instance);
        }
        return instance;
    }

    private RepDatabase(Context context, TwitterSession twitterSession, RepsResponder repsResponder) {
        this.repMap = new HashMap<>();
        this.zipToRepIds = new HashMap<>();
        this.context = context;
        this.repsResponder = repsResponder;
        this.twitterApi = new TwitterAPI(twitterSession, context, this);
    }

    public List<Representative> getRepresentatives(List<String> repIds) {
        List<Representative> reps = new ArrayList<>();
        for (String repId : repIds) {
            Representative rep = repMap.get(repId);
            if (rep != null) {
                reps.add(rep);
            }
            else {
                Log.d("api", "Map returned a null Representative...");
            }

        }
        return reps;
    }

    public Representative getRepresentative(String repId) {
        Representative rep = repMap.get(repId);
        if (rep != null) {
             return rep;
        }
        else {
            Log.d("api", "Map returned a null Representative...");
            return null;
        }
    }

    public void fetchRepresentatives(String zipCode) {
        if (zipToRepIds.containsKey(zipCode)) {
            // The zip is cached
            Log.d("api", "Reps cached. Loading...");
            List<String> repIds = zipToRepIds.get(zipCode);
            repsResponder.onRepsFetched(repIds);
        }
        else {
            Log.d("api", "Reps not in cache. Hitting Sunlight API...");
            this.latestQueryType = QUERY_TYPE_ZIP;
            this.lastZipCode = zipCode;
            getSunlightReps = new GetSunlightReps(this, GetSunlightReps.RESPONSE_TYPE_ZIP, null, zipCode);
            getSunlightReps.getRepresentatives(zipCode);
        }
    }

    public void fetchRepresentatives(Location location) {
        this.latestQueryType = QUERY_TYPE_LOCATION;
        this.lastLocation = location;
        getSunlightReps = new GetSunlightReps(this, GetSunlightReps.RESPONSE_TYPE_LOCATION, location, null);
        getSunlightReps.getRepresentatives(location);
    }

    public void processSunlightResponse(List<SunlightRep> reps, String responseType, String zipCode, Location location) {
        List<String> repIds = new ArrayList<>();
        twitterResponsesRemaining = reps.size();

        if (responseType.equals(GetSunlightReps.RESPONSE_TYPE_ZIP)) {
            for (SunlightRep sunlightRep : reps) {
                Representative rep = repFromSunlightRep(sunlightRep);
                repMap.put(rep.getId(), rep);
                String repId = rep.getId();
                repIds.add(repId);
                Log.d("phone", "twitter handle: " + rep.getTwitterHandle());
                if (rep.getTwitterHandle() != null) {
                    twitterApi.getTwitterForId(repId, rep.getTwitterHandle());
                }
                else {
                    Drawable drawable = context.getResources().getDrawable(R.drawable.congress_circle, null);
                    Bitmap userImage = ((BitmapDrawable) drawable).getBitmap();
                    Bitmap userImageSmaller = Bitmap.createScaledBitmap(userImage, userImage.getWidth() / 2, userImage.getHeight() / 2, false);
                    TwitterResponse response = new TwitterResponse();
                    response.setImage(userImageSmaller);
                    processTwitterResponse(repId, response);
                }
            }
            zipToRepIds.put(zipCode, repIds);
            currentRepIds = repIds;
        }
        else { //responseType.equals(GetSunlightReps.RESPONSE_TYPE_LOCATION)
            for (SunlightRep sunlightRep : reps) {
                Representative rep = repFromSunlightRep(sunlightRep);
                repMap.put(rep.getId(), rep);
                String repId = rep.getId();
                repIds.add(repId);
                Log.d("phone", "twitter handle: " + rep.getTwitterHandle());
                if (rep.getTwitterHandle() != null) {
                    twitterApi.getTwitterForId(repId, rep.getTwitterHandle());
                }
                else {
                    Drawable drawable = context.getResources().getDrawable(R.drawable.congress_circle, null);
                    Bitmap userImage = ((BitmapDrawable) drawable).getBitmap();
                    Bitmap userImageSmaller = Bitmap.createScaledBitmap(userImage, userImage.getWidth() / 2, userImage.getHeight() / 2, false);
                    TwitterResponse response = new TwitterResponse();
                    response.setImage(userImageSmaller);
                    processTwitterResponse(repId, response);
                }
            }
            currentRepIds = repIds;
        }
    }

    private Representative repFromSunlightRep(SunlightRep sunlightRep) {
        if (sunlightRep.getChamber().equals("house")) {
            return HouseRepresentative.fromSunlightRep(sunlightRep);
        } else {
            return Senator.fromSunlightRep(sunlightRep);
        }
    }

    public void processTwitterResponse(String repId, TwitterResponse response) {
        if (response != null) {
            Representative rep = repMap.get(repId);
            rep.setImage(response.getImage());
            rep.setLatestTweet(response.getLastTweet());
            Log.d("api", "Got Twitter response, " + twitterResponsesRemaining + " remaining...");
        }
        twitterResponsesRemaining--;
        if (twitterResponsesRemaining == 0) {
            this.repsResponder.onRepsFetched(currentRepIds);
        }
    }
}
