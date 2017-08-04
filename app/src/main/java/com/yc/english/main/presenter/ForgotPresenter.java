package com.yc.english.main.presenter;

import android.content.Context;

import com.blankj.utilcode.util.EmptyUtils;
import com.blankj.utilcode.util.RegexUtils;
import com.blankj.utilcode.util.ToastUtils;
import com.hwangjr.rxbus.RxBus;
import com.kk.securityhttp.domain.ResultInfo;
import com.yc.english.base.helper.TipsHelper;
import com.yc.english.base.presenter.BasePresenter;
import com.yc.english.main.contract.ForgotContract;
import com.yc.english.main.hepler.UserInfoHelper;
import com.yc.english.main.model.domain.Constant;
import com.yc.english.main.model.domain.UserInfo;
import com.yc.english.main.model.engin.ForgotEngin;

import rx.Subscriber;
import rx.Subscription;

/**
 * Created by zhangkai on 2017/7/26.
 */

public class ForgotPresenter extends BasePresenter<ForgotEngin, ForgotContract.View> implements ForgotContract.Presenter {
    public ForgotPresenter(Context context, ForgotContract.View view) {
        super(context, view);
        mEngin = new ForgotEngin(context);
    }


    @Override
    public void loadData(boolean forceUpdate, boolean showLoadingUI) {

    }

    @Override
    public void sendCode(String mobile) {
        if (!RegexUtils.isMobileSimple(mobile)) {
            TipsHelper.tips(mContext, "手机号填写不正确");
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
                handleResultInfo(resultInfo, new Runnable() {
                    @Override
                    public void run() {
                        ToastUtils.showShort("短信已发送， 请注意查收");
                    }
                });
            }
        });
        mSubscriptions.add(subscription);
    }

    @Override
    public void resetPassword(String mobile, String pwd, String code) {
        if (!RegexUtils.isMobileSimple(mobile)) {
            TipsHelper.tips(mContext, "手机号填写不正确");
            return;
        }

        if (EmptyUtils.isEmpty(code)) {
            TipsHelper.tips(mContext, "验证码不正确");
            return;
        }

        if (EmptyUtils.isEmpty(pwd) && pwd.length() < 6) {
            TipsHelper.tips(mContext, "密码不能少于6位");
            return;
        }

        mView.showLoadingDialog("正在设置密码, 请稍后");
        Subscription subscription = mEngin.resetPassword(mobile, pwd, code).subscribe(new Subscriber<ResultInfo<UserInfo>>() {
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
                        RxBus.get().post(Constant.FINISH_LOGIN, true);
                        mView.finish();
                    }
                });
            }
        });
        mSubscriptions.add(subscription);
    }
}
