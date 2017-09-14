package com.kareem.mynursery.model;

import java.util.ArrayList;

/**
 * Created by kareem on 9/14/17.
 */

public class User {
    private String name = "";
    private int type = 1;
    private ArrayList<String> nurseriesId = new ArrayList<>();

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public int getType() {
        return type;
    }

    public void setType(int type) {
        this.type = type;
    }

    public ArrayList<String> getNurseriesId() {
        return nurseriesId;
    }

    public void setNurseriesId(ArrayList<String> nurseriesId) {
        this.nurseriesId = nurseriesId;
    }
}
