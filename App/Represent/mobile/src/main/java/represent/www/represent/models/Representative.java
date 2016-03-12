package represent.www.represent.models;

import android.graphics.Bitmap;
import android.os.Parcelable;

import java.util.List;
import java.util.Map;

/**
 * Created by Brian on 2/29/16.
 */
public interface Representative extends Parcelable {

    public enum Party {
        REPUBLICAN, DEMOCRAT, INDEPENDENT
    }

    public void setImage(Bitmap image);

    public String getTitledAbbrevName();

    public String getName();

    public String getTitle();

    public Party getParty();

    public String getPartyString();

    public String getWebsiteURL();

    public String getEmailAddress();

    public String getTwitterHandle();

    public String getLatestTweet();

    public void setName(String name);

    public void setParty(Party party);

    public void setWebsiteURL(String websiteURL);

    public void setTwitterHandle(String twitterHandle);

    public void setLatestTweet(String latestTweet);

    public void setId(String id);

    public String getId();

    public Bitmap getCoverImage();

    public String getTermEndText();

    public WatchRep toWatchRep();

    public void setCoverImage(Bitmap coverImage);

    public Bitmap getCircleImage();

    public String toString();

    public List<String> getCommittees();

    public void setCommittees(List<String> committees);

    public String getStateAbbrev();

    public String getState();

    public List<String> getBills();

    public void setBills(List<String> bills);

    public List<String> getBillDates();

    public List<String> getBillDatesHuman();

    public void setBillDates(List<String> billDates);

}
