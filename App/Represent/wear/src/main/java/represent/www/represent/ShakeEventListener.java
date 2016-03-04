package represent.www.represent;

import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.util.Log;


/**
 * Class is from http://stackoverflow.com/questions/2317428/android-i-want-to-shake-it
 */
public class ShakeEventListener implements SensorEventListener {


    /** Time when the last movement started. */
    private long mLastTime;

    /** The last x position. */
    private float lastX = 0;

    /** The last y position. */
    private float lastY = 0;

    /** The last z position. */
    private float lastZ = 0;

    /** OnShakeListener that is called when shake is detected. */
    private OnShakeListener mShakeListener;

    /**
     * Interface for shake gesture.
     */
    public interface OnShakeListener {

        /**
         * Called when shake gesture is detected.
         */
        void onShake();
    }

    public void setOnShakeListener(OnShakeListener listener) {
        mShakeListener = listener;
    }

    @Override
    public void onSensorChanged(SensorEvent se) {
        Log.d("bri", "onSensorChanged fired...");

        // get sensor data
        float x = se.values[0];
        float y = se.values[1];
        float z = se.values[2];

        // calculate movement
        float totalSpeed = Math.abs(x + y + z - lastX - lastY - lastZ);

        Log.d("bri", "totalSpeed: " + totalSpeed);
        
        // get time
        long now = System.currentTimeMillis();
        long timeElapsed = now - mLastTime;

        if (timeElapsed > 100) {
            mLastTime = now;

            // store last sensor data
            lastX = x;
            lastY = y;
            lastZ = z;
            if (totalSpeed > 100) {
                mShakeListener.onShake();
            }
        }
    }

    /**
     * Resets the shake parameters to their default values.
     */
    private void resetShakeParameters() {
        mLastTime = 0;
        lastX = 0;
        lastY = 0;
        lastZ = 0;
    }

    @Override
    public void onAccuracyChanged(Sensor sensor, int accuracy) {
    }

}