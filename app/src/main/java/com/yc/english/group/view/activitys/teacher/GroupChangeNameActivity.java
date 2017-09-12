package com.yc.english.group.view.activitys.teacher;

import android.text.TextUtils;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;

import com.blankj.utilcode.util.ToastUtils;
import com.hwangjr.rxbus.RxBus;
import com.jakewharton.rxbinding.view.RxView;
import com.yc.english.R;
import com.yc.english.base.view.FullScreenActivity;
import com.yc.english.group.constant.BusAction;
import com.yc.english.group.contract.GroupChangeInfoContract;
import com.yc.english.group.model.bean.GroupInfoHelper;
import com.yc.english.group.model.bean.RemoveGroupInfo;
import com.yc.english.group.presenter.GroupChangeInfoPresenter;
import com.yc.english.group.rong.models.GroupInfo;

import java.util.concurrent.TimeUnit;

import butterknife.BindView;
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
    private String name;

    @Override
    public void init() {

        name = GroupInfoHelper.getClassInfo().getType().equals("1") ?
                getResources().getString(R.string.union_name) : getResources().getString(R.string.group_name);

        mToolbar.setTitle(name);
        mToolbar.showNavigationIcon();
        mPresenter = new GroupChangeInfoPresenter(this, this);

        etClassGroup.setText(GroupInfoHelper.getGroupInfo().getName());
        etClassGroup.setSelection(GroupInfoHelper.getGroupInfo().getName().length());

        RxView.clicks(btnCreate).filter(new Func1<Void, Boolean>() {
            @Override
            public Boolean call(Void aVoid) {
                String etName = etClassGroup.getText().toString().trim();
                if (TextUtils.isEmpty(etName))
                    ToastUtils.showShort("请输入要修改的" + name);
                return !TextUtils.isEmpty(etName);
            }
        }).throttleFirst(200, TimeUnit.MILLISECONDS).subscribe(new Action1<Void>() {
            @Override
            public void call(Void aVoid) {

                mPresenter.changeGroupInfo(GroupChangeNameActivity.this, GroupInfoHelper.getGroupInfo().getId(), etClassGroup.getText().toString().trim(), "", "");
            }
        });

    }

    @Override
    public int getLayoutId() {
        return R.layout.group_activity_change_class;
    }


    @Override
    public void showChangeResult(RemoveGroupInfo data) {
        RxBus.get().post(BusAction.CHANGE_NAME, data.getClass_name());
        RxBus.get().post(BusAction.GROUP_LIST, "changeName");
        finish();
    }
}
