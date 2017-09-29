package com.kareem.mynursery.nursery;

import android.app.TimePickerDialog;
import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.TimePicker;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.kareem.mynursery.R;
import com.kareem.mynursery.model.Auth;
import com.kareem.mynursery.model.Nursery;

import java.util.ArrayList;
import java.util.Calendar;

public class AddNursery extends FileUploaderActivity implements TimePickerDialog.OnTimeSetListener{


    private  final String ADD_NURSERY="ADD_NURSERY";
    private final String EDIT_NURSERY="EDIT_NURSERY";
    private Intent intent;
    private ArrayList<String> pickedImagesPath;
    private ArrayList<String>  activities;
    private Nursery nurseryObj;
    private String NurseryId;
    private int LOCATION_CODE =1;
    Toast errorMessage;

    //Form Buttons
    Button  addLocation , addNursery;

    EditText nurseryName , nurseryDescription  ,
             phone1 , phone2 , facebook , instagram , snapchat , price,
             minAge , maxAge , additionalActivities ,city ,district ,
            street  , building ,whats;
    TextView startTime , endTime;

    CheckBox swimming  , disabilites , english , arabic , bus;

    String nurseryNameData ,nurseryDescriptionData , startTimeData , endTimeData,
            phone1Data , phone2Data , facebookData , instagramData , snapchatData ,
             additionalActivitiesData ,cityData ,districtData , streetData  , buildingData
            , notesData,whatsData;
    ImageView img1 ,img2,img3,img4,img5,img6;
    double  priceData;
    long minAgeData , maxAgeData;
    boolean disabilitesData = false,arabicVal=false,englishVal=false,busVal=false;
    TimePickerDialog timePickerDialog;
    private View lastClickedView;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        nurseryObj=new Nursery();
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_nursery);
        this.intent=getIntent();
        this.setComponents();

        if (this.intent.hasExtra("action")&&this.intent.getStringExtra("action").equals(this.EDIT_NURSERY)) {
            NurseryId = intent.getStringExtra("NurseryId");
            editRender();//TODO fill the form with the nursery data
        }


        //save Nursery action
        addNursery.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
               if (fetchData()) {
                   if (intent.hasExtra("action") && intent.getStringExtra("action").equals(EDIT_NURSERY)) {

                   } else {
                       parseToNursery();
                       nurseryObj.save();
                       Auth.getLoggedUser().addNursery(nurseryObj.getId());
                   }
               }



            }
        });
        addLocation.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent pickLocationIntent= new Intent(AddNursery.this , LocationPicker.class);
                startActivityForResult(pickLocationIntent,LOCATION_CODE);
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

    }



    private void setComponents(){

        addLocation = (Button) findViewById(R.id.addNurseryAddLocation_btn);
        addNursery = (Button) findViewById(R.id.addNurseryAdd_btn);
        nurseryName = (EditText) findViewById(R.id.addNurseryName);
        nurseryDescription = (EditText) findViewById(R.id.addNurseryDescription);

        phone1= (EditText) findViewById(R.id.addNurseryPhone1);
        phone2 = (EditText) findViewById(R.id.addNurseryPhone2);
        facebook = (EditText) findViewById(R.id.addNurseryFacebook);
        instagram = (EditText) findViewById(R.id.addNurseryInstagram);
        snapchat= (EditText) findViewById(R.id.addNurserySnapchat);
        whats=(EditText) findViewById(R.id.addNurseryWhats);
        price= (EditText) findViewById(R.id.addNurseryExpenses);
        minAge = (EditText) findViewById(R.id.addNurseryMinAge);
        maxAge = (EditText) findViewById(R.id.addNurseryMaxAge);
        additionalActivities = (EditText) findViewById(R.id.addNursery_activity_additional);
        city = (EditText) findViewById(R.id.addNurseryCity);
        district = (EditText) findViewById(R.id.addNurseryDistrict);
        street = (EditText) findViewById(R.id.addNurseryStreet);
        building = (EditText) findViewById(R.id.addNurseryBuilding);

        startTime = (TextView) findViewById(R.id.addNurseryStartTime);
        endTime= (TextView) findViewById(R.id.addNurseryEndTime);

        swimming = (CheckBox) findViewById(R.id.addNursery_activity_swim);
        disabilites=(CheckBox) findViewById(R.id.addNurseryDisability);
        arabic= (CheckBox) findViewById(R.id.addNursery_arabic);
        english= (CheckBox) findViewById(R.id.addNursery_english);
        bus= (CheckBox) findViewById(R.id.addNursery_bus);
    }

    private boolean fetchData(){
        pickedImagesPath = new ArrayList<String>();
        activities = new ArrayList<String>();
        String str_price,str_min,str_max;


        nurseryNameData = nurseryName.getText().toString();
        nurseryDescriptionData = nurseryDescription.getText().toString();
        startTimeData = startTime.getText().toString();
        endTimeData = endTime.getText().toString();
        phone1Data = phone1.getText().toString();
        phone2Data = phone2.getText().toString();
        facebookData = facebook.getText().toString();
        instagramData = instagram.getText().toString();
        snapchatData = snapchat.getText().toString();
        str_price = price.getText().toString();
        str_min = minAge.getText().toString();
        str_max = maxAge.getText().toString();
        additionalActivitiesData = additionalActivities.getText().toString();
        cityData = city.getText().toString();
        districtData =district.getText().toString();
        buildingData = building.getText().toString();
        streetData = street.getText().toString();
        whatsData=whats.getText().toString();



        if (nurseryNameData.equals("") || nurseryNameData.length()==0){
            showRequiredError(getString(R.string.name));
            return false;
        }
        if (nurseryDescriptionData.equals("") || nurseryDescriptionData.length()==0){
            showRequiredError(getString(R.string.description));
            return false;
        }
        if (startTimeData.equals("") || startTimeData.length()==0){
            showRequiredError(getString(R.string.start_time));
            return false;
        }
        if (endTimeData.equals("") || endTimeData.length()==0){
            showRequiredError(getString(R.string.end_time));
            return false;
        }
        if (phone1Data.equals("") || phone1Data.length()==0){
            showRequiredError(getString(R.string.phone1));
            return false;
        }
        if (str_price.equals("") || str_price.length()==0){
            showRequiredError(getString(R.string.fees));
            return false;
        }
        if (str_min.equals("") || str_min.length()==0){
            showRequiredError(getString(R.string.min_age));
            return false;
        }
        if (str_max.equals("") || str_max.length()==0){
            showRequiredError(getString(R.string.max_age));
            return false;
        }
        if (cityData.equals("") || cityData.length()==0){
            showRequiredError(getString(R.string.city));
            return false;
        }
        if (districtData.equals("") || districtData.length()==0){
            showRequiredError(getString(R.string.district));
            return false;
        }
        if (buildingData.equals("") || buildingData.length()==0){
            showRequiredError(getString(R.string.building));
            return false;
        }
        if (streetData.equals("") || streetData.length()==0){
            showRequiredError(getString(R.string.street));
            return false;
        }


        if (swimming.isChecked())
            activities.add("SWIMMING");
        if (additionalActivitiesData!=null && !additionalActivitiesData.equals(""))
            activities.add(additionalActivitiesData);
        if (disabilites.isChecked())
            disabilitesData=true;
        if (arabic.isChecked())
            arabicVal=true;
        if (english.isChecked())
            englishVal=true;
        if (bus.isChecked())
            busVal=true;

        priceData = Double.parseDouble(str_price);
        minAgeData = Long.parseLong(str_min);
        maxAgeData = Long.parseLong(str_max);
        return true;
    }

    private void parseToNursery(){
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
        nurseryObj.setArabic(arabicVal);
        nurseryObj.setEnglish(englishVal);
        nurseryObj.setBus(busVal);
        nurseryObj.setWhatsapp(whatsData);
    }

    private void parseFromNursery(){
        //TODO feach nursery obj by id

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
        arabicVal=nurseryObj.isArabic();
        englishVal=nurseryObj.isEnglish();
        busVal=nurseryObj.isBus();
        whatsData=nurseryObj.getWhatsapp();

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


    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {

        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode==LOCATION_CODE){
            if (resultCode==RESULT_OK){

                double lat = data.getDoubleExtra(LocationPicker.LAT,0);
                double lng = data.getDoubleExtra(LocationPicker.LNG,0);
                String city = data.getStringExtra(LocationPicker.CITY);
                String address = data.getStringExtra(LocationPicker.ADDRESS);
                String state = data.getStringExtra(LocationPicker.STATE);
                String country = data.getStringExtra(LocationPicker.COUNTRY);
                nurseryObj.setLatitude(lat);
                nurseryObj.setLongitude(lng);
                this.city.setText(city);
                nurseryObj.setMoreDetails(address);

                Toast toast = Toast.makeText(this,
             "lat : "+lat+"\n lng : "+lng+  "\n country : "+country+"\n address: "+address+"\n city : "+city +"\n state : "+state
                        ,Toast.LENGTH_LONG);
            }
        }
    }

    @Override
    public void onUploadComplete(String imageName) {

    }

    @Override
    public void uponImagePicked(String imageLocation) {

    }

    private void setImageViewFromFile(ImageView imageView, String fileLocation)
    {
        Glide.with(this)
                .load(fileLocation).centerCrop()
                .into(imageView);
    }



public void showRequiredError(String message){
    if (errorMessage!=null)
        errorMessage.cancel();
    errorMessage=Toast.makeText(this.getBaseContext(),message+R.string.required,Toast.LENGTH_LONG);
    errorMessage.show();
}
}
