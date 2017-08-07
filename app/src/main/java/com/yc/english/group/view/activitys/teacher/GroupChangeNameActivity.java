package com.yc.english.group.view.activitys.teacher;

import android.os.Bundle;
import android.text.TextUtils;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;

import com.blankj.utilcode.util.ToastUtils;
import com.jakewharton.rxbinding.view.RxView;
import com.yc.english.R;
import com.yc.english.base.view.FullScreenActivity;
import com.yc.english.group.contract.GroupChangeInfoContract;
import com.yc.english.group.presenter.GroupChangeInfoPresenter;
import com.yc.english.group.rong.models.GroupInfo;

import java.util.concurrent.TimeUnit;

import butterknife.BindView;
import butterknife.ButterKnife;
import rx.functions.Action1;
import rx.functions.Func1;

/**
 * Created by wanglin  on 2017/7/26 18:20.
 */

public class GroupChangeNameActivity extends FullScreenActivity<GroupChangeInfoPresenter> implements GroupChangeInfoContract.View {


    @BindView(R.id.et_class_group)
    EditText etClassGroup;
    @BindView(R.id.ib_delete)
    ImageButton ibDelete;
    @BindView(R.id.btn_create)
    Button btnCreate;
    private GroupInfo groupInfo;

    @Override
    public void init() {
        mToolbar.setTitle(getResources().getString(R.string.group_name));
        mToolbar.showNavigationIcon();
        mPresenter = new GroupChangeInfoPresenter(this, this);
        if (getIntent() != null) {
            groupInfo = (GroupInfo) getIntent().getSerializableExtra("group");
            etClassGroup.setText(groupInfo.getName());
        }
        RxView.clicks(btnCreate).filter(new Func1<Void, Boolean>() {
            @Override
            public Boolean call(Void aVoid) {
                ToastUtils.showShort("请输入要修改的班群名称");
                return !TextUtils.isEmpty(etClassGroup.getText().toString().trim());
            }
        }).throttleFirst(200, TimeUnit.MILLISECONDS).subscribe(new Action1<Void>() {
            @Override
            public void call(Void aVoid) {

                mPresenter.changeGroupInfo(groupInfo.getId(), etClassGroup.getText().toString().trim(), "", "");
            }
        });

    }

    @Override
    public int getLayoutId() {
        return R.layout.group_activity_change_class;
    }


}
