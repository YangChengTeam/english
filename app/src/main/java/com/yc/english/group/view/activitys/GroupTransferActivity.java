package com.yc.english.group.view.activitys;

import android.text.TextUtils;
import android.view.inputmethod.EditorInfo;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;

import com.blankj.utilcode.util.ToastUtils;
import com.jakewharton.rxbinding.view.RxView;
import com.yc.english.R;
import com.yc.english.base.view.FullScreenActivity;

import java.util.concurrent.TimeUnit;

import butterknife.BindView;
import rx.functions.Action1;
import rx.functions.Func1;

/**
 * Created by wanglin  on 2017/7/27 14:04.
 */

public class GroupTransferActivity extends FullScreenActivity {
    @BindView(R.id.et_class_group)
    EditText etClassGroup;
    @BindView(R.id.ib_delete)
    ImageButton ibDelete;
    @BindView(R.id.btn_create)
    Button btnCreate;

    @Override
    public void init() {
        mToolbar.setTitle(getResources().getString(R.string.transfer_group));
        mToolbar.showNavigationIcon();
        btnCreate.setText(getResources().getString(R.string.confirm_transfer));
        etClassGroup.setHint(getResources().getString(R.string.recevicer_phone));
        etClassGroup.setInputType(EditorInfo.TYPE_CLASS_PHONE);
        initListener();
    }

    private void initListener() {
        RxView.clicks(btnCreate).filter(new Func1<Void, Boolean>() {
            @Override
            public Boolean call(Void aVoid) {
                ToastUtils.showShort("请输入要转让的班群名称");
                return !TextUtils.isEmpty(etClassGroup.getText().toString().trim());
            }
        }).throttleFirst(200, TimeUnit.MILLISECONDS).subscribe(new Action1<Void>() {
            @Override
            public void call(Void aVoid) {
                ToastUtils.showShort("确认转让");
            }
        });
    }

    @Override
    public int getLayoutId() {
        return R.layout.group_activity_change_class;
    }


}
