package com.yc.english.vip.views.activity;

import android.support.v7.app.ActionBar;
import android.support.v7.widget.Toolbar;
import android.view.View;

import com.yc.english.R;
import com.yc.english.base.view.BaseActivity;

import butterknife.BindView;

/**
 * Created by wanglin  on 2017/12/21 14:16.
 */

public class ProtocolActivity extends BaseActivity {
    @BindView(R.id.toolbar)
    Toolbar toolbar;

    @Override
    public void init() {
        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
    }

    @Override
    public int getLayoutId() {
        return R.layout.activity_protocol;
    }


}
