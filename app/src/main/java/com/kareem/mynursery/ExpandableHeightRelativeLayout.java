package com.kareem.mynursery;

import android.content.Context;
import android.icu.util.Measure;
import android.os.Build;
import android.support.annotation.RequiresApi;
import android.util.AttributeSet;
import android.view.View;
import android.widget.RelativeLayout;

import com.google.firebase.remoteconfig.FirebaseRemoteConfig;

/**
 * Created by kareem on 10/14/17.
 */

public class ExpandableHeightRelativeLayout extends RelativeLayout {
    public ExpandableHeightRelativeLayout(Context context) {
        super(context);
    }

    public ExpandableHeightRelativeLayout(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public ExpandableHeightRelativeLayout(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }

    @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
    public ExpandableHeightRelativeLayout(Context context, AttributeSet attrs, int defStyleAttr, int defStyleRes) {
        super(context, attrs, defStyleAttr, defStyleRes);
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        int mode = MeasureSpec.getMode(widthMeasureSpec);
        int size = MeasureSpec.getSize(widthMeasureSpec);
        double ratio = FirebaseRemoteConfig
                .getInstance()
                .getDouble("nursery_item_width_height_ratio");
        heightMeasureSpec = MeasureSpec.makeMeasureSpec(
                (int) (size *ratio),mode);
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);

    }
}
