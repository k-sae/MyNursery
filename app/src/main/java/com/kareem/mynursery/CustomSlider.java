package com.kareem.mynursery;

import android.content.Context;
import android.util.AttributeSet;

import com.daimajia.slider.library.SliderLayout;

/**
 * Created by kareem on 10/8/17.
 */

public class CustomSlider extends SliderLayout {
    public CustomSlider(Context context) {
        super(context);
    }

    public CustomSlider(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public CustomSlider(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
    }
//
//    @Override
//    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
//        super.onMeasure(widthMeasureSpec, (int) (widthMeasureSpec * (3.0/4.0)));
//    }

}
