package com.yc.english.main.presenter;

import android.content.Context;

import com.blankj.utilcode.util.EmptyUtils;
import com.blankj.utilcode.util.RegexUtils;
import com.blankj.utilcode.util.ToastUtils;
import com.hwangjr.rxbus.RxBus;
import com.kk.securityhttp.domain.ResultInfo;
import com.kk.securityhttp.net.contains.HttpConfig;
import com.yc.english.base.presenter.BasePresenter;
import com.yc.english.main.contract.RegisterContract;
import com.yc.english.main.hepler.UserInfoHelper;
import com.yc.english.main.model.domain.Constant;
import com.yc.english.main.model.domain.UserInfo;
import com.yc.english.main.model.engin.RegisterEngin;

import rx.Subscriber;
import rx.Subscription;

/**
 * Created by zhangkai on 2017/7/26.
 */

public class RegisterPresenter extends BasePresenter<RegisterEngin, RegisterContract.View> implements
        RegisterContract.Presenter {

    public RegisterPresenter(Context context, RegisterContract.View view) {
        super(view);
        mEngin = new RegisterEngin(context);
    }


    @Override
    public void loadData(boolean forceUpdate, boolean showLoadingUI) {

    }

    @Override
    public void sendCode(String mobile) {
        if (!RegexUtils.isMobileSimple(mobile)) {
            ToastUtils.showShort("手机号填写不正确");
            return;
        }
        mView.showLoadingDialog("发送验证码中, 请稍后");
        mView.codeRefresh();
        Subscription subscription = mEngin.sendCode(mobile).subscribe(new Subscriber<ResultInfo<String>>() {
            @Override
            public void onCompleted() {
                mView.dismissLoadingDialog();
            }

            @Override
            public void onError(Throwable e) {
                mView.dismissLoadingDialog();
            }

            @Override
            public void onNext(final ResultInfo<String> resultInfo) {
                handleResultInfo(resultInfo);
            }
        });
        mSubscriptions.add(subscription);
    }

    @Override
    public void register(String mobile, String pwd, String code) {
        if (!RegexUtils.isMobileSimple(mobile)) {
            ToastUtils.showShort("手机号填写不正确");
            return;
        }

        if (EmptyUtils.isEmpty(code)) {
            ToastUtils.showShort("验证码不正确");
            return;
        }

        if (EmptyUtils.isEmpty(pwd) && pwd.length() < 6) {
            ToastUtils.showShort("密码不能少于6位");
            return;
        }

        mView.showLoadingDialog("正在注册, 请稍后");
        Subscription subscription = mEngin.register(mobile, pwd, code).subscribe(new Subscriber<ResultInfo<UserInfo>>() {
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
                        RxBus.get().post(Constant.MAIN, true);
                        mView.finish();
                    }
                });
            }
        });
        mSubscriptions.add(subscription);
    }
}
