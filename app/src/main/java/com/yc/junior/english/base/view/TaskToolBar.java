package com.yc.junior.english.base.view;

import android.content.Context;
import android.util.AttributeSet;
import android.view.View;
import android.widget.TextView;

import com.jakewharton.rxbinding.view.RxView;
import com.yc.junior.english.R;

import java.util.concurrent.TimeUnit;

import butterknife.BindView;
import rx.functions.Action1;

/**
 * Created by zhangkai on 2017/7/25.
 */

public class TaskToolBar extends BaseToolBar {

    @BindView(R.id.tv_menu)
    TextView mMenuTextView;

    @BindView(R.id.status_bar)
    View mStatusBar;


    public View getStatusBar() {
        return mStatusBar;
    }

    public TaskToolBar(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    @Override
    public int getLayoutId() {
        return R.layout.base_task_toolbar;
    }

    public void showNavigationIcon() {
        mToolbar.setNavigationIcon(R.mipmap.intelligent_back);
        isShowNavigationIcon = true;
    }

    @Override
    public void setMenuTitle(String mMenuTitle) {
        hasMenu = hasMenu ? hasMenu : false;
        this.mMenuTitle = mMenuTitle;
        ((View) mMenuTextView.getParent()).setVisibility(View.VISIBLE);
        mMenuTextView.setText(mMenuTitle);
    }

    @Override
    public void setMenuIcon(int iconResid) {
        super.setMenuIcon(iconResid);
        ((View) mMenuTextView.getParent()).setVisibility(View.GONE);
    }

    @Override
    public void setMenuTitleColor(int color) {
        mMenuTextView.setTextColor(color);
    }

    @Override
    public void setOnItemClickLisener(final OnItemClickLisener onItemClickLisener) {
        super.setOnItemClickLisener(onItemClickLisener);
        RxView.clicks(mMenuTextView).throttleFirst(200, TimeUnit.MILLISECONDS).subscribe(new Action1<Void>() {
            @Override
            public void call(Void aVoid) {
                onItemClickLisener.onClick();
            }
        });
    }



}
