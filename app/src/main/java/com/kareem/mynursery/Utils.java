package com.kareem.mynursery;

import android.content.Context;
import android.location.Location;
import android.widget.Toast;

import com.google.firebase.database.FirebaseDatabase;

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
    private static FirebaseDatabase mDatabase;

    public static FirebaseDatabase initDatabase() {
        if (mDatabase == null) {
            mDatabase = FirebaseDatabase.getInstance();
            mDatabase.setPersistenceEnabled(true);
        }
        return mDatabase;
    }
}
