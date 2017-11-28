package com.yc.english.union.view.activitys.teacher;

import android.text.TextUtils;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.Button;
import android.widget.EditText;

import com.jakewharton.rxbinding.view.RxView;
import com.yc.english.R;
import com.yc.english.base.view.FullScreenActivity;
import com.yc.english.group.contract.GroupCreateContract;
import com.yc.english.group.presenter.GroupCreatePresenter;
import com.yc.english.main.hepler.UserInfoHelper;
import com.yc.english.pay.alipay.AnimationUtil;

import java.util.concurrent.TimeUnit;

import butterknife.BindView;
import rx.functions.Action1;

/**
 * Created by wanglin  on 2017/7/24 18:36.
 */

public class UnionCreateActivity extends FullScreenActivity<GroupCreatePresenter> implements GroupCreateContract.View {

    private static final String TAG = "UnionCreateActivity";
    @BindView(R.id.et_class_group)
    EditText etClassGroup;
    @BindView(R.id.btn_create)
    Button btnCreate;

    @Override
    public void init() {

        mPresenter = new GroupCreatePresenter(this, this);
        mToolbar.showNavigationIcon();
        mToolbar.setTitle(getResources().getString(R.string.create_union));
        etClassGroup.setHint(getString(R.string.write_union_name));
        initListener();
    }

    private void initListener() {

        RxView.clicks(btnCreate).throttleFirst(200, TimeUnit.MILLISECONDS).subscribe(new Action1<Void>() {
            @Override
            public void call(Void aVoid) {
                if (TextUtils.isEmpty(etClassGroup.getText().toString().trim())) {
                    Animation animation = AnimationUtils.loadAnimation(UnionCreateActivity.this, R.anim.shake);
                    etClassGroup.startAnimation(animation);
                }

                mPresenter.createGroup(UserInfoHelper.getUserInfo().getUid(), etClassGroup.getText().toString().trim(), "", "1");
            }
        });

    }

    @Override
    public int getLayoutId() {
        return R.layout.group_activity_create_class;
    }

}
