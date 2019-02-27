package com.yc.junior.english.setting.view.activitys;

import android.view.View;
import android.widget.Button;

import com.hwangjr.rxbus.RxBus;
import com.jakewharton.rxbinding.view.RxView;
import com.kk.securityhttp.domain.GoagalInfo;
import com.tencent.bugly.beta.Beta;

import com.yc.junior.english.R;
import com.yc.junior.english.base.helper.GlideCatchHelper;
import com.yc.junior.english.base.helper.TipsHelper;
import com.yc.junior.english.base.view.FullScreenActivity;
import com.yc.junior.english.group.constant.BusAction;
import com.yc.junior.english.main.hepler.UserInfoHelper;
import com.yc.junior.english.main.model.domain.Constant;
import com.yc.junior.english.setting.contract.SettingContract;
import com.yc.junior.english.setting.presenter.SettingPresenter;
import com.yc.junior.english.setting.view.widgets.SettingItemView;

import java.util.concurrent.TimeUnit;

import butterknife.BindView;
import rx.functions.Action1;


/**
 * Created by zhangkai on 2017/7/24.
 */

public class SettingActivity extends FullScreenActivity<SettingPresenter> implements SettingContract.View {
    @BindView(R.id.si_cache)
    SettingItemView mCacheSettingItemView;

    @BindView(R.id.si_version)
    SettingItemView mVersionSettingItemView;

    @BindView(R.id.btn_exit)
    Button mExitButton;


    @Override
    public void init() {
        mToolbar.setTitle("设置");
        mToolbar.showNavigationIcon();

        mCacheSettingItemView.rightInfo();

        RxView.clicks(mCacheSettingItemView).throttleFirst(200, TimeUnit.MILLISECONDS).subscribe(new Action1<Void>() {
            @Override
            public void call(Void aVoid) {
                if (GlideCatchHelper.getInstance(SettingActivity.this).cleanCatchDisk()) {
                    TipsHelper.tips(SettingActivity.this, "清除缓存成功");
                    mCacheSettingItemView.setInfo("0.0Byte");
                }
            }
        });


        mVersionSettingItemView.setInfo(GoagalInfo.get().packageInfo.versionName);
        RxView.clicks(mVersionSettingItemView).throttleFirst(200, TimeUnit.MILLISECONDS).subscribe(new Action1<Void>() {
            @Override
            public void call(Void aVoid) {
                Beta.checkUpgrade(true, false);
            }
        });

        RxView.clicks(mExitButton).throttleFirst(200, TimeUnit.MILLISECONDS).subscribe(new Action1<Void>() {
            @Override
            public void call(Void aVoid) {
                UserInfoHelper.clearUserInfo();
                mExitButton.setVisibility(View.GONE);
                TipsHelper.tips(SettingActivity.this, "成功退出");
                RxBus.get().post(Constant.NO_LOGIN, true);
                RxBus.get().post(BusAction.GROUP_LIST, "from logout");
                RxBus.get().post(Constant.GET_UNIT, "from logout");

            }
        });

        mPresenter = new SettingPresenter(this, this);
    }

    @Override
    public int getLayoutId() {
        return R.layout.setting_activity_setting;
    }

    @Override
    public void ShowCacheSize(String size) {
        mCacheSettingItemView.setInfo(size);
    }

    @Override
    public void showExitButton() {
        mExitButton.setVisibility(View.VISIBLE);
    }
}
