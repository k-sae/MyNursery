package com.kareem.mynursery.model;

import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.Exclude;
import com.google.firebase.database.FirebaseDatabase;

import java.util.ArrayList;

/**
 * Created by kareem on 9/14/17.
 */

public class User implements DatabaseReference.CompletionListener{
    private static final String REFRENCE_NAME = "users";
    private String id;
    private String name = "";
    private int type = 1;
    private ArrayList<String> nurseriesId = new ArrayList<>();

    public static User get(String id){
        return new User();
    }

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

    @Exclude
    public ArrayList<String> getNurseriesId() {
        return nurseriesId;
    }

    public void setNurseriesId(ArrayList<String> nurseriesId) {
        this.nurseriesId = nurseriesId;
    }

    @Exclude
    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public void save()
    {
        DatabaseReference databaseReference = FirebaseDatabase.getInstance().getReference();
        if (id != null)
        {
            databaseReference.child(REFRENCE_NAME).child(id).setValue(this, this);
        }
    }
    //override this in order to listen to update event
    //most of cases just ignore it
    @Override
    public void onComplete(DatabaseError databaseError, DatabaseReference databaseReference) {

    }
    public void addNursery(String nurseryId)
    {
        DatabaseReference databaseReference = FirebaseDatabase.getInstance().getReference();
        databaseReference.child(REFRENCE_NAME).child(id).child("nurseries").setValue(nurseryId, this);
    }
}
