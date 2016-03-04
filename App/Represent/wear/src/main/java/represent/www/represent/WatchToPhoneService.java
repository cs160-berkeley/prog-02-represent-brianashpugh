package represent.www.represent;

import android.app.Service;
import android.content.Intent;
import android.os.Bundle;
import android.os.IBinder;
import android.util.Log;

import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.common.api.ResultCallback;
import com.google.android.gms.wearable.Node;
import com.google.android.gms.wearable.NodeApi;
import com.google.android.gms.wearable.Wearable;

import java.util.ArrayList;
import java.util.List;

public class WatchToPhoneService extends Service implements GoogleApiClient.ConnectionCallbacks {

    private GoogleApiClient watchApiClient;
    private List<Node> nodes = new ArrayList<>();
    public static final String SHAKE_FLAG = "SHAKE";
    public static final String REP_SELECTED_FLAG = "REP_SELECTED";
    Intent intent;

    @Override
    public void onCreate() {
        Log.d("bri", "WatchToPhoneService created...");
        super.onCreate();
        //initialize the googleAPIClient for message passing
        watchApiClient = new GoogleApiClient.Builder(this)
                .addApi(Wearable.API)
                .addConnectionCallbacks(this)
                .build();
        //and actually connect it
        watchApiClient.connect();

    }

    @Override
    public int onStartCommand(final Intent intent, int flags, int startId) {
        Log.d("bri", "onStartCommand fired...");

        new Thread(new Runnable() {
            @Override
            public void run() {
                watchApiClient.connect();
                if (intent != null) {
                    if (intent.getBooleanExtra(SHAKE_FLAG, false)) {
                        Log.d("watch", "sending shake message...");
                        sendMessage("/shake", "shaken");
                    }
                    else if (intent.getBooleanExtra(REP_SELECTED_FLAG, false)) {
                        Log.d("watch", "sending rep selection to phone...");
                        sendMessage("/rep_selected", intent.getStringExtra("REP_NAME"));
                    }
                }
            }
        }).start();

        return START_STICKY;
    }

    @Override //alternate method to connecting: no longer create this in a new thread, but as a callback
    public void onConnected(Bundle bundle) {
        Log.d("bri", "fired onConnected...");
        Wearable.NodeApi.getConnectedNodes(watchApiClient)
                .setResultCallback(new ResultCallback<NodeApi.GetConnectedNodesResult>() {
                    @Override
                    public void onResult(NodeApi.GetConnectedNodesResult getConnectedNodesResult) {
                        nodes = getConnectedNodesResult.getNodes();
                        Log.d("bri", "callback fired in node...");
                        //when we find a connected node, we populate the list declared above
                        //finally, we can send a message
                        if (intent != null && intent.getBooleanExtra(SHAKE_FLAG, false)) {
                            sendMessage("/shake", "shaken");
                        }
                    }
                });
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        watchApiClient.disconnect();
    }


    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }

    @Override
    public void onConnectionSuspended(int i) {}

    private void sendMessage(final String path, final String text ) {
        for (Node node : nodes) {
            Wearable.MessageApi.sendMessage(
                    watchApiClient, node.getId(), path, text.getBytes());
        }
    }

}
