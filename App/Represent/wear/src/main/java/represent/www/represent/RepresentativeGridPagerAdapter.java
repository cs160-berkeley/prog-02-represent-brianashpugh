package represent.www.represent;

import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.support.wearable.view.GridPagerAdapter;
import android.support.wearable.view.GridViewPager;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.concurrent.ThreadLocalRandom;

import represent.www.represent.models.VotingData;
import represent.www.represent.models.WatchRep;

/**
 * Created by Brian on 3/1/16.
 */
public class RepresentativeGridPagerAdapter extends GridPagerAdapter {

    private final VotingData votingData;
    private GridViewPager gridViewPager;
    private List<WatchRep> watchReps;

    RepresentativeGridPagerAdapter(GridViewPager gridViewPager, List<WatchRep> watchReps, VotingData votingData) {
        this.gridViewPager = gridViewPager;
        this.watchReps = watchReps;
        this.votingData = votingData;
    }

    @Override
    public int getRowCount() {
        return 2;
    }

    @Override
    public int getColumnCount(int i) {
        return this.watchReps.size();
    }

    @Override
    public Object instantiateItem(ViewGroup viewGroup, int row, int col) {
        final View view;

        if (row == 0) {
            view = LayoutInflater.from(gridViewPager.getContext()).inflate(R.layout.rep, viewGroup, false);

            final TextView repName = (TextView) view.findViewById(R.id.rep_name);
            final WatchRep watchRep = this.watchReps.get(col);
            repName.setText(watchRep.getTitledAbbrevName());

            ImageView repImage = (ImageView) view.findViewById(R.id.rep_image_circle);
            repImage.setImageBitmap(watchRep.getImage());

            TextView repParty = (TextView) view.findViewById(R.id.party);
            repParty.setText(watchRep.getPartyAbbrev());

            LinearLayout layer = (LinearLayout) view.findViewById(R.id.layer);
            if (watchRep.getPartyAbbrev().equals("REP")) {
                layer.setBackgroundColor(Color.parseColor("#CA2600"));
            } else if (watchRep.getPartyAbbrev().equals("IND")) {
                layer.setBackgroundColor(Color.parseColor("#1E7744"));
            }

            TextView repDistrict = (TextView) view.findViewById(R.id.district);
            LinearLayout repFullDistrict = (LinearLayout) view.findViewById(R.id.full_district);
            LinearLayout parent = (LinearLayout) view.findViewById(R.id.divider_parent);
            if (watchRep.getDistrict() == null) {
                parent.removeView(repFullDistrict);
                parent.removeView(view.findViewById(R.id.divider_one));
            }
            else {
                repDistrict.setText(ordinal(Integer.parseInt(watchRep.getDistrict())));
            }

            TextView repState = (TextView) view.findViewById(R.id.state);
            repState.setText(watchRep.getState());
            Log.d("watch", "state" + watchRep.getState());


            FrameLayout layout = (FrameLayout) view.findViewById(R.id.rep_layout);
            layout.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Intent watchToPhoneIntent = new Intent(gridViewPager.getContext(), WatchToPhoneService.class);
                    watchToPhoneIntent.putExtra(WatchToPhoneService.REP_SELECTED_FLAG, true);
                    watchToPhoneIntent.putExtra("REP_ID", watchRep.getRepId());
                    gridViewPager.getContext().startService(watchToPhoneIntent);
                }
            });
        }
        else {
            view = LayoutInflater.from(gridViewPager.getContext()).inflate(R.layout.election, viewGroup, false);
            TextView locationText = (TextView) view.findViewById(R.id.location_text);
            locationText.setText(votingData.getCounty() + " County, " + votingData.getState());

            TextView obamaPercText = (TextView) view.findViewById(R.id.obama_percentage);
            obamaPercText.setText(votingData.getObamaPerc() + "%");

            TextView romneyPercText = (TextView) view.findViewById(R.id.romney_percentage);
            romneyPercText.setText(votingData.getRomneyPerc() + "%");

            TextView voteCountText = (TextView) view.findViewById(R.id.vote_count);
            voteCountText.setText(getRoundOffValue(votingData.getVoteCount()) + " Votes");
        }
        viewGroup.addView(view);
        return view;
    }

    @Override
    public void destroyItem(ViewGroup viewGroup, int i, int i1, Object view) {
        viewGroup.removeView((View) view);
    }

    @Override
    public boolean isViewFromObject(View view, Object o) {
        return view.equals(o);
    }

    public static String ordinal(int i) {
        String[] sufixes = new String[] { "th", "st", "nd", "rd", "th", "th", "th", "th", "th", "th" };
        switch (i % 100) {
            case 11:
            case 12:
            case 13:
                return i + "th";
            default:
                return i + sufixes[i % 10];

        }
    }

    public static String getRoundOffValue(String value){
        Double val = Double.parseDouble(value);
        DecimalFormat df = new DecimalFormat("###,###,###,###,###");
        return df.format(val);
    }
}
