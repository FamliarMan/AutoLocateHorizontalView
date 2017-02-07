package com.jianglei.view;

import android.content.Context;
import android.support.v7.widget.LinearLayoutManager;
import android.util.AttributeSet;

/**
 * Created by jianglei on 2/7/17.
 */

public class AutoLocateLayoutManager extends LinearLayoutManager {
    public AutoLocateLayoutManager(Context context) {
        super(context);
    }

    public AutoLocateLayoutManager(Context context, int orientation, boolean reverseLayout) {
        super(context, orientation, reverseLayout);
    }

    public AutoLocateLayoutManager(Context context, AttributeSet attrs, int defStyleAttr, int defStyleRes) {
        super(context, attrs, defStyleAttr, defStyleRes);
    }
}
