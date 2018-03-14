package com.sqin.libbaseframe.title.interfaces;

import android.view.View;

/**
 * 作者：sqin
 * 日期：2016/3/3.
 */
public interface ITitleDelegete {
    void setContentView(View contentView);
    View getTitleView();
    void setTitleText(String titleText);
}
