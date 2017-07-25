package com.yc.english.group.view.activitys;

import android.graphics.BitmapFactory;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.blankj.utilcode.util.ImageUtils;
import com.blankj.utilcode.util.LogUtils;
import com.yc.english.R;
import com.yc.english.base.view.FullScreenActivity;

import butterknife.BindView;
import butterknife.OnClick;

/**
 * Created by wanglin  on 2017/7/25 08:30.
 */

public class GroupJoinActivity extends FullScreenActivity {
    private static final String TAG = "GroupJoinActivity";
    @BindView(R.id.et_class_group)
    EditText etClassGroup;
    @BindView(R.id.ib_delete)
    ImageButton ibDelete;

    @BindView(R.id.tv_class_name)
    TextView tvClassName;
    @BindView(R.id.ll_class_name)
    LinearLayout llClassName;
    @BindView(R.id.btn_join)
    Button btnJoin;
    @BindView(R.id.roundView)
    ImageView roundView;

    @Override
    public void init() {
        mToolbar.showNavigationIcon();
        roundView.setImageBitmap(ImageUtils.toRound(BitmapFactory.decodeResource(getResources(), R.mipmap.portial)));
        initListener();
    }

    private void initListener() {
        etClassGroup.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(Editable s) {
                showOrHideView(s);
            }
        });

    }

    @Override
    public int getLayoutID() {
        return R.layout.group_activity_join_class;
    }

    @OnClick({R.id.btn_join, R.id.ib_delete})
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.btn_join:
                break;
            case R.id.ib_delete:
                etClassGroup.setText("");
                break;
        }

    }


    public void showOrHideView(Editable s) {
        if (!TextUtils.isEmpty(s.toString())) {
            ibDelete.setVisibility(View.VISIBLE);
            llClassName.setVisibility(View.VISIBLE);
            btnJoin.setVisibility(View.VISIBLE);
        } else {
            ibDelete.setVisibility(View.GONE);
            llClassName.setVisibility(View.GONE);
            btnJoin.setVisibility(View.GONE);
        }

    }

}
