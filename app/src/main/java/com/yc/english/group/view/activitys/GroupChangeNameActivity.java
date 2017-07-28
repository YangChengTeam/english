package com.yc.english.group.view.activitys;

import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.yc.english.R;
import com.yc.english.base.view.BaseActivity;

import butterknife.BindView;

/**
 * Created by wanglin  on 2017/7/26 18:20.
 */

public class GroupChangeNameActivity extends BaseActivity {

    @BindView(R.id.txt1)
    TextView txt1;

    @BindView(R.id.img3)
    ImageView img3;


    @Override
    public void init() {
        txt1.setText(getResources().getString(R.string.group_name));
        img3.setVisibility(View.GONE);
    }

    @Override
    public int getLayoutId() {
        return R.layout.group_activity_change_class;
    }


}
