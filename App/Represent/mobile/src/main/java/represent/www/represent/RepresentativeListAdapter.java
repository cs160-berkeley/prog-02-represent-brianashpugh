package represent.www.represent;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;

/**
 * Created by Brian on 2/29/16.
 */
public class RepresentativeListAdapter extends BaseAdapter {

    List<Representative> representatives;
    LayoutInflater inflater;
    RepresentativeList representativeList;
    boolean shuffle;

    protected RepresentativeListAdapter(RepresentativeList representativeList, List<Representative> representatives, boolean shuffle) {
        this.inflater = (LayoutInflater) representativeList.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        this.representativeList = representativeList;
        this.shuffle = shuffle;

        this.representatives = new ArrayList<Representative>();

        Representative boxer = new Senator("Barbara Boxer",
                Representative.Party.DEMOCRAT, "boxer.senate.gov", "barbara@boxer.senate.gov",
                "@SenatorBoxer", "Putting the country first means...", R.drawable.barbara_boxer_circle_small, R.drawable.barbara_boxer_cover_small);
        this.representatives.add(0, boxer);

        Representative lee = new HouseRepresentative("Barbara Lee",
                Representative.Party.DEMOCRAT, "13th District", "lee.house.gov", "barbara@lee.house.gov",
                "@RepBarbaraLee", "Healthier food provides a better...", R.drawable.barbara_lee_circle, R.drawable.barbara_lee_cover);
        this.representatives.add(1, lee);


        Representative feinstein = new Senator("Dianne Feinstein",
                Representative.Party.DEMOCRAT, "feinstein.senate.gov", "dianne@feinstein.senate.gov",
                "@SenFeinstein", "#BlackHistoryMonth: My friend...", R.drawable.dianne_feinstein_circle, R.drawable.dianne_feinstein_cover);
        this.representatives.add(2, feinstein);

        if (shuffle) {

            Collections.shuffle(this.representatives);
        }
    }

    @Override
    public int getCount() {
        return 3;
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

        final Representative representative = representatives.get(position);
        ImageView representativeImage = (ImageView) rowView.findViewById(R.id.representative_image);
        representativeImage.setImageResource(representative.getImageResource());

        TextView representativePartyTextView = (TextView) rowView.findViewById(R.id.representative_party);
        representativePartyTextView.setText(representative.getPartyString());

        TextView representativeTitleTextView = (TextView) rowView.findViewById(R.id.representative_title);
        representativeTitleTextView.setText(representative.getName());

        TextView representativeDistrictTextView = (TextView) rowView.findViewById(R.id.representative_district);
        StringBuilder locationBuilder = new StringBuilder();
        try {
            HouseRepresentative houseRepresentative = (HouseRepresentative) representative;
            locationBuilder.append(houseRepresentative.getDistrict());
            locationBuilder.append(", ");
        }
        catch(ClassCastException e) {

        }
        locationBuilder.append("California");
        representativeDistrictTextView.setText(locationBuilder.toString());

        TextView representativeWebsiteTextView = (TextView) rowView.findViewById(R.id.representative_website);
        representativeWebsiteTextView.setText(representative.getWebsiteURL());

        TextView representativeEmailTextView = (TextView) rowView.findViewById(R.id.representative_email);
        representativeEmailTextView.setText(representative.getEmailAddress());

        TextView representativeTwitterTextView = (TextView) rowView.findViewById(R.id.representative_twitter);
        representativeTwitterTextView.setText(representative.getTwitterHandle());


        TextView representativeLastTweetTextView = (TextView) rowView.findViewById(R.id.representative_last_tweet);
        representativeLastTweetTextView.setText(representative.getLatestTweet());

        rowView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent representativeViewIntent = new Intent(representativeList.getBaseContext(), RepresentativeViewActivity.class);
                representativeViewIntent.putExtra("REP_NAME", representative.getName());
                representativeList.startActivity(representativeViewIntent);

                Intent phoneToWatchIntent = new Intent(representativeList.getBaseContext(), PhoneToWatchService.class);
                phoneToWatchIntent.putExtra("REP_BYTES", representative.toString());
                phoneToWatchIntent.putExtra("REP_CHOSEN", true);
                representativeList.startService(phoneToWatchIntent);
            }
        });

        return rowView;

    }
}
