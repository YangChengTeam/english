package com.yc.english.group.listener;

import android.widget.CompoundButton;

/**
 * Created by wanglin  on 2017/7/27 16:41.
 * 多选选择监听
 */

public interface onCheckedChangeListener {
    void onCheckedChange(int position, CompoundButton buttonView, boolean isChecked);
}
