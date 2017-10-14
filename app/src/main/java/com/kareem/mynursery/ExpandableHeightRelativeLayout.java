package com.kareem.mynursery;

import android.content.Context;
import android.icu.util.Measure;
import android.os.Build;
import android.support.annotation.RequiresApi;
import android.util.AttributeSet;
import android.view.View;
import android.widget.RelativeLayout;

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
        super.onMeasure(widthMeasureSpec, MeasureSpec.makeMeasureSpec((int) (size * (9.0/16.0)), mode));

    }
}
