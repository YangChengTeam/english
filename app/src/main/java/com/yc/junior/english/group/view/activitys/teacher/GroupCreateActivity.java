package com.yc.junior.english.group.view.activitys.teacher;

import android.widget.Button;
import android.widget.EditText;

import com.jakewharton.rxbinding.view.RxView;
import com.yc.junior.english.R;
import com.yc.junior.english.base.view.FullScreenActivity;
import com.yc.junior.english.group.contract.GroupCreateContract;
import com.yc.junior.english.group.presenter.GroupCreatePresenter;
import com.yc.junior.english.main.hepler.UserInfoHelper;

import java.util.concurrent.TimeUnit;

import butterknife.BindView;
import rx.functions.Action1;

/**
 * Created by wanglin  on 2017/7/24 18:36.
 */

public class GroupCreateActivity extends FullScreenActivity<GroupCreatePresenter> implements GroupCreateContract.View {

    private static final String TAG = "UnionCreateActivity";
    @BindView(R.id.et_class_group)
    EditText etClassGroup;
    @BindView(R.id.btn_create)
    Button btnCreate;


    @Override
    public void init() {

        mPresenter = new GroupCreatePresenter(this, this);
        mToolbar.showNavigationIcon();
        mToolbar.setTitle(getResources().getString(R.string.create_group));
        initListener();
    }

    private void initListener() {

        RxView.clicks(btnCreate).throttleFirst(200, TimeUnit.MILLISECONDS).subscribe(new Action1<Void>() {
            @Override
            public void call(Void aVoid) {
                mPresenter.createGroup(UserInfoHelper.getUserInfo().getUid(), etClassGroup.getText().toString().trim(), "", "0");
            }
        });

    }

    @Override
    public int getLayoutId() {
        return R.layout.group_activity_create_class;
    }

}
