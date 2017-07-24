package com.yc.english.base.view;

import android.content.Context;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.AttributeSet;
import android.view.View;

import com.yc.english.R;

import butterknife.BindView;

/**
 * Created by zhangkai on 2017/7/24.
 */

public class MainToolBar extends BaseView {
    private AppCompatActivity mActivity;
    private boolean isShowNavigationIcon;

    @BindView(R.id.toolbar_sub)
    Toolbar mToolbar;

    public MainToolBar(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public void init(AppCompatActivity activity) {
        mToolbar.setTitle("");
        mActivity = activity;
        activity.setSupportActionBar(mToolbar);
        if(isShowNavigationIcon){
            mToolbar.setNavigationOnClickListener(new OnClickListener() {
                @Override
                public void onClick(View v) {
                    mActivity.finish();
                }
            });
        }
    }

    public void showNavigationIcon(){
        mToolbar.setNavigationIcon(R.mipmap.back);
        isShowNavigationIcon = true;
    }

    @Override
    public int getLayoutId() {
        return R.layout.view_toolbar;
    }
}
