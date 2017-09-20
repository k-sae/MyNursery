package com.kareem.mynursery.home;


import android.app.Activity;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.kareem.mynursery.NavigationContext;
import com.kareem.mynursery.R;
import com.kareem.mynursery.nursery.NurserySearchResults;

/**
 * A simple {@link Fragment} subclass.
 */
public class HomeFragment extends Fragment implements View.OnClickListener {
    private Activity parentActivity;

    public HomeFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view  = inflater.inflate(R.layout.fragment_home, container, false);
        view.findViewById(R.id.search_for_nursery).setOnClickListener(this);
        return view;
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
                ((NavigationContext)parentActivity).navigate(NurserySearchResults.class);
        }
    }
}
