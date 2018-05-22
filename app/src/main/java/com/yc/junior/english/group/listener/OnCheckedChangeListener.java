package com.yc.junior.english.group.listener;

import android.view.View;

/**
 * Created by wanglin  on 2017/7/27 16:41.
 * 多选选择监听
 */

public interface OnCheckedChangeListener<T> {

    void onClick(View view, boolean isClicked, T t);
}
