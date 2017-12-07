package com.yc.english.main.presenter;

import android.content.Context;

import com.blankj.utilcode.util.EmptyUtils;
import com.blankj.utilcode.util.RegexUtils;
import com.blankj.utilcode.util.SPUtils;
import com.blankj.utilcode.util.StringUtils;
import com.hwangjr.rxbus.RxBus;
import com.kk.securityhttp.domain.ResultInfo;
import com.yc.english.base.helper.TipsHelper;
import com.yc.english.base.presenter.BasePresenter;
import com.yc.english.main.contract.LoginContract;
import com.yc.english.main.hepler.UserInfoHelper;
import com.yc.english.main.model.domain.Constant;
import com.yc.english.main.model.domain.UserInfoWrapper;
import com.yc.english.main.model.engin.LoginEngin;

import rx.Observable;
import rx.Subscriber;
import rx.Subscription;
import rx.android.schedulers.AndroidSchedulers;
import rx.functions.Action1;
import rx.functions.Func1;
import rx.schedulers.Schedulers;

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
        if (!forceUpdate) return;
        getPhone();
    }

    @Override
    public void login(final String username, String pwd) {
        if (!RegexUtils.isMobileSimple(username)) {
            TipsHelper.tips(mContext, "手机号填写不正确");
            return;
        }

        if (EmptyUtils.isEmpty(pwd) && pwd.length() < 6) {
            TipsHelper.tips(mContext, "密码不能少于6位");
            return;
        }

        mView.showLoadingDialog("正在登录， 请稍后");
        Subscription subscription = mEngin.login(username, pwd).subscribe(new Subscriber<ResultInfo<UserInfoWrapper>>() {
            @Override
            public void onCompleted() {
                mView.dismissLoadingDialog();
            }

            @Override
            public void onError(Throwable e) {
                mView.dismissLoadingDialog();
            }

            @Override
            public void onNext(final ResultInfo<UserInfoWrapper> resultInfo) {
                handleResultInfo(resultInfo, new Runnable() {
                    @Override
                    public void run() {
                        UserInfoHelper.utils(mContext, resultInfo);
                        RxBus.get().post(Constant.COMMUNITY_ACTIVITY_REFRESH, "form getUserInfo");
                        mView.finish();
                    }
                });
            }
        });
        mSubscriptions.add(subscription);
    }

    @Override
    public void getPhone() {
        Subscription subscription = Observable.just("").map(new Func1<String, String>() {
            @Override
            public String call(String s) {
                String phone = SPUtils.getInstance().getString(Constant.PHONE);
                return phone;
            }
        }).subscribeOn(Schedulers.io()).filter(new Func1<String, Boolean>() {
            @Override
            public Boolean call(String s) {
                return !StringUtils.isEmpty(s);
            }
        }).observeOn(AndroidSchedulers.mainThread()).subscribe(new Action1<String>() {
            @Override
            public void call(String s) {
                mView.showPhone(s);
            }
        });
        mSubscriptions.add(subscription);
    }

}
