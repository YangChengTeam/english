package com.yc.english.main.view.activitys;

import android.content.Intent;
import android.view.KeyEvent;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.TextView;

import com.blankj.utilcode.util.UIUitls;
import com.qq.e.ads.splash.SplashAD;
import com.qq.e.ads.splash.SplashADListener;
import com.qq.e.comm.util.AdError;
import com.yc.english.R;
import com.yc.english.base.utils.StatusBarCompat;
import com.yc.english.base.view.BaseActivity;
import com.yc.english.main.contract.SplashContract;
import com.yc.english.main.model.domain.Constant;
import com.yc.english.main.model.domain.SlideInfo;
import com.yc.english.main.presenter.SplashPresenter;

import butterknife.BindView;

/**
 * Created by zhangkai on 2017/8/1.
 */

public class SplashActivity extends BaseActivity<SplashPresenter> implements SplashContract.View, SplashADListener {


    @BindView(R.id.status_bar)
    View mStatusBar;

    private static final int Time = 1000;
    @BindView(R.id.splash_container)
    FrameLayout splashContainer;
    @BindView(R.id.skip_view)
    TextView skipView;
    @BindView(R.id.iv_splash_bg)
    ImageView ivSplashBg;

    private long featchAdTime = 0;//开始拉取广告时间

    private boolean canJump = false;

    @Override
    public void init() {
        mPresenter = new SplashPresenter(this, this);
        StatusBarCompat.light(this);
        StatusBarCompat.compat(this, mStatusBar);
        showSplasAdv();
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
        long delayTime = 0;
        if (delay < Time) {
            delayTime = Time - delay;
        }
        UIUitls.postDelayed(delayTime, new Runnable() {
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


    private void showSplasAdv() {
        featchAdTime = System.currentTimeMillis();
        SplashAD splashAD = new SplashAD(this, splashContainer, skipView, Constant.TENCENT_ADV_ID, Constant.SPLASH_ADV_ID, this, 0);


    }

    /**
     * 设置一个变量来控制当前开屏页面是否可以跳转，当开屏广告为普链类广告时，点击会打开一个广告落地页，此时开发者还不能打开自己的App主页。当从广告落地页返回以后，
     * 才可以跳转到开发者自己的App主页；当开屏广告是App类广告时只会下载App。
     */
    @Override
    public void onADDismissed() {
        if (canJump) {
            switchMain(null, Time);
        } else {
            canJump = true;
        }
    }

    @Override
    public void onNoAD(AdError adError) {
        long alreadyDelayMills = System.currentTimeMillis() - featchAdTime;//从拉广告开始到onNoAD已经消耗了多少时间
        switchMain(null, alreadyDelayMills);
    }

    @Override
    public void onADPresent() {
        ivSplashBg.setVisibility(View.GONE);
    }

    @Override
    public void onADClicked() {

    }

    @Override
    public void onADTick(long l) {
        skipView.setText(String.format(getString(R.string.click_to_skip),
                Math.round(l / 1000f)));

    }

    @Override
    public void onADExposure() {

    }

    @Override
    protected void onResume() {
        super.onResume();
        if (canJump) {
            switchMain(null, Time);
        }
        canJump = true;
    }

    @Override
    protected void onPause() {
        super.onPause();
        canJump = false;

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


}
