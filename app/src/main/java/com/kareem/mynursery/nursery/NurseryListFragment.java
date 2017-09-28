package com.kareem.mynursery.nursery;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
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

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
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
    private final static int FILTER_CODE = 122;
    private  HashMap<String,String> filters;
    private ArrayList<Nursery> nurseries;
    private Location location;
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
        nurseries = new ArrayList<>();
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
        view.findViewById(R.id.filter_textView).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                    Intent intent = new Intent(parent, FilterActivity.class);
                    startActivityForResult(intent, FILTER_CODE);
            }
        });
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
        nurseries.clear();
        for (DataSnapshot snapshot : dataSnapshot.getChildren()
                ) {
            Nursery nursery = new ObjectParser().getValue(Nursery.class, snapshot);
            nursery.setId(snapshot.getKey());
            nurseries.add(nursery);
        }
        update();
    }

    @Override
    public void onCancelled(DatabaseError databaseError) {

    }

    @Override
    public void onLocationChange(Location location) {
        this.location = location;
        update();
    }

    @SuppressWarnings("unchecked")
    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode !=  Activity.RESULT_OK) return;
        if (requestCode == FILTER_CODE)
        {
            filters = (HashMap<String, String>) data.getSerializableExtra("filter");
            update();
        }
    }

    private void update()
    {
        new Thread(new Runnable() {
            @Override
            public void run() {

                myNurseryRecyclerViewAdapter.getmValues().clear();
                for (Nursery nursery: nurseries
                        ) {
                    if(NurseryListFragment.this.location != null){
                        Location location = new Location("loc A");
                        location.setLongitude(nursery.getLongitude());
                        location.setLatitude(nursery.getLatitude());
                        nursery.setDistanceFromUser((double) location.distanceTo(NurseryListFragment.this.location ));
                    }
                    if (isBelongs(nursery)) myNurseryRecyclerViewAdapter.getmValues().add(nursery);
                }
                sort((ArrayList<Nursery>) myNurseryRecyclerViewAdapter.getmValues());
                parent.runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        myNurseryRecyclerViewAdapter.notifyDataSetChanged();
                    }
                });
            }
        }).start();

    }

    private void sort(ArrayList<Nursery> sortedList) {
        Comparator<Nursery> nurseryComparator = new Comparator<Nursery>() {
            @Override
            public int compare(Nursery nursery, Nursery t1) {
                double distance1 = nursery.getDistanceFromUser();
                double distance2  = t1.getDistanceFromUser();
                if (distance1 > distance2) return 1;
                else if (distance1 == distance2) return 0;
                else return -1;
            }
        };
        if (location != null) Collections.sort(sortedList, nurseryComparator);
    }



    public boolean isBelongs(Nursery nursery) {
            return filters == null
                || compareTo(nursery.getCity(), "city")
                && compareTo(nursery.getCity(), "government")
                && compareTo(nursery.getBuilding(), "block")
                && compareTo(nursery.getMinAge() + "", "age_from")
                && compareTo(nursery.getMaxAge() + "", "age_to")
                && compareTo(nursery.getStartTime()+ "", "time_from")
                && compareTo(nursery.getEndTime()+ "", "time_to");

    }

    private boolean compareTo(String s1, String key) {
        return filters.get(key).equals("")
                || s1.toLowerCase().contains(filters.get(key).toLowerCase());
    }
}
