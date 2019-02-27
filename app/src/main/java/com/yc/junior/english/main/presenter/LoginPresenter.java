package com.yc.junior.english.main.presenter;

import android.content.Context;

import com.hwangjr.rxbus.RxBus;
import com.kk.securityhttp.domain.ResultInfo;
import com.kk.utils.LogUtil;
import com.yc.junior.english.EnglishApp;
import com.yc.junior.english.base.helper.ResultInfoHelper;
import com.yc.junior.english.base.helper.TipsHelper;
import com.yc.junior.english.base.presenter.BasePresenter;
import com.yc.junior.english.group.utils.EngineUtils;
import com.yc.junior.english.main.contract.LoginContract;
import com.yc.junior.english.main.hepler.UserInfoHelper;
import com.yc.junior.english.main.model.domain.Constant;
import com.yc.junior.english.main.model.domain.UserInfoWrapper;
import com.yc.junior.english.main.model.engin.LoginEngin;
import com.yc.junior.english.setting.model.bean.ShareStateInfo;

import rx.Observable;
import rx.Subscriber;
import rx.Subscription;
import rx.android.schedulers.AndroidSchedulers;
import rx.functions.Action1;
import rx.functions.Func1;
import rx.schedulers.Schedulers;
import yc.com.base.EmptyUtils;
import yc.com.blankj.utilcode.util.RegexUtils;
import yc.com.blankj.utilcode.util.SPUtils;
import yc.com.blankj.utilcode.util.StringUtils;

/**
 * Created by zhangkai on 2017/7/25.
 */

public class LoginPresenter extends BasePresenter<LoginEngin, LoginContract.View> implements LoginContract.Presenter {

    public LoginPresenter(Context context, LoginContract.View view) {
        super(context, view);
        mEngine = new LoginEngin(context);
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
        Subscription subscription = mEngine.login(username, pwd).subscribe(new Subscriber<ResultInfo<UserInfoWrapper>>() {
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
                        RxBus.get().post(Constant.GET_UNIT, "form getUserInfo");
                        RxBus.get().post(Constant.USER_INFO, "form getUserInfo");
//                        getOpenShareVip(resultInfo.data.getInfo().getUid());
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

    /**
     * 分享是否开启体验VIP
     */
    private void getOpenShareVip(String usr_id) {

        LogUtil.msg("login success  " + usr_id);
        Subscription subscription = EngineUtils.getShareVipState(mContext, usr_id).subscribe(new Subscriber<ResultInfo<ShareStateInfo>>() {
            @Override
            public void onCompleted() {

            }

            @Override
            public void onError(Throwable e) {

            }

            @Override
            public void onNext(final ResultInfo<ShareStateInfo> shareResult) {
                ResultInfoHelper.handleResultInfo(shareResult, new ResultInfoHelper.Callback() {
                    @Override
                    public void resultInfoEmpty(String message) {

                    }

                    @Override
                    public void resultInfoNotOk(String message) {

                    }

                    @Override
                    public void reulstInfoOk() {
                        if (shareResult.data != null) {
                            if (shareResult.data.getStatus() == 1) {
                                EnglishApp.isOpenShareVip = true;
                                EnglishApp.trialDays = shareResult.data.getDays();
                            }
                        }
                    }
                });

            }
        });
        mSubscriptions.add(subscription);
    }
}
