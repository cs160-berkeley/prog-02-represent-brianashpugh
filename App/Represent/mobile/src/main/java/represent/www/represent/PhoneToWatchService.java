package represent.www.represent;

import android.app.Service;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.os.IBinder;
import android.util.Log;

import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.common.api.PendingResult;
import com.google.android.gms.wearable.Asset;
import com.google.android.gms.wearable.DataApi;
import com.google.android.gms.wearable.DataMap;
import com.google.android.gms.wearable.MessageApi;
import com.google.android.gms.wearable.Node;
import com.google.android.gms.wearable.NodeApi;
import com.google.android.gms.wearable.PutDataMapRequest;
import com.google.android.gms.wearable.PutDataRequest;
import com.google.android.gms.wearable.Wearable;

import java.io.ByteArrayOutputStream;
import java.util.ArrayList;
import java.util.List;

import represent.www.represent.models.Representative;
import represent.www.represent.models.WatchRep;

public class PhoneToWatchService extends Service implements GoogleApiClient.ConnectionCallbacks {

    private GoogleApiClient apiClient;
    private Intent intent;

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

    // This is called upon connecting

    @Override
    public int onStartCommand(final Intent intent, int flags, int startId) {
        Log.d("phone", "PhoneToWatch onStartCommand called...");
        this.intent = intent;
        if (apiClient.isConnected()) {
            apiClient.reconnect();
        }
        if (intent != null) {
            apiClient.connect();
        }
        return START_REDELIVER_INTENT;
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
        Log.d("phone", "PhoneToWatch connected...");
        if (intent != null) {
            final boolean repsLoaded = intent.getBooleanExtra("REPS_LOADED", false);
            final boolean repChosen = intent.getBooleanExtra("REP_CHOSEN", false);

            final List<WatchRep> watchReps = new ArrayList<>();

            if (repsLoaded) {
                final List<String> repIds = intent.getStringArrayListExtra("REP_IDS");
                for (String repId : repIds) {
                    watchReps.add((WatchRep) intent.getParcelableExtra(repId.toString()));
                }
                VotingData votingData = intent.getParcelableExtra("VOTING_DATA");
                PutDataMapRequest putDataMapRequest = PutDataMapRequest.create("/reps_loaded");
                DataMap dataMap = putDataMapRequest.getDataMap();
                for (WatchRep watchRep : watchReps) {
                    watchRep.addToDataMap(dataMap);
                }
                dataMap.putStringArrayList("rep_ids", new ArrayList<>(repIds));
                dataMap.putLong("Time", System.currentTimeMillis());
                votingData.addToDataMap(dataMap);
                PutDataRequest putDataReq = putDataMapRequest.asPutDataRequest();
                Log.d("phone", "Putting DataItem from phone...");
                putDataReq.setUrgent();
                PendingResult<DataApi.DataItemResult> pendingResult = Wearable.DataApi.putDataItem(apiClient, putDataReq);
                Log.d("phone", pendingResult.toString());
            } else if (repChosen) {
                Log.d("phone", "sending location update to watch...");
                Log.d("phone", intent.getStringExtra("LOCATION"));
                sendMessage("/location", intent.getStringExtra("LOCATION"));
            }
        }
    }

    @Override
    public void onConnectionSuspended(int i) {

    }

    private void sendImageAsset(Bitmap bitmap) {
        Asset asset = createAssetFromBitmap(bitmap);
        PutDataMapRequest dataMap = PutDataMapRequest.create("/image");
        dataMap.getDataMap().putAsset("profileImage", asset);
        PutDataRequest request = dataMap.asPutDataRequest();
        PendingResult<DataApi.DataItemResult> pendingResult = Wearable.DataApi
                .putDataItem(apiClient, request);
    }

    private Asset createAssetFromBitmap(Bitmap bitmap) {
        final ByteArrayOutputStream byteStream = new ByteArrayOutputStream();
        bitmap.compress(Bitmap.CompressFormat.JPEG, 20, byteStream);
        return Asset.createFromBytes(byteStream.toByteArray());
    }


}
