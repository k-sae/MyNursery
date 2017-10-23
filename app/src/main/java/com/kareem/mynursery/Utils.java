package com.kareem.mynursery;

import android.content.Context;
import android.content.Intent;
import android.location.Location;
import android.os.Bundle;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.daimajia.slider.library.SliderTypes.BaseSliderView;
import com.google.firebase.analytics.FirebaseAnalytics;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.kareem.mynursery.model.FirebaseParser.ObjectParser;
import com.kareem.mynursery.model.Nursery;
import com.kareem.mynursery.model.TestModule;
import com.kareem.mynursery.nurseryProfile.NurseryProfileActivity;

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
        //for some reason this not works
//        Bundle bundle = new Bundle();
//        bundle.putString(FirebaseAnalytics.Param.ITEM_ID, id);
//        bundle.putString(FirebaseAnalytics.Param.ITEM_NAME, name);
//        firebaseAnalytics.logEvent(FirebaseAnalytics.Event.SELECT_CONTENT, bundle);
        FirebaseDatabase.getInstance()
                .getReference("tests")
                .push()
                .setValue(new TestModule(id, name)
                );

    }
    public static BaseSliderView getImageNotFoundSlider(Context context)
    {
       return new GlideSliderView(context).loadGif(true).image(R.mipmap.not_found);
    }
}
