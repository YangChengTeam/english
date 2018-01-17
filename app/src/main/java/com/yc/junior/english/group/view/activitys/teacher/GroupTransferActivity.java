package com.yc.junior.english.group.view.activitys.teacher;

import android.text.InputFilter;
import android.view.inputmethod.EditorInfo;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;

import com.jakewharton.rxbinding.view.RxView;
import com.yc.junior.english.R;
import com.yc.junior.english.base.view.FullScreenActivity;
import com.yc.junior.english.group.contract.GroupTransferGroupContract;
import com.yc.junior.english.group.model.bean.GroupInfoHelper;
import com.yc.junior.english.group.presenter.GroupTransferGroupPresenter;

import java.util.concurrent.TimeUnit;

import butterknife.BindView;
import rx.functions.Action1;

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


    @Override
    public void init() {
        mPresenter = new GroupTransferGroupPresenter(this, this);

        mToolbar.setTitle(GroupInfoHelper.getClassInfo().getType().equals("1") ?
                getResources().getString(R.string.transfer_union) : getResources().getString(R.string.transfer_group));
        mToolbar.showNavigationIcon();

        btnCreate.setText(getResources().getString(R.string.confirm_transfer));

        String transferName = GroupInfoHelper.getClassInfo().getType().equals("1") ?
                getResources().getString(R.string.receive_union_phone) : getResources().getString(R.string.receive_group_phone);
        etClassGroup.setHint(transferName);
        etClassGroup.setInputType(EditorInfo.TYPE_CLASS_PHONE);

        etClassGroup.setFilters(new InputFilter[]{new InputFilter.LengthFilter(11)});

        initListener();
    }

    private void initListener() {
        RxView.clicks(btnCreate).throttleFirst(200, TimeUnit.MILLISECONDS).subscribe(new Action1<Void>() {
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


}
