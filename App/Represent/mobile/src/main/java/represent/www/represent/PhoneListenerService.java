package represent.www.represent;

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

            String repName = new String(messageEvent.getData(), StandardCharsets.UTF_8);

            Intent representativeViewIntent = new Intent(context, RepresentativeViewActivity.class);
            representativeViewIntent.putExtra("REP_NAME", repName);
            representativeViewIntent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
            startActivity(representativeViewIntent);

        } else if (messageEvent.getPath().equalsIgnoreCase(SHAKE_PATH)) {
            Log.d("phone", "received shake message...");
            Intent representativeListIntent = new Intent(context, RepresentativeList.class);
            representativeListIntent.putExtra("SHUFFLE", true);
            representativeListIntent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
            startActivity(representativeListIntent);
        } else {
            super.onMessageReceived(messageEvent);
        }
    }

}
