package com.yc.junior.english.vip.presenter;

import android.content.Context;
import android.text.TextUtils;

import com.kk.securityhttp.domain.ResultInfo;
import com.kk.securityhttp.net.contains.HttpConfig;
import com.yc.junior.english.base.helper.EnginHelper;
import com.yc.junior.english.main.model.domain.URLConfig;
import com.yc.junior.english.vip.contract.BindPhoneContract;
import com.yc.junior.english.vip.model.engine.BindPhoneEngine;

import rx.Subscriber;
import rx.Subscription;
import yc.com.base.BasePresenter;
import yc.com.base.TipsHelper;
import yc.com.blankj.utilcode.util.RegexUtils;
import yc.com.blankj.utilcode.util.ToastUtils;

/**
 * Created by wanglin  on 2019/5/21 16:43.
 */
public class BindPhonePresenter extends BasePresenter<BindPhoneEngine, BindPhoneContract.View> implements BindPhoneContract.Presenter {
    public BindPhonePresenter(Context context, BindPhoneContract.View view) {
        super(context, view);
        mEngine = new BindPhoneEngine(context);

    }

    @Override
    public void loadData(boolean isForceUI, boolean isLoadingUI) {

    }


    @Override
    public void bindPhone(String phone, String code, String pwd, String resetPwd) {

        if (TextUtils.isEmpty(phone)) {
            TipsHelper.tips(mContext, "手机号不能为空");
            return;
        }
        if (!RegexUtils.isMobileExact(phone)) {
            TipsHelper.tips(mContext, "手机号格式不正确，请确认后重新输入");
            return;
        }

        if (TextUtils.isEmpty(code)) {
            TipsHelper.tips(mContext, "验证码不能为空");
            return;
        }
        if (TextUtils.isEmpty(pwd) || TextUtils.isEmpty(resetPwd)) {
            TipsHelper.tips(mContext, "密码不能为空");
            return;
        }

        if (!TextUtils.equals(pwd, resetPwd)) {
            TipsHelper.tips(mContext, "两次密码不一致，请重新输入");
            return;
        }

        mView.showLoadingDialog("正在绑定手机号,请稍候...");
        Subscription subscription = mEngine.bindPhone(phone, code, pwd).subscribe(new Subscriber<ResultInfo<String>>() {
            @Override
            public void onCompleted() {

            }

            @Override
            public void onError(Throwable e) {
                mView.dismissDialog();
            }

            @Override
            public void onNext(ResultInfo<String> stringResultInfo) {
                mView.dismissDialog();
                if (stringResultInfo != null) {
                    if (stringResultInfo.code == HttpConfig.STATUS_OK) {
                        mView.showBindSuccess();
                    } else {
                        TipsHelper.tips(mContext, stringResultInfo.message);
                    }
                } else {
                    TipsHelper.tips(mContext, HttpConfig.JSON_ERROR);
                }
            }
        });
        mSubscriptions.add(subscription);


    }


    public void sendCode(String mobile) {
        if (TextUtils.isEmpty(mobile)){
            TipsHelper.tips(mContext, "手机号不能为空");
            return;
        }

        if (!RegexUtils.isMobileExact(mobile)) {
            TipsHelper.tips(mContext, "手机号填写不正确");
            return;
        }
        mView.showLoadingDialog("验证码发送中, 请稍后");
        Subscription subscription = EnginHelper.sendCode(mContext, URLConfig.REGISTER_SEND_CODE_URL, mobile).subscribe(new Subscriber<ResultInfo<String>>() {
            @Override
            public void onCompleted() {
                mView.dismissDialog();
            }

            @Override
            public void onError(Throwable e) {
                mView.dismissDialog();
            }

            @Override
            public void onNext(final ResultInfo<String> resultInfo) {
                handleResultInfo(resultInfo, new Runnable() {
                    @Override
                    public void run() {
                        mView.senCodeSuccess();
                        ToastUtils.showShort("短信已发送， 请注意查收");
                    }
                });
            }
        });
        mSubscriptions.add(subscription);
    }
}
