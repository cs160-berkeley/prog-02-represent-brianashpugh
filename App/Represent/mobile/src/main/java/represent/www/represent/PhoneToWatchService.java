package represent.www.represent;

import android.app.Service;
import android.content.Intent;
import android.os.Bundle;
import android.os.IBinder;
import android.util.Log;

import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.wearable.MessageApi;
import com.google.android.gms.wearable.Node;
import com.google.android.gms.wearable.NodeApi;
import com.google.android.gms.wearable.Wearable;

import java.util.ArrayList;
import java.util.List;

public class PhoneToWatchService extends Service implements GoogleApiClient.ConnectionCallbacks {

    private GoogleApiClient apiClient;

    @Override
    public void onCreate() {
        super.onCreate();
        Log.d("phone", "PhoneToWatch onCreate called...");
        apiClient = new GoogleApiClient.Builder(this)
                .addApi( Wearable.API )
                .addConnectionCallbacks(this)
                .build();
        apiClient.connect();
        Log.d("phone", "connected...");
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        apiClient.disconnect();
    }

    @Override
    public int onStartCommand(final Intent intent, int flags, int startId) {
        Log.d("phone", "PhoneToWatch onStartCommand called...");

        final boolean repChosen = intent.getBooleanExtra("REP_CHOSEN", false);

        new Thread(new Runnable() {
            @Override
            public void run() {
                apiClient.connect();
                Log.d("phon", "PhoneToWatch service connected...");
                if (repChosen) {
                    List<String> repData = new ArrayList<String>();
                    sendMessage("REP_BYTES", intent.getStringExtra("REP_BYTES"));
                }
                else {
                    Log.d("phone", "sending location update to watch...");
                    sendMessage("/location", intent.getStringExtra("LOCATION"));
                }
            }
        }).start();

        return START_STICKY;
    }

    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }

    private void sendMessage( final String path, final String text ) {
        new Thread( new Runnable() {
            @Override
            public void run() {
                NodeApi.GetConnectedNodesResult nodes = Wearable.NodeApi.getConnectedNodes(apiClient).await();
                for(Node node : nodes.getNodes()) {
                    MessageApi.SendMessageResult result = Wearable.MessageApi.sendMessage(
                            apiClient, node.getId(), path, text.getBytes() ).await();
                }
            }
        }).start();
    }

    @Override
    public void onConnected(Bundle bundle) {

    }

    @Override
    public void onConnectionSuspended(int i) {

    }
}
