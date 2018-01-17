package com.yc.junior.english.setting.presenter;

import android.content.Context;

import com.blankj.utilcode.util.StringUtils;
import com.hwangjr.rxbus.RxBus;
import com.kk.securityhttp.domain.ResultInfo;
import com.kk.utils.UIUitls;
import com.yc.junior.english.base.helper.TipsHelper;
import com.yc.junior.english.base.presenter.BasePresenter;
import com.yc.junior.english.main.hepler.UserInfoHelper;
import com.yc.junior.english.main.model.domain.Constant;
import com.yc.junior.english.main.model.domain.UserInfo;
import com.yc.junior.english.setting.contract.ModifyPasswordContract;
import com.yc.junior.english.setting.model.engin.MyEngin;

import rx.Subscriber;
import rx.Subscription;

/**
 * Created by zhangkai on 2017/8/3.
 */

public class ModifyPasswordPresenter extends BasePresenter<MyEngin, ModifyPasswordContract.View> implements ModifyPasswordContract.Presenter {

    public ModifyPasswordPresenter(Context context, ModifyPasswordContract.View iView) {
        super(context, iView);
        mEngin = new MyEngin(context);
    }

    @Override
    public void loadData(boolean forceUpdate, boolean showLoadingUI) {
        if (!forceUpdate) return;
    }

    @Override
    public void updatePassword(final String oldPwd,final String pwd, final String newPwd) {
        if (StringUtils.isEmpty(oldPwd) || oldPwd.length() < 6) {
            TipsHelper.tips(mContext, "老密码不能少于6位");
            return;
        }

        if ((StringUtils.isEmpty(pwd) || pwd.length() < 6) || (StringUtils.isEmpty(newPwd) || newPwd.length() < 6)) {
            TipsHelper.tips(mContext, "新密码不能少于6位");
            return;
        }

        if (!newPwd.equals(pwd)) {
            TipsHelper.tips(mContext, "两次密码输入不一致");
            return;
        }
        mView.showLoadingDialog("正在修改，请稍后");

        Subscription subscription = mEngin.updatePassword(oldPwd, newPwd).subscribe(new Subscriber<ResultInfo<String>>() {
            @Override
            public void onCompleted() {
                mView.dismissLoadingDialog();
            }

            @Override
            public void onError(Throwable e) {
                mView.dismissLoadingDialog();
            }

            @Override
            public void onNext(ResultInfo<String> resultInfo) {
                handleResultInfo(resultInfo, new Runnable() {
                    @Override
                    public void run() {
                        UIUitls.post(new Runnable() {
                            @Override
                            public void run() {
                                TipsHelper.tips(mContext, "修改成功");
                                UserInfo userInfo = UserInfoHelper.getUserInfo();
                                userInfo.setPwd(newPwd);
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
