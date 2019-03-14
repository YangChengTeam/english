package com.yc.junior.english.main.view.activitys;

import android.content.Intent;
import android.os.Build;
import android.support.annotation.NonNull;
import android.text.TextUtils;
import android.view.KeyEvent;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.TextView;

import com.qq.e.ads.nativ.NativeExpressADView;
import com.yc.junior.english.R;
import com.yc.junior.english.main.contract.SplashContract;
import com.yc.junior.english.main.hepler.UserInfoHelper;
import com.yc.junior.english.main.model.domain.Constant;
import com.yc.junior.english.main.model.domain.SlideInfo;
import com.yc.junior.english.main.presenter.SplashPresenter;

import java.util.Map;

import butterknife.BindView;
import yc.com.base.StatusBarCompat;
import yc.com.blankj.utilcode.util.UIUitls;
import yc.com.tencent_adv.AdvDispatchManager;
import yc.com.tencent_adv.AdvType;
import yc.com.tencent_adv.OnAdvStateListener;


/**
 * Created by zhangkai on 2017/8/1.
 */

public class SplashActivity extends yc.com.base.BaseActivity<SplashPresenter> implements SplashContract.View, OnAdvStateListener {


    @BindView(R.id.status_bar)
    View mStatusBar;

    private static final int Time = 1000;
    @BindView(R.id.splash_container)
    FrameLayout splashContainer;
    @BindView(R.id.skip_view)
    TextView skipView;
    @BindView(R.id.iv_splash_bg)
    ImageView ivSplashBg;


    @Override
    public void init() {
        mPresenter = new SplashPresenter(this, this);
        StatusBarCompat.light(this);
        StatusBarCompat.compat(this, mStatusBar);
        if (TextUtils.equals("Xiaomi", Build.BRAND) || TextUtils.equals("xiaomi", Build.BRAND) || UserInfoHelper.isVip(UserInfoHelper.getUserInfo())) {
            skipView.setVisibility(View.GONE);
            switchMain(null, Time);
        } else {
            AdvDispatchManager.getManager().init(this, AdvType.SPLASH, splashContainer, skipView, Constant.TENCENT_ADV_ID, Constant.SPLASH_ADV_ID, this);
        }

    }

    @Override
    public int getLayoutId() {
        return R.layout.main_activity_splash;
    }

    @Override
    public void gotToMain(long l) {
        switchMain(null, l);
    }

    @Override
    public void showAdvInfo(SlideInfo info, long delay) {
//        switchMain(info, delay);
    }

    private void switchMain(final SlideInfo info, long delay) {
//        long delayTime = 0;
//        if (delay < Time) {
//            delayTime = Time - delay;
//        }
        UIUitls.postDelayed(delay, new Runnable() {
            @Override
            public void run() {
                Intent intent = new Intent(SplashActivity.this, MainActivity.class);
                if (null != info) {
                    intent.putExtra("dialogInfo", info);
                }
                startActivity(intent);
                finish();
            }
        });

    }


    @Override
    public boolean isStatusBarMateria() {
        return true;
    }

    @Override
    protected void onResume() {
        super.onResume();
        if (!(TextUtils.equals("Xiaomi", Build.BRAND) || TextUtils.equals("xiaomi", Build.BRAND) || UserInfoHelper.isVip(UserInfoHelper.getUserInfo())))
            AdvDispatchManager.getManager().onResume();
    }

    @Override
    protected void onPause() {
        super.onPause();
        if (!(TextUtils.equals("Xiaomi", Build.BRAND) || TextUtils.equals("xiaomi", Build.BRAND) || UserInfoHelper.isVip(UserInfoHelper.getUserInfo())))
            AdvDispatchManager.getManager().onPause();

    }

    //防止用户返回键退出 APP
    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if (keyCode == KeyEvent.KEYCODE_BACK
                || keyCode == KeyEvent.KEYCODE_HOME) {
            return true;
        }
        return super.onKeyDown(keyCode, event);
    }


    @Override
    public void onShow() {
        ivSplashBg.setVisibility(View.GONE);
        skipView.setVisibility(View.VISIBLE);
    }

    @Override
    public void onDismiss(long delayTime) {
        switchMain(null, delayTime);
    }

    @Override
    public void onError() {

    }

    @Override
    public void onNativeExpressDismiss(NativeExpressADView view) {

    }

    @Override
    public void onNativeExpressShow(Map<NativeExpressADView, Integer> mDatas) {

    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if (!(TextUtils.equals("Xiaomi", Build.BRAND) || TextUtils.equals("xiaomi", Build.BRAND) || UserInfoHelper.isVip(UserInfoHelper.getUserInfo()))) {
            AdvDispatchManager.getManager().onRequestPermissionsResult(requestCode, permissions, grantResults);
        }
    }
}
