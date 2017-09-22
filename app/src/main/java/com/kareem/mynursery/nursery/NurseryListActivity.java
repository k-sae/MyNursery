package com.kareem.mynursery.nursery;

import android.location.Location;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v4.app.Fragment;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;

import com.kareem.mynursery.LocationTrackerActivity;
import com.kareem.mynursery.LocationTrackerFragment;
import com.kareem.mynursery.R;

public class NurseryListActivity extends LocationTrackerActivity {

    final static String TAG = NurseryListActivity.class.getName();
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_nursery_list);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

    }

    @Override
    protected void onLocationChange(Location location) {
        //no need for implementation because iam using a fragment
    }

}
