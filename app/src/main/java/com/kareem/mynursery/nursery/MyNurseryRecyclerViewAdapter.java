package com.kareem.mynursery.nursery;

import android.content.Context;
import android.content.Intent;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.daimajia.slider.library.SliderLayout;
import com.daimajia.slider.library.SliderTypes.BaseSliderView;
import com.daimajia.slider.library.SliderTypes.DefaultSliderView;
import com.kareem.mynursery.R;
import com.kareem.mynursery.model.Nursery;
import com.kareem.mynursery.nurseryProfile.NurseryProfileActivity;


import java.util.ArrayList;
import java.util.List;

/**

 * TODO: Replace the implementation with code for your data type.
 */
public class MyNurseryRecyclerViewAdapter extends RecyclerView.Adapter<MyNurseryRecyclerViewAdapter.ViewHolder> {

    private final List<Nursery> mValues;
    private final Context context;

    public MyNurseryRecyclerViewAdapter(List<Nursery> items , Context context) {
        mValues = items;
        this.context = context;
    }
    public MyNurseryRecyclerViewAdapter(Context context) {
        mValues = new ArrayList<>();
        this.context = context;
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

        holder.titleView.setText(mValues.get(position).getName());
        holder.sliderLayout.removeAllSliders();
        final int mPosition = position;
        String distance = holder.mItem.getCity() + " " + (mValues.get(position).getDistanceFromUser() == null ? "~ " + context.getString(R.string.km) : mValues.get(position).getDistanceFromUser() + " " + context.getString(R.string.km) );
        holder.locationView.setText(distance);
        for (String s: holder.mItem.getImagesId()
             ) {
            holder.sliderLayout.addSlider(new DefaultSliderView(context).image(Nursery.BASE_IMAGE_URL + s).setOnSliderClickListener(new BaseSliderView.OnSliderClickListener() {
                @Override
                public void onSliderClick(BaseSliderView slider) {
                    startActivity(mPosition);
                }
            }));
        }

        holder.mView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                   //TODo
              startActivity(mPosition);
            }
        });
    }

    public List<Nursery> getmValues() {
        return mValues;
    }

    private void startActivity(int mPosition)
    {
        Intent intent = new Intent(context, NurseryProfileActivity.class);
        intent.putExtra("NurseryId", mValues.get(mPosition).getId() );
        context.startActivity(intent);
    }

    @Override
    public int getItemCount() {
        return mValues.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        public final View mView;
        public final TextView titleView;
        public final TextView locationView;
        public final SliderLayout sliderLayout;
        public Nursery mItem;

        public ViewHolder(View view) {
            super(view);
            mView = view;
            titleView = (TextView) view.findViewById(R.id.title);
            locationView = (TextView) view.findViewById(R.id.location);
            sliderLayout = view.findViewById(R.id.slider);
            sliderLayout.stopAutoCycle();
        }

        @Override
        public String toString() {
            return super.toString() + " '" + locationView.getText() + "'";
        }
    }
}
