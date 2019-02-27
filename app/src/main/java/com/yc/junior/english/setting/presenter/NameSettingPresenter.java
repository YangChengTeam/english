package com.yc.junior.english.setting.presenter;

import android.content.Context;

import com.hwangjr.rxbus.RxBus;
import com.kk.securityhttp.domain.ResultInfo;
import com.yc.junior.english.base.helper.TipsHelper;
import com.yc.junior.english.base.presenter.BasePresenter;
import com.yc.junior.english.main.hepler.UserInfoHelper;
import com.yc.junior.english.main.model.domain.Constant;
import com.yc.junior.english.main.model.domain.UserInfo;
import com.yc.junior.english.setting.contract.NameSettingContract;
import com.yc.junior.english.setting.model.engin.MyEngin;

import rx.Subscriber;
import rx.Subscription;
import yc.com.blankj.utilcode.util.StringUtils;
import yc.com.blankj.utilcode.util.UIUitls;

/**
 * Created by zhangkai on 2017/8/3.
 */

public class NameSettingPresenter extends BasePresenter<MyEngin, NameSettingContract.View> implements NameSettingContract.Presenter {

    public NameSettingPresenter(Context context, NameSettingContract.View iView) {
        super(context, iView);
        mEngine = new MyEngin(context);
    }

    @Override
    public void loadData(boolean forceUpdate, boolean showLoadingUI) {
        if (!forceUpdate) return;
    }

    @Override
    public void udpateUserInfo(final String name, final String school) {
        mView.showLoadingDialog("正在修改，请稍后");
        Subscription subscription = mEngine.updateMessage("", name, school).subscribe(new Subscriber<ResultInfo<UserInfo>>() {
            @Override
            public void onCompleted() {
                mView.dismissDialog();
            }

            @Override
            public void onError(Throwable e) {
                mView.dismissDialog();
            }

            @Override
            public void onNext(ResultInfo<UserInfo> resultInfo) {
                handleResultInfo(resultInfo, new Runnable() {
                    @Override
                    public void run() {
                        UIUitls.post(new Runnable() {
                            @Override
                            public void run() {
                                TipsHelper.tips(mContext, "修改成功");
                                UserInfo userInfo = UserInfoHelper.getUserInfo();
                                if (!StringUtils.isEmpty(name)) {
                                    userInfo.setNickname(name);
                                }
                                if (!StringUtils.isEmpty(school)) {
                                    userInfo.setSchool(school);
                                }
                                UserInfoHelper.saveUserInfo(userInfo);
                                RxBus.get().post(Constant.USER_INFO, userInfo);

                                mView.finish();
                            }
                        });
                    }
                });
            }
        });
        mSubscriptions.add(subscription);
    }
}
