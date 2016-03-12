package represent.www.represent.models;

import android.content.res.Resources;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Parcel;
import android.os.Parcelable;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import represent.www.represent.ImageProcessor;
import represent.www.represent.R;
import represent.www.represent.StateUtil;

/**
 * Created by Brian on 2/29/16.
 */
public class Senator implements Representative, Parcelable {

    private String name;
    private Party party;
    private String state;
    private String termEnd;
    private String websiteURL;
    private String emailAddress;
    private String twitterHandle;
    private String latestTweet;
    private Bitmap circleImage;
    private Bitmap coverImage;
    private String id;
    private List<String> committees;
    private List<String> bills;
    private List<String> billDates;

    public Senator(String id,
                   String name,
                   Party party,
                   String state,
                   String termEnd,
                   String websiteURL,
                   String emailAddress,
                   String twitterHandle,
                   String latestTweet,
                   Bitmap image,
                   List<String> committees,
                   List<String> bills,
                   List<String> billDates) {
        this.name = name;
        this.party = party;
        this.state = state;
        this.termEnd = termEnd;
        this.websiteURL = websiteURL;
        this.emailAddress = emailAddress;
        this.twitterHandle = twitterHandle;
        this.latestTweet = latestTweet;
        this.id = id;
        this.committees = committees;
        this.bills = bills;
        this.billDates = billDates;
        if (image != null) {
            ImageProcessor imageProcessor = new ImageProcessor();
            this.coverImage = imageProcessor.cropToCoverSize(image);
            this.circleImage = imageProcessor.maskCircle(image);
        } else {
            this.coverImage = BitmapFactory.decodeResource(Resources.getSystem(), R.drawable.congress_cover);
            this.circleImage = BitmapFactory.decodeResource(Resources.getSystem(), R.drawable.congress_circle);
        }

    }

    public Senator(String id,
                   String name,
                   Party party,
                   String state,
                   String termEnd,
                   String websiteURL,
                   String emailAddress,
                   String twitterHandle,
                   String latestTweet,
                   Bitmap circleImage,
                   Bitmap coverImage,
                   List<String> committees,
                   List<String> bills,
                   List<String> billDates) {
        this.name = name;
        this.party = party;
        this.state = state;
        this.termEnd = termEnd;
        this.websiteURL = websiteURL;
        this.emailAddress = emailAddress;
        this.twitterHandle = twitterHandle;
        this.latestTweet = latestTweet;
        this.id = id;
        this.committees = committees;
        this.bills = bills;
        this.billDates = billDates;
        this.coverImage = coverImage;
        this.circleImage = circleImage;
        if (coverImage == null && circleImage == null) {
            this.coverImage = BitmapFactory.decodeResource(Resources.getSystem(), R.drawable.congress_cover);
            this.circleImage = BitmapFactory.decodeResource(Resources.getSystem(), R.drawable.congress_circle);
        }
    }

    public static Senator fromSunlightRep(SunlightRep sunlightRep) {
        return new Senator(sunlightRep.getId(), sunlightRep.getFirstName() + " " + sunlightRep.getLastName(),
                sunlightRep.getPartyFromEnum(),
                sunlightRep.getState(),
                sunlightRep.getTermEnd(),
                sunlightRep.getWebsiteURL(),
                sunlightRep.getContactForm(),
                sunlightRep.getTwitterId(),
                null,
                null,
                sunlightRep.getCommittees(),
                sunlightRep.getBills(),
                sunlightRep.getBillDates());
    }

    @Override
    public String getName() {
        return name;
    }

    @Override
    public String getTitledAbbrevName() {
        return "Sen. " + name;
    }

    public String getTitle() {
        return "Senator";
    }

    @Override
    public Party getParty() {
        return party;
    }

    public String getPartyString() {
        return this.party == Party.DEMOCRAT ? "Democrat" : party == Party.REPUBLICAN ? "Republican" : "Independent";
    }


    public String getStateAbbrev() {
        return state;
    }

    @Override
    public String getWebsiteURL() {
        return websiteURL;
    }

    @Override
    public String getEmailAddress() {
        return emailAddress;
    }

    @Override
    public String getTwitterHandle() {
        return twitterHandle;
    }

    @Override
    public String getLatestTweet() {
        return latestTweet;
    }

    public String getId() {
        return id;
    }

    public String toString() {
        String div = "$$$";
        String combined = getName() + div + getTitle() + div + getPartyString();
        return combined;
    }

    public void setName(String name) {
        this.name = name;
    }

    public void setParty(Party party) {
        this.party = party;
    }

    public void setWebsiteURL(String websiteURL) {
        this.websiteURL = websiteURL;
    }

    public void setEmailAddress(String emailAddress) {
        this.emailAddress = emailAddress;
    }

    public void setTwitterHandle(String twitterHandle) {
        this.twitterHandle = twitterHandle;
    }

    public void setLatestTweet(String latestTweet) {
        this.latestTweet = latestTweet;
    }

    public void setId(String id) {
        this.id = id;
    }

    public Bitmap getCoverImage() {
        return coverImage;
    }

    public void setCoverImage(Bitmap coverImage) {
        this.coverImage = coverImage;
    }

    public Bitmap getCircleImage() {
        return circleImage;
    }

    public String getState() {
        return new StateUtil().getStateName(state);
    }

    public void setState(String state) {
        this.state = state;
    }

    public String getTermEnd() {
        return termEnd;
    }

    public String getTermEndText() {
        String year = this.termEnd.substring(0, 4);
        String month = this.termEnd.substring(5, 7);
        Map<String, String> monthsMap = monthsMap();
        month = monthsMap.get(month);
        return month + " " + year;
    }


    public void setTermEnd(String termEnd) {
        this.termEnd = termEnd;
    }

    public List<String> getCommittees() {
        return committees;
    }

    public void setCommittees(List<String> committees) {
        this.committees = committees;
    }

    public List<String> getBills() {
        return bills;
    }

    public void setBills(List<String> bills) {
        this.bills = bills;
    }

    public List<String> getBillDates() {
        return billDates;
    }

    public void setBillDates(List<String> billDates) {
        this.billDates = billDates;
    }

    public List<String> getBillDatesHuman() {
        List<String> billDatesHuman = new ArrayList<>();
        Map<String, String> monthsMap = Senator.monthsMap();
        for (String billDate : billDates) {
            StringBuilder builder = new StringBuilder();
            String month = billDate.substring(5,7);
            builder.append(monthsMap.get(month));
            builder.append(" ");
            String day = billDate.substring(8);
            day  = day.charAt(0) == '0' ? day.substring(1) : day;
            builder.append(day);
            billDatesHuman.add(builder.toString());
        }
        return billDatesHuman;
    }

    public void setImage(Bitmap image) {
        ImageProcessor imageProcessor = new ImageProcessor();
        this.coverImage = imageProcessor.cropToCoverSize(image);
        this.circleImage = imageProcessor.maskCircle(image);
    }

    public WatchRep toWatchRep() {
        Bitmap circleImage = getCircleImage();
        Bitmap circleImageSmall;
        if (circleImage == null) {
            circleImage = BitmapFactory.decodeResource(Resources.getSystem(), R.drawable.congress_circle);
            circleImageSmall = Bitmap.createScaledBitmap(circleImage, circleImage.getWidth() / 8, circleImage.getHeight() / 8, false);
        }
        else {
            circleImageSmall = Bitmap.createScaledBitmap(circleImage, circleImage.getWidth() / 2, circleImage.getHeight() / 2, false);
        }
        return new WatchRep(getId(), getTitle(), getName(), getPartyString(), null, getStateAbbrev(), getTermEndText(), circleImageSmall);
    }

    public static Map<String, String> monthsMap() {
        Map<String, String> map = new HashMap<>();
        map.put("01", "January");
        map.put("02", "February");
        map.put("03", "March");
        map.put("04", "April");
        map.put("05", "May");
        map.put("06", "June");
        map.put("07", "July");
        map.put("08", "August");
        map.put("09", "September");
        map.put("10", "October");
        map.put("11", "November");
        map.put("12", "December");
        return map;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(getId());
        dest.writeString(getName());
        dest.writeString(getPartyString());
        dest.writeString(getStateAbbrev());
        dest.writeString(getTermEnd());
        dest.writeString(getWebsiteURL());
        dest.writeString(getEmailAddress());
        dest.writeString(getTwitterHandle());
        dest.writeString(getLatestTweet());
        dest.writeParcelable(getCircleImage(), 0);
        dest.writeParcelable(getCoverImage(), 0);
        dest.writeStringArray(getCommittees().toArray(new String[0]));
        dest.writeStringArray(getBills().toArray(new String[0]));
        dest.writeStringArray(getBillDates().toArray(new String[0]));
    }

    public static final Parcelable.Creator CREATOR = new Parcelable.Creator() {
        @Override
        public Senator createFromParcel(Parcel source) {
            return new Senator(source.readString(), source.readString(),
                    Senator.partyFromString(source.readString()),
                    source.readString(),
                    source.readString(),
                    source.readString(),
                    source.readString(),
                    source.readString(),
                    source.readString(),
                    (Bitmap) source.readParcelable(Bitmap.class.getClassLoader()),
                    (Bitmap) source.readParcelable(Bitmap.class.getClassLoader()),
                    Arrays.asList(source.createStringArray()),
                    Arrays.asList(source.createStringArray()),
                    Arrays.asList(source.createStringArray()));
        }

        @Override
        public Senator[] newArray(int size) {
            return new Senator[size];
        }
    };

    public static Party partyFromString(String party) {
        if (party.equals("Democrat")) {
            return Party.DEMOCRAT;
        }
        else if (party.equals("Republican")) {
            return Party.REPUBLICAN;
        }
        else {
            return Party.INDEPENDENT;
        }
    }
}
