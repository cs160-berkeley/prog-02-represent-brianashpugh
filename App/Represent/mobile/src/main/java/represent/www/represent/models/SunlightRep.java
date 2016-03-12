package represent.www.represent.models;

import com.google.gson.annotations.SerializedName;

import java.util.List;

import represent.www.represent.models.Representative.Party;

/**
 * Created by Brian on 3/6/16.
 */
public class SunlightRep {
    @SerializedName("last_name")
    private String lastName;

    @SerializedName("first_name")
    private String firstName;

    private String party;
    private String chamber;
    private String district;
    private String state;

    @SerializedName("term_end")
    private String termEnd;

    @SerializedName("contact_form")
    private String contactForm;

    @SerializedName("website")
    private String websiteURL;

    @SerializedName("twitter_id")
    private String twitterId;

    @SerializedName("bioguide_id")
    private String id;

    private List<String> committees;
    private List<String> bills;
    private List<String> billDates;

    public String getLastName() {
        return lastName;
    }

    public void setLastName(String lastName) {
        this.lastName = lastName;
    }

    public String getFirstName() {
        return firstName;
    }

    public void setFirstName(String firstName) {
        this.firstName = firstName;
    }

    public Party getPartyFromEnum() {
        if (getParty().equals("D")) {
            return Party.DEMOCRAT;
        } else if (getParty().equals("R")) {
            return Party.REPUBLICAN;
        } else {
            return Party.INDEPENDENT;
        }
    }

    public String getParty() {
        return party;
    }

    public void setParty(String party) {
        this.party = party;
    }

    public String getChamber() {
        return chamber;
    }

    public void setChamber(String chamber) {
        this.chamber = chamber;
    }

    public String getDistrict() {
        return district;
    }

    public void setDistrict(String district) {
        this.district = district;
    }

    public String getState() {
        return state;
    }

    public void setState(String state) {
        this.state = state;
    }

    public String getTermEnd() {
        return termEnd;
    }

    public void setTermEnd(String termEnd) {
        this.termEnd = termEnd;
    }

    public String getContactForm() {
        return contactForm;
    }

    public void setContactForm(String contactForm) {
        this.contactForm = contactForm;
    }

    public String getWebsiteURL() {
        return websiteURL;
    }

    public void setWebsiteURL(String websiteURL) {
        this.websiteURL = websiteURL;
    }

    public String getTwitterId() {
        return twitterId;
    }

    public void setTwitterId(String twitterId) {
        this.twitterId = twitterId;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
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
}
