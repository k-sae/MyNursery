package com.kareem.mynursery;

import android.app.Application;

import com.crashlytics.android.Crashlytics;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.FirebaseDatabase;
import com.kareem.mynursery.model.Auth;
import com.kareem.mynursery.model.RealmUtils;
import com.kareem.mynursery.model.UserPreferences;

import io.fabric.sdk.android.Fabric;
import net.gotev.uploadservice.UploadService;

/**
 * Created by kareem on 9/29/17.
 */
public class Initializer extends Application {
    public static UserPreferences userPreferences;
    @Override
    public void onCreate() {
        super.onCreate();
        Fabric.with(this, new Crashlytics());
        // setup the broadcast action namespace string which will
        // be used to notify upload status.
        // Gradle automatically generates proper variable as below.
        UploadService.NAMESPACE = BuildConfig.APPLICATION_ID;
        // Or, you can define it manually.
        UploadService.NAMESPACE = "com.kareem.mynursery";
        FirebaseDatabase.getInstance().setPersistenceEnabled(true);
        if ( FirebaseAuth.getInstance().getCurrentUser() == null) FirebaseAuth.getInstance().signInAnonymously();
        if (Auth.getLoggedUser() != null) Auth.getLoggedUser().startSync();
        userPreferences = new RealmUtils(this).getUserPreference();
    }
}
