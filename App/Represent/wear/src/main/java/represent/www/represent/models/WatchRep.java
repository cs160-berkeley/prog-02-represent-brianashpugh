package represent.www.represent.models;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Parcel;
import android.os.Parcelable;

import com.google.android.gms.wearable.DataMap;

import java.io.ByteArrayOutputStream;

/**
 * Created by Brian on 3/9/16.
 */
public class WatchRep implements Parcelable {
    private String repId;
    private String title;
    private String name;
    private String party;
    private String district;
    private String state;
    private String termEnds;
    private Bitmap image;

    public WatchRep(String repId, String title, String name, String party, String district, String state, String termEnds, Bitmap image) {
        this.repId = repId;
        this.title = title;
        this.name = name;
        this.party = party;
        this.district = district;
        this.state = state;
        this.termEnds = termEnds;
        this.image = image;
    }

    public String getRepId() {
        return repId;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getTitledAbbrevName() {
        return getDistrict() == null ? "Sen. " + getName() : "Rep. " + getName();
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getPartyAbbrev() {
        return party.equals("Democrat") ? "DEM" : party.equals("Republican") ? "REP" : "IND";
    }

    public String getParty() {
        return party;
    }

    public void setParty(String party) {
        this.party = party;
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

    public String getTermEnds() {
        return termEnds;
    }

    public void setTermEnds(String termEnds) {
        this.termEnds = termEnds;
    }

    public Bitmap getImage() {
        return image;
    }

    public void setImage(Bitmap image) {
        this.image = image;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(getRepId());
        dest.writeString(getTitle());
        dest.writeString(getName());
        dest.writeString(getParty());
        dest.writeString(getDistrict());
        dest.writeString(getState());
        dest.writeString(getTermEnds());
        dest.writeParcelable(getImage(), 0);
    }

    public static final Parcelable.Creator CREATOR = new Parcelable.Creator() {
        public WatchRep createFromParcel(Parcel parcel) {
            return new WatchRep(parcel.readString(), parcel.readString(),
                    parcel.readString(), parcel.readString(),
                    parcel.readString(), parcel.readString(),
                    parcel.readString(),
                    (Bitmap) parcel.readParcelable(Bitmap.class.getClassLoader()));
        }

        public WatchRep[] newArray(int size) {
            return new WatchRep[size];
        }
    };

    public void addToDataMap(DataMap dataMap) {
        dataMap.putString(repId + "/rep_id", getRepId());
        dataMap.putString(repId + "/title", getTitle());
        dataMap.putString(repId + "/name", getName());
        dataMap.putString(repId + "/party", getParty());
        dataMap.putString(repId + "/district", getDistrict());
        dataMap.putString(repId + "/state", getState());
        dataMap.putString(repId + "/term_end", getTermEnds());
        ByteArrayOutputStream stream = new ByteArrayOutputStream();
        getImage().compress(Bitmap.CompressFormat.PNG, 100, stream);
        byte[] byteArray = stream.toByteArray();
        dataMap.putByteArray(repId + "/image", byteArray);
    }

    public static WatchRep fromDataMap(DataMap dataMap, String repId) {
        byte[] byteArray = dataMap.getByteArray(repId + "/image");
        Bitmap bitmap = BitmapFactory.decodeByteArray(byteArray, 0, byteArray.length);
        WatchRep watchRep = new WatchRep(
                dataMap.getString(repId + "/rep_id"),
                dataMap.getString(repId + "/title"),
                dataMap.getString(repId + "/name"),
                dataMap.getString(repId + "/party"),
                dataMap.getString(repId + "/district"),
                dataMap.getString(repId + "/state"),
                dataMap.getString(repId + "/term_end"),
                bitmap
        );
        return watchRep;
    }
}
