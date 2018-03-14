package com.sqin.libbaseframe.widgets;

import android.content.Context;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RelativeLayout;

import java.util.ArrayList;
import java.util.List;

/**
 * 作者：sqin
 * 日期：2016/3/2.
 */
public class BannerView extends RelativeLayout {
    public BannerView(Context context) {
        super(context);
    }

    public BannerView(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public BannerView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }

    List<View> itemViewList = new ArrayList<>();

    float startX, lastX, lastY;

    boolean isCaughtMotionEvent = false;
    boolean isCheckedMotionEvent = false;

    int currentIndex = -1;
    int dealingIndex = -1;

    float maxProgress = 0;
    float currentProgress = 0;

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);
        maxProgress = getMeasuredWidth();
    }

    @Override
    public void addView(View child) {
        super.addView(child);
    }

    @Override
    public void addView(View child, int index, ViewGroup.LayoutParams params) {
        LayoutParams p = new LayoutParams(LayoutParams.MATCH_PARENT, LayoutParams.WRAP_CONTENT);

        if(index > currentIndex || (currentIndex >= 0 && index < 0))
            child.setVisibility(View.GONE);
        else {
            currentIndex++;
            child.setVisibility(View.VISIBLE);
        }

        super.addView(child, index, p);
    }

    @Override
    public boolean onInterceptTouchEvent(MotionEvent ev) {
        switch (ev.getAction()){
            case MotionEvent.ACTION_DOWN:
                startX =ev.getX();
                lastX = ev.getX();
                lastY = ev.getY();
//                isCheckedMotionEvent = false;
//                isCaughtMotionEvent = true;
                break;
            case MotionEvent.ACTION_MOVE:
                if(isCheckedMotionEvent) break;
                isCheckedMotionEvent = false;
                float x = ev.getX();
                float y = ev.getY();
                float offsetX = x - lastX;
                float offsetY = y - lastY;
                lastX = ev.getX();
                lastY = ev.getY();
                if(Math.abs(offsetX) < Math.abs(offsetY)*1.5){
                    isCaughtMotionEvent = false;
                }else{
                    isCaughtMotionEvent = true;
                }
                break;
            case MotionEvent.ACTION_UP:
                isCheckedMotionEvent = false;
                isCaughtMotionEvent = false;
                break;
        }
        return isCaughtMotionEvent;
    }

    @Override
    public boolean onTouchEvent(MotionEvent ev) {
        switch (ev.getAction()){
            case MotionEvent.ACTION_DOWN:
                break;
            case MotionEvent.ACTION_MOVE:
                float x = ev.getX();
                float offsetX = x - startX;
                dealingIndex = -1;
                if(offsetX > 0 && currentIndex > 0){
                    dealingIndex = currentIndex;
                }else if(offsetX < 0 && currentIndex < getChildCount()-1){
                    dealingIndex = currentIndex + 1;
                }
                if(dealingIndex >= 0){
                    currentProgress = (int) Math.abs(offsetX);
                    float alpha = currentProgress / maxProgress;
                    if(alpha > 1) alpha = 1;
                    else if(alpha < 0) alpha = 0;
                    showNextChild(alpha);
                }
        }
        return super.onTouchEvent(ev);
    }

    protected void showNextChild(final float alpha){
        final View child = getChildAt(dealingIndex);
        child.post(new Runnable() {
            @Override
            public void run() {
                int visible = alpha == 0 ? View.GONE : View.VISIBLE;
                child.setVisibility(visible);
                child.setAlpha(alpha);
                if(alpha == 1){
                    getChildAt(currentIndex).setVisibility(View.GONE);
                    currentIndex = dealingIndex;
                }
            }
        });
    }
}
