package com.yc.english.setting.view.fragments;

import android.content.Intent;
import android.graphics.Color;
import android.support.v4.content.ContextCompat;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.blankj.utilcode.util.ActivityUtils;
import com.blankj.utilcode.util.StringUtils;
import com.blankj.utilcode.util.ToastUtils;
import com.hwangjr.rxbus.annotation.Subscribe;
import com.hwangjr.rxbus.annotation.Tag;
import com.hwangjr.rxbus.thread.EventThread;
import com.jakewharton.rxbinding.view.RxView;
import com.yc.english.R;
import com.yc.english.base.helper.GlideHelper;
import com.yc.english.base.utils.QQUtils;
import com.yc.english.base.view.AlertDialog;
import com.yc.english.base.view.SharePopupWindow;
import com.yc.english.base.view.ToolbarFragment;
import com.yc.english.main.hepler.UserInfoHelper;
import com.yc.english.main.model.domain.Constant;
import com.yc.english.main.model.domain.UserInfo;
import com.yc.english.news.view.widget.NewsScrollView;
import com.yc.english.setting.contract.MyContract;
import com.yc.english.setting.model.bean.MyOrderInfo;
import com.yc.english.setting.presenter.MyPresenter;
import com.yc.english.setting.view.activitys.BuyVipActivity;
import com.yc.english.setting.view.activitys.FeedbackActivity;
import com.yc.english.setting.view.activitys.PersonCenterActivity;
import com.yc.english.setting.view.activitys.SettingActivity;
import com.yc.english.setting.view.activitys.VipEquitiesActivity;
import com.yc.english.setting.view.popupwindows.FollowWeiXinPopupWindow;
import com.yc.english.setting.view.widgets.MenuItemView;

import java.util.List;
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

    @BindView(R.id.miv_buy_vip)
    MenuItemView mBuyVipMenuItemView;

    @BindView(R.id.miv_to_market)
    MenuItemView mMarketMenuItemView;

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

    @BindView(R.id.sv_content)
    NewsScrollView mContentScrollView;

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

        //TODO
        RxView.clicks(mBuyVipMenuItemView).throttleFirst(200, TimeUnit.MILLISECONDS).subscribe(new Action1<Void>() {
            @Override
            public void call(Void aVoid) {
                if (!UserInfoHelper.isGotoLogin(getActivity())) {
                    Intent intent;
                    if (UserInfoHelper.getUserInfo().getIsVip() == 0) {
                        intent = new Intent(getActivity(), BuyVipActivity.class);
                    } else {
                        intent = new Intent(getActivity(), VipEquitiesActivity.class);
                    }
                    startActivity(intent);
                }
            }
        });

        RxView.clicks(mMarketMenuItemView).throttleFirst(200, TimeUnit.MILLISECONDS).subscribe(new Action1<Void>() {
            @Override
            public void call(Void aVoid) {
                ToastUtils.showLong("应用市场点评");
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
                AlertDialog alertDialog = new AlertDialog(getActivity());
                alertDialog.setDesc("打开QQ群与客服进行沟通？");
                alertDialog.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        QQUtils.joinQQGroup(getActivity(), "C9GzeOgLm4zrKerAk3Hr8gUiWsOhMzR7");
                    }
                });
                alertDialog.show();
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
                sharePopupWindow.show(mContentScrollView);
            }
        });


        RxView.clicks(mSettingMenuItemView).throttleFirst(200, TimeUnit.MILLISECONDS).subscribe(new Action1<Void>() {
            @Override
            public void call(Void aVoid) {
                Intent intent = new Intent(getActivity(), SettingActivity.class);
                startActivity(intent);
            }
        });

        mContentScrollView.setOnScrollChangeListener(new NewsScrollView.onScrollChangeListener() {
            @Override
            public void onScrollChange(int l, int t, int oldl, int oldt) {
//                mToolbar.setTranslationY(-t);
                mAvatarImageView.setTranslationY(t);
                mNickNameTextView.setTranslationY(t * 2 / 3);
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
        GlideHelper.circleBorderImageView(getActivity(), mAvatarImageView, userInfo.getAvatar(), R.mipmap
                .default_avatar, 0.5f, Color.WHITE);

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
        if (ActivityUtils.isValidContext(getActivity())) {
            mAvatarImageView.setImageDrawable(ContextCompat.getDrawable(getActivity(), R.mipmap.default_big_avatar));
            mNickNameTextView.setText("还没有登录，点击立即登录");
            mSchoolTextView.setText("");
        }
    }

    @Override
    public void showMyOrderInfoList(List<MyOrderInfo> list) {

    }

    @Override
    public void hideStateView() {

    }

    @Override
    public void showNoNet() {

    }

    @Override
    public void showNoData() {

    }

    @Override
    public void showLoadingDialog(String msg) {

    }

    @Override
    public void finish() {

    }

    @Override
    public void showLoading() {

    }

    @Override
    public void dismissLoadingDialog() {

    }
}
