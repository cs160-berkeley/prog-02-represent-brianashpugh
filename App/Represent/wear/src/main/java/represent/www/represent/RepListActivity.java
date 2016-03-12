package represent.www.represent;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;
import android.os.Bundle;
import android.support.wearable.activity.WearableActivity;
import android.support.wearable.view.BoxInsetLayout;
import android.support.wearable.view.GridViewPager;
import android.util.Log;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import com.twitter.sdk.android.Twitter;
import com.twitter.sdk.android.core.TwitterAuthConfig;
import io.fabric.sdk.android.Fabric;
import represent.www.represent.models.VotingData;
import represent.www.represent.models.WatchRep;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Locale;

/**
 *
 */

public class RepListActivity extends WearableActivity {

    // Note: Your consumer key and secret should be obfuscated in your source code before shipping.
    private static final String TWITTER_KEY = "DSEXyubyFF9ExuKHiTvaogNyc";
    private static final String TWITTER_SECRET = "rv7LiLmROktaFaylP30FvjSLJZc80yplMZbEZYHOypP4HIL2CH";


    GridViewPager gridViewPager;
    private SensorManager mSensorManager;
    private ShakeEventListener mSensorListener;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_representatives_grid);

        Intent intent = getIntent();
        VotingData votingData = intent.getParcelableExtra("VOTING_DATA");
        List<String> repIds = intent.getStringArrayListExtra("rep_ids");
        List<WatchRep> watchReps = new ArrayList<>();
        for (String repId : repIds) {
            watchReps.add((WatchRep) intent.getParcelableExtra(repId));
        }

        gridViewPager = (GridViewPager) findViewById(R.id.pager);
        gridViewPager.setAdapter(new RepresentativeGridPagerAdapter(gridViewPager, watchReps, votingData));

        mSensorManager = (SensorManager) getSystemService(Context.SENSOR_SERVICE);
        mSensorListener = new ShakeEventListener();

        Log.d("bri", "ShakeEventListener attached...");

        mSensorListener.setOnShakeListener(new ShakeEventListener.OnShakeListener() {

            public void onShake() {
                Log.d("bri", "onShake fired...");
                RepresentativeGridPagerAdapter adapter = (RepresentativeGridPagerAdapter) gridViewPager.getAdapter();
                Intent shakenIntent = new Intent(getBaseContext(), WatchToPhoneService.class);
                shakenIntent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                shakenIntent.putExtra(WatchToPhoneService.SHAKE_FLAG, true);
                startService(shakenIntent);
            }
        });
    }

    @Override
    protected void onResume() {
        super.onResume();
        mSensorManager.registerListener(mSensorListener,
                mSensorManager.getDefaultSensor(Sensor.TYPE_ACCELEROMETER),
                SensorManager.SENSOR_DELAY_UI);
    }

    @Override
    protected void onPause() {
        mSensorManager.unregisterListener(mSensorListener);
        super.onPause();
    }

}
