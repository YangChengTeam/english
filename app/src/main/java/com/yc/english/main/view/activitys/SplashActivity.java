package com.yc.english.main.view.activitys;

import android.content.Intent;
import android.widget.ImageView;

import com.bumptech.glide.Glide;
import com.kk.utils.UIUitls;
import com.yc.english.R;
import com.yc.english.base.view.BaseActivity;
import com.yc.english.group.view.activitys.GroupListJoinActivity;
import com.yc.english.main.contract.LoginContract;
import com.yc.english.main.contract.SplashContract;
import com.yc.english.main.hepler.UserInfoHelper;
import com.yc.english.main.model.domain.UserInfo;
import com.yc.english.main.presenter.LoginPresenter;
import com.yc.english.main.presenter.SplashPresenter;

import net.lucode.hackware.magicindicator.buildins.UIUtil;

import butterknife.BindView;

/**
 * Created by zhangkai on 2017/8/1.
 */

public class SplashActivity extends BaseActivity<SplashPresenter> implements SplashContract.View {

    @BindView(R.id.iv_splash_logo)
    ImageView mSplashLogoImageView;

    @Override
    public void init() {
        mPresenter = new SplashPresenter(this, this);
        Glide.with(this).load(R.mipmap.splash_logo).into(mSplashLogoImageView);

        UserInfo userInfo = UserInfoHelper.getUserInfo();
        if (userInfo != null) {
            mPresenter.connect(userInfo.getUid());
            UIUitls.postDelayed(1500, new Runnable() {
                @Override
                public void run() {
                    Intent intent = new Intent(SplashActivity.this, MainActivity.class);
                    startActivity(intent);
                    finish();
                }
            });

        } else {
            UIUitls.postDelayed(1500, new Runnable() {
                @Override
                public void run() {
                    Intent intent = new Intent(SplashActivity.this, LoginActivity.class);
                    startActivity(intent);
                    finish();
                }
            });
        }
    }

    @Override
    public int getLayoutId() {
        return R.layout.main_activity_splash;
    }
}
