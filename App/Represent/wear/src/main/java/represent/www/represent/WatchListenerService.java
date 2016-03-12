package represent.www.represent;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.util.Log;

import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.wearable.Asset;
import com.google.android.gms.wearable.DataApi;
import com.google.android.gms.wearable.DataEvent;
import com.google.android.gms.wearable.DataEventBuffer;
import com.google.android.gms.wearable.DataMap;
import com.google.android.gms.wearable.DataMapItem;
import com.google.android.gms.wearable.MessageEvent;
import com.google.android.gms.wearable.Wearable;
import com.google.android.gms.wearable.WearableListenerService;

import java.io.InputStream;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.TimeUnit;

import represent.www.represent.models.VotingData;
import represent.www.represent.models.WatchRep;


public class WatchListenerService extends WearableListenerService {
    private static final String ZIP_CODE_FEED = "/zip_code";
    private static final String LOCATION_FEED = "/location";
    private GoogleApiClient mGoogleApiClient;

    @Override
    public void onDataChanged(DataEventBuffer dataEvents) {
        Log.d("watch", "Received data event...");
        for (DataEvent event : dataEvents) {
            Log.d("dd", event.getDataItem().getUri().getPath());
            if (event.getType() == DataEvent.TYPE_CHANGED &&
                    event.getDataItem().getUri().getPath().equals("/reps_loaded")) {
                Log.d("watch", "Received reps data event...");
                DataMapItem dataMapItem = DataMapItem.fromDataItem(event.getDataItem());
                DataMap dataMap = dataMapItem.getDataMap();
                VotingData votingData = VotingData.fromDataMap(dataMap);
                List<String> repIds = dataMap.getStringArrayList("rep_ids");
                Intent repListIntent = new Intent(this, RepListActivity.class);
                repListIntent.putExtra("VOTING_DATA", votingData);
                for (String repId : repIds) {
                    repListIntent.putExtra(repId.toString(), WatchRep.fromDataMap(dataMap, repId));
                }
                repListIntent.putStringArrayListExtra("rep_ids", new ArrayList<>(repIds));
                repListIntent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                this.startActivity(repListIntent);
            }
        }
    }
}