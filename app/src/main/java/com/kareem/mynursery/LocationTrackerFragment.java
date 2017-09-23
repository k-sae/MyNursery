package com.kareem.mynursery;

import android.location.Location;
import android.support.v4.app.Fragment;

/**
 * Created by kareem on 9/22/17.
 */

public interface LocationTrackerFragment {
     void onLocationChange(Location location);
}
