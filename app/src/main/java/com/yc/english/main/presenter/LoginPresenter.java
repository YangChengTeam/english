package com.yc.english.main.presenter;

import android.content.Context;

import com.blankj.utilcode.util.EmptyUtils;
import com.blankj.utilcode.util.RegexUtils;
import com.hwangjr.rxbus.RxBus;
import com.kk.securityhttp.domain.ResultInfo;
import com.yc.english.base.helper.TipsHelper;
import com.yc.english.base.presenter.BasePresenter;
import com.yc.english.main.contract.LoginContract;
import com.yc.english.main.hepler.UserInfoHelper;
import com.yc.english.main.model.domain.Constant;
import com.yc.english.main.model.domain.UserInfo;
import com.yc.english.main.model.engin.LoginEngin;

import rx.Subscriber;
import rx.Subscription;

/**
 * Created by zhangkai on 2017/7/25.
 */

public class LoginPresenter extends BasePresenter<LoginEngin, LoginContract.View> implements LoginContract.Presenter {

    public LoginPresenter(Context context, LoginContract.View view) {
        super(context, view);
        mEngin = new LoginEngin(context);
    }


    @Override
    public void loadData(boolean forceUpdate, boolean showLoadingUI) {

    }

    @Override
    public void login(String username, String pwd) {
        if (!RegexUtils.isMobileSimple(username)) {
            TipsHelper.tips(mContext, "手机号填写不正确");
            return;
        }

        if (EmptyUtils.isEmpty(pwd) && pwd.length() < 6) {
            TipsHelper.tips(mContext, "密码不能少于6位");
            return;
        }

        mView.showLoadingDialog("正在登录， 请稍后");
        Subscription subscription = mEngin.login(username, pwd).subscribe(new Subscriber<ResultInfo<UserInfo>>() {
            @Override
            public void onCompleted() {
                mView.dismissLoadingDialog();
            }

            @Override
            public void onError(Throwable e) {
                mView.dismissLoadingDialog();
            }

            @Override
            public void onNext(final ResultInfo<UserInfo> resultInfo) {
                handleResultInfo(resultInfo, new Runnable() {
                    @Override
                    public void run() {
                        UserInfoHelper.saveUserInfo(resultInfo.data);
                        UserInfoHelper.connect(mContext, resultInfo.data.getUid());
                        RxBus.get().post(Constant.USER_INFO, resultInfo.data);
                        mView.finish();
                    }
                });
            }
        });
        mSubscriptions.add(subscription);
    }

}
