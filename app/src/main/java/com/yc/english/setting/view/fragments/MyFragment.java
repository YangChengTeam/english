package com.yc.english.setting.view.fragments;

import android.content.Intent;
import android.graphics.Color;
import android.net.Uri;
import android.support.v4.content.ContextCompat;
import android.widget.ImageView;
import android.widget.TextView;

import com.blankj.utilcode.util.SnackbarUtils;
import com.blankj.utilcode.util.StringUtils;
import com.bumptech.glide.Glide;
import com.bumptech.glide.load.resource.bitmap.CircleCrop;
import com.bumptech.glide.request.RequestOptions;
import com.hwangjr.rxbus.annotation.Subscribe;
import com.hwangjr.rxbus.annotation.Tag;
import com.hwangjr.rxbus.thread.EventThread;
import com.jakewharton.rxbinding.view.RxView;
import com.yc.english.R;
import com.yc.english.base.helper.GlideCircleTransformation;
import com.yc.english.base.view.SharePopupWindow;
import com.yc.english.base.view.ToolbarFragment;
import com.yc.english.main.hepler.UserInfoHelper;
import com.yc.english.main.model.domain.Constant;
import com.yc.english.main.model.domain.UserInfo;
import com.yc.english.main.view.popupwindows.FollowWeiXinPopupWindow;
import com.yc.english.setting.contract.MyContract;
import com.yc.english.setting.presenter.MyPresenter;
import com.yc.english.setting.view.activitys.FeedbackActivity;
import com.yc.english.setting.view.activitys.PersonCenterActivity;
import com.yc.english.setting.view.activitys.SettingActivity;
import com.yc.english.setting.view.widgets.MenuItemView;

import java.util.concurrent.TimeUnit;

import butterknife.BindView;
import rx.functions.Action1;

/**
 * Created by zhangkai on 2017/7/24.
 */

public class MyFragment extends ToolbarFragment<MyPresenter> implements MyContract.View {

    @BindView(R.id.iv_avatar)
    ImageView mAvatarImageView;

    @BindView(R.id.tv_nickname)
    TextView mNickNameTextView;

    @BindView(R.id.tv_school)
    TextView mSchoolTextView;

    @BindView(R.id.miv_weixin)
    MenuItemView mWeixinMenuItemView;

    @BindView(R.id.miv_qq)
    MenuItemView mQQMenuItemView;

    @BindView(R.id.miv_feedback)
    MenuItemView mFeedbackMenuItemView;

    @BindView(R.id.miv_share)
    MenuItemView mShareMenuItemView;

    @BindView(R.id.miv_setting)
    MenuItemView mSettingMenuItemView;

    @Override
    public void init() {
        super.init();
        mPresenter = new MyPresenter(getActivity(), this);

        mToolbar.setTitle("用户中心");

        RxView.clicks(mAvatarImageView).throttleFirst(200, TimeUnit.MILLISECONDS).subscribe(new Action1<Void>() {
            @Override
            public void call(Void aVoid) {
                if (!UserInfoHelper.isGotoLogin(getActivity())) {
                    Intent intent = new Intent(getActivity(), PersonCenterActivity.class);
                    startActivity(intent);
                }
            }
        });

        RxView.clicks(mNickNameTextView).throttleFirst(200, TimeUnit.MILLISECONDS).subscribe(new Action1<Void>() {
            @Override
            public void call(Void aVoid) {
                if (!UserInfoHelper.isGotoLogin(getActivity())) {
                    Intent intent = new Intent(getActivity(), PersonCenterActivity.class);
                    startActivity(intent);
                }
            }
        });

        RxView.clicks(mWeixinMenuItemView).throttleFirst(200, TimeUnit.MILLISECONDS).subscribe(new Action1<Void>() {
            @Override
            public void call(Void aVoid) {
                FollowWeiXinPopupWindow followWeiXinPopupWindow = new FollowWeiXinPopupWindow(getActivity());
                followWeiXinPopupWindow.show(mRootView);
            }
        });

        RxView.clicks(mQQMenuItemView).throttleFirst(200, TimeUnit.MILLISECONDS).subscribe(new Action1<Void>() {
            @Override
            public void call(Void aVoid) {
                Intent intent = new Intent();
                String key = "sJFXu6TS1Rq1ppK4PCyMUIfeQjILjACK";
                intent.setData(Uri.parse("mqqopensdkapi://bizAgent/qm/qr?url=http%3A%2F%2Fqm.qq" +
                        ".com%2Fcgi-bin%2Fqm%2Fqr%3Ffrom%3Dapp%26p%3Dandroid%26k%3D" + key));
                try {
                    startActivity(intent);
                } catch (Exception e) {
                    SnackbarUtils.with(getActivity().getWindow().getDecorView()).setMessage("未安装手Q或安装的版本不支持").show();
                }
            }
        });

        RxView.clicks(mFeedbackMenuItemView).throttleFirst(200, TimeUnit.MILLISECONDS).subscribe(new Action1<Void>() {
            @Override
            public void call(Void aVoid) {
                if (!UserInfoHelper.isGotoLogin(getActivity())) {
                    Intent intent = new Intent(getActivity(), FeedbackActivity.class);
                    startActivity(intent);
                }
            }
        });

        RxView.clicks(mShareMenuItemView).throttleFirst(200, TimeUnit.MILLISECONDS).subscribe(new Action1<Void>() {
            @Override
            public void call(Void aVoid) {
                SharePopupWindow sharePopupWindow = new SharePopupWindow(getActivity());
                sharePopupWindow.show(mRootView);
            }
        });


        RxView.clicks(mSettingMenuItemView).throttleFirst(200, TimeUnit.MILLISECONDS).subscribe(new Action1<Void>() {
            @Override
            public void call(Void aVoid) {
                Intent intent = new Intent(getActivity(), SettingActivity.class);
                startActivity(intent);
            }
        });
    }

    @Override
    public boolean isInstallToolbar() {
        return false;
    }

    @Override
    public int getLayoutId() {
        return R.layout.setting_fragment_my;
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
        options.centerCrop().placeholder(R.mipmap.default_avatar).transform(new GlideCircleTransformation(getActivity(), 1,
                Color.WHITE));
        Glide.with(this).load(userInfo.getAvatar()).apply(options).into(mAvatarImageView);
        if (!StringUtils.isEmpty(userInfo.getSchool())) {
            mSchoolTextView.setText(userInfo.getSchool());
        }
        if (!StringUtils.isEmpty(userInfo.getNickname())) {
            mNickNameTextView.setText(userInfo.getNickname());
        }
    }

    @Subscribe(
            thread = EventThread.MAIN_THREAD,
            tags = {
                    @Tag(Constant.NO_LOGIN)
            }
    )
    @Override
    public void showNoLogin(Boolean flag) {
        mAvatarImageView.setImageDrawable(ContextCompat.getDrawable(getActivity(), R.mipmap.default_big_avatar));
        mNickNameTextView.setText("还没有登录，点击立即登录");
        mSchoolTextView.setText("");
    }
}
