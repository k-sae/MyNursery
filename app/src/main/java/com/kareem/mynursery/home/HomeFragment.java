package com.kareem.mynursery.home;


import android.app.Activity;
import android.content.Intent;
import android.location.Location;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.daimajia.slider.library.SliderLayout;
import com.daimajia.slider.library.SliderTypes.BaseSliderView;
import com.daimajia.slider.library.Tricks.ViewPagerEx;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.kareem.mynursery.AuthorizationNavigationContext;
import com.kareem.mynursery.CountryFragment;
import com.kareem.mynursery.GlideSliderView;
import com.kareem.mynursery.Initializer;
import com.kareem.mynursery.NavigationContext;
import com.kareem.mynursery.R;
import com.kareem.mynursery.Utils;
import com.kareem.mynursery.model.Auth;
import com.kareem.mynursery.model.FirebaseParser.ObjectParser;
import com.kareem.mynursery.model.Nursery;
import com.kareem.mynursery.model.ObjectChangedListener;
import com.kareem.mynursery.model.RealTimeObject;
import com.kareem.mynursery.nursery.AddNursery;
import com.kareem.mynursery.nursery.NurseryListActivity;
import com.kareem.mynursery.nurseryProfile.NurseryProfileActivity;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;

/**
 * A simple {@link Fragment} subclass.
 */
public class HomeFragment extends Fragment implements ViewPagerEx.OnPageChangeListener, View.OnClickListener, ValueEventListener , CountryFragment{
    private Activity parentActivity;
    private SliderLayout sliderLayout;
    private ArrayList<Nursery> nurseries;
    private ArrayList<Nursery> filteredNurseries;
    private Location mLocation = new Location("A");
    private TextView titleTextView;
    private TextView locationTextView;
    private View addNurseryButton;
    public HomeFragment() {
        // Required empty public constructor
    }
    private int sliderCount = 0;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_home, container, false);
        sliderLayout = view.findViewById(R.id.slider);
        view.findViewById(R.id.search_for_nursery).setOnClickListener(this);
        addNurseryButton = view.findViewById(R.id.profileAddNursery);
        updateAddNurseryButton();

        titleTextView = view.findViewById(R.id.title);
        locationTextView = view.findViewById(R.id.location);
        sliderLayout.addOnPageChangeListener(this);
        nurseries = new ArrayList<>();
        filteredNurseries = new ArrayList<>();
        startSync();
        return view;
    }

    private void startSync() {
        FirebaseDatabase.getInstance().getReference().child(Nursery.REFERENCE_NAME).addValueEventListener(this);

    }

    public void onDataChange(DataSnapshot dataSnapshot) {
        clearSliders();
        nurseries.clear();
        for (DataSnapshot snapshot : dataSnapshot.getChildren()
                ) {
            Nursery nursery = new ObjectParser().getValue(Nursery.class, snapshot);
            nursery.setId(snapshot.getKey());
            nurseries.add(nursery);
            try {
                if (isBelongs(nursery)) addSlider(nursery);

            } catch (ParseException e) {
                e.printStackTrace();
            }
        }
        checkForAvailability();
    }

    private void checkForAvailability() {
        if (sliderCount == 0)
        {
            //TODO
            //add slider contain the error message
            sliderLayout.addSlider( Utils.getImageNotFoundSlider(parentActivity));
        }
    }

    private void addSlider(final Nursery nursery) throws ParseException {
        if (nursery.getImagesId().size() < 1 || nursery.getSponsorshipEndDate().equals("")) return;
        SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy");
        Date strDate = sdf.parse(nursery.getSponsorshipEndDate());
        if (new Date().after(strDate)) return;
        filteredNurseries.add(nursery);
        sliderLayout.addSlider(new GlideSliderView(parentActivity).image(Nursery.BASE_IMAGE_URL + nursery.getImagesId().get(0)).setOnSliderClickListener(new BaseSliderView.OnSliderClickListener() {
            @Override
            public void onSliderClick(BaseSliderView slider) {
                Intent intent = new Intent(parentActivity, NurseryProfileActivity.class);
                intent.putExtra("NurseryId", nursery.getId());
                parentActivity.startActivity(intent);
            }
        }));
        sliderCount++;
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
        switch (view.getId()) {
            case R.id.search_for_nursery:
                ((NavigationContext) parentActivity).navigate(NurseryListActivity.class);
                break;
            case R.id.profileAddNursery:
                ((AuthorizationNavigationContext) parentActivity).redirectIfNotAuth(AddNursery.class);
                break;
        }
    }

    @Override
    public void onStop() {
        sliderLayout.stopAutoCycle();
        super.onStop();
    }

    @Override
    public void onStart() {
        super.onStart();
        if (sliderLayout != null) sliderLayout.startAutoCycle();
        if (Auth.getLoggedUser() != null)
            Auth.getLoggedUser().setOnChangeListener(new ObjectChangedListener() {
                @Override
                public void onChange(RealTimeObject realTimeObject) {
                    updateAddNurseryButton();
                    updateFiltered();
                }
            });

    }

    @Override
    public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

    }

    @Override
    public void onPageSelected(int position) {
        if (filteredNurseries.size() < 1) {
            titleTextView.setText(R.string.no_sponsored_nurseries);
            locationTextView.setText("");
            return;
        }
        titleTextView.setText(filteredNurseries.get(position).getName());
        String distance ="";
        if (Utils.location == null) distance += "~";
        else {
            mLocation.setLatitude(filteredNurseries.get(position).getLatitude());
            mLocation.setLongitude(filteredNurseries.get(position).getLongitude());
            distance += Utils.calculateDistance(mLocation, Utils.location);
        }
        distance += " " + parentActivity.getString(R.string.km);
        locationTextView.setText(distance);
    }

    @Override
    public void onPageScrollStateChanged(int state) {

    }
    private void updateAddNurseryButton(){
        if (Auth.getLoggedUser() == null || Auth.getLoggedUser().getType() == 3)
           addNurseryButton.setVisibility(View.GONE);
        else {
            addNurseryButton.setVisibility(View.VISIBLE);
            addNurseryButton.setOnClickListener(this);
        }
    }
    private void updateFiltered(){
        if (sliderLayout == null) return;
        clearSliders();
        for (Nursery nursery: nurseries
             ) {
            try {
                if(isBelongs(nursery))   addSlider(nursery);
            } catch (ParseException e) {
                e.printStackTrace();
            }
        }
        checkForAvailability();
    }

    private void clearSliders() {
        sliderLayout.removeAllSliders();
        filteredNurseries.clear();
        sliderCount = 0;
    }

    private boolean isBelongs(Nursery nursery) {
        return ((nursery.getCountry().equalsIgnoreCase(Initializer.userPreferences.getCountry())
                ||(Auth.getLoggedUser() != null)
                && Auth.getLoggedUser().getType() == 2));
    }

    @Override
    public void onCountryChanged(String countryCode) {
        updateFiltered();
    }
}
