package represent.www.represent.api;

import android.graphics.Bitmap;

/**
 * Created by Brian on 3/7/16.
 */
public class TwitterResponse {

    private Bitmap image;
    private String lastTweet;

    public Bitmap getImage() {
        return image;
    }

    public void setImage(Bitmap image) {
        this.image = image;
    }

    public String getLastTweet() {
        return lastTweet;
    }

    public void setLastTweet(String lastTweet) {
        this.lastTweet = lastTweet;
    }
}
