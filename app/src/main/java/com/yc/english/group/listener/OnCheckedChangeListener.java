package com.yc.english.group.listener;

import android.view.View;

import com.yc.english.group.model.bean.StudentInfo;

/**
 * Created by wanglin  on 2017/7/27 16:41.
 * 多选选择监听
 */

public interface OnCheckedChangeListener<T> {

    void onClick(int position, View view, boolean isClicked, T studentInfo);
}
