package com.kareem.mynursery;

import android.content.Context;
import android.view.View;
import android.widget.ImageView;

import com.bumptech.glide.Glide;
import com.daimajia.slider.library.SliderTypes.DefaultSliderView;

/**
 * Created by kareem on 9/21/17.
 */

public class GlideSliderView extends DefaultSliderView {
    public GlideSliderView(Context context) {
        super(context);
    }
    protected void bindEventAndShow(final View v, ImageView targetImageView) {
        View progressBar = v.findViewById(com.daimajia.slider.library.R.id.loading_bar);
        if (progressBar != null) {
            progressBar.setVisibility(View.GONE);
        }

        Glide.with(getContext())
                .load(getUrl()).centerCrop()
                .into(targetImageView);
    }
}
