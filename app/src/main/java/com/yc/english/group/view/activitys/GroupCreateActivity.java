package com.yc.english.group.view.activitys;

import android.text.TextUtils;
import android.view.View;
import android.widget.EditText;

import com.blankj.utilcode.util.ToastUtils;
import com.google.gson.Gson;
import com.yc.english.R;
import com.yc.english.base.view.FullScreenActivity;
import com.yc.english.group.rong.Login;

import butterknife.BindView;
import butterknife.OnClick;
import rx.android.schedulers.AndroidSchedulers;
import rx.functions.Action1;

/**
 * Created by wanglin  on 2017/7/24 18:36.
 */

public class GroupCreateActivity extends FullScreenActivity {
    @BindView(R.id.et_class_group)
    EditText etClassGroup;
    //正式环境下换服务器返回的数据
    private String username = "username";
    private String userId = "userId1";
    private String userPorait = "http://www.rongcloud.cn/images/logo.png";

    @Override
    public void init() {
        mToolbar.showNavigationIcon();
    }

    @Override
    public int getLayoutID() {
        return R.layout.group_activity_create_class;
    }


    @OnClick(R.id.btn_create)
    public void onClick(View view) {
        String groupInfo = etClassGroup.getText().toString().trim();
        if (TextUtils.isEmpty(groupInfo)) {
            ToastUtils.showShort("请输入班级名称...");
            return;
        }
        Login.login(username, userId, userPorait).observeOn(AndroidSchedulers.mainThread()).subscribe(new Action1<String>() {
            @Override
            public void call(String s) {
//                Gson gson
            }
        });


    }


}
