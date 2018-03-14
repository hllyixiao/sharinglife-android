package com.sqin.libbaseframe.adapter;

import android.util.SparseArray;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;

import java.util.List;

/**
 * Created by sqin on 2016/4/15.
 */
public abstract class BaseListAdapter<T> extends BaseAdapter {
    @Override
    public int getCount() {
        return getData() == null ? 0 : getData().size();
    }

    @Override
    public T getItem(int position) {
        if(getData() == null) return null;
        else if(position < 0 || getData().size() <= position) return null;
        else return getData().get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View v, ViewGroup parent) {
        if(v == null){
            v = getView();
        }
        setView(position, v);
        return v;
    }

    public abstract List<T> getData();

    public abstract View getView();

    public abstract void setView(int position, View v);

    /**
     * 实现ViewHolder模式
     * ImageView view = get(convertView, R.id.imageView);
     * @param view
     * @param id
     * @return
     */
    @SuppressWarnings("unchecked")
    public static <T extends View> T get(View view, int id) {
        SparseArray<View> viewHolder = (SparseArray<View>) view.getTag();

        if (viewHolder == null) {
            viewHolder = new SparseArray<View>();
            view.setTag(viewHolder);
        }
        View childView = viewHolder.get(id);
        if (childView == null) {
            childView = view.findViewById(id);
            viewHolder.put(id, childView);
        }
        return (T) childView;
    }
}
