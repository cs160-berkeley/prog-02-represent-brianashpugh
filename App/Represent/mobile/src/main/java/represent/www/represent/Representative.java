package represent.www.represent;

/**
 * Created by Brian on 2/29/16.
 */
public interface Representative {

    public enum Party {
        REPUBLICAN, DEMOCRAT, INDEPENDENT
    }

    public String getName();

    public String getTitle();

    public Party getParty();

    public String getPartyString();

    public String getWebsiteURL();

    public String getEmailAddress();

    public String getTwitterHandle();

    public String getLatestTweet();

    public int getImageResource();

    public int getCoverImageResource();

    public String toString();

}
