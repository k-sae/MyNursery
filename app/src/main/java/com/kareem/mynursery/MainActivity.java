package com.kareem.mynursery;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.BottomNavigationView;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.MenuItem;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.kareem.mynursery.authentication.LoginActivity;
import com.kareem.mynursery.home.FavouriteFragment;
import com.kareem.mynursery.home.HomeFragment;
import com.kareem.mynursery.model.Auth;
import com.kareem.mynursery.model.Nursery;
import com.kareem.mynursery.model.ObjectChangedListener;
import com.kareem.mynursery.model.RealTimeObject;
import com.kareem.mynursery.model.User;
import com.kareem.mynursery.nursery.LocationPicker;
import com.kareem.mynursery.profile.ProfileFragment;

import org.mini2Dx.beanutils.PropertyUtils;

import java.lang.reflect.InvocationTargetException;


public class MainActivity extends AppCompatActivity implements  NavigationContext  {

    private static final int LOGIN_ACTIVITY_RESULT_CODE = 2133;
    private static final String TAG =  MainActivity.class.getName();
    //leave it as it is i will need it later
    private Fragment activeFragment;
    private HomeFragment homeFragment;
    private ProfileFragment profileFragment;
    private FavouriteFragment favouritesFragment;

    @Override
    protected void onStart() {
        super.onStart();
    }

    private BottomNavigationView.OnNavigationItemSelectedListener mOnNavigationItemSelectedListener
            = new BottomNavigationView.OnNavigationItemSelectedListener() {

        @Override
        public boolean onNavigationItemSelected(@NonNull MenuItem item) {
            switch (item.getItemId()) {
                case R.id.navigation_home:
                    navigate(homeFragment);
                    return true;
                case R.id.favourites:
                    return redirectIfNotAuth(favouritesFragment);
                case R.id.navigation_notifications:
                    return redirectIfNotAuth(profileFragment);
            }
            return false;
        }

    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        initFragments();
        Utils.initDatabase();
        navigate(homeFragment);
        BottomNavigationView navigation = findViewById(R.id.navigation);
        navigation.setOnNavigationItemSelectedListener(mOnNavigationItemSelectedListener);
        //this may introduce a bug in app first run
        //if so just w8 for the async task to finish then continue as normal
        if ( FirebaseAuth.getInstance().getCurrentUser() == null) FirebaseAuth.getInstance().signInAnonymously();
        Log.e("firebase", "onCreate: " + FirebaseAuth.getInstance().getCurrentUser());
        test();
    }


    @Override
    public void navigate(Fragment fragment) {
        FragmentManager fragmentManager = getSupportFragmentManager();
        if (fragmentManager == null) return;
        FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();

        if (fragment == null) initFragments();

        if (fragmentTransaction == null) return;
        activeFragment = fragment;
        fragmentTransaction
                .replace(R.id.content,
                        fragment)
                .commit();
    }

    private void initFragments() {
        homeFragment = new HomeFragment();
        profileFragment = new ProfileFragment();
        favouritesFragment = new FavouriteFragment();
    }

    @Override
    public void navigate(Class<?> cls) {

        navigate(cls, LOGIN_ACTIVITY_RESULT_CODE);
    }
    public void navigate(Class<?> cls, int resultCode) {
        Intent intent = new Intent(this, cls);
        startActivityForResult(intent,resultCode);
    }

    //TODO after login navigate to the appropriate page
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

    }

    private boolean redirectIfNotAuth(Fragment fragment)
    {
        if (FirebaseAuth.getInstance().getCurrentUser() == null) {
            Utils.showToast(getString(R.string.please_wait), this);
            return false;
        }
        if (FirebaseAuth.getInstance().getCurrentUser().isAnonymous())
        {
            navigate(LoginActivity.class);
            return false;
        }
        else
        navigate(fragment);
        return true;
    }

    private void test()
    {
        Auth.getLoggedUser().addFavourite("-Kuj6ypRkc9FTx0e_igl");
        Auth.getLoggedUser().addFavourite("-Kuu7bJB9ZyITY6v7EUI");
        Auth.getLoggedUser().addFavourite("-KvER015nE_ejlUM8meY");
        Auth.getLoggedUser().addFavourite("-Kv-Bn6Ht96DST2IJ3YR");

    }


}
