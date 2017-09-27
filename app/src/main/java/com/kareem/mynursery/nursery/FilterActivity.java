package com.kareem.mynursery.nursery;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.EditText;
import android.widget.Switch;

import com.kareem.mynursery.R;

import java.util.HashMap;
import java.util.Map;

public class FilterActivity extends AppCompatActivity implements View.OnClickListener {

    private Switch specialNeedsSwitch;
    private EditText governmentEditText;
    private EditText cityEditText;
    private EditText blockEditText;
    private EditText neighbourhoodEditText;
    private EditText ageFrom;
    private EditText ageTo;
    private EditText timeFrom;
    private EditText timeTo;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_filter);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        populateViews();
        setListeners();
    }

    private void populateViews()
    {
        specialNeedsSwitch = findViewById(R.id.special_needs_switch);
        governmentEditText = findViewById(R.id.government);
        cityEditText = findViewById(R.id.city);
        blockEditText = findViewById(R.id.block);
        neighbourhoodEditText = findViewById(R.id.neighborhood);
        ageFrom = findViewById(R.id.age_from);
        ageTo = findViewById(R.id.age_to);
        timeFrom = findViewById(R.id.time_from);
        timeTo = findViewById(R.id.time_to);
    }
    private void setListeners()
    {
        findViewById(R.id.apply_button).setOnClickListener(this);
    }

    @Override
    public void onClick(View view) {
        HashMap<String, String> items = new HashMap<>();
        items.put("special_needs", specialNeedsSwitch.isChecked() +"" );
        items.put("government", governmentEditText.getText().toString());
        items.put("city", cityEditText.getText().toString());
        items.put("block", blockEditText.getText().toString());
        items.put("neighbourhood", neighbourhoodEditText.getText().toString());
        items.put("age_from", ageFrom.getText().toString());
        items.put("age_to", ageTo.getText().toString());
        items.put("time_from", timeFrom.getText().toString());
        items.put("time_to", timeTo.getText().toString());
        Intent intent = new Intent();
        intent.putExtra("filter", items);
        setResult(Activity.RESULT_OK, intent);
        finish();
    }
}
