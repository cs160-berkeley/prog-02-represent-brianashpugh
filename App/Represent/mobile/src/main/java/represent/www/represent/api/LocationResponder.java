package represent.www.represent.api;

import android.location.Location;

/**
 * Created by Brian on 3/6/16.
 */
public interface LocationResponder {

    public void onLocationFetched(boolean zipCodeUsed, String zipCode, Location location);

}
