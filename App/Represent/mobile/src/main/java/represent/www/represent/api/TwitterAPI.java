package represent.www.represent.api;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.os.AsyncTask;
import android.util.Log;

import com.twitter.sdk.android.Twitter;
import com.twitter.sdk.android.core.Callback;
import com.twitter.sdk.android.core.Result;
import com.twitter.sdk.android.core.Session;
import com.twitter.sdk.android.core.TwitterApiClient;
import com.twitter.sdk.android.core.TwitterAuthConfig;
import com.twitter.sdk.android.core.TwitterException;
import com.twitter.sdk.android.core.internal.TwitterApi;
import com.twitter.sdk.android.core.models.Tweet;
import com.twitter.sdk.android.core.models.User;
import com.twitter.sdk.android.core.services.StatusesService;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import io.fabric.sdk.android.Fabric;
import represent.www.represent.R;
import represent.www.represent.RepDatabase;
import represent.www.represent.api.BasicImageDownloader.OnImageLoaderListener;
import retrofit.http.GET;
import retrofit.http.Query;

/**
 * Created by Brian on 3/7/16.
 */
public class TwitterAPI extends TwitterApiClient implements OnImageLoaderListener {

    private final RepDatabase repDatabase;
    private final Context context;


    public interface UserService {
        @GET("/1.1/users/show.json")
        void getUserByHandle(@Query("screen_name") String handle,
                                  Callback<User> cb);
    }

    // Note: Your consumer key and secret should be obfuscated in your source code before shipping.
    private static final String RESPONSE_TYPE_TWEET = "TWEET";
    private static final String RESPONSE_TYPE_IMAGE = "IMAGE";

    Map<String, TwitterResponse> activeQueries;

    //"profile_image_url"

    public TwitterAPI(Session session, Context context, RepDatabase repDatabase) {
        super(session);
        this.activeQueries = new HashMap<>();
        this.repDatabase = repDatabase;
        this.context = context;
    }

    public UserService getUserService() {
        return getService(UserService.class);
    }


    public void getTwitterForId(final String repId, String handle) {
        Log.d("api", "Hitting Twitter for last tweet and image url...");
        activeQueries.put(repId, new TwitterResponse());
        StatusesService statusesService = this.getStatusesService();
        final TwitterAPI self = this;
        statusesService.userTimeline(null, handle, 1, null, null, false, true, false, false, new Callback<List<Tweet>>() {
            @Override
            public void success(Result<List<Tweet>> result) {
                //result.data.get(0).text
                self.onDataFetched(repId, RESPONSE_TYPE_TWEET, result.data.get(0).text.toString(), null);
            }

            public void failure(TwitterException exception) {
                Log.d("api", "Twitter API failed on Timeline call: " + exception.getMessage());
            }
        });

        final TwitterAPI twitterApi = this;
        UserService userService = this.getUserService();
        userService.getUserByHandle(handle, new Callback<User>() {
            @Override
            public void success(Result<User> result) {
                Log.d("api", "Received avatar url. Forwarding to BasicImageDownloader...");
                BasicImageDownloader downloader = new BasicImageDownloader(twitterApi);
                downloader.download(result.data.profileImageUrl.replace("_normal", ""), repId, false); //"https://pbs.twimg.com/profile_images/430378206353317888/3QKYak-Z.jpeg", repId, false);
            }

            public void failure(TwitterException exception) {
                Log.d("api", "Twitter API failed on User call: " + exception.getMessage());            }
        });

    }

    public void onDataFetched(String repId, String type, String lastTweet, Bitmap userImage) {
        TwitterResponse query = activeQueries.get(repId);
        if (type.equals(this.RESPONSE_TYPE_TWEET)) {
            Log.d("api", "Received tweet from Twitter API...");
            query.setLastTweet(lastTweet);
        }
        else { // type.equals(this.RESPONSE_TYPE_IMAGE)
            Log.d("api", "Received image url from Twitter API...");
            if (userImage == null) {
                Drawable drawable = context.getResources().getDrawable(R.drawable.congress_circle, null);
                Bitmap userImageLarge = ((BitmapDrawable) drawable).getBitmap();
                userImage = Bitmap.createScaledBitmap(userImageLarge, userImageLarge.getWidth() / 2, userImageLarge.getHeight() / 2, false);
            }
            query.setImage(userImage);
        }
        if (query.getLastTweet() != null && query.getImage() != null) {
            this.repDatabase.processTwitterResponse(repId, query);
            activeQueries.remove(repId);
        }
    }

    // METHODS FOR IMAGE DOWNLOADER LISTENER
    @Override
    public void onError(BasicImageDownloader.ImageError error) {
        Log.d("api", "Image Downloader error: " + error.getMessage());
    }

    @Override
    public void onProgressChange(int percent) {
        Log.i("api", "Downloading image: " + percent);
    }

    @Override
    public void onComplete(Bitmap image, String repId) {
        int resolution = 150;
        Bitmap resized = Bitmap.createScaledBitmap(image, resolution, resolution, false);
        this.onDataFetched(repId, RESPONSE_TYPE_IMAGE, null, resized);

    }

}
