package com.kareem.mynursery;

import android.app.TimePickerDialog;
import android.content.Intent;
import android.icu.text.DateFormat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.TimePicker;
import android.widget.Toast;

import com.kareem.mynursery.model.Nursery;

import java.util.ArrayList;
import java.util.Calendar;

public class AddNursery extends AppCompatActivity implements TimePickerDialog.OnTimeSetListener{


    private  final String ADD_NURSERY="ADD_NURSERY";
    private final String EDIT_NURSERY="EDIT_NURSERY";
    private Intent intent;
    private ArrayList<String> pickedImagesPath;
    private ArrayList<String>  activities;
    private Nursery nurseryObj;
    private String NurseryId;

    //Form Buttons
    Button addPic , addLocation , addNursery;

    EditText nurseryName , nurseryDescription  ,
             phone1 , phone2 , facebook , instagram , snapchat , price,
             minAge , maxAge , additionalActivities ,city ,district ,
            street  , building , notes;
    TextView startTime , endTime;

    CheckBox swimming , drawing, football , disabilites;

    String nurseryNameData ,nurseryDescriptionData , startTimeData , endTimeData,
            phone1Data , phone2Data , facebookData , instagramData , snapchatData ,
             additionalActivitiesData ,cityData ,districtData , streetData  , buildingData
            , notesData;
    double  priceData;
    long minAgeData , maxAgeData;
    boolean disabilitesData = false;
    TimePickerDialog timePickerDialog;
    private View lastClickedView;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_nursery);
        this.intent=getIntent();
        this.setComponents();
        if (this.intent.getAction().equals(this.EDIT_NURSERY)) {
            NurseryId = intent.getStringExtra("NurseryId");
            editRender();//TODO fill the form with the nursery data
        }


        //save Nursery action
        addNursery.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                fetchData();
                if (intent.getAction().equals(EDIT_NURSERY)) {

                }else {
                    Log.e("Entered","here");
                    parseToNursery();
                    nurseryObj.save();
                }



            }
        });
    }

    private void editRender(){
        parseFromNursery();
        nurseryName.setText(nurseryNameData);

        nurseryDescription .setText(nurseryDescriptionData) ;
        startTime .setText(startTimeData) ;
        endTime.setText(endTimeData) ;
        phone1.setText(phone1Data) ;
        phone2 .setText(phone2Data) ;
        facebook .setText(facebookData) ;
        instagram .setText(instagramData) ;
        snapchat.setText(snapchatData) ;
        price.setText(priceData+"") ;
        minAge .setText(minAgeData+"") ;
        maxAge .setText(maxAgeData+"") ;
        additionalActivities .setText(additionalActivitiesData) ;
        city .setText(cityData) ;
        district .setText(districtData) ;
        street .setText(streetData) ;
        building .setText(buildingData) ;
        notes .setText(notesData) ;

    }



    private void setComponents(){

        addPic = (Button) findViewById(R.id.addNurseryAddPic_btn);
        addLocation = (Button) findViewById(R.id.addNurseryAddLocation_btn);
        addNursery = (Button) findViewById(R.id.addNurseryAdd_btn);
        nurseryName = (EditText) findViewById(R.id.addNurseryName);
        nurseryDescription = (EditText) findViewById(R.id.addNurseryDescription);

        phone1= (EditText) findViewById(R.id.addNurseryPhone1);
        phone2 = (EditText) findViewById(R.id.addNurseryPhone2);
        facebook = (EditText) findViewById(R.id.addNurseryFacebook);
        instagram = (EditText) findViewById(R.id.addNurseryInstagram);
        snapchat= (EditText) findViewById(R.id.addNurserySnapchat);
        price= (EditText) findViewById(R.id.addNurseryExpenses);
        minAge = (EditText) findViewById(R.id.addNurseryMinAge);
        maxAge = (EditText) findViewById(R.id.addNurseryMaxAge);
        additionalActivities = (EditText) findViewById(R.id.addNursery_activity_additional);
        city = (EditText) findViewById(R.id.addNurseryCity);
        district = (EditText) findViewById(R.id.addNurseryDistrict);
        street = (EditText) findViewById(R.id.addNurseryStreet);
        building = (EditText) findViewById(R.id.addNurseryBuilding);
        notes = (EditText) findViewById(R.id.addNurseryNotes);

        startTime = (TextView) findViewById(R.id.addNurseryStartTime);
        endTime= (TextView) findViewById(R.id.addNurseryEndTime);

        swimming = (CheckBox) findViewById(R.id.addNursery_activity_swim);
        football =(CheckBox) findViewById(R.id.addNursery_activity_football);
        drawing = (CheckBox) findViewById(R.id.addNursery_activity_draw);
        disabilites=(CheckBox) findViewById(R.id.addNurseryDisability);
    }

    private void fetchData(){
        pickedImagesPath = new ArrayList<String>();
        activities = new ArrayList<String>();


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
        minAgeData = Long.parseLong(minAge.getText().toString());
        maxAgeData = Long.parseLong(maxAge.getText().toString());
        additionalActivitiesData = additionalActivities.getText().toString();
        cityData = city.getText().toString();
        districtData =district.getText().toString();
        buildingData = building.getText().toString();
        streetData = street.getText().toString();
        notesData = notes.getText().toString();


        if (swimming.isChecked())
            activities.add("SWIMMING");
        if (football.isChecked())
            activities.add("FOOTBALL");
        if (drawing.isChecked())
            activities.add("DRAWING");
        if (additionalActivitiesData!=null && !additionalActivitiesData.equals(""))
            activities.add(additionalActivitiesData);
        if (disabilites.isChecked())
            disabilitesData=true;


    }

    private void parseToNursery(){
        nurseryObj = new Nursery();
        nurseryObj.setName(nurseryNameData);
        nurseryObj.setDescription(nurseryDescriptionData);
        nurseryObj.setStartTime(startTimeData);
        nurseryObj.setEndTime(endTimeData);
        nurseryObj.setPhone1(phone1Data);
        nurseryObj.setPhone2(phone2Data);
        nurseryObj.setFacebook(facebookData);
        nurseryObj.setInstagram(instagramData);
        nurseryObj.setSnapchat(snapchatData);
        nurseryObj.setExpenses(priceData);
        nurseryObj.setMinAge(minAgeData);
        nurseryObj.setMaxAge(maxAgeData);
        nurseryObj.setActivities(activities);
        nurseryObj.setSupportingDisablilites(disabilitesData);
        nurseryObj.setCity(cityData);
        nurseryObj.setDistrict(districtData);
        nurseryObj.setStreet(streetData);
        nurseryObj.setBuilding(buildingData);
        nurseryObj.setMoreDetails(notesData);
    }

    private void parseFromNursery(){
        //TODO feach nursery obj by id
        nurseryObj = new Nursery();
        activities = nurseryObj.getActivities();


        nurseryNameData = nurseryObj.getName();
        nurseryDescriptionData = nurseryObj.getDescription();
        startTimeData = nurseryObj.getStartTime();
        endTimeData = nurseryObj.getEndTime();
        phone1Data = nurseryObj.getPhone1();
        phone2Data = nurseryObj.getPhone2();
        facebookData = nurseryObj.getFacebook();
        instagramData = nurseryObj.getInstagram();
        snapchatData = nurseryObj.getSnapchat();
        priceData = nurseryObj.getExpenses();
        minAgeData = nurseryObj.getMinAge();
        maxAgeData = nurseryObj.getMaxAge();
        additionalActivitiesData =activities.get(activities.size()-1);
        cityData = nurseryObj.getCity();
        districtData =nurseryObj.getDistrict();
        buildingData = nurseryObj.getBuilding();
        streetData = nurseryObj.getStreet();
        notesData = nurseryObj.getMoreDetails();


    }
public void pickTime(View v){
lastClickedView=v;
 TextView textView = (TextView) v;
    String currentPickedTime =textView.getText().toString();


    Calendar calendar = Calendar.getInstance();

        timePickerDialog = new TimePickerDialog(AddNursery.this,AddNursery.this,calendar.get(Calendar.HOUR_OF_DAY),
                calendar.get(Calendar.MINUTE), android.text.format.DateFormat.is24HourFormat(this));
   
    if (currentPickedTime.contains(":")) {
        String state = currentPickedTime.split(" ")[1];
        currentPickedTime = currentPickedTime.split(" ")[0];
        int hour = Integer.parseInt(currentPickedTime.split(":")[0]);
        int minute = Integer.parseInt(currentPickedTime.split(":")[1]);
        if (state.contains("PM"))
            hour += 12;
        timePickerDialog.updateTime(hour,minute);
    }


    timePickerDialog.show();

}

    @Override
    public void onTimeSet(TimePicker timePicker, int i, int i1) {

        int hour=0;
        if (i==0 || i==12)
            hour=12;

        else if (i>12)
            hour=i-12;
        else
            hour=i;
        String time = hour+":"+i1+" "+((i<12)?"AM":"PM");
        if (lastClickedView.getId()==R.id.addNurseryStartTime)
            startTime.setText(time);
        else if (lastClickedView.getId()==R.id.addNurseryEndTime)
            endTime.setText(time);
    }
}
