package com.yc.english.setting.view.activitys;

import com.blankj.utilcode.util.SnackbarUtils;
import com.jakewharton.rxbinding.view.RxView;
import com.tencent.bugly.beta.Beta;
import com.yc.english.R;
import com.yc.english.base.helper.GlideCatchHelper;
import com.yc.english.base.view.FullScreenActivity;
import com.yc.english.setting.contract.SettingContract;
import com.yc.english.setting.presenter.SettingPresenter;
import com.yc.english.setting.view.widgets.SettingItemView;

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


    @Override
    public void init() {
        mToolbar.setTitle("设置");
        mToolbar.showNavigationIcon();

        mCacheSettingItemView.rightInfo();

        RxView.clicks(mCacheSettingItemView).throttleFirst(200, TimeUnit.MILLISECONDS).subscribe(new Action1<Void>() {
            @Override
            public void call(Void aVoid) {
                if(GlideCatchHelper.getInstance(SettingActivity.this).cleanCatchDisk()) {
                    SnackbarUtils.with(getWindow().getDecorView()).setMessage("清除缓存成功").show();
                    mCacheSettingItemView.setInfo("0.0Byte");
                }
            }
        });

        RxView.clicks(mVersionSettingItemView).throttleFirst(200, TimeUnit.MILLISECONDS).subscribe(new Action1<Void>() {
            @Override
            public void call(Void aVoid) {
                Beta.checkUpgrade(true,false);
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
}
