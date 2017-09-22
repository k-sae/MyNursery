package com.kareem.mynursery.nursery;

import android.app.Activity;
import android.content.Context;
import android.location.Location;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.kareem.mynursery.LocationTrackerFragment;
import com.kareem.mynursery.R;
import com.kareem.mynursery.model.FirebaseParser.ObjectParser;
import com.kareem.mynursery.model.Nursery;
import com.kareem.mynursery.model.RealTimeObject;

import java.util.List;

import static android.content.ContentValues.TAG;

/**
 * A fragment representing a list of Items.
 * <p/>

 * interface.
 */
public class NurseryListFragment extends Fragment implements ValueEventListener, LocationTrackerFragment {

    // TODO: Customize parameter argument names
    private static final String ARG_COLUMN_COUNT = "column-count";
    // TODO: Customize parameters
    private int mColumnCount = 1;
    private Activity parent;
    private MyNurseryRecyclerViewAdapter myNurseryRecyclerViewAdapter;
    /**
     * Mandatory empty constructor for the fragment manager to instantiate the
     * fragment (e.g. upon screen orientation changes).
     */
    public NurseryListFragment() {
    }

    // TODO: Customize parameter initialization
    @SuppressWarnings("unused")
    public static NurseryListFragment newInstance(int columnCount) {
        NurseryListFragment fragment = new NurseryListFragment();
        Bundle args = new Bundle();
        args.putInt(ARG_COLUMN_COUNT, columnCount);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        if (getArguments() != null) {
            mColumnCount = getArguments().getInt(ARG_COLUMN_COUNT);
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_nursery_list, container, false);

        myNurseryRecyclerViewAdapter = new MyNurseryRecyclerViewAdapter(parent);
        myNurseryRecyclerViewAdapter.notifyDataSetChanged();
            RecyclerView recyclerView = (RecyclerView) view.findViewById(R.id.list);
            recyclerView.setAdapter(myNurseryRecyclerViewAdapter);
        startSync();
        return view;
    }

    private void startSync() {
            FirebaseDatabase.getInstance().getReference().child(Nursery.REFERENCE_NAME).addValueEventListener(this);

    }

    @Override
    public void onDestroy() {
        super.onDestroy();
    }

    @Override
    public void onAttach(Activity activity) {
        super.onAttach(activity);
        parent = activity;
    }

    @Override
    public void onDetach() {
        super.onDetach();
    }

    //TODO check if it will make any difference when removing listener
    @Override
    public void onDataChange(DataSnapshot dataSnapshot) {
        myNurseryRecyclerViewAdapter.getmValues().clear();
        for (DataSnapshot snapshot : dataSnapshot.getChildren()
                ) {
            myNurseryRecyclerViewAdapter.getmValues().add(new ObjectParser().getValue(Nursery.class, snapshot));
        }
        myNurseryRecyclerViewAdapter.notifyDataSetChanged();
    }

    @Override
    public void onCancelled(DatabaseError databaseError) {

    }

    @Override
    public void onLocationChange(Location location) {
        Log.e(TAG, "onLocationChange: " + location.getLatitude());
    }
}
