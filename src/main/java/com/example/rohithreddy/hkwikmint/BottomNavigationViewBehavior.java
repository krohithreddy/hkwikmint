package com.example.rohithreddy.hkwikmint;

import android.content.Context;
import android.content.res.Resources;
import android.inputmethodservice.KeyboardView;
import android.support.design.widget.AppBarLayout;
import android.support.design.widget.BottomNavigationView;
import android.support.design.widget.CoordinatorLayout;
import android.support.v4.view.ViewCompat;
import android.util.AttributeSet;
import android.util.DisplayMetrics;
import android.view.View;

/**
 * Created by rohithreddy on 28/09/17.
 */
public class BottomNavigationViewBehavior extends CoordinatorLayout.Behavior<View> {
    private boolean visible = true;
    private boolean inStartPosition = true;
    private float oldY;
    private DisplayMetrics metrics;



    public BottomNavigationViewBehavior(Context context, AttributeSet attrs) {
        super(context, attrs);
        metrics = Resources.getSystem().getDisplayMetrics();
    }

    @Override
    public boolean layoutDependsOn(CoordinatorLayout parent, View fab, View dependency) {
        return dependency instanceof AppBarLayout;
    }

    @Override
    public boolean onDependentViewChanged(CoordinatorLayout parent, View child, View dependency) {
        if (dependency instanceof AppBarLayout) {
            CoordinatorLayout.LayoutParams lp = (CoordinatorLayout.LayoutParams) child.getLayoutParams();
            float dy  = oldY - dependency.getY();
            if(dy > 0 && visible){
                moveDown(child, oldY);
            } else if(!inStartPosition) {
                moveUp(child, oldY);
            }
            oldY = dependency.getY();
        }
        return true;
    }

    private void moveUp(View child, float dy){
        if(child.getY() + dy >= metrics.heightPixels - child.getHeight()){
            child.setY(metrics.heightPixels - child.getHeight());
        } else {
            child.setY(child.getY() + dy);
        }
        inStartPosition = child.getY() == metrics.heightPixels - child.getHeight();
        visible = child.getY() > metrics.heightPixels;
    }


    private void moveDown(View child, float dy){
        child.setY(child.getY() + dy);

        visible = child.getY() > metrics.heightPixels;
        if(!visible) {
            child.setY(metrics.heightPixels);
        }
    }

//    @Override
//    public boolean onStartNestedScroll(final CoordinatorLayout coordinatorLayout, final View child,
//                                       final View directTargetChild, final View target, final int nestedScrollAxes) {
//        return true;
//    }
//
//    @Override
//    public void onNestedScroll(final CoordinatorLayout coordinatorLayout,
//                               final View child,
//                               final View target, final int dxConsumed, final int dy,
//                               final int dxUnconsumed, final int dyUnconsumed) {
//        super.onNestedScroll(coordinatorLayout, child, target, dxConsumed, dy, dxUnconsumed, dyUnconsumed);
//        if(dy > 0 && visible){
//            moveDown(child, dy);
//        } else if(!inStartPosition) {
//            moveUp(child, dy);
//        }
//    }


}