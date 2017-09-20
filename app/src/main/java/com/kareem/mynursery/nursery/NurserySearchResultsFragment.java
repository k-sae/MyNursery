package com.kareem.mynursery.nursery;

import android.support.v4.app.Fragment;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.kareem.mynursery.R;

/**
 * A placeholder fragment containing a simple view.
 */
public class NurserySearchResultsFragment extends Fragment {

    public NurserySearchResultsFragment() {
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_nursery_search_results, container, false);
    }
}
