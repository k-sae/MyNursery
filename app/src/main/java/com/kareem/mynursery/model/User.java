package com.kareem.mynursery.model;

import android.util.Log;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.Exclude;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.kareem.mynursery.model.FirebaseParser.KeyList;
import com.kareem.mynursery.model.FirebaseParser.ObjectParser;

import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.ArrayList;

/**
 * Created by kareem on 9/14/17.
 */

@SuppressWarnings({"DefaultFileTemplate", "WeakerAccess"})
public class User extends  RealTimeObject{
    @Exclude
    private static final String REFERENCE_NAME = "users";
    //database objects
    private String name = "";
    private long type = 1;
    @KeyList
    private ArrayList<String> nurseries = new ArrayList<>();
    @KeyList
    private ArrayList<String> favourites = new ArrayList<>();
    //end of database Objects
    //TODO
    public static User get(String id){
        User user = new User();
        user.setId(id);
        user.updateData();
        return user;
    }
    public static User get(String id, final ObjectChangedListener objectChangedListener){
        User user = new User(){
            @Override
            public void onChange(RealTimeObject newObject) {
                super.onChange(newObject);
                objectChangedListener.onChange(newObject);
            }
        };
        user.setId(id);
        user.updateData();
        return user;
    }
    @Exclude
    public void update(String key, Object value)
    {
        DatabaseReference databaseReference = FirebaseDatabase.getInstance().getReference();
        if (getId() != null)
        {
            databaseReference.child(REFERENCE_NAME).child(getId()).child(key).setValue(value, this);
        }
    }
    //override this in order to listen to update event
    //most of cases just ignore it
    @Exclude
    public void addNursery(String nurseryId)
    {
        DatabaseReference databaseReference = FirebaseDatabase.getInstance().getReference();
        databaseReference.child(REFERENCE_NAME).child(getId()).child("nurseries").child(nurseryId).setValue(true, this);
    }


    //override this in order to be notified upon user updates

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public long getType() {
        return type;
    }

    public void setType(int type) {
        this.type = type;
    }

    @Exclude
    public ArrayList<String> getNurseries() {
        return nurseries;
    }

    public void setNurseries(ArrayList<String> nurseries) {
        this.nurseries = nurseries;
    }

    @Override
    protected String getReferenceName() {
        return REFERENCE_NAME;
    }

    public ArrayList<String> getFavourites() {
        return favourites;
    }

    public void addFavourite(String nurseryId)
    {
        DatabaseReference databaseReference = FirebaseDatabase.getInstance().getReference();
        databaseReference.child(REFERENCE_NAME).child(getId()).child("favourites").child(nurseryId).setValue(true, this);

    }
}
