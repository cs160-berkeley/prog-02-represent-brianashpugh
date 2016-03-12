package represent.www.represent;

import android.os.Parcel;
import android.os.Parcelable;

import com.google.android.gms.wearable.DataMap;

/**
 * Created by Brian on 3/6/16.
 */
public class VotingData implements Parcelable {
    private String state;
    private String county;
    private String obamaPerc;
    private String romneyPerc;
    private String voteCount;

    public VotingData(String state, String county, String obamaPerc, String romneyPerc, String voteCount) {
        this.state = state;
        this.county = county;
        this.obamaPerc = obamaPerc;
        this.romneyPerc = romneyPerc;
        this.voteCount = voteCount;
    }

    public String getState() {
        return state;
    }

    public void setState(String state) {
        this.state = state;
    }

    public String getCounty() {
        return county;
    }

    public void setCounty(String county) {
        this.county = county;
    }

    public String getObamaPerc() {
        return obamaPerc;
    }

    public void setObamaPerc(String obamaPerc) {
        this.obamaPerc = obamaPerc;
    }

    public String getRomneyPerc() {
        return romneyPerc;
    }

    public void setRomneyPerc(String romneyPerc) {
        this.romneyPerc = romneyPerc;
    }

    public String getVoteCount() {
        return voteCount;
    }

    public void setVoteCount(String voteCount) {
        this.voteCount = voteCount;
    }


    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(getState());
        dest.writeString(getCounty());
        dest.writeString(getObamaPerc());
        dest.writeString(getRomneyPerc());
        dest.writeString(getVoteCount());
    }

    public static final Parcelable.Creator CREATOR = new Parcelable.Creator() {
        public VotingData createFromParcel(Parcel parcel) {
            return new VotingData(
                    parcel.readString(),
                    parcel.readString(),
                    parcel.readString(),
                    parcel.readString(),
                    parcel.readString());
        }

        public VotingData[] newArray(int size) {
            return new VotingData[size];
        }
    };

    public void addToDataMap(DataMap dataMap) {
        dataMap.putString("state", getState());
        dataMap.putString("county", getCounty());
        dataMap.putString("obama_perc", getObamaPerc());
        dataMap.putString("romney_perc", getRomneyPerc());
        dataMap.putString("vote_count", getVoteCount());
    }

    public static VotingData fromDataMap(DataMap dataMap) {
        return new VotingData(
                dataMap.getString("state"),
                dataMap.getString("county"),
                dataMap.getString("obama_perc"),
                dataMap.getString("romney_perc"),
                dataMap.getString("vote_count")
                );
    }
}
