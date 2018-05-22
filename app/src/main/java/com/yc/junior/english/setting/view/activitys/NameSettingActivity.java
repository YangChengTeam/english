package com.yc.junior.english.setting.view.activitys;

import android.content.Intent;
import android.widget.Button;
import android.widget.EditText;

import com.blankj.utilcode.util.KeyboardUtils;
import com.blankj.utilcode.util.StringUtils;
import com.jakewharton.rxbinding.view.RxView;
import com.yc.junior.english.R;
import com.yc.junior.english.base.helper.TipsHelper;
import com.yc.junior.english.base.view.FullScreenActivity;
import com.yc.junior.english.setting.contract.NameSettingContract;
import com.yc.junior.english.setting.presenter.NameSettingPresenter;

import java.util.concurrent.TimeUnit;

import butterknife.BindView;
import rx.functions.Action1;

/**
 * Created by zhangkai on 2017/7/27.
 */

public class NameSettingActivity extends FullScreenActivity<NameSettingPresenter> implements NameSettingContract.View {

    @BindView(R.id.et_value)
    EditText mValueEditText;

    @BindView(R.id.btn_complete)
    Button mCompleteButton;

    @Override
    public void init() {
        Intent intent = this.getIntent();
        final String type = intent.getStringExtra("type");
        String name = intent.getStringExtra("name");
        String value = intent.getStringExtra("value");
        mValueEditText.setText(value);
        mToolbar.setTitle(name);
        mToolbar.showNavigationIcon();

        mPresenter = new NameSettingPresenter(this, this);

        RxView.clicks(mCompleteButton).throttleFirst(200, TimeUnit.MILLISECONDS).subscribe(new Action1<Void>() {
            @Override
            public void call(Void aVoid) {
                KeyboardUtils.hideSoftInput(NameSettingActivity.this);
                String value = mValueEditText.getText().toString();
                String name = type.equals("0") ? "姓名" :  "学校";
                if (StringUtils.isEmpty(value)) {
                    TipsHelper.tips(NameSettingActivity.this, name + "不能为空");
                    return;
                }
                mPresenter.udpateUserInfo(type.equals("0") ? value : "", type.equals("1") ? value : "");
            }
        });

    }

    @Override
    public int getLayoutId() {
        return R.layout.setting_activity_name;
    }
}
