package com.example.bluepea.ringingflashlight;

import android.content.Context;
import android.support.constraint.ConstraintLayout;
import android.support.constraint.ConstraintSet;
import android.support.design.widget.AppBarLayout;
import android.util.AttributeSet;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.view.animation.TranslateAnimation;
import android.widget.ScrollView;
import android.widget.TextView;

public class AnimBarNotification extends ConstraintLayout implements AppBarLayout.OnOffsetChangedListener{
    public AnimBarNotification(Context context) {
        super(context);
    }

    public AnimBarNotification(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public AnimBarNotification(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }

//    public boolean isUnderThreshold(){
//        return isUnderThreshold;
//    }




    @Override
    public void onOffsetChanged(AppBarLayout appBarLayout, int verticalOffset) {
//
//        if(lastPosition == verticalOffset){
//            return;
//        }
//
//        lastPosition =verticalOffset;
//        float progress = Math.abs(((float)verticalOffset)/((float)appBarLayout.getHeight()));
//        AppBarLayout.LayoutParams params = (AppBarLayout.LayoutParams)getLayoutParams();
//        params.topMargin = -verticalOffset;
//        Log.d("truoc","......................");
//        setLayoutParams(params);
//
//
//
//        if(isBarOpen && progress >threshold){
//            isUnderThreshold = true;
//            closeSet.applyTo(this);
//            isBarOpen=false;
//        }
//        else if(!isBarOpen && progress <threshold ){
//            isUnderThreshold = false;
//            openSet.applyTo(this);
//            isBarOpen=true;
//        }
    }

//    public ScrollView getScrollView() {
//        return scrollView;
//    }
//
//    public void setScrollView(ScrollView scrollView) {
//        this.scrollView = scrollView;
//    }



    @Override
    protected void onLayout(boolean changed, int left, int top, int right, int bottom) {
        super.onLayout(changed, left, top, right, bottom);
//        Log.d("uuhuhuhuuuhu","...............");
//        if(isBarOpen && !isAnimOpenStart){
//            TranslateAnimation anim = new TranslateAnimation(0,0,previous-title.getY(),0);
//            anim.setDuration(200);
//            title.startAnimation(anim);
//
//            isAnimOpenStart = true;
//            isAnimCloseStart =false;
//
//        }
//        else if(!isBarOpen && !isAnimCloseStart){
//            TranslateAnimation anim = new TranslateAnimation(0,0,previous-title.getY(),0);
//            anim.setDuration(200);
//            title.startAnimation(anim);
//            isAnimCloseStart = true;
//            isAnimOpenStart = false;
//        }
//        previous = title.getY();
    }
//
//    private float threshold = 0.5f;
//    private int lastPosition = 0;
//    private boolean isBarOpen = true;
//    public boolean isUnderThreshold = false;
//    public AppBarLayout barLayout;
//    private ConstraintSet openSet = new ConstraintSet();
//    private ConstraintSet closeSet = new ConstraintSet();
//    private ScrollView scrollView;
//    public TextView title;
//    private boolean isAnimCloseStart =false;
//    private boolean isAnimOpenStart =true;
//    private float previous;


    @Override
    protected void onAttachedToWindow() {
        super.onAttachedToWindow();
//
//        barLayout = (AppBarLayout) this.getParent();
//        barLayout.addOnOffsetChangedListener(this);
//        openSet.clone(getContext(), R.layout.bar_open);
//        closeSet.clone(getContext(),R.layout.bar_close);
//
//        scrollView.setOnTouchListener(new View.OnTouchListener() {
//            @Override
//            public boolean onTouch(View v, MotionEvent event) {
//                if(event.getAction() ==MotionEvent.ACTION_UP) {
//                    if (isUnderThreshold) {
//                        barLayout.setExpanded(false);
//                    } else {
//                        barLayout.setExpanded(true);
//                    }
//                }
//                return false;
//            }
//        });

    }

}
