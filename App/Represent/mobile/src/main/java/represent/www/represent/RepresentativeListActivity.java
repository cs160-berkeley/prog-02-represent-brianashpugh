package represent.www.represent;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.widget.ListAdapter;
import android.widget.ListView;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;

import represent.www.represent.models.Representative;

public class RepresentativeListActivity extends AppCompatActivity {

    protected List<String> representativeKeys;
    List<String> districts;
    String currentDistrict;
    RepresentativeListAdapter listAdapter;
    private BroadcastReceiver broadcastReceiver;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_representative_list);

        Intent intent = getIntent();
        RepDatabase repDatabase = RepDatabase.getInstance(getBaseContext(), null, null);
        List<Representative> reps = repDatabase.getRepresentatives(intent.getStringArrayListExtra("REP_IDS"));
        ListView representativeListView = (ListView) findViewById(R.id.representative_list_view);
        listAdapter = new RepresentativeListAdapter(this, reps);
        representativeListView.setAdapter(listAdapter);

        final RepresentativeListActivity thisActivity = this;
        broadcastReceiver = new BroadcastReceiver() {

            @Override
            public void onReceive(Context context, Intent intent) {
                thisActivity.getAdapter().repSelected(intent.getStringExtra("REP_ID"));
            }
        };

        IntentFilter filter = new IntentFilter("REP_SELECTED");
        this.registerReceiver(broadcastReceiver, filter);

        TextView locationTextView = (TextView) findViewById(R.id.location_bar);
        if (intent.getStringExtra("LOCATION_TYPE").equals("ZIP_CODE")) {
            locationTextView.setText("Representatives for " + intent.getStringExtra("LOCATION"));
        }
        else if (intent.getStringExtra("LOCATION_TYPE").equals("LOCATION")) {
            locationTextView.setText("Representatives for Current Location");
        }
        else {
            locationTextView.setText("Representatives for Random Location");
        }

    }

    @Override
    protected void onResume() {
        super.onResume();
        Intent intent = getIntent();
        TextView locationTextView = (TextView) findViewById(R.id.location_bar);
        if (intent.getStringExtra("LOCATION_TYPE").equals("ZIP_CODE")) {
            locationTextView.setText("Representatives for " + intent.getStringExtra("LOCATION"));
        }
        else if (intent.getStringExtra("LOCATION_TYPE").equals("LOCATION")) {
            locationTextView.setText("Representatives for Current Location");
        }
        else {
            locationTextView.setText("Representatives for Random Location");
        }
    }

    public RepresentativeListAdapter getAdapter() {
        return this.listAdapter;
    }

    @Override
    protected void onPause() {
        super.onPause();
        if (broadcastReceiver != null){
            unregisterReceiver(broadcastReceiver);
            Log.w("phone", "playerReceiver has been unregistered...");
            broadcastReceiver = null;
        }
    }

    @Override
    protected void onRestart() {
        super.onRestart();

        final RepresentativeListActivity thisActivity = this;
        broadcastReceiver = new BroadcastReceiver() {

            @Override
            public void onReceive(Context context, Intent intent) {
                thisActivity.getAdapter().repSelected(intent.getStringExtra("REP_ID"));
            }
        };

        IntentFilter filter = new IntentFilter("REP_SELECTED");
        this.registerReceiver(broadcastReceiver, filter);
    }
}
