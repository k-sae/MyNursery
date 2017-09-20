package com.kareem.mynursery.profile;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

import com.kareem.mynursery.R;
import com.kareem.mynursery.home.HomeFragment;
import com.kareem.mynursery.profile.dummy.DummyContent;

public class Test extends AppCompatActivity implements ProfileFragment.OnListFragmentInteractionListener {



    private ProfileFragment profileFragment;
    private Fragment activeFragment;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_test);
        initFragments();
        navigate(profileFragment);
    }



    public void navigate(Fragment fragment) {
        FragmentManager fragmentManager = getSupportFragmentManager();
        if (fragmentManager == null) return;
        FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();

        if (fragment == null) initFragments();

        if (fragmentTransaction == null) return;
        activeFragment = fragment;
        fragmentTransaction
                .replace(R.id.klp,
                        fragment)
                .commit();
    }

    private void initFragments() {
        profileFragment = new ProfileFragment();
    }

    @Override
    public void onListFragmentInteraction(DummyContent.DummyItem item) {

    }
}
