package com.yc.junior.english.vip.views.activity;

import android.support.v7.widget.Toolbar;
import android.view.View;

import com.yc.junior.english.R;
import com.yc.junior.english.base.view.BaseActivity;

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
