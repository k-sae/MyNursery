package com.kareem.mynursery.model;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.Exclude;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.kareem.mynursery.model.FirebaseParser.KeyList;
import com.kareem.mynursery.model.FirebaseParser.ObjectParser;

import java.util.ArrayList;

/**
 * Created by kareem on 9/14/17.
 */

public class Nursery extends RealTimeObject{

    @Exclude
    private static final String REFERENCE_NAME = "nurseries";

    @KeyList
    private ArrayList<String> likes = new ArrayList<>();
    @KeyList
    private ArrayList<Comment> comments = new ArrayList<>();

    // area info

    private double longitude =  0;
    private double latitude = 0;
    private String govenment = "";
    private String city  = "";
    private String district = "";
    private String street = "";
    private String building = "";
    private String moreDetails = "";

    // contact info
    private String phone1 = "";
    private String phone2 = "";
    private String whatsapp = "";
    private String facebook  = "";
    private String instagram = "";
    private String snapchat = "";

    //profile info
    private String name = "";
    private String description = "";
    private double expenses = 0;
    private long minAge = 0;
    private long maxAge = 0;
    private String startTime = "";
    private String endTime = "";
    private boolean isSupportingDisablilites;
    private ArrayList<String> imagesId = new ArrayList<>();
    @KeyList
    private ArrayList<String> activities = new ArrayList<>();
    //custom access info
    private boolean isSponsored;
    private String sponsershipEndDate;




    public double getLongitude() {
        return longitude;
    }

    public void setLongitude(double longitude) {
        this.longitude = longitude;
    }

    public double getLatitude() {
        return latitude;
    }

    public void setLatitude(double latitude) {
        this.latitude = latitude;
    }

    public String getGovenment() {
        return govenment;
    }

    public void setGovenment(String govenment) {
        this.govenment = govenment;
    }

    public String getCity() {
        return city;
    }

    public void setCity(String city) {
        this.city = city;
    }

    public String getDistrict() {
        return district;
    }

    public void setDistrict(String district) {
        this.district = district;
    }

    public String getStreet() {
        return street;
    }

    public void setStreet(String street) {
        this.street = street;
    }

    public String getBuilding() {
        return building;
    }

    public void setBuilding(String building) {
        this.building = building;
    }

    public String getMoreDetails() {
        return moreDetails;
    }

    public void setMoreDetails(String moreDetails) {
        this.moreDetails = moreDetails;
    }

    public String getPhone1() {
        return phone1;
    }

    public void setPhone1(String phone1) {
        this.phone1 = phone1;
    }

    public String getPhone2() {
        return phone2;
    }

    public void setPhone2(String phone2) {
        this.phone2 = phone2;
    }

    public String getWhatsapp() {
        return whatsapp;
    }

    public void setWhatsapp(String whatsapp) {
        this.whatsapp = whatsapp;
    }

    public String getFacebook() {
        return facebook;
    }

    public void setFacebook(String facebook) {
        this.facebook = facebook;
    }

    public String getInstagram() {
        return instagram;
    }

    public void setInstagram(String instagram) {
        this.instagram = instagram;
    }

    public String getSnapchat() {
        return snapchat;
    }

    public void setSnapchat(String snapchat) {
        this.snapchat = snapchat;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public double getExpenses() {
        return expenses;
    }

    public void setExpenses(double expenses) {
        this.expenses = expenses;
    }

    public long getMinAge() {
        return minAge;
    }

    public void setMinAge(int minAge) {
        this.minAge = minAge;
    }

    public long getMaxAge() {
        return maxAge;
    }

    public void setMaxAge(int maxAge) {
        this.maxAge = maxAge;
    }

    public String getStartTime() {
        return startTime;
    }

    public void setStartTime(String startTime) {
        this.startTime = startTime;
    }

    public String getEndTime() {
        return endTime;
    }

    public void setEndTime(String endTime) {
        this.endTime = endTime;
    }

    public boolean isSupportingDisablilites() {
        return isSupportingDisablilites;
    }

    public void setSupportingDisablilites(boolean supportingDisablilites) {
        isSupportingDisablilites = supportingDisablilites;
    }

    public ArrayList<String> getImagesId() {
        return imagesId;
    }

    public void setImagesId(ArrayList<String> imagesId) {
        this.imagesId = imagesId;
    }

    public ArrayList<String> getActivities() {
        return activities;
    }

    public void setActivities(ArrayList<String> activities) {
        this.activities = activities;
    }

    public boolean isSponsored() {
        return isSponsored;
    }

    public void setSponsored(boolean sponsored) {
        isSponsored = sponsored;
    }

    public String getSponsershipEndDate() {
        return sponsershipEndDate;
    }

    public void setSponsershipEndDate(String sponsershipEndDate) {
        this.sponsershipEndDate = sponsershipEndDate;
    }

    @Override
    protected String getReferenceName() {
        return REFERENCE_NAME;
    }
}
