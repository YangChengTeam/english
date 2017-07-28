package com.yc.english.setting.view.activitys;

import android.content.Intent;

import com.jakewharton.rxbinding.view.RxView;
import com.yc.english.R;
import com.yc.english.base.view.FullScreenActivity;
import com.yc.english.setting.view.widgets.SettingItemView;

import java.util.concurrent.TimeUnit;

import butterknife.BindView;
import rx.functions.Action1;

/**
 * Created by zhangkai on 2017/7/24.
 */

public class PersonCenterActivity extends FullScreenActivity {

    @BindView(R.id.si_avatar)
    SettingItemView mAvatarSettingItemView;

    @BindView(R.id.si_name)
    SettingItemView mNameSettingItemView;

    @BindView(R.id.si_school)
    SettingItemView mSchoolSettingItemView;

    @BindView(R.id.si_phone)
    SettingItemView mPhoneSettingItemView;

    @BindView(R.id.si_password)
    SettingItemView mPasswordSettingItemView;


    @Override
    public void init() {
        mToolbar.setTitle("个人信息");
        mToolbar.showNavigationIcon();

        mPhoneSettingItemView.hideArrow();
        mAvatarSettingItemView.setAvatar(R.drawable.sample_avatar);

        RxView.clicks(mAvatarSettingItemView).throttleFirst(200, TimeUnit.MILLISECONDS).subscribe(new Action1<Void>() {
            @Override
            public void call(Void aVoid) {

            }
        });

        RxView.clicks(mNameSettingItemView).throttleFirst(200, TimeUnit.MILLISECONDS).subscribe(new Action1<Void>() {
            @Override
            public void call(Void aVoid) {
                Intent intent = new Intent(PersonCenterActivity.this, NameSettingActivity.class);
                intent.putExtra("name", "修改姓名");
                startActivity(intent);
            }
        });

        RxView.clicks(mSchoolSettingItemView).throttleFirst(200, TimeUnit.MILLISECONDS).subscribe(new Action1<Void>() {
            @Override
            public void call(Void aVoid) {
                Intent intent = new Intent(PersonCenterActivity.this, NameSettingActivity.class);
                intent.putExtra("name", "修改学校");
                startActivity(intent);
            }
        });



        RxView.clicks(mPasswordSettingItemView).throttleFirst(200, TimeUnit.MILLISECONDS).subscribe(new Action1<Void>() {
            @Override
            public void call(Void aVoid) {
                Intent intent = new Intent(PersonCenterActivity.this, ModifyPasswordActivity.class);
                startActivity(intent);
            }
        });
    }

    @Override
    public int getLayoutID() {
        return R.layout.setting_person_center_activity;
    }
}
