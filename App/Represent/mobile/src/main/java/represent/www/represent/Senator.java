package represent.www.represent;

import java.util.concurrent.ThreadLocalRandom;

/**
 * Created by Brian on 2/29/16.
 */
public class Senator implements Representative {

    private String name;
    private Party party;
    private String websiteURL;
    private String emailAddress;
    private String twitterHandle;
    private String latestTweet;
    private int imageResource;
    private int coverImageResource;
    private int id;

    public Senator(String name,
            Party party,
            String websiteURL,
            String emailAddress,
            String twitterHandle, String latestTweet,
                   int imageResource,
                   int coverImageResource,
            int id) {
        this.name = name;
        this.party = party;
        this.websiteURL = websiteURL;
        this.emailAddress = emailAddress;
        this.twitterHandle = twitterHandle;
        this.latestTweet = latestTweet;
        this.imageResource = imageResource;
        this.coverImageResource = coverImageResource;
        this.id = id;
    }

    public Senator(String name,
                   Party party,
                   String websiteURL,
                   String emailAddress,
                   String twitterHandle,
                   String latestTweet,
                   int imageResource, int coverImageResource) {
        this(name, party, websiteURL, emailAddress, twitterHandle, latestTweet, imageResource, coverImageResource,
                ThreadLocalRandom.current().nextInt(0, Integer.MAX_VALUE));
    }

    @Override
    public String getName() {
        return name;
    }

    public String getTitle() {
        return "Senator " + name;
    }

    @Override
    public Party getParty() {
        return party;
    }

    public String getPartyString() {
        return this.party == Party.DEMOCRAT ? "Democrat" : "Republican";
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
    public int getCoverImageResource() {
        return coverImageResource;
    }

    @Override
    public int getImageResource() {
        return imageResource;
    }

    public int getId() {
        return id;
    }

    public String toString() {
        String div = "$$$";
        String combined = getName() + div + getTitle() + div + getPartyString();
        return combined;
    }
}
