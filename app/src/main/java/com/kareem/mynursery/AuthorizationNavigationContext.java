package com.kareem.mynursery;

import android.support.v4.app.Fragment;

/**
 * Created by kareem on 10/6/17.
 */

public interface AuthorizationNavigationContext {
     boolean redirectIfNotAuth(Class<?> cls);
    boolean redirectIfNotAuth(Fragment fragment);
}
