package com.yc.english.base.view;

import android.content.Context;
import android.graphics.Color;
import android.graphics.Typeface;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.AttributeSet;
import android.view.View;
import android.widget.TextView;

import com.blankj.utilcode.util.BarUtils;
import com.yc.english.R;
import butterknife.BindView;

/**
 * Created by zhangkai on 2017/7/25.
 */

public abstract class BaseToolBar extends BaseView {
    private AppCompatActivity mActivity;
    protected boolean isShowNavigationIcon;

    @BindView(R.id.toolbar_sub)
    Toolbar mToolbar;

    @BindView(R.id.tv_tb_title)
    TextView mTitleTextView;

    public BaseToolBar(Context context, AttributeSet attrs) {
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

    public void setTitle(String title){
        mTitleTextView.setTypeface(null, Typeface.BOLD);
        mTitleTextView.setText(title);
    }

    public void showNavigationIcon(){
        mToolbar.setNavigationIcon(R.mipmap.base_back);
        isShowNavigationIcon = true;
    }
}
