package com.kareem.mynursery;

import android.content.Context;
import android.view.View;
import android.widget.ImageView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.target.GlideDrawableImageViewTarget;
import com.daimajia.slider.library.SliderTypes.BaseSliderView;
import com.daimajia.slider.library.SliderTypes.DefaultSliderView;

/**
 * Created by kareem on 9/21/17.
 */

public class GlideSliderView extends DefaultSliderView {
    public GlideSliderView(Context context) {
        super(context);
    }
    public boolean loadError = false;
    protected void bindEventAndShow(final View v, ImageView targetImageView) {
        View progressBar = v.findViewById(com.daimajia.slider.library.R.id.loading_bar);
        if (progressBar != null) {
            progressBar.setVisibility(View.GONE);
        }
        final BaseSliderView me = this;

        v.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(mOnSliderClickListener != null){
                    mOnSliderClickListener.onSliderClick(me);
                }
            }
        });
        if (loadError) {
            GlideDrawableImageViewTarget imageViewTarget = new GlideDrawableImageViewTarget(targetImageView);
            Glide.with(getContext()).load(R.mipmap.image_not_found).centerCrop().into(imageViewTarget);
        }else
        Glide.with(getContext())
                .load(getUrl()).centerCrop()
                .into(targetImageView);

    }

    public BaseSliderView loadGif(boolean loadGif){
        this.loadError = loadGif;
        return this;
    }
}
