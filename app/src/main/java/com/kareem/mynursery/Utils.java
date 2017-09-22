package com.kareem.mynursery;

import android.content.Context;
import android.location.Location;
import android.widget.Toast;

/**
 * Created by kareem on 9/13/17.
 */

public class Utils {
    public static void showToast(String text, Context context)
    {
        Toast.makeText(context, text, Toast.LENGTH_LONG).show();
    }
    public static double calculateDistance(Location locationA,Location locationB )
    {
       return locationA.distanceTo(locationB) ;
    }
}
