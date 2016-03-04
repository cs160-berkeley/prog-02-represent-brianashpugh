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

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

/**
 *
 */

public class MainActivity extends WearableActivity {

    GridViewPager gridViewPager;
    private SensorManager mSensorManager;
    private ShakeEventListener mSensorListener;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_representatives_grid);

        gridViewPager = (GridViewPager) findViewById(R.id.pager);
        gridViewPager.setAdapter(new RepresentativeGridPagerAdapter(gridViewPager));

        mSensorManager = (SensorManager) getSystemService(Context.SENSOR_SERVICE);
        mSensorListener = new ShakeEventListener();

        Log.d("bri", "ShakeEventListener attached...");

        mSensorListener.setOnShakeListener(new ShakeEventListener.OnShakeListener() {

            public void onShake() {
                Log.d("bri", "onShake fired...");
                RepresentativeGridPagerAdapter adapter = (RepresentativeGridPagerAdapter) gridViewPager.getAdapter();
                adapter.shuffleItems();
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
