package com.kareem.mynursery.nurseryProfile;

import android.location.Location;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

import com.daimajia.slider.library.SliderLayout;
import com.kareem.mynursery.LocationTrackerActivity;
import com.kareem.mynursery.R;
import com.kareem.mynursery.profile.ProfileFragment;

import java.util.Locale;

public class NurseryProfileActivity extends LocationTrackerActivity {



    private ProfileFragment profileFragment;
    private Fragment activeFragment;
    private NurseryProfileFragment nurseryProfileFragment;
    private SliderLayout sliderLayout;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_nursery_profile);
        initFragments();
        navigate(nurseryProfileFragment);
    }

    @Override
    protected void onLocationChange(Location location) {

    }


    public void navigate(Fragment fragment) {
        FragmentManager fragmentManager = getSupportFragmentManager();
        if (fragmentManager == null) return;
        FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();

        if (fragment == null) initFragments();

        if (fragmentTransaction == null) return;
        activeFragment = fragment;
        fragmentTransaction
                .replace(R.id.nurseryprofile,
                        fragment)
                .commit();
    }

    private void initFragments() {
        profileFragment = new ProfileFragment();
        nurseryProfileFragment = new NurseryProfileFragment();
    }


}
