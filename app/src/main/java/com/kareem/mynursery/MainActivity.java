package com.kareem.mynursery;

import android.content.Intent;
import android.location.Location;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.BottomNavigationView;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.MenuItem;

import com.google.firebase.analytics.FirebaseAnalytics;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.hbb20.CountryCodePicker;
import com.kareem.mynursery.authentication.LoginActivity;
import com.kareem.mynursery.home.FavouriteFragment;
import com.kareem.mynursery.home.HomeFragment;
import com.kareem.mynursery.model.Auth;
import com.kareem.mynursery.model.Nursery;
import com.kareem.mynursery.model.ObjectChangedListener;
import com.kareem.mynursery.model.RealTimeObject;
import com.kareem.mynursery.model.RealmUtils;
import com.kareem.mynursery.model.User;
import com.kareem.mynursery.model.UserPreferences;
import com.kareem.mynursery.nursery.LocationPicker;
import com.kareem.mynursery.profile.ProfileFragment;

import org.mini2Dx.beanutils.PropertyUtils;

import java.lang.reflect.InvocationTargetException;


public class MainActivity extends LocationTrackerActivity implements CountryCodePicker.OnCountryChangeListener, NavigationContext, AuthorizationNavigationContext  {

    private static final int LOGIN_ACTIVITY_RESULT_CODE = 2133;
    private static final String TAG =  MainActivity.class.getName();
    public static final int HOME = 0;
    //leave it as it is i will need it later
    private Fragment activeFragment;
    private HomeFragment homeFragment;
    private ProfileFragment profileFragment;
    private FavouriteFragment favouritesFragment;
    private CountryCodePicker countryCodePicker;
    private RealmUtils realmUtils;
    BottomNavigationView navigation;
    @Override
    protected void onStart() {
        setPromptTheUserForLocation(false);
        super.onStart();
    }

    @Override
    protected void onLocationChange(Location location) {
        Utils.location = location;
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
        navigate(homeFragment);
        navigation = findViewById(R.id.navigation);
        navigation.setOnNavigationItemSelectedListener(mOnNavigationItemSelectedListener);
        //this may introduce a bug in app first run
        //if so just w8 for the async task to finish then continue as normal
        // in case iam wondering where is the async task:
        //          the signout function returns an async task :)
//        FirebaseAuth.getInstance().signOut();
        if ( FirebaseAuth.getInstance().getCurrentUser() == null) FirebaseAuth.getInstance().signInAnonymously();

        Log.e("firebase", "onCreate: " + FirebaseAuth.getInstance().getCurrentUser());
        realmUtils =  new RealmUtils(this);
        countryCodePicker = findViewById(R.id.country_picker);
        countryCodePicker.setOnCountryChangeListener(this);
        if (Initializer.userPreferences == null)
        {
            Initializer.userPreferences =new UserPreferences();
            Initializer.userPreferences.setCountry(countryCodePicker.getDefaultCountryNameCode());

            realmUtils.save(Initializer.userPreferences);
        }
        else  countryCodePicker.setCountryForNameCode(Initializer.userPreferences.getCountry());
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

    @Override
    public void navigate(int code) {
      navigation.setSelectedItemId(code);
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

    public boolean redirectIfNotAuth(Fragment fragment)
    {
        if (FirebaseAuth.getInstance().getCurrentUser() ==null) {
            Utils.showToast(getString(R.string.please_wait), this);
            FirebaseAuth.getInstance().signInAnonymously();
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
    public boolean redirectIfNotAuth(Class<?> cls)
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
            navigate(cls);
        return true;
    }
    private void test()
    {
    }


    @Override
    public void onCountrySelected() {
        realmUtils.getRealm().beginTransaction();
        Initializer.userPreferences.setCountry(countryCodePicker.getSelectedCountryNameCode());
        realmUtils.getRealm().commitTransaction();
        if(activeFragment instanceof CountryFragment) ((CountryFragment)activeFragment).onCountryChanged( Initializer.userPreferences.getCountry());
    }
}
