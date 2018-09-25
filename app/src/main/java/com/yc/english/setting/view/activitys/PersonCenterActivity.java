package com.yc.english.setting.view.activitys;

import android.content.Intent;
import android.graphics.Color;
import android.text.TextUtils;
import android.view.TextureView;

import com.blankj.utilcode.util.SPUtils;
import com.blankj.utilcode.util.StringUtils;
import com.hwangjr.rxbus.annotation.Subscribe;
import com.hwangjr.rxbus.annotation.Tag;
import com.hwangjr.rxbus.thread.EventThread;
import com.jakewharton.rxbinding.view.RxView;
import com.yc.english.R;
import com.yc.english.base.helper.AvatarHelper;
import com.yc.english.base.helper.GlideHelper;
import com.yc.english.base.view.FullScreenActivity;
import com.yc.english.base.view.SelectGradePopupWindow;
import com.yc.english.main.hepler.UserInfoHelper;
import com.yc.english.main.model.domain.Constant;
import com.yc.english.main.model.domain.UserInfo;
import com.yc.english.setting.contract.PersonCenterContract;
import com.yc.english.setting.presenter.PersonCenterPresenter;
import com.yc.english.setting.view.widgets.SettingItemView;

import java.io.File;
import java.util.concurrent.TimeUnit;

import butterknife.BindView;
import rx.functions.Action1;

/**
 * Created by zhangkai on 2017/7/24.
 */

public class PersonCenterActivity extends FullScreenActivity<PersonCenterPresenter> implements PersonCenterContract
        .View {
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

    @BindView(R.id.si_grade)
    SettingItemView mGradeSettingItemView;

    @Override
    public void init() {
        mToolbar.setTitle("个人信息");
        mToolbar.showNavigationIcon();

        setGradeInfo();

        mPhoneSettingItemView.hideArrow();

        RxView.clicks(mAvatarSettingItemView).throttleFirst(200, TimeUnit.MILLISECONDS).subscribe(new Action1<Void>() {
            @Override
            public void call(Void aVoid) {
                AvatarHelper.openAlbum(PersonCenterActivity.this);
            }
        });

        RxView.clicks(mNameSettingItemView).throttleFirst(200, TimeUnit.MILLISECONDS).subscribe(new Action1<Void>() {
            @Override
            public void call(Void aVoid) {
                Intent intent = new Intent(PersonCenterActivity.this, NameSettingActivity.class);
                intent.putExtra("type", "0");
                intent.putExtra("name", "修改姓名");
                UserInfo userInfo = UserInfoHelper.getUserInfo();
                intent.putExtra("value", userInfo.getNickname());
                startActivity(intent);
            }
        });

        RxView.clicks(mSchoolSettingItemView).throttleFirst(200, TimeUnit.MILLISECONDS).subscribe(new Action1<Void>() {
            @Override
            public void call(Void aVoid) {
                Intent intent = new Intent(PersonCenterActivity.this, NameSettingActivity.class);
                intent.putExtra("type", "1");
                intent.putExtra("name", "修改学校");
                UserInfo userInfo = UserInfoHelper.getUserInfo();
                intent.putExtra("value", userInfo.getSchool());
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

        RxView.clicks(mGradeSettingItemView).throttleFirst(200, TimeUnit.MILLISECONDS).subscribe(new Action1<Void>() {
            @Override
            public void call(Void aVoid) {
                SelectGradePopupWindow selectGradePopupWindow = new SelectGradePopupWindow(PersonCenterActivity.this);
                selectGradePopupWindow.show(getWindow().getDecorView().getRootView(), new Runnable() {
                    @Override
                    public void run() {
                        setGradeInfo();
                    }
                });
            }
        });

        mPresenter = new PersonCenterPresenter(this, this);
    }

    @Override
    public int getLayoutId() {
        return R.layout.setting_person_center_activity;
    }

    @Subscribe(
            thread = EventThread.MAIN_THREAD,
            tags = {
                    @Tag(Constant.USER_INFO)
            }
    )
    @Override
    public void showUserInfo(UserInfo userInfo) {
        GlideHelper.circleBorderImageView(this, mAvatarSettingItemView.getAvatarImageView(), userInfo.getAvatar(), R.mipmap
                .default_avatar, 0.5f, Color.parseColor("#dbdbe0"));

        if (!StringUtils.isEmpty(userInfo.getSchool())) {
            mSchoolSettingItemView.setInfo(userInfo.getSchool());
        } else {
            mSchoolSettingItemView.setHintInfo("还有填写学校~");
        }

        if (!StringUtils.isEmpty(userInfo.getNickname())) {
            mNameSettingItemView.setInfo(userInfo.getNickname());
        } else {
            mNameSettingItemView.setHintInfo("还有填写姓名~");
        }

        String phone = userInfo.getMobile();
        if (!TextUtils.isEmpty(phone)) {
            phone = phone.replace(phone.substring(3, 7), "****");
        }
        mPhoneSettingItemView.setInfo(phone);
    }

    private void setGradeInfo() {
        String grade = "一年级";
        switch (SPUtils.getInstance().getInt("grade", 0)) {
            case 0:
                grade = "通用";
                break;
            case 1:
                grade = "一年级";
                break;
            case 2:
                grade = "二年级";
                break;
            case 3:
                grade = "三年级";
                break;
            case 4:
                grade = "四年级";
                break;
            case 5:
                grade = "五年级";
                break;
            case 6:
                grade = "六年级";
                break;
            case 7:
                grade = "七年级";
                break;
            case 8:
                grade = "八年级";
                break;
            case 9:
                grade = "九年级";
                break;

        }
        mGradeSettingItemView.setInfo(grade);

    }

    @Override
    protected void onActivityResult(final int requestCode, final int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        AvatarHelper.onActivityForResult(PersonCenterActivity.this, requestCode, resultCode, data, new AvatarHelper.IAvatar() {
            @Override
            public void uploadAvatar(String image) {
                mPresenter.uploadAvatar(image);
            }
        });
    }
}
