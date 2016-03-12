package represent.www.represent;

import android.content.Context;
import android.content.Intent;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.KeyEvent;
import android.view.View;
import android.view.inputmethod.EditorInfo;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.common.api.GoogleApiClient.ConnectionCallbacks;
import com.google.android.gms.common.api.GoogleApiClient.OnConnectionFailedListener;
import com.google.android.gms.location.LocationServices;
import com.twitter.sdk.android.core.Callback;
import com.twitter.sdk.android.core.Result;
import com.twitter.sdk.android.core.TwitterException;
import com.twitter.sdk.android.core.TwitterSession;
import com.twitter.sdk.android.core.identity.TwitterAuthClient;

import java.util.ArrayList;
import java.util.List;

import represent.www.represent.api.GeocodingAPI;
import represent.www.represent.api.RepsResponder;
import represent.www.represent.api.VotingDataAPI;
import represent.www.represent.models.Representative;
import represent.www.represent.models.WatchRep;

public class LocationInputActivity extends AppCompatActivity implements
        LocationListener, GeolocationResponder, RepsResponder,
        ConnectionCallbacks, OnConnectionFailedListener {

    RepDatabase repDatabase;
    GeocodingAPI geocodingApi;
    private VotingDataAPI votingDataApi;
    VotingData votingData;
    List<String> repIds;
    Location mLastLocation;
    TwitterSession twitterSession;
    boolean locationFetched = false;

    private boolean repsFetched;
    private boolean votingFetched;
    private boolean zipCodeUsed = false;
    private String county;
    private String state;
    private GoogleApiClient mGoogleApiClient;
    private String zipCode;
    private boolean receivedShake;
    private TwitterAuthClient twitterAuthClient;
    private boolean currentLocationClicked = false;
    private boolean onConnectedRan;
    private boolean authInProgress;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_location_input);

        onConnectedRan = false;
        final LocationInputActivity thisActivity = this;
        currentLocationClicked = false;

        LocationManager manager = (LocationManager) this.getSystemService(Context.LOCATION_SERVICE);
        if (!manager.isProviderEnabled(LocationManager.GPS_PROVIDER) && !manager.isProviderEnabled(LocationManager.NETWORK_PROVIDER)) {
            Toast.makeText(this, "Enable location services for accurate data", Toast.LENGTH_SHORT).show();
        }

        Log.d("phone", "Setting up Twitter login...");
        if (twitterAuthClient == null || twitterSession == null) {
            this.twitterAuthClient = new TwitterAuthClient();
            if (!authInProgress) {
                authInProgress = true;
                this.twitterAuthClient.authorize(this, new Callback<TwitterSession>() {
                    @Override
                    public void success(Result<TwitterSession> result) {
                        Log.d("api", "Twitter login success: " + result.data.toString());
                        authInProgress = false;
                        thisActivity.twitterSession = result.data;
                        thisActivity.onTwitterLoginSuccessful();
                    }

                    @Override
                    public void failure(TwitterException e) {
                        Log.d("api", "Twitter login failed: " + e.getMessage());
                    }
                });
            }
        }
        else {
            Log.d("api", "Skipped Twitter login...");
            thisActivity.onTwitterLoginSuccessful();
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        this.twitterAuthClient.onActivityResult(requestCode, resultCode, data);
    }

    protected void onTwitterLoginSuccessful() {

        final LocationInputActivity thisActivity = this;
        this.repDatabase = RepDatabase.getInstance(this, this.twitterSession, this);
        this.votingDataApi = new VotingDataAPI(this);

        this.votingFetched = false;
        this.repsFetched = false;

        // Intercept if we need a random location from a shake
        Intent intent = getIntent();
        this.receivedShake = false;
        if (intent != null) {
            if (intent.getBooleanExtra("SHAKE", false)) {
                Log.d("phone", "LocationInputActivity received a shake...");
                receivedShake = true;
                MainApplication application = (MainApplication) getApplication();
                this.onLocationFetched(false, null, application.getRandomLocationUtil().getRandomLocation());
                return;
            }
        }

        Button byCurrentLocationButton = (Button) findViewById(R.id.by_current_location_button);
        byCurrentLocationButton.setTransformationMethod(null);

        byCurrentLocationButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Get current location, then figure out the county
                onConnectedRan = false;
                if (!currentLocationClicked) {
                    zipCodeUsed = false;
                    locationFetched = false;
                    currentLocationClicked = true;
                    if (thisActivity.mGoogleApiClient == null) {
                        thisActivity.mGoogleApiClient = new GoogleApiClient.Builder(thisActivity)
                                .addApi(LocationServices.API)
                                .addConnectionCallbacks(thisActivity)
                                .addOnConnectionFailedListener(thisActivity)
                                .build();
                    }
                    Log.d("api", "Connecting to Google Play Services...");
                    thisActivity.mGoogleApiClient.connect();
                }
            }
        });

        EditText byZipCodeField = (EditText) findViewById(R.id.by_zip_code_field);
        byZipCodeField.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View v, boolean hasFocus) {
                if (hasFocus) {
                    EditText byZipCodeField = (EditText) v;
                    byZipCodeField.setText("");
                }
            }
        });

        // ON ZIP CODE ENTERED

        byZipCodeField.setImeActionLabel("Find", KeyEvent.KEYCODE_ENTER);
        byZipCodeField.setOnEditorActionListener(new TextView.OnEditorActionListener() {
            @Override
            public boolean onEditorAction(TextView v, int actionId, KeyEvent event) {
                if (actionId == 66 || (event != null && event.getAction() == KeyEvent.KEYCODE_ENTER) || actionId == EditorInfo.IME_ACTION_DONE) {
                    onConnectedRan = false;
                    thisActivity.zipCodeUsed = true;
                    thisActivity.onZipCodeEntered(v.getText().toString());
                    locationFetched = false;
                }
                return true;
            }
        });
    }

    @Override
    public void onConnected(Bundle bundle) {
        try {

            if (locationFetched == false && !onConnectedRan) {
                onConnectedRan = true;

                Log.d("phone", "Getting location from Google Play API...");
                mLastLocation = LocationServices.FusedLocationApi.getLastLocation(
                        mGoogleApiClient);
                Log.d("api", "Location fetched: " + mLastLocation);
                if (mLastLocation != null) {
                    this.onLocationFetched(false, null, mLastLocation);
                }
                mGoogleApiClient.disconnect();
            }
        }
        catch (SecurityException e) {
            Log.d("api", "Insufficient permissions for getting location: " + e.getMessage());
        }
    }

    protected void onZipCodeEntered(String zipCode) {
        this.zipCode = zipCode;
        onLocationFetched(true, zipCode, null);
    }

    public void onLocationFetched(boolean zipCodeUsed, String zipCode, Location location) {
        locationFetched = true;
        this.geocodingApi = new GeocodingAPI(this);
        if (zipCodeUsed) {
            Log.d("api", "Location fetched: " + zipCode);
            repDatabase.fetchRepresentatives(zipCode);
            this.geocodingApi.getCountyForZipCode(zipCode);
        }
        else {
            Log.d("api", "Location fetched: " + ((Double) location.getLatitude()).toString() + ", " + ((Double) location.getLongitude()).toString() + "...");
            repDatabase.fetchRepresentatives(location);
            geocodingApi.getCountyForLocation(location);
        }
    }

    @Override
    public void onGeolocationFetched(List<String> counties, List<String> states) {
        if (counties.size() > 0 && states.size() > 0) {
            this.county = counties.get(0);
            this.state = states.get(0);
            Log.d("api", "Fetched geolocation: " + this.county + ", " + this.state);
            this.votingData = votingDataApi.getData(county);
            this.votingFetched = true;
            ApisFinished();
        }
    }

    public void onRepsFetched(List<String> repIds) {
        this.repsFetched = true;
        this.repIds = repIds;
        Log.d("api", "Fetched " + ((Integer) repIds.size()).toString() + " reps...");
        ApisFinished();
    }

    void ApisFinished() {
        if (this.votingFetched && this.repsFetched) {
            Log.d("api", "Data fetching finished. Sending data to watch...");

            currentLocationClicked = false;

            Intent representativeListIntent = new Intent(this, RepresentativeListActivity.class);
            representativeListIntent.putStringArrayListExtra("REP_IDS", new ArrayList<>(this.repIds));

            Intent watchIntent = new Intent(this, PhoneToWatchService.class);
            watchIntent.putExtra("REPS_LOADED", true);
            watchIntent.putStringArrayListExtra("REP_IDS", new ArrayList<>(this.repIds));
            watchIntent.putExtra("VOTING_DATA", this.votingData);

            List<WatchRep> watchReps = new ArrayList<>();
            ArrayList<Representative> reps = new ArrayList<>();
            for (String repId : this.repIds) {
                Representative rep = repDatabase.getRepresentative(repId);
                WatchRep watchRep = rep.toWatchRep();
                watchIntent.putExtra(repId, watchRep);
            }

            if (receivedShake) {
                representativeListIntent.putExtra("LOCATION_TYPE", "RANDOM");
                receivedShake = false;
            }
            else if (zipCodeUsed) {
                representativeListIntent.putExtra("LOCATION_TYPE", "ZIP_CODE");
                representativeListIntent.putExtra("LOCATION", this.zipCode);
                zipCodeUsed = false;
            }
            else {
                representativeListIntent.putExtra("LOCATION_TYPE", "LOCATION");
            }
            Log.d("phone", "starting PhoneToWatch service...");
            this.votingFetched = false;
            this.repsFetched = false;
            this.startService(watchIntent);
            this.startActivity(representativeListIntent);
        }
    }

    @Override
    public void onLocationChanged(Location location) {

    }

    @Override
    public void onStatusChanged(String provider, int status, Bundle extras) {

    }

    @Override
    public void onProviderEnabled(String provider) {

    }

    @Override
    public void onProviderDisabled(String provider) {

    }

    @Override
    public void onConnectionSuspended(int i) {

    }

    @Override
    public void onConnectionFailed(ConnectionResult connectionResult) {

    }

}
