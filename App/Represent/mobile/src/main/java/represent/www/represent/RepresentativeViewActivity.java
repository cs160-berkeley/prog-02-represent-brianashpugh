package represent.www.represent;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;

public class RepresentativeViewActivity extends AppCompatActivity {

    List<Representative> representatives;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_representative_view);

        Intent intent = getIntent();
        String repName = intent.getStringExtra("REP_NAME");

        this.representatives = new ArrayList<Representative>();

        Representative boxer = new Senator("Barbara Boxer",
                Representative.Party.DEMOCRAT, "boxer.senate.gov", "barbara@boxer.senate.gov",
                "@SenatorBoxer", "Putting the country first means...", R.drawable.barbara_boxer_circle_small, R. drawable.barbara_boxer_cover_small);
        this.representatives.add(0, boxer);

        Representative lee = new HouseRepresentative("Barbara Lee",
                Representative.Party.DEMOCRAT, "13th District", "lee.house.gov", "barbara@lee.house.gov",
                "@RepBarbaraLee", "Healthier food provides a better...", R.drawable.barbara_lee_circle, R.drawable.barbara_lee_cover);
        this.representatives.add(1, lee);


        Representative feinstein = new Senator("Dianne Feinstein",
                Representative.Party.DEMOCRAT, "feinstein.senate.gov", "dianne@feinstein.senate.gov",
                "@SenFeinstein", "#BlackHistoryMonth: My friend...", R.drawable.dianne_feinstein_circle, R.drawable.dianne_feinstein_cover);
        this.representatives.add(2, feinstein);

        Representative selectedRep = findRepByName(repName, representatives);

        ImageView coverImageView = (ImageView) findViewById(R.id.cover_image);
        coverImageView.setImageResource(selectedRep.getCoverImageResource());

        TextView titleTextView = (TextView) findViewById(R.id.representative_title);
        titleTextView.setText(selectedRep.getTitle());

        TextView subtitleTextView = (TextView) findViewById(R.id.representative_subtitle);
        subtitleTextView.setText("Democrat - Term ends January 2016");

        TextView currentCommitteesTextView = (TextView) findViewById(R.id.current_committees);
        currentCommitteesTextView.setText("Vice Chairman, Senate Select Committee on Intelligence\n" +
                "Senate Committee on Appropriations\n" +
                "Senate Committee on the Judiciary\n" +
                "Senate Committee on Rules and...\n" +
                "United States Senate Caucus on International Narcotics Control");

        TextView recentBillsTextView = (TextView) findViewById(R.id.recent_bills);
        recentBillsTextView.setText("S. 2552: A bill to amend section 875(c) of...\n" +
                "S. 2533: California Long-Term Provisions for...\n" +
                "S. 2442: A bill to authorize the use of...\n" +
                "S. 2422: Fiscal Year 2016 Department of...\n" +
                "S. 2372: Requiring Reporting of Online...\n" +
                "S. 2337: Visa Waiver Program Security...\n" +
                "S.Res. 299: A resolution honoring the life...");
    }

    Representative findRepByName(String name, List<Representative> reps) {
        for (Representative rep : reps) {
            if (name.equals(rep.getName())) {
                return rep;
            }
        }
        return reps.get(0);
    }
}
