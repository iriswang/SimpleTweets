package com.codepath.apps.mysimpletweets;

import android.content.Context;
import android.support.design.widget.CoordinatorLayout;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.view.ViewCompat;
import android.util.AttributeSet;
import android.view.View;

/**
 * Created by iris on 8/7/16.
 */
public class ScrollAwareFABBehavior extends FloatingActionButton.Behavior {
    @Override
    public boolean onStartNestedScroll(CoordinatorLayout coordinatorLayout,
        FloatingActionButton child, View directTargetChild, View target, int nestedScrollAxes) {
        return nestedScrollAxes == ViewCompat.SCROLL_AXIS_VERTICAL ||
            super.onStartNestedScroll(coordinatorLayout, child, directTargetChild, target,
                nestedScrollAxes);
    }

    public ScrollAwareFABBehavior(Context context, AttributeSet attrs) {
        super();
    }
}
