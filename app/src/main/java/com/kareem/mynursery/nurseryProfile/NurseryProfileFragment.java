package com.kareem.mynursery.nurseryProfile;

import android.app.DatePickerDialog;
import android.content.Context;
import android.content.Intent;
import android.location.Location;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.content.ContextCompat;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.OrientationHelper;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.Toast;

import com.daimajia.slider.library.SliderLayout;
import com.kareem.mynursery.LocationTrackerFragment;
import com.kareem.mynursery.R;
import com.kareem.mynursery.Utils;
import com.kareem.mynursery.model.Auth;
import com.kareem.mynursery.model.Nursery;
import com.kareem.mynursery.model.ObjectChangedListener;
import com.kareem.mynursery.model.RealTimeObject;
import com.kareem.mynursery.model.User;
import com.kareem.mynursery.nursery.AddNursery;


import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

/**
 * A fragment representing a list of Items.
 * <p/>
 * Activities containing this fragment MUST implement the {@link }
 * interface.
 */
public class NurseryProfileFragment extends Fragment implements LocationTrackerFragment,  DatePickerDialog.OnDateSetListener {

    // TODO: Customize parameter argument names
    private static final String ARG_COLUMN_COUNT = "column-count";
    // TODO: Customize parameters
    private int mColumnCount = 1;
    private Intent intent;
    private SliderLayout sliderLayout;
    Button like_btn,edit,delete,sponsored;
    private boolean liked;
    Nursery nursery;
    Context context;

    MyNurseryProfileRecyclerViewAdapter myNurseryProfileRecyclerViewAdapter;

    /**
     * Mandatory empty constructor for the fragment manager to instantiate the
     * fragment (e.g. upon screen orientation changes).
     */
    public NurseryProfileFragment() {
    }

    // TODO: Customize parameter initialization
    @SuppressWarnings("unused")
    public static NurseryProfileFragment newInstance(int columnCount) {
        NurseryProfileFragment fragment = new NurseryProfileFragment();
        Bundle args = new Bundle();
        args.putInt(ARG_COLUMN_COUNT, columnCount);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        if (getArguments() != null) {
            mColumnCount = getArguments().getInt(ARG_COLUMN_COUNT);
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        intent=getActivity().getIntent();

        View view = inflater.inflate(R.layout.fragment_nurseryprofile_list, container, false);
        android.support.v7.widget.RecyclerView recycler =(android.support.v7.widget.RecyclerView )view.findViewById(R.id.np_list);
                sliderLayout=inflater.inflate(R.layout.fragment_nurseryprofile, container, false).findViewById(R.id.nurseryProfileSlider);

        // Set the adapter
        if (recycler instanceof RecyclerView) {
            Context context = recycler.getContext();
            RecyclerView recyclerView = (RecyclerView) recycler ;

            LinearLayoutManager linearLayoutManager =new LinearLayoutManager(context, OrientationHelper.VERTICAL, false);

            recyclerView.setLayoutManager(linearLayoutManager);
             myNurseryProfileRecyclerViewAdapter =new MyNurseryProfileRecyclerViewAdapter(this.getContext(),intent.getStringExtra("NurseryId"));
            recyclerView.setAdapter(myNurseryProfileRecyclerViewAdapter);
        }

        nursery = new Nursery();
        nursery.setId(intent.getStringExtra("NurseryId"));
        nursery.startSync();
        nursery.setOnChangeListener(new ObjectChangedListener() {
            @Override
            public void onChange(RealTimeObject realTimeObject) {
                checkLike();
            }
        });
        like_btn = (Button) view.findViewById(R.id.navigation_like);
        edit = (Button)view.findViewById(R.id.navigation_edit);
        delete = (Button)view.findViewById(R.id.navigation_delete);
        sponsored = (Button)view.findViewById(R.id.navigation_sponsored);


        like_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                likeToggle();

            }
        });

        edit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent( getContext(), AddNursery.class);
                intent.putExtra("action",AddNursery.EDIT_NURSERY);
                intent.putExtra(AddNursery.ID,nursery.getId());
                getContext().startActivity(intent);

            }
        });
        delete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Auth.getLoggedUser().removeNursery(nursery.getId());
                nursery.delete();
            }
        });
        sponsored.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                pickDate();
            }
        });
        final User current_user=Auth.getLoggedUser();
        if (current_user!=null) {
            current_user.startSync();
            current_user.setOnChangeListener(new ObjectChangedListener() {
                @Override
                public void onChange(RealTimeObject realTimeObject) {
                    if (current_user == null || !current_user.getNurseries().contains(nursery.getId())) {
                        edit.setVisibility(View.GONE);
                        delete.setVisibility(View.GONE);
                    } else {
                        edit.setVisibility(View.VISIBLE);
                        delete.setVisibility(View.VISIBLE);
                    }
                }
            });
            if (current_user.getType()==2){
                sponsored.setVisibility(View.VISIBLE);
            }
            else
                sponsored.setVisibility(View.GONE);
        }
        if (Auth.getLoggedUser() == null || !Auth.getLoggedUser().getNurseries().contains(nursery.getId())) {
            edit.setVisibility(View.GONE);
            delete.setVisibility(View.GONE);
        }
        else {
            edit.setVisibility(View.VISIBLE);
            delete.setVisibility(View.VISIBLE);
        }
        if (getContext()!=null)
        context=getContext();
        return view;
    }


    @Override
    public void onAttach(Context context) {
        super.onAttach(context);

    }

    @Override
    public void onDetach() {
        super.onDetach();
    }

    @Override
    public void onStop() {
        sliderLayout.startAutoCycle();
        super.onStop();
    }
/**
     * This interface must be implemented by activities that contain this
     * fragment to allow an interaction in this fragment to be communicated
     * to the activity and potentially other fragments contained in that
     * activity.
     * <p/>
     * See the Android Training lesson <a href=
     * "http://developer.android.com/training/basics/fragments/communicating.html"
     * >Communicating with Other Fragments</a> for more information.
     */




private void likeToggle(){

    if (liked){

        ArrayList<String> likes = nursery.getLikes();
        if (likes.contains(Auth.getLoggedUser().getId()))
            nursery.disLike();

    }
    else {

        ArrayList<String> likes = nursery.getLikes();
        if (!likes.contains(Auth.getLoggedUser().getId()))
            nursery.like();
    }
    if (nursery.getLikes().size()==0)
        myNurseryProfileRecyclerViewAdapter.nursery.setLikes(new ArrayList<String>());
    myNurseryProfileRecyclerViewAdapter.notifyDataSetChanged();
    checkLike();
}
    private void checkLike(){
        if (Auth.getLoggedUser()!=null) {

            isLiked();
            if (liked){
                like_btn.setText("DisLike");
                like_btn.setTextColor(ContextCompat.getColor(context,R.color.textSecondary));
                like_btn.setBackgroundColor(ContextCompat.getColor(context,R.color.colorPrimary));

            }
            else{

                like_btn.setText("Like");
                like_btn.setBackgroundColor(ContextCompat.getColor(context,R.color.textSecondary));
                like_btn.setTextColor(ContextCompat.getColor(context,R.color.textPrimary));

            }
        }
    }
    private void isLiked(){

        if (nursery.getLikes().size()>0&&nursery.getLikes().contains(Auth.getLoggedUser().getId()))
            liked=true;
        else
            liked=false;

    }


    private void pickDate(){
        final Calendar c = Calendar.getInstance();
        int year = c.get(Calendar.YEAR);
        int month = c.get(Calendar.MONTH);
        int day = c.get(Calendar.DAY_OF_MONTH);
        DatePickerDialog datePickerDialog =new DatePickerDialog(context, this, year, month, day);
        datePickerDialog.show();
    }

    @Override
    public void onLocationChange(Location location) {
        Location location1 = new Location("");
        location1.setLatitude(nursery.getLatitude());
        location1.setLongitude(nursery.getLongitude());
        myNurseryProfileRecyclerViewAdapter.str_distance=Utils.calculateDistance(location,location1)+"";
        myNurseryProfileRecyclerViewAdapter.notifyDataSetChanged();
    }

    @Override
    public void onDateSet(DatePicker datePicker, int i, int i1, int i2) {
        String date =i2+"/"+i1+"/"+i;
        nursery.setSponsorshipEndDate(date);
        nursery.save(); 
        Toast.makeText(context,i+"-"+i1+"-"+i2,Toast.LENGTH_LONG).show();
    }
}
