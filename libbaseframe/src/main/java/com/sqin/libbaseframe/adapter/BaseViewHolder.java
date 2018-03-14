package com.sqin.libbaseframe.adapter;

import android.support.v7.widget.RecyclerView;
import android.util.SparseArray;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

/**
 * 作者：sqin
 * 日期：2016/3/1.
 */
public class BaseViewHolder extends RecyclerView.ViewHolder{
    private SparseArray<View> viewHolder;
    private View view;
    private int viewType;


    public static BaseViewHolder getViewHolder(View view, int viewType) {
        BaseViewHolder viewHolder = (BaseViewHolder) view.getTag();
        if (viewHolder == null) {
            viewHolder = new BaseViewHolder(view);
            view.setTag(viewHolder);
        }
        return viewHolder;
    }

    public BaseViewHolder(View view) {
        super(view);
        this.view = view;
        viewHolder = new SparseArray<View>();
        view.setTag(viewHolder);
    }

    public int getViewType(){
        return viewType;
    }

    public <T extends View> T get(int id) {
        View childView = viewHolder.get(id);
        if (childView == null) {
            childView = view.findViewById(id);
            viewHolder.put(id, childView);
        }
        return (T) childView;
    }

    public View getConvertView() {
        return view;
    }

    public TextView getTextView(int id) {

        return get(id);
    }

    public Button getButton(int id) {

        return get(id);
    }


    public ImageView getImageView(int id) {
        return get(id);
    }

    public void setTextView(int id, CharSequence charSequence) {
        getTextView(id).setText(charSequence);
    }


}
