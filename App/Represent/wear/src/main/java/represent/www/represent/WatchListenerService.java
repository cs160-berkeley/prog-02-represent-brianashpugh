package represent.www.represent;

import android.content.Intent;
import android.util.Log;

import com.google.android.gms.wearable.MessageEvent;
import com.google.android.gms.wearable.WearableListenerService;

import java.nio.charset.StandardCharsets;

public class WatchListenerService extends WearableListenerService {
    private static final String ZIP_CODE_FEED = "/zip_code";
    private static final String LOCATION_FEED = "/location";

    @Override
    public void onMessageReceived(MessageEvent messageEvent) {
        Log.d("bri", "in WatchListenerService, got: " + messageEvent.getPath());

        if( messageEvent.getPath().equalsIgnoreCase( ZIP_CODE_FEED ) ) {

            String value = new String(messageEvent.getData(), StandardCharsets.UTF_8);
            Intent intent = new Intent(this, MainActivity.class);
            intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
            intent.putExtra("ZIP_CODE", "ZIP_CODE");
            Log.d("watch", "received zip code from phone, starting MainActivity...");
            startActivity(intent);

        } else if (messageEvent.getPath().equalsIgnoreCase( LOCATION_FEED )) {
            Log.d("T", "starting main activity for location");

            String value = new String(messageEvent.getData(), StandardCharsets.UTF_8);
            Intent intent = new Intent(this, MainActivity.class );
            intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
            intent.putExtra("LOCATION", "LOCATION");
            startActivity(intent);

        } else {
            super.onMessageReceived( messageEvent );
        }

    }
}