package com.kareem.mynursery.model;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.Exclude;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.lang.reflect.Field;
import java.util.ArrayList;

/**
 * Created by kareem on 9/14/17.
 */

@SuppressWarnings({"DefaultFileTemplate", "WeakerAccess"})
public class User implements DatabaseReference.CompletionListener, RealTimeObject<User>{
    private static final String REFERENCE_NAME = "users";
    //database objects
    private String id;
    private String name = "";
    private long type = 1;
    private ArrayList<String> nurseries = new ArrayList<>();
    //end of database Objects
    //TODO
    public static User get(String id){
        return new User();
    }

    /**
     *   using this method will cause the nurseries to be overwrited
     *   use update instead
      */
    @Exclude
    @Deprecated
    public void save()
    {
        DatabaseReference databaseReference = FirebaseDatabase.getInstance().getReference();
        if (id != null)
        {
            databaseReference.child(REFERENCE_NAME).child(id).setValue(this, this);
        }
    }

    @Exclude
    public void update(String key, Object value)
    {
        DatabaseReference databaseReference = FirebaseDatabase.getInstance().getReference();
        if (id != null)
        {
            databaseReference.child(REFERENCE_NAME).child(id).child(key).setValue(value, this);
        }
    }
    //override this in order to listen to update event
    //most of cases just ignore it
    @Exclude
    @Override
    public void onComplete(DatabaseError databaseError, DatabaseReference databaseReference) {

    }
    @Exclude
    public void addNursery(String nurseryId)
    {
        DatabaseReference databaseReference = FirebaseDatabase.getInstance().getReference();
        databaseReference.child(REFERENCE_NAME).child(id).child("nurseries").child(nurseryId).setValue(true, this);
    }

    @Exclude
    public void startSync()
    {
        FirebaseDatabase.getInstance().getReference().child(REFERENCE_NAME).child(id).addValueEventListener(new ValueEventListener() {
            @SuppressWarnings("TryWithIdenticalCatches")
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                for (DataSnapshot snapshot: dataSnapshot.getChildren()
                     ) {
                    try {

                        if (!snapshot.getKey().equals("nurseries")) {
                            Field field = User.class.getDeclaredField(snapshot.getKey());
                            field.setAccessible(true);
                            field.set(User.this, snapshot.getValue());
                            field.setAccessible(false);
                        }
                        else {
                            for (DataSnapshot subSnap :
                                    snapshot.getChildren()) {
                                nurseries.add(subSnap.getKey());
                            }
                        }
                    } catch (IllegalAccessException e) {
                        e.printStackTrace();
                    } catch (NoSuchFieldException e) {
                        e.printStackTrace();
                    }
                }

                onChange(User.this);
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });

    }

    //override this in order to be notified upon user updates
    @Exclude
    @Override
    public void onChange(User newObject) {
    }


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

    @Exclude
    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }



}
