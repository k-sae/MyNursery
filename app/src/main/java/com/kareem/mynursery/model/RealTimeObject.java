package com.kareem.mynursery.model;

import android.util.Log;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.Exclude;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.kareem.mynursery.model.FirebaseParser.ObjectParser;

/**
 * Created by kareem on 9/14/17.
 */

public abstract class RealTimeObject implements DatabaseReference.CompletionListener{
    private String id;
    private ObjectChangedListener objectChangedListener;
    @Exclude
    public void startSync()
    {
        FirebaseDatabase.getInstance().getReference().child(getReferenceName()).child(id).addValueEventListener(new ValueEventListener() {
            @SuppressWarnings("TryWithIdenticalCatches")
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                new ObjectParser().getValue(RealTimeObject.this, dataSnapshot);
                onChange(RealTimeObject.this);
                if (objectChangedListener != null) objectChangedListener.onChange(RealTimeObject.this);
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });

    }

    public void updateData()
    {
        FirebaseDatabase.getInstance().getReference().child(getReferenceName()).child(id).addListenerForSingleValueEvent(new ValueEventListener() {
            @SuppressWarnings("TryWithIdenticalCatches")
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                new ObjectParser().getValue(RealTimeObject.this, dataSnapshot);
                onChange(RealTimeObject.this);
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });
    }

    public  void onChange(RealTimeObject newObject){
        Log.e("onChange: ", newObject.getId() );
    }

    public void save()
    {
        DatabaseReference databaseReference = FirebaseDatabase.getInstance().getReference();
        if (getId() != null)
        {
            databaseReference.child(getReferenceName()).child(getId()).updateChildren(new ObjectParser().mapObject(this), this);
        }
        else
        {
            DatabaseReference reference = databaseReference.child(getReferenceName()).push();
            setId(reference.getKey());
            reference.setValue(new ObjectParser().mapObject(this), this);
        }
    }

    protected abstract String getReferenceName();
    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    @Override
    public void onComplete(DatabaseError databaseError, DatabaseReference databaseReference) {

    }

    public void setOnChangeListener(ObjectChangedListener objectChangedListener) {
        this.objectChangedListener = objectChangedListener;
    }
}
