package com.kareem.mynursery;

import android.content.Context;
import android.location.Location;
import android.os.Bundle;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.firebase.analytics.FirebaseAnalytics;
import com.google.firebase.database.FirebaseDatabase;

/**
 * Created by kareem on 9/13/17.
 */

public class Utils {

    public static Location location;
    public static FirebaseAnalytics firebaseAnalytics;
    public static void showToast(String text, Context context)
    {
        Toast.makeText(context, text, Toast.LENGTH_LONG).show();
    }
    public static double calculateDistance(Location locationA,Location locationB )
    {
       return ((double)Math.round((locationA.distanceTo(locationB)/1000) * 10)) / 10.0;
    }

    public static void sendAnalytics(String id, String name)
    {
        Bundle bundle = new Bundle();
        bundle.putString(FirebaseAnalytics.Param.ITEM_ID, id);
        bundle.putString(FirebaseAnalytics.Param.ITEM_NAME, name);
        firebaseAnalytics.logEvent(FirebaseAnalytics.Event.SELECT_CONTENT, bundle);

    }

}
