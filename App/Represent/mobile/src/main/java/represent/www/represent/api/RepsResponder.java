package represent.www.represent.api;

import java.util.List;

/**
 * Created by Brian on 3/6/16.
 */
public interface RepsResponder {
    public void onRepsFetched(List<String> repIds);
}
