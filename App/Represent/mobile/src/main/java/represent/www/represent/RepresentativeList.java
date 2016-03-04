package represent.www.represent;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListAdapter;
import android.widget.ListView;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;

public class RepresentativeList extends AppCompatActivity {

    protected List<String> representativeKeys;

    String[] districtsArr = {"8th District of California", "5th District of Oklahoma", "9th District of Texas",
            "2nd District of Florida", "1st District of Alabama", "5th District of New Hampshire"};
    List<String> districts;
    String currentDistrict;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_representative_list);

        districts = Arrays.asList(districtsArr);

        Intent intent = getIntent();
        boolean shuffle = intent.getBooleanExtra("SHUFFLE", false);
        ListView representativeListView = (ListView) findViewById(R.id.representative_list_view);
        ListAdapter representativeListAdapter = new RepresentativeListAdapter(this, new ArrayList<Representative>(), shuffle);
        representativeListView.setAdapter(representativeListAdapter);

        if (shuffle) {
            Collections.shuffle(districts);
            this.currentDistrict = districts.get(0);
        }
        else {
            this.currentDistrict = intent.getStringExtra("LOCATION");
        }

        TextView locationTextView = (TextView) findViewById(R.id.location_bar);
        locationTextView.setText(this.currentDistrict);

    }
}
