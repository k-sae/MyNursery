package com.kareem.mynursery.home;


import android.content.Intent;
import android.location.Location;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.kareem.mynursery.LocationTrackerFragment;
import com.kareem.mynursery.R;
import com.kareem.mynursery.Utils;
import com.kareem.mynursery.model.Auth;
import com.kareem.mynursery.model.Nursery;
import com.kareem.mynursery.model.ObjectChangedListener;
import com.kareem.mynursery.model.RealTimeObject;
import com.kareem.mynursery.model.User;
import com.kareem.mynursery.nursery.FilterActivity;
import com.kareem.mynursery.nursery.MyNurseryRecyclerViewAdapter;
import com.kareem.mynursery.nursery.NurseryListFragment;

import java.util.ArrayList;

/**
 * A simple {@link Fragment} subclass.
 */
public class FavouriteFragment extends Fragment implements ObjectChangedListener, LocationTrackerFragment
{
    private MyNurseryRecyclerViewAdapter myNurseryRecyclerViewAdapter;
    private User loggedUser;
    private Location location;
    public FavouriteFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_favourits, container, false);
        myNurseryRecyclerViewAdapter = new MyNurseryRecyclerViewAdapter(getActivity());
        myNurseryRecyclerViewAdapter.notifyDataSetChanged();
        RecyclerView recyclerView = (RecyclerView) view.findViewById(R.id.list);
        recyclerView.setAdapter(myNurseryRecyclerViewAdapter);
        startSync();
        onChange(loggedUser);
        return view;
    }
    private void startSync() {
        User user = Auth.getLoggedUser();
        user.setOnChangeListener(this);
        user.startSync();
        loggedUser = user;
    }

    @Override
    public void onChange(RealTimeObject realTimeObject) {
        if (realTimeObject instanceof User) {
            myNurseryRecyclerViewAdapter.getmValues().clear();
            for (String nurseryID : loggedUser.getFavourites()
                    ) {
                Nursery nursery = new Nursery();
                nursery.setId( nurseryID);
                setNurseryLocation(nursery);
                myNurseryRecyclerViewAdapter.getmValues().add(nursery);
                nursery.setOnChangeListener(this);
                nursery.updateData();
            }
        }
        else myNurseryRecyclerViewAdapter.notifyDataSetChanged();

    }

    @Override
    public void onLocationChange(Location location) {
        this.location = location;
        update();
    }
    private void update()
    {
        for (Nursery nursery: myNurseryRecyclerViewAdapter.getmValues()
             ) {
            setNurseryLocation(nursery);
        }
        myNurseryRecyclerViewAdapter.notifyDataSetChanged();
    }
    private void setNurseryLocation(Nursery nursery)
    {
        if(FavouriteFragment.this.location != null){
            Location location = new Location("loc A");
            location.setLongitude(nursery.getLongitude());
            location.setLatitude(nursery.getLatitude());
            nursery.setDistanceFromUser(Utils.calculateDistance(location, FavouriteFragment.this.location));
        }
    }
}
