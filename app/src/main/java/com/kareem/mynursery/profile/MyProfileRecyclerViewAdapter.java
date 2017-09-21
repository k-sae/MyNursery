package com.kareem.mynursery.profile;

import android.os.Bundle;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.kareem.mynursery.AddNursery;
import com.kareem.mynursery.R;
import com.kareem.mynursery.profile.ProfileFragment.OnListFragmentInteractionListener;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import com.daimajia.slider.library.Animations.DescriptionAnimation;
import com.daimajia.slider.library.Indicators.PagerIndicator;
import com.daimajia.slider.library.SliderLayout;
import com.daimajia.slider.library.SliderTypes.BaseSliderView;
import com.daimajia.slider.library.SliderTypes.TextSliderView;
import com.daimajia.slider.library.Tricks.ViewPagerEx;

/**
 * {@link RecyclerView.Adapter} that can display a {@link } and makes a call to the
 * specified {@link OnListFragmentInteractionListener}.
 * TODO: Replace the implementation with code for your data type.
 */
public class MyProfileRecyclerViewAdapter extends RecyclerView.Adapter<MyProfileRecyclerViewAdapter.ViewHolder> {

   private final HashMap<String,String> mValues;
//    private final OnListFragmentInteractionListener mListener;
    private int itemNumber;
    private View parentView;


    public MyProfileRecyclerViewAdapter(HashMap<String,String> mValues) {
        this.mValues=mValues;
     this.itemNumber=mValues.size();
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.fragment_profile, parent, false);
        parentView = parent;
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(final ViewHolder holder, int position) {

        holder.bind(position);

        holder.mView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

            }
        });
    }

    @Override
    public int getItemCount() {
        return itemNumber;
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        public final View mView;
        public final TextView mIdView;
        public final TextView mContentView;
        public final SliderLayout slider;

        public ViewHolder(View view) {
            super(view);
            mView = view;
            mIdView = (TextView) view.findViewById(R.id.id);
            mContentView = (TextView) view.findViewById(R.id.content);
            slider = (SliderLayout)view.findViewById(R.id.slider);

        }
        public void bind(int index ) {
            for (String name : mValues.keySet()){
                TextSliderView textSliderView = new TextSliderView(parentView.getContext());
                textSliderView
                        .description(name)
                        .image(mValues.get(name))
                        .setScaleType(BaseSliderView.ScaleType.Fit);

                //add your extra information
                textSliderView.bundle(new Bundle());
                textSliderView.getBundle()
                        .putString("extra",name);
                slider.addSlider(textSliderView);

            }
            slider.setPresetTransformer(SliderLayout.Transformer.Accordion);
            slider.setPresetIndicator(SliderLayout.PresetIndicators.Center_Bottom);
            slider.setCustomAnimation(new DescriptionAnimation());
            slider.setDuration(4000);
        }
        @Override
        public String toString() {
            return super.toString() + " '" + mContentView.getText() + "'";
        }
    }
}
