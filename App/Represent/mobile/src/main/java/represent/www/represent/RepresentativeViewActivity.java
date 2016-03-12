package represent.www.represent;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import org.w3c.dom.Text;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.zip.Inflater;

import represent.www.represent.models.HouseRepresentative;
import represent.www.represent.models.Representative;
import represent.www.represent.models.Senator;

public class RepresentativeViewActivity extends AppCompatActivity {

    List<Representative> representatives;
    private LayoutInflater inflater;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_representative_view);

        Intent intent = getIntent();
        Representative rep = intent.getParcelableExtra("REP");

        ImageView coverImageView = (ImageView) findViewById(R.id.cover_image);
        Bitmap coverImage = rep.getCoverImage();
        Log.d("phone", "Cover image is " + coverImage.getWidth() + "x" + coverImage.getHeight() + "...");
        coverImageView.setImageBitmap(coverImage);

        LinearLayout banner = (LinearLayout) findViewById(R.id.rep_view_banner);
        if (rep.getParty().equals(Representative.Party.REPUBLICAN)) {
            banner.setBackgroundColor(Color.parseColor("#CA2600"));
        } else if (rep.getParty().equals(Representative.Party.INDEPENDENT)) {
            banner.setBackgroundColor(Color.parseColor("#1E7744"));
        }

        TextView titleTextView = (TextView) findViewById(R.id.representative_title);
        titleTextView.setText(rep.getTitledAbbrevName());

        TextView subtitleTextView = (TextView) findViewById(R.id.representative_subtitle);
        subtitleTextView.setText(rep.getPartyString() + " - Term ends " + rep.getTermEndText());

        inflater = (LayoutInflater) getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        Iterator<String> billIterator = rep.getBills().iterator();
        Iterator<String> billDateIterator = rep.getBillDatesHuman().iterator();
        LinearLayout billsParent = (LinearLayout) findViewById(R.id.bills_parent);
        int billsCount = 0;
        int billsLimit = 7;
        while (billIterator.hasNext() && billDateIterator.hasNext() && billsCount < billsLimit) {
            LinearLayout rowLayout = (LinearLayout) inflater.inflate(R.layout.recent_bills_list_item, null);
            TextView billDate = (TextView) rowLayout.findViewById(R.id.bill_date);
            billDate.setText(billDateIterator.next());
            TextView billName = (TextView) rowLayout.findViewById(R.id.bill_name);
            billName.setText(billIterator.next());
            billsParent.addView(rowLayout);
            billsCount++;
        }

        LinearLayout committeesParent = (LinearLayout) findViewById(R.id.committees_parent);
        int committeesCount = 0;
        int committeesLimit = 6;
        for (String committee : rep.getCommittees()) {
            if (committeesCount < committeesLimit) {
                TextView text = (TextView) inflater.inflate(R.layout.committees_list_item, null);
                text.setText(committee);
                committeesParent.addView(text);
                committeesCount++;
            }
        }
    }

}
