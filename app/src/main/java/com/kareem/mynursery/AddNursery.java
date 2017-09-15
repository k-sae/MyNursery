package com.kareem.mynursery;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.kareem.mynursery.model.Nursery;

import java.util.ArrayList;

public class AddNursery extends AppCompatActivity {


    private  final String ADD_NURSERY="ADD_NURSERY";
    private final String EDIT_NURSERY="EDIT_NURSERY";
    private Intent intent;
    private ArrayList<String> pickedImagesPaht;
    private ArrayList<String>  activities;
    private Nursery nurseryObj;

    //Form Buttons
    Button addPic , addLocation , addNursery;

    EditText nurseryName , nurseryDescription , startTime , endTime,
             phone1 , phone2 , facebook , instagram , snapchat , price,
             minAge , maxAge , additionalActivities ;

    CheckBox swimming , drawing, football ;

    String nurseryNameData ,nurseryDescriptionData , startTimeData , endTimeData,
            phone1Data , phone2Data , facebookData , instagramData , snapchatData ,
             additionalActivitiesData ;
    double  priceData;
    int minAgeData , maxAgeData;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_nursery);
        this.intent=getIntent();
        this.setComponents();
        if (this.intent.getAction().equals(this.EDIT_NURSERY))
            editRender();//TODO fill the form with the nursery data
        else
            render();
        String s = nurseryName.getText().toString();
        Toast toast =  Toast.makeText(this,s,Toast.LENGTH_LONG);
        toast.show();
    }

    public void editRender(){

    }

    public void render(){

    }

    private void setComponents(){

        addPic = (Button) findViewById(R.id.addNurseryAddPic_btn);
        addLocation = (Button) findViewById(R.id.addNurseryAddLocation_btn);
        addNursery = (Button) findViewById(R.id.addNurseryAdd_btn);
        nurseryName = (EditText) findViewById(R.id.addNurseryName);
        nurseryDescription = (EditText) findViewById(R.id.addNurseryDescription);
        startTime = (EditText) findViewById(R.id.addNurseryStartTime);
        endTime= (EditText) findViewById(R.id.addNurseryEndTime);
        phone1= (EditText) findViewById(R.id.addNurseryPhone1);
        phone2 = (EditText) findViewById(R.id.addNurseryPhone2);
        facebook = (EditText) findViewById(R.id.addNurseryFacebook);
        instagram = (EditText) findViewById(R.id.addNurseryInstagram);
        snapchat= (EditText) findViewById(R.id.addNurserySnapchat);
        price= (EditText) findViewById(R.id.addNurseryExpenses);
        minAge = (EditText) findViewById(R.id.addNurseryMinAge);
        maxAge = (EditText) findViewById(R.id.addNurseryMaxAge);
        additionalActivities = (EditText) findViewById(R.id.addNursery_activity_additional);
        swimming = (CheckBox) findViewById(R.id.addNursery_activity_swim);
        football =(CheckBox) findViewById(R.id.addNursery_activity_football);
        drawing = (CheckBox) findViewById(R.id.addNursery_activity_draw);
    }

    private void featchData(){
        nurseryNameData = nurseryName.getText().toString();
        nurseryDescriptionData = nurseryDescription.getText().toString();
        startTimeData = startTime.getText().toString();
        endTimeData = endTime.getText().toString();
        phone1Data = phone1.getText().toString();
        phone2Data = phone2.getText().toString();
        facebookData = facebook.getText().toString();
        instagramData = instagram.getText().toString();
        snapchatData = snapchat.getText().toString();
        priceData = Double.parseDouble(price.getText().toString());
        minAgeData = Integer.parseInt(minAge.getText().toString());
        maxAgeData = Integer.parseInt(maxAge.getText().toString());
        additionalActivitiesData = additionalActivities.getText().toString();

    }

    public void parseToNursery(){
        nurseryObj = new Nursery();
        nurseryObj
    }




}
