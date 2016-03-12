package represent.www.represent;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.graphics.Color;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.Collections;
import java.util.List;

import represent.www.represent.models.HouseRepresentative;
import represent.www.represent.models.Representative;

/**
 * Created by Brian on 2/29/16.
 */
public class RepresentativeListAdapter extends BaseAdapter {

    List<Representative> representatives;
    LayoutInflater inflater;
    RepresentativeListActivity representativeList;

    protected RepresentativeListAdapter(RepresentativeListActivity representativeListActivity, List<Representative> representatives) {
        this.inflater = (LayoutInflater) representativeListActivity.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        this.representativeList = representativeListActivity;
        this.representatives = representatives;
        final RepresentativeListAdapter thisAdapter = this;

    }

    @Override
    public int getCount() {
        return representatives.size();
    }

    @Override
    public Object getItem(int position) {
        return null;
    }

    @Override
    public long getItemId(int position) {
        return 0;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        View rowView = inflater.inflate(R.layout.representative_list_item, null);

        final Representative representative = this.representatives.get(position);
        ImageView representativeImage = (ImageView) rowView.findViewById(R.id.representative_image);
        representativeImage.setImageBitmap(representative.getCircleImage());

        ImageView banner = (ImageView) rowView.findViewById(R.id.banner);
        if (representative.getParty().equals(Representative.Party.REPUBLICAN)) {
            banner.setBackgroundColor(Color.parseColor("#CA2600"));
        } else if (representative.getParty().equals(Representative.Party.INDEPENDENT)) {
            banner.setBackgroundColor(Color.parseColor("#1E7744"));
        }

        TextView representativePartyTextView = (TextView) rowView.findViewById(R.id.representative_party);
        representativePartyTextView.setText(representative.getPartyString());

        TextView representativeTitleTextView = (TextView) rowView.findViewById(R.id.representative_title);
        representativeTitleTextView.setText(representative.getTitledAbbrevName());

        TextView representativeDistrictTextView = (TextView) rowView.findViewById(R.id.representative_district);
        StringBuilder locationBuilder = new StringBuilder();
        try {
            HouseRepresentative houseRepresentative = (HouseRepresentative) representative;
            locationBuilder.append("District ");
            locationBuilder.append(houseRepresentative.getDistrict());
            locationBuilder.append(", ");
        }
        catch(ClassCastException e) {

        }
        locationBuilder.append(representative.getState());
        representativeDistrictTextView.setText(locationBuilder.toString());

        TextView representativeWebsiteTextView = (TextView) rowView.findViewById(R.id.representative_website);
        representativeWebsiteTextView.setText(representative.getWebsiteURL());

        TextView representativeEmailTextView = (TextView) rowView.findViewById(R.id.representative_email);
        representativeEmailTextView.setText(
                representative.getEmailAddress() == null || representative.getEmailAddress().length() < 14 ? "Email Unavailable" : representative.getEmailAddress());

        TextView representativeTwitterTextView = (TextView) rowView.findViewById(R.id.representative_twitter);
        representativeTwitterTextView.setText(representative.getTwitterHandle());


        TextView representativeLastTweetTextView = (TextView) rowView.findViewById(R.id.representative_last_tweet);
        representativeLastTweetTextView.setText(representative.getLatestTweet());

        rowView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                repSelected(representative.getId());
            }
        });

        return rowView;

    }

    protected void repSelected(String repId) {
        Representative representative = getRepById(repId);
        Intent representativeViewIntent = new Intent(representativeList, RepresentativeViewActivity.class);
        representativeViewIntent.putExtra("REP", representative);
        representativeList.startActivity(representativeViewIntent);

        /*Intent phoneToWatchIntent = new Intent(representativeList, PhoneToWatchService.class);
        phoneToWatchIntent.putExtra("REP_ID", representative.getId());
        phoneToWatchIntent.putExtra("REP_CHOSEN", true);
        representativeList.startService(phoneToWatchIntent);*/
    }

    private Representative getRepById(String repId) {
        for (Representative rep : representatives) {
            if (rep.getId().equals(repId)) {
                return rep;
            }
        }
        return null;
    }
}
