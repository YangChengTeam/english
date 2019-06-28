package com.yc.junior.english.setting.view.activitys;

import android.os.Bundle;
import android.support.design.widget.AppBarLayout;
import android.support.design.widget.CollapsingToolbarLayout;
import android.support.design.widget.CoordinatorLayout;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.hwangjr.rxbus.annotation.Subscribe;
import com.hwangjr.rxbus.annotation.Tag;
import com.hwangjr.rxbus.thread.EventThread;
import com.jakewharton.rxbinding.view.RxView;
import com.yc.junior.english.R;
import com.yc.junior.english.base.helper.GlideHelper;
import com.yc.junior.english.main.hepler.UserInfoHelper;
import com.yc.junior.english.main.model.domain.Constant;
import com.yc.junior.english.main.model.domain.UserInfo;
import com.yc.junior.english.vip.model.bean.GoodsType;
import com.yc.junior.english.vip.utils.VipDialogHelper;
import com.yc.junior.english.vip.views.fragments.BasePayItemView;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;
import java.util.concurrent.TimeUnit;

import butterknife.BindView;
import rx.functions.Action1;
import yc.com.base.BaseActivity;
import yc.com.base.StatusBarCompat;
import yc.com.blankj.utilcode.util.TimeUtils;


/**
 * Created by wanglin  on 2017/11/8 11:19.
 */

public class VipEquitiesActivity extends BaseActivity {


    @BindView(R.id.iv_avatar)
    ImageView ivAvatar;
    @BindView(R.id.tv_nickname)
    TextView mTvNickname;
    @BindView(R.id.tv_vip_end_time)
    TextView mTvEndTime;
    @BindView(R.id.ll_head)
    LinearLayout llHead;
    @BindView(R.id.toolbar)
    Toolbar toolbar;
    @BindView(R.id.collapsingToolbarLayout)
    CollapsingToolbarLayout collapsingToolbarLayout;
    @BindView(R.id.coordinatorLayout)
    CoordinatorLayout coordinatorLayout;
    @BindView(R.id.appbar_layout)
    AppBarLayout appbarLayout;
    @BindView(R.id.btn_open_vip)
    Button mBtnOpenVip;
    @BindView(R.id.ll_vip_container)
    LinearLayout llVipContainer;
    @BindView(R.id.tv_rights_title)
    TextView mTvRightsTitle;
    @BindView(R.id.basePayItemView_vip)
    BasePayItemView basePayItemViewVip;
    @BindView(R.id.basePayItemView_ceping)
    BasePayItemView basePayItemViewCeping;
    @BindView(R.id.vip_icon)
    ImageView mVipIcon;
    @BindView(R.id.baseItemView_weike)
    BasePayItemView baseItemViewWeike;
    @BindView(R.id.baseItemView_teach)
    BasePayItemView baseItemViewTeach;
    @BindView(R.id.basePayItemView_task_tutorship)
    BasePayItemView basePayItemViewTaskTutorship;
    @BindView(R.id.baseItemView_plan_teach)
    BasePayItemView baseItemViewPlanTeach;
    @BindView(R.id.baseItemView_book_read)
    BasePayItemView baseItemViewBookRead;
    @BindView(R.id.ll_right_container)
    LinearLayout llRightContainer;

    private UserInfo userInfo;


    @Override
    public void init() {
        userInfo = UserInfoHelper.getUserInfo();

        toolbar.setNavigationIcon(R.mipmap.vip_back);
        toolbar.setNavigationOnClickListener(v -> finish());
        StatusBarCompat.compat(this, appbarLayout, toolbar);

        mTvNickname.setText(userInfo.getNickname());

        GlideHelper.circleImageView(this, ivAvatar, userInfo.getAvatar(), R.mipmap.default_big_avatar);

        initView();
        initListener();
    }

    private void initListener() {
        RxView.clicks(mBtnOpenVip).throttleFirst(200, TimeUnit.MILLISECONDS).subscribe(aVoid -> {
            Bundle bundle = new Bundle();
            bundle.putInt(GoodsType.GOODS_KEY, GoodsType.TYPE_GENERAL_VIP);
            VipDialogHelper.showVipDialog(getSupportFragmentManager(), "", bundle);
        });
    }

    private void initView() {
        if (userInfo != null) {
            if (UserInfoHelper.isVip(userInfo)) {
                if (userInfo.getIsSVip() == 1) {
                    mTvEndTime.setText(getString(R.string.forever_vip));
                } else {
                    String endTime = TimeUtils.date2String(new Date(userInfo.getVip_end_time() * 1000), new SimpleDateFormat("yyyy-MM-dd", Locale.getDefault()));
                    mTvEndTime.setText(String.format(getString(R.string.vip_end_time), endTime));
                }
            }

//            if (userInfo.getIsVip() != 0 && endTime.equals("0")) {
//                mTvEndTime.setText(getString(R.string.forever_vip));
//            }

            if (!UserInfoHelper.isVip(userInfo)) {//非会员
                mBtnOpenVip.setVisibility(View.VISIBLE);
                llVipContainer.setVisibility(View.GONE);
                basePayItemViewVip.setVisibility(View.GONE);
                basePayItemViewCeping.setVisibility(View.VISIBLE);
                mTvRightsTitle.setText(getString(R.string.exclusive_right));
            } else {
                mBtnOpenVip.setVisibility(View.GONE);
                llVipContainer.setVisibility(View.VISIBLE);
                if (userInfo.getIsVip() == 1) {

                    mTvRightsTitle.setText(getString(R.string.general_vip_right));
                    basePayItemViewVip.setVisibility(View.GONE);
//                    basePayItemViewCeping.setVisibility(View.GONE);
//                    basePayItemViewTaskTutorship.setVisibility(View.GONE);
                    mVipIcon.setImageResource(R.mipmap.vip_vip);
                } else if (userInfo.getIsVip() == 2) {
                    mTvRightsTitle.setText(getString(R.string.tutorship_vip_right));
                    mVipIcon.setImageResource(R.mipmap.vip_tifen);
                    baseItemViewWeike.setContentAndIcon("同步微课", 0);
                    baseItemViewTeach.setContentAndIcon("VIP专属教学", R.mipmap.vip_self_icon);
                    basePayItemViewVip.setVisibility(View.GONE);
                } else if (userInfo.getIsVip() == 3) {
                    mTvRightsTitle.setText(getString(R.string.synchronization_weike_right));
                    mVipIcon.setImageResource(R.mipmap.vip_vip_weike);
                    llRightContainer.setVisibility(View.GONE);
                    baseItemViewTeach.setVisibility(View.GONE);
                    baseItemViewPlanTeach.setVisibility(View.GONE);
                    baseItemViewBookRead.setVisibility(View.GONE);
                } else if (userInfo.getIsVip() == 4) {
                    mTvRightsTitle.setText(getString(R.string.read_book_right));
                    mVipIcon.setImageResource(R.mipmap.vip_vip_diandu);
                    llRightContainer.setVisibility(View.GONE);
                    baseItemViewTeach.setVisibility(View.GONE);
                    baseItemViewPlanTeach.setVisibility(View.GONE);
                    baseItemViewWeike.setVisibility(View.GONE);
                }
            }
        }
    }

    @Override
    public int getLayoutId() {
        return R.layout.activity_vip_equity;
    }

    @Subscribe(
            thread = EventThread.MAIN_THREAD,
            tags = {
                    @Tag(Constant.COMMUNITY_ACTIVITY_REFRESH)
            }
    )
    public void getInfo(String loginInfo) {
        userInfo = UserInfoHelper.getUserInfo();
        initView();
    }


    @Override
    public boolean isStatusBarMateria() {
        return true;
    }
}
