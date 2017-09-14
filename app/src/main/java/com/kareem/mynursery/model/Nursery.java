package com.kareem.mynursery.model;

import java.util.ArrayList;

/**
 * Created by kareem on 9/14/17.
 */

public class Nursery {

    private ArrayList<String> likes = new ArrayList<>();
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
    private int minAge = 0;
    private int maxAge = 0;
    private String startTime = "";
    private String endTime = "";
    private boolean isSupportingDisablilites;
    private ArrayList<String> imagesId = new ArrayList<>();
    private ArrayList<String> activities = new ArrayList<>();
    //custom access info
    private boolean isSponsored;
    private String sponsershipEndDate;
}
