package represent.www.represent;

import java.util.concurrent.ThreadLocalRandom;

/**
 * Created by Brian on 2/29/16.
 */
public class HouseRepresentative implements Representative {

    String name;
    Party party;
    private String district;
    private String websiteURL;
    private String emailAddress;
    private String twitterHandle;
    private String latestTweet;
    private int imageResource;
    private int coverImageResource;

    public int getId() {
        return id;
    }

    private int id;

    public HouseRepresentative(String name,
                   Party party, String district,
                   String websiteURL,
                   String emailAddress,
                   String twitterHandle,
                   String latestTweet,
                   int imageResource, int coverImageResource, int id) {
        this.name = name;
        this.party = party;
        this.district = district;
        this.websiteURL = websiteURL;
        this.emailAddress = emailAddress;
        this.twitterHandle = twitterHandle;
        this.latestTweet = latestTweet;
        this.imageResource = imageResource;
        this.coverImageResource = coverImageResource;
        this.id = id;
    }

    public HouseRepresentative(String name,
                               Party party,
                               String district,
                               String websiteURL,
                               String emailAddress,
                               String twitterHandle,
                               String latestTweet,
                               int imageResource, int coverImageResource) {
        this(name, party, district, websiteURL, emailAddress, twitterHandle, latestTweet, imageResource, coverImageResource,
                ThreadLocalRandom.current().nextInt(0, Integer.MAX_VALUE));
    }

    @Override
    public String getName() {
        return name;
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

    @Override
    public int getImageResource() {
        return imageResource;
    }

    @Override
    public int getCoverImageResource() {
        return coverImageResource;
    }

    public String toString() {
        String div = "$$$";
        String combined = getName() + div + getTitle() + div + getPartyString();
        return combined;
    }
}
