package com.yc.junior.english.setting.presenter;

import android.content.Context;

import com.hwangjr.rxbus.RxBus;
import com.kk.securityhttp.domain.ResultInfo;
import com.yc.junior.english.base.helper.TipsHelper;
import com.yc.junior.english.main.hepler.UserInfoHelper;
import com.yc.junior.english.main.model.domain.Constant;
import com.yc.junior.english.main.model.domain.UserInfo;
import com.yc.junior.english.setting.contract.PersonCenterContract;
import com.yc.junior.english.setting.model.engin.MyEngin;

import rx.Subscriber;
import rx.Subscription;
import yc.com.base.BasePresenter;
import yc.com.blankj.utilcode.util.UIUitls;


/**
 * Created by zhangkai on 2017/8/3.
 */

public class PersonCenterPresenter extends BasePresenter<MyEngin, PersonCenterContract.View> implements PersonCenterContract.Presenter {

    public PersonCenterPresenter(Context context, PersonCenterContract.View iView) {
        super(context, iView);
        mEngine = new MyEngin(context);
    }

    @Override
    public void loadData(boolean forceUpdate, boolean showLoadingUI) {
        if (!forceUpdate) return;
        getUserInfo();
    }

    @Override
    public void getUserInfo() {
        UserInfoHelper.getUserInfoDo(new UserInfoHelper.Callback() {
            @Override
            public void showUserInfo(UserInfo userInfo) {
                mView.showUserInfo(userInfo);
            }

            @Override
            public void showNoLogin() {

            }
        });
    }

    @Override
    public void uploadAvatar(String avatar) {
        mView.showLoadingDialog("正在上传图像, 请稍后");
        Subscription subscription = mEngine.updateMessage(avatar, "", "").subscribe(new Subscriber<ResultInfo<UserInfo>>() {
            @Override
            public void onCompleted() {
                mView.dismissDialog();
            }

            @Override
            public void onError(Throwable e) {
                mView.dismissDialog();
            }

            @Override
            public void onNext(final ResultInfo<UserInfo> resultInfo) {
                handleResultInfo(resultInfo, new Runnable() {
                    @Override
                    public void run() {
                        UIUitls.post(new Runnable() {
                            @Override
                            public void run() {
                                TipsHelper.tips(mContext, "修改成功");
                                UserInfo userInfo = UserInfoHelper.getUserInfo();
                                userInfo.setAvatar(resultInfo.data.getAvatar());
                                UserInfoHelper.saveUserInfo(userInfo);
                                RxBus.get().post(Constant.USER_INFO, userInfo);
                            }
                        });
                    }
                });
            }
        });
        mSubscriptions.add(subscription);
    }
}
