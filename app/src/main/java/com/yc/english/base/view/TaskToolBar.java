package com.yc.english.base.view;

import android.content.Context;
import android.graphics.Color;
import android.support.v7.app.AppCompatActivity;
import android.util.AttributeSet;

import com.blankj.utilcode.util.BarUtils;
import com.yc.english.R;

/**
 * Created by zhangkai on 2017/7/25.
 */

public class TaskToolBar extends BaseToolBar {
    public TaskToolBar(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    @Override
    public int getLayoutId() {
        return R.layout.base_task_toolbar;
    }

    public void showNavigationIcon(){
        mToolbar.setNavigationIcon(R.mipmap.task_back);
        isShowNavigationIcon = true;
    }

    @Override
    public void init(AppCompatActivity activity) {
        super.init(activity);
        BarUtils.setStatusBarColor(activity, Color.BLACK);
    }
}
