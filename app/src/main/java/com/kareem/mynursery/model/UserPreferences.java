package com.kareem.mynursery.model;

import io.realm.RealmObject;

/**
 * Created by kareem on 10/8/17.
 */

public class UserPreferences extends RealmObject{
    private String country;

    public String getCountry() {
        return country;
    }

    public void setCountry(String country) {
        this.country = country;
    }
}
