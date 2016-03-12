package represent.www.represent;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.util.Log;

import com.google.android.gms.wearable.MessageEvent;
import com.google.android.gms.wearable.WearableListenerService;

import java.nio.charset.StandardCharsets;

public class PhoneListenerService extends WearableListenerService {

    //   WearableListenerServices don't need an iBinder or an onStartCommand: they just need an onMessageReceieved.
    public static final String REP_SELECTED_PATH = "/rep_selected";
    private static final String SHAKE_PATH = "/shake";

    @Override
    public void onMessageReceived(MessageEvent messageEvent) {
        Log.d("phone", "in PhoneListenerService, got: " + messageEvent.getPath());
        Context context = getApplicationContext();

        if (messageEvent.getPath().equalsIgnoreCase(REP_SELECTED_PATH)) {

            String repId = new String(messageEvent.getData(), StandardCharsets.UTF_8);

            Intent representativeListIntent = new Intent("REP_SELECTED");
            representativeListIntent.putExtra("REP_ID", repId);
            sendBroadcast(representativeListIntent);

        } else if (messageEvent.getPath().equalsIgnoreCase(SHAKE_PATH)) {
            Log.d("phone", "Received shake message...");
            Intent representativeListIntent = new Intent(context, LocationInputActivity.class);
            representativeListIntent.putExtra("SHAKE", true);
            representativeListIntent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
            startActivity(representativeListIntent);
        } else {
            super.onMessageReceived(messageEvent);
        }
    }

}
