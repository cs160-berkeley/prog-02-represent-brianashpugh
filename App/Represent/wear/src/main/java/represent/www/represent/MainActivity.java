package represent.www.represent;

import android.app.Activity;
import android.app.ActivityManager;
import android.content.Context;
import android.content.Intent;
import android.hardware.Sensor;
import android.hardware.SensorManager;
import android.os.Bundle;
import android.os.PersistableBundle;
import android.support.wearable.activity.WearableActivity;
import android.util.Log;

import represent.www.represent.ShakeEventListener.OnShakeListener;

/**
 * Created by Brian on 3/9/16.
 */
public class MainActivity extends WearableActivity {

    private SensorManager mSensorManager;
    private ShakeEventListener mSensorListener;

    @Override
    public void onCreate(Bundle savedInstanceState, PersistableBundle persistentState) {
        super.onCreate(savedInstanceState, persistentState);
        setContentView(R.layout.main_activity);

        registerListener();
    }

    protected void registerListener() {
        mSensorManager = (SensorManager) getSystemService(Context.SENSOR_SERVICE);
        mSensorListener = new ShakeEventListener();
        Log.d("bri", "ShakeEventListener attached...");

        mSensorListener.setOnShakeListener(new OnShakeListener() {

            public void onShake() {
                Log.d("bri", "onShake fired...");
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

        if (isMyServiceRunning(WatchListenerService.class)) {
            Log.d("watch", "Had to start listener service from onResume...");
            getBaseContext().startService(new Intent(getBaseContext(), WatchListenerService.class));
        }

        if (mSensorListener == null) {
            registerListener();
            setContentView(R.layout.main_activity);
        }
        Log.d("watch", "onResume: listener is: " + mSensorListener);
        mSensorManager = (SensorManager) getSystemService(Context.SENSOR_SERVICE);
        mSensorManager.registerListener(mSensorListener,
                mSensorManager.getDefaultSensor(Sensor.TYPE_ACCELEROMETER),
                SensorManager.SENSOR_DELAY_UI);
    }

    @Override
    protected void onPause() {
        mSensorManager.unregisterListener(mSensorListener);
        super.onPause();
    }

    private boolean isMyServiceRunning(Class<?> serviceClass) {
        ActivityManager manager = (ActivityManager) getSystemService(Context.ACTIVITY_SERVICE);
        for (ActivityManager.RunningServiceInfo service : manager.getRunningServices(Integer.MAX_VALUE)) {
            if (serviceClass.getName().equals(service.service.getClassName())) {
                return true;
            }
        }
        return false;
    }
}
