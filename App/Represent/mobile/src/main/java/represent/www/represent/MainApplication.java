package represent.www.represent;

import android.app.Application;
import android.util.Log;

import com.twitter.sdk.android.Twitter;
import com.twitter.sdk.android.core.TwitterAuthConfig;

import io.fabric.sdk.android.Fabric;

/**
 * Created by Brian on 3/8/16.
 */
public class MainApplication extends Application {

    private static final String TWITTER_KEY = "RzZXVAvKiRPcMboeQY0w3AaPZ";
    private static final String TWITTER_SECRET = "ynZ0CuamlwjbKLKAXOmAxwTCI1fQHXxtrqMzo9SVzPTHcvxExI";
    private RandomLocationUtil randomLocationUtil;

    @Override
    public void onCreate() {
        Log.d("phone", "Authorizing Twitter Fabric...");
        TwitterAuthConfig authConfig = new TwitterAuthConfig(TWITTER_KEY, TWITTER_SECRET);
        Fabric.with(this, new Twitter(authConfig));
        super.onCreate();
        this.randomLocationUtil = new RandomLocationUtil(getBaseContext());
    }

    public RandomLocationUtil getRandomLocationUtil() {
        return randomLocationUtil;
    }
}
