package represent.www.represent;

import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.KeyEvent;
import android.view.View;
import android.view.inputmethod.EditorInfo;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;

public class LocationInputActivity extends AppCompatActivity {

    List<String> districts;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_location_input);

        String[] districtsArr = {"8th District of California", "5th District of Oklahoma", "9th District of Texas",
                "2nd District of Florida", "1st District of Alabama", "5th District of New Hampshire"};
        districts = Arrays.asList(districtsArr);

        Button byCurrentLocationButton = (Button) findViewById(R.id.by_current_location_button);
        byCurrentLocationButton.setTransformationMethod(null);
        byCurrentLocationButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                // This needs to get the representatives

                openRepresentativeListActivity(false, "", new ArrayList<Representative>());
            }
        });

        EditText byZipCodeField = (EditText) findViewById(R.id.by_zip_code_field);
        byZipCodeField.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View v, boolean hasFocus) {
                if (hasFocus) {
                    EditText byZipCodeField = (EditText) v;
                    byZipCodeField.setText("");
                }
            }
        });

        // ON ZIP CODE ENTER

        byZipCodeField.setImeActionLabel("Find", KeyEvent.KEYCODE_ENTER);
        final LocationInputActivity thisActivity = this;
        byZipCodeField.setOnEditorActionListener(new TextView.OnEditorActionListener() {
            @Override
            public boolean onEditorAction(TextView v, int actionId, KeyEvent event) {
                Log.d("phone", "Keyboard actionId: " + actionId + ", KeyEvent: " + event + ".");
                if (actionId == 66 || (event!= null && event.getAction() == KeyEvent.KEYCODE_ENTER)) {
                    thisActivity.onZipCodeEntered(v.getText().toString());
                }
                return true;
            }
        });
    }

    protected void onZipCodeEntered(String zipCode) {
        openRepresentativeListActivity(true, zipCode, new ArrayList<Representative>());
    }

    protected void openRepresentativeListActivity(boolean zipCodeUsed, String zipCode, List<Representative> representatives) {

        // TODO: attach location information to intents

        Intent watchIntent = new Intent(this, PhoneToWatchService.class);
        Intent representativeListIntent = new Intent(this, RepresentativeList.class);

        if (zipCodeUsed) {
            watchIntent.putExtra("LOCATION", zipCode);
            representativeListIntent.putExtra("LOCATION", zipCode);
        }
        else {
            Collections.shuffle(districts);
            watchIntent.putExtra("LOCATION", districts.get(0));
            representativeListIntent.putExtra("LOCATION", districts.get(0));
        }
        Log.d("phone", "starting PhoneToWatch service...");
        this.startService(watchIntent);
        startActivity(representativeListIntent);

    }

}
