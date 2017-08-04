package com.yc.english.setting.view.activitys;

import android.app.Activity;
import android.content.Intent;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.util.Base64;

import com.blankj.utilcode.util.LogUtils;
import com.blankj.utilcode.util.StringUtils;
import com.bumptech.glide.Glide;
import com.bumptech.glide.load.resource.bitmap.CircleCrop;
import com.bumptech.glide.request.RequestOptions;
import com.hwangjr.rxbus.annotation.Subscribe;
import com.hwangjr.rxbus.annotation.Tag;
import com.hwangjr.rxbus.thread.EventThread;
import com.jakewharton.rxbinding.view.RxView;
import com.yc.english.R;
import com.yc.english.base.helper.TipsHelper;
import com.yc.english.base.view.FullScreenActivity;
import com.yc.english.main.hepler.UserInfoHelper;
import com.yc.english.main.model.domain.Constant;
import com.yc.english.main.model.domain.UserInfo;
import com.yc.english.setting.contract.PersonCenterContract;
import com.yc.english.setting.presenter.PersonCenterPresenter;
import com.yc.english.setting.view.widgets.SettingItemView;

import java.io.ByteArrayOutputStream;
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

    @Override
    public void init() {
        mToolbar.setTitle("个人信息");
        mToolbar.showNavigationIcon();

        mPhoneSettingItemView.hideArrow();
        mAvatarSettingItemView.setAvatar(R.drawable.sample_avatar);

        RxView.clicks(mAvatarSettingItemView).throttleFirst(200, TimeUnit.MILLISECONDS).subscribe(new Action1<Void>() {
            @Override
            public void call(Void aVoid) {
                Intent intent = new Intent(Intent.ACTION_PICK); // 打开相册
                intent.setDataAndType(MediaStore.Images.Media.INTERNAL_CONTENT_URI, "image/*");
                startActivityForResult(intent, 1);
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
        RequestOptions options = new RequestOptions();
        options.centerCrop().placeholder(R.mipmap.default_big_avatar).transform(new CircleCrop());
        Glide.with(this).load(userInfo.getAvatar()).apply(options).into(mAvatarSettingItemView.getAvatarImageView());

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

        mPhoneSettingItemView.setInfo(userInfo.getMobile());
    }

    @Override
    protected void onActivityResult(final int requestCode, final int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == Activity.RESULT_OK && null != data) {
            try {
                Bundle extras = data.getExtras();
                Bitmap photo = null;
                if (extras != null) {
                    photo = extras.getParcelable("data");
                }

                if (photo == null) {
                    Uri selectedImage = data.getData();

                    String[] filePathColumn = {MediaStore.Images.Media.DATA};
                    Cursor cursor = getContentResolver().query(selectedImage,
                            filePathColumn, null, null, null);
                    String picturePath = "";
                    if(cursor != null) {
                        cursor.moveToFirst();
                        int columnIndex = cursor.getColumnIndex(filePathColumn[0]);
                        picturePath = cursor.getString(columnIndex);
                        cursor.close();
                    } else {
                        picturePath = selectedImage.getPath();
                    }
                    if(requestCode == 1) {
                        Intent intent = new Intent("com.android.camera.action.CROP");
                        intent.setDataAndType(selectedImage, "image/*");
                        intent.putExtra("crop", "true");
                        intent.putExtra("aspectX", 1);
                        intent.putExtra("aspectY", 1);
                        intent.putExtra("outputX", 160);
                        intent.putExtra("outputY", 160);
                        intent.putExtra("return-data", true);
                        startActivityForResult(intent, 2);
                        return;
                    }
                    photo = BitmapFactory.decodeFile(picturePath);
                }

                if(photo == null){
                    TipsHelper.tips(PersonCenterActivity.this, "获取图片失败");
                    return;
                }

                ByteArrayOutputStream stream = new ByteArrayOutputStream();
                photo.compress(Bitmap.CompressFormat.JPEG, 100, stream);// (0 - 100)压缩文件
                byte[] byteArray = stream.toByteArray();
                String streamStr = Base64.encodeToString(byteArray, Base64.DEFAULT);
                String image = "data:image/png;base64," + streamStr;
                mPresenter.uploadAvatar(image);

            }catch (Exception e){
                dismissLoadingDialog();
                TipsHelper.tips(PersonCenterActivity.this, "修改失败" + e);
                LogUtils.i("修改失败" + e);
            }
        }
    }
}
