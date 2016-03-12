package represent.www.represent;

import java.util.List;

/**
 * Created by Brian on 3/6/16.
 */
public interface GeolocationResponder {
    public void onGeolocationFetched(List<String> counties, List<String> states);
}
