package com.yc.english.group.view.activitys;

import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.yc.english.R;
import com.yc.english.base.view.FullScreenActivity;
import com.yc.english.group.view.widget.RoundView;

import butterknife.BindView;

/**
 * Created by wanglin  on 2017/7/25 08:30.
 */

public class GroupJoinActivity extends FullScreenActivity {
    @BindView(R.id.et_class_group)
    EditText etClassGroup;
    @BindView(R.id.ib_delete)
    ImageButton ibDelete;
    @BindView(R.id.roundView)
    RoundView roundView;
    @BindView(R.id.tv_class_name)
    TextView tvClassName;
    @BindView(R.id.ll_class_name)
    LinearLayout llClassName;
    @BindView(R.id.btn_join)
    Button btnJoin;

    @Override
    public void init() {
        mToolbar.showNavigationIcon();
        roundView.setImageDrawable(getResources().getDrawable(R.drawable.portial));
    }

    @Override
    public int getLayoutID() {
        return R.layout.group_activity_join_class;
    }


}
