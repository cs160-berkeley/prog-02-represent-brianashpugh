package represent.www.represent;

import android.content.Context;
import android.content.Intent;
import android.support.wearable.view.GridPagerAdapter;
import android.support.wearable.view.GridViewPager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.concurrent.ThreadLocalRandom;

/**
 * Created by Brian on 3/1/16.
 */
public class RepresentativeGridPagerAdapter extends GridPagerAdapter {

    String zipCode;
    GridViewPager gridViewPager;

    String[] districtsArr = {"8th District of California", "5th District of Oklahoma", "9th District of Texas",
            "2nd District of Florida", "1st District of Alabama", "5th District of New Hampshire"};
    List<String> districts;
    String district;

    private final List<Representative> reps = new ArrayList<>();

    RepresentativeGridPagerAdapter(GridViewPager gridViewPager) {
        this.gridViewPager = gridViewPager;
        districts = Arrays.asList(districtsArr);
        this.district = districts.get(0);
        reps.add(0, new Representative("Senator", "Barbara Boxer", "Democrat"));
        reps.add(1, new Representative("Representative", "Barbara Lee", "Democrat"));
        reps.add(2, new Representative("Senator", "Dianne Feinstein", "Democrat"));
    }

    @Override
    public int getRowCount() {
        return 2;
    }

    @Override
    public int getColumnCount(int i) {
        return 3;
    }

    @Override
    public Object instantiateItem(ViewGroup viewGroup, int row, int col) {
        final View view;
        Integer romneyPerc = ThreadLocalRandom.current().nextInt(0, 50);
        Integer obamaPerc = ThreadLocalRandom.current().nextInt(0, 50);

        if (row == 0) {
            view = LayoutInflater.from(gridViewPager.getContext()).inflate(R.layout.rep, viewGroup, false);

            final TextView repName = (TextView) view.findViewById(R.id.rep_name);
            StringBuilder repTitleBuilder = new StringBuilder();
            final Representative rep = this.reps.get(col);
            repTitleBuilder.append(rep.title);
            repTitleBuilder.append(" ");
            repTitleBuilder.append(rep.name);
            repName.setText(repTitleBuilder.toString());

            TextView repParty = (TextView) view.findViewById(R.id.rep_party);
            repParty.setText(rep.party);

            LinearLayout layout = (LinearLayout) view.findViewById(R.id.rep_layout);
            layout.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Intent watchToPhoneIntent = new Intent(gridViewPager.getContext(), WatchToPhoneService.class);
                    watchToPhoneIntent.putExtra(WatchToPhoneService.REP_SELECTED_FLAG, true);
                    watchToPhoneIntent.putExtra("REP_NAME", rep.name);
                    gridViewPager.getContext().startService(watchToPhoneIntent);
                }
            });
        }
        else {
            view = LayoutInflater.from(gridViewPager.getContext()).inflate(R.layout.election, viewGroup, false);
            TextView locationText = (TextView) view.findViewById(R.id.location_text);

            locationText.setText(district);


            TextView obamaPercText = (TextView) view.findViewById(R.id.obama_percentage);
            obamaPercText.setText("Obama: " + obamaPerc.toString() + "%");

            TextView romneyPercText = (TextView) view.findViewById(R.id.romney_percentage);
            romneyPercText.setText("Romney: " + romneyPerc.toString() + "%");
        }
        viewGroup.addView(view);
        return view;
    }

    @Override
    public void destroyItem(ViewGroup viewGroup, int i, int i1, Object view) {
        viewGroup.removeView((View) view);
    }

    public void shuffleItems() {
        Collections.shuffle(this.reps);
    }

    @Override
    public boolean isViewFromObject(View view, Object o) {
        return view.equals(o);
    }

}
