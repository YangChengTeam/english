package com.yc.english.group.view.activitys.teacher;

import android.text.InputFilter;
import android.text.TextUtils;
import android.view.inputmethod.EditorInfo;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;

import com.hwangjr.rxbus.RxBus;
import com.jakewharton.rxbinding.view.RxView;
import com.yc.english.R;
import com.yc.english.base.helper.TipsHelper;
import com.yc.english.base.view.FullScreenActivity;
import com.yc.english.group.constant.BusAction;
import com.yc.english.group.contract.GroupTransferGroupContract;
import com.yc.english.group.model.bean.GroupInfoHelper;
import com.yc.english.group.presenter.GroupTransferGroupPresenter;

import java.util.concurrent.TimeUnit;

import butterknife.BindView;
import rx.functions.Action1;
import rx.functions.Func1;

/**
 * Created by wanglin  on 2017/7/27 14:04.
 * 转让班级
 */

public class GroupTransferActivity extends FullScreenActivity<GroupTransferGroupPresenter> implements GroupTransferGroupContract.View {
    @BindView(R.id.et_class_group)
    EditText etClassGroup;
    @BindView(R.id.ib_delete)
    ImageButton ibDelete;
    @BindView(R.id.btn_create)
    Button btnCreate;

    private String transferName = "";

    @Override
    public void init() {
        mPresenter = new GroupTransferGroupPresenter(this, this);

        mToolbar.setTitle(GroupInfoHelper.getClassInfo().getType().equals("1") ?
                getResources().getString(R.string.transfer_union) : getResources().getString(R.string.transfer_group));
        mToolbar.showNavigationIcon();
        btnCreate.setText(getResources().getString(R.string.confirm_transfer));
        transferName = GroupInfoHelper.getClassInfo().getType().equals("1") ?
                getResources().getString(R.string.recevicer_union_phone) : getResources().getString(R.string.recevicer_group_phone);
        etClassGroup.setHint(transferName);
        etClassGroup.setInputType(EditorInfo.TYPE_CLASS_PHONE);

        etClassGroup.setFilters(new InputFilter[]{new InputFilter.LengthFilter(11)});

        initListener();
    }

    private void initListener() {
        RxView.clicks(btnCreate).filter(new Func1<Void, Boolean>() {
            @Override
            public Boolean call(Void aVoid) {
                if (TextUtils.isEmpty(etClassGroup.getText().toString().trim())) {
                    TipsHelper.tips(GroupTransferActivity.this, transferName);
                }
                return !TextUtils.isEmpty(etClassGroup.getText().toString().trim());
            }
        }).throttleFirst(200, TimeUnit.MILLISECONDS).subscribe(new Action1<Void>() {
            @Override
            public void call(Void aVoid) {
                mPresenter.transferGroup(GroupInfoHelper.getClassInfo().getClass_id(), GroupInfoHelper.getClassInfo().getMaster_id(), etClassGroup.getText().toString().trim());
            }
        });
    }

    @Override
    public int getLayoutId() {
        return R.layout.group_activity_change_class;
    }


    @Override
    public void showTransferResult() {
        finish();
        RxBus.get().post(BusAction.FINISH, BusAction.REMOVE_GROUP);
        RxBus.get().post(BusAction.GROUP_LIST, "transfer group");
    }
}
