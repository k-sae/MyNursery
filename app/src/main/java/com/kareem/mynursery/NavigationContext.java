package com.kareem.mynursery;

import android.app.Activity;
import android.support.v4.app.Fragment;

/**
 * Created by kareem on 9/13/17.
 */

public interface NavigationContext {
    void navigate(Fragment fragment);
    void navigate(Activity fragment);
}
