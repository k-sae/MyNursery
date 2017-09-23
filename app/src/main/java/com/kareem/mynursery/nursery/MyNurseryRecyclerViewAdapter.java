package com.kareem.mynursery.nursery;

import android.content.Context;
import android.location.Location;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.daimajia.slider.library.SliderLayout;
import com.kareem.mynursery.GlideSliderView;
import com.kareem.mynursery.R;
import com.kareem.mynursery.Utils;
import com.kareem.mynursery.model.Nursery;


import java.util.ArrayList;
import java.util.List;

/**

 * TODO: Replace the implementation with code for your data type.
 */
public class MyNurseryRecyclerViewAdapter extends RecyclerView.Adapter<MyNurseryRecyclerViewAdapter.ViewHolder> {

    private final List<Nursery> mValues;
    private final Context context;
    private Location nurseryLocation;
    private Location userLocation;
    private String distance;
    public MyNurseryRecyclerViewAdapter(List<Nursery> items , Context context) {
        mValues = items;
        this.context = context;
    }
    public MyNurseryRecyclerViewAdapter(Context context) {
        mValues = new ArrayList<>();
        this.context = context;
        nurseryLocation = new Location("nursery Location");
    }


    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.nursery_list_item, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(final ViewHolder holder, int position) {
        holder.mItem = mValues.get(position);

        holder.mContentView.setText(mValues.get(position).getName());
        holder.sliderLayout.removeAllSliders();
        nurseryLocation.setLatitude(holder.mItem.getLatitude());
        nurseryLocation.setLongitude(holder.mItem.getLongitude());
        distance = userLocation == null ? "~ KM" : Utils.calculateDistance(userLocation, nurseryLocation) + " KM";
        holder.mIdView.setText(distance);
        for (String s: holder.mItem.getImagesId()
             ) {
            holder.sliderLayout.addSlider(new GlideSliderView(context).image(s));

        }

        holder.mView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                   //TODo
            }
        });
    }

    public List<Nursery> getmValues() {
        return mValues;
    }

    public Location getUserLocation() {
        if (userLocation == null) userLocation = new Location("user Location");
        return userLocation;
    }

    @Override
    public int getItemCount() {
        return mValues.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        public final View mView;
        public final TextView mIdView;
        public final TextView mContentView;
        public final SliderLayout sliderLayout;
        public Nursery mItem;

        public ViewHolder(View view) {
            super(view);
            mView = view;
            mIdView = (TextView) view.findViewById(R.id.title);
            mContentView = (TextView) view.findViewById(R.id.location);
            sliderLayout = view.findViewById(R.id.slider);
            sliderLayout.stopAutoCycle();
        }

        @Override
        public String toString() {
            return super.toString() + " '" + mContentView.getText() + "'";
        }
    }
}
