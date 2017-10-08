package com.kareem.mynursery.model;

import android.content.Context;
import android.support.annotation.Nullable;

import io.realm.Realm;
import io.realm.RealmConfiguration;
import io.realm.RealmResults;

/**
 * Created by kareem on 10/8/17.
 */

public class RealmUtils {

    private Realm realm;
    public RealmUtils(Context context) {
        realm = Realm.getInstance(new RealmConfiguration.Builder(context)
                .deleteRealmIfMigrationNeeded()
                .name("user_preference").build());
    }
    public void save(UserPreferences user)
    {
        clearRealmItems();
        realm.beginTransaction();
        realm.copyToRealm( user);
        realm.commitTransaction();
        realm.close();
    }
    public void  clearRealmItems()
    {
        RealmResults<UserPreferences> realmResults = realm.where(UserPreferences.class).findAll();
        realm.beginTransaction();
        realmResults.clear();
        realm.commitTransaction();
    }

    @Nullable
    public UserPreferences getUserPreference()
    {
        RealmResults<UserPreferences> userPreferences = realm.where(UserPreferences.class).findAll();
        if (userPreferences.size() > 0) {
            return (userPreferences.get(0));
        }
        else return null;
    }
    public Realm getRealm() {
        return realm;
    }

}
