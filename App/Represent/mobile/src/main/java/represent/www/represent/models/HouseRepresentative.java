package represent.www.represent.models;

import android.content.res.Resources;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Parcel;
import android.os.Parcelable;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ThreadLocalRandom;

import represent.www.represent.ImageProcessor;
import represent.www.represent.R;
import represent.www.represent.StateUtil;
import represent.www.represent.api.BasicImageDownloader;

/**
 * Created by Brian on 2/29/16.
 */
public class HouseRepresentative implements Representative, Parcelable {

    String name;
    Party party;
    private String district;
    private String state;
    private String websiteURL;
    private String emailAddress;
    private String twitterHandle;
    private String latestTweet;
    private Bitmap circleImage;
    private Bitmap coverImage;
    private String termEnd;
    private List<String> committees;
    private List<String> bills;
    private List<String> billDates;

    public String getId() {
        return id;
    }

    private String id;

    public HouseRepresentative(String id,
                   String name,
                   Party party,
                               String district,
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
        this.district = district;
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

    public HouseRepresentative(String id,
                               String name,
                               Party party,
                               String district,
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
        this.district = district;
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

    public static HouseRepresentative fromSunlightRep(SunlightRep sunlightRep) {
        return new HouseRepresentative(sunlightRep.getId(), sunlightRep.getFirstName() + " " + sunlightRep.getLastName(),
                sunlightRep.getPartyFromEnum(),
                sunlightRep.getDistrict(),
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
        return "Rep. " + name;
    }

    public String getTitle() {
        return "Representative " + name;
    }

    @Override
    public Party getParty() {
        return party;
    }

    public String getPartyString() {
        return this.party == Party.DEMOCRAT ? "Democrat" : "Republican";
    }

    public String getDistrict() {
        return district;
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

    public void setCircleImage(Bitmap circleImage) {
        this.circleImage = circleImage;
    }

    @Override
    public void setCoverImage(Bitmap coverImage) {
        this.coverImage = coverImage;
    }

    public Bitmap getCircleImage() {
        return circleImage;
    }

    @Override
    public Bitmap getCoverImage() {
        return coverImage;
    }

    public void setName(String name) {
        this.name = name;
    }

    public void setParty(Party party) {
        this.party = party;
    }

    public void setDistrict(String district) {
        this.district = district;
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

    public String getState() {
        return new StateUtil().getStateName(state);
    }

    public String getStateAbbrev() {
        return state;
    }

    public void setState(String state) {
        this.state = state;
    }

    public void setTermEnd(String termEnd) {
        this.termEnd = termEnd;
    }

    public String getTermEnd() {
        return termEnd;
    }

    public String getTermEndText() {
        String year = this.termEnd.substring(0, 4);
        String month = this.termEnd.substring(5, 7);
        Map<String, String> monthsMap = Senator.monthsMap();
        month = monthsMap.get(month);
        return month + " " + year;
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


    public void setBillDates(List<String> billDates) {
        this.billDates = billDates;
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
        return new WatchRep(getId(), getTitle(), getName(), getPartyString(), getDistrict(), getStateAbbrev(), getTermEndText(), circleImageSmall);
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
        dest.writeString(getDistrict());
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
        public HouseRepresentative createFromParcel(Parcel source) {
            return new HouseRepresentative(source.readString(), source.readString(),
                    Senator.partyFromString(source.readString()),
                    source.readString(),
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
                    Arrays.asList(source.createStringArray())
            );
        }

        @Override
        public HouseRepresentative[] newArray(int size) {
            return new HouseRepresentative[size];
        }
    };

}
