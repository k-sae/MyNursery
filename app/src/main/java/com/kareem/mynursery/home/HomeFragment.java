package com.kareem.mynursery.home;


import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.daimajia.slider.library.SliderLayout;
import com.daimajia.slider.library.SliderTypes.BaseSliderView;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.kareem.mynursery.GlideSliderView;
import com.kareem.mynursery.NavigationContext;
import com.kareem.mynursery.R;
import com.kareem.mynursery.model.FirebaseParser.ObjectParser;
import com.kareem.mynursery.model.Nursery;
import com.kareem.mynursery.nursery.NurseryListActivity;
import com.kareem.mynursery.nurseryProfile.NurseryProfileActivity;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * A simple {@link Fragment} subclass.
 */
public class HomeFragment extends Fragment implements View.OnClickListener, ValueEventListener {
    private Activity parentActivity;
    private SliderLayout sliderLayout;
    public HomeFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view  = inflater.inflate(R.layout.fragment_home, container, false);
        view.findViewById(R.id.search_for_nursery).setOnClickListener(this);
        sliderLayout = view.findViewById(R.id.slider);
        startSync();
        return view;
    }

    private void startSync() {
        FirebaseDatabase.getInstance().getReference().child(Nursery.REFERENCE_NAME).addValueEventListener(this);

    }

    public void onDataChange(DataSnapshot dataSnapshot) {
        sliderLayout.removeAllSliders();
        for (DataSnapshot snapshot : dataSnapshot.getChildren()
                ) {
            Nursery nursery = new ObjectParser().getValue(Nursery.class, snapshot);
            nursery.setId(snapshot.getKey());
            try {
                addSlider(nursery);
            } catch (ParseException e) {
                e.printStackTrace();
            }
        }
    }

    private void addSlider(final Nursery nursery) throws ParseException {
        if (nursery.getImagesId().size() < 1 || nursery.getSponsorshipEndDate().equals("")) return;
        SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy");
        Date strDate = sdf.parse(nursery.getSponsorshipEndDate());
        if (new Date().after(strDate))return;
        sliderLayout.addSlider(new GlideSliderView(parentActivity).image(nursery.getImagesId().get(0)).setOnSliderClickListener(new BaseSliderView.OnSliderClickListener() {
            @Override
            public void onSliderClick(BaseSliderView slider) {
                    Intent intent = new Intent(parentActivity, NurseryProfileActivity.class);
                    intent.putExtra("NurseryId", nursery.getId());
                    parentActivity.startActivity(intent);
            }
        }));
    }

    @Override
    public void onCancelled(DatabaseError databaseError) {

    }

    @Override
    public void onAttach(Activity activity) {
        super.onAttach(activity);
        parentActivity = activity;
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()){
            case R.id.search_for_nursery:
                ((NavigationContext)parentActivity).navigate(NurseryListActivity.class);
        }
    }
}
