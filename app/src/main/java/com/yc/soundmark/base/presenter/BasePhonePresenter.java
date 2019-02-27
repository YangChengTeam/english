package com.yc.soundmark.base.presenter;

import android.content.Context;
import android.telephony.PhoneNumberUtils;
import android.text.TextUtils;

import com.kk.securityhttp.domain.ResultInfo;
import com.kk.securityhttp.net.contains.HttpConfig;
import com.kk.utils.ToastUtil;
import com.yc.soundmark.base.contract.BasePhoneContract;
import com.yc.soundmark.base.model.engine.BasePhoneEngine;

import rx.Subscriber;
import rx.Subscription;
import yc.com.base.BasePresenter;
import yc.com.blankj.utilcode.util.PhoneUtils;
import yc.com.blankj.utilcode.util.RegexUtils;


/**
 * Created by wanglin  on 2018/11/1 15:00.
 */
public class BasePhonePresenter extends BasePresenter<BasePhoneEngine, BasePhoneContract.View> implements BasePhoneContract.Presenter {
    public BasePhonePresenter(Context context, BasePhoneContract.View view) {
        super(context, view);
        mEngine = new BasePhoneEngine(context);
    }

    @Override
    public void loadData(boolean isForceUI, boolean isLoadingUI) {

    }

    public void uploadPhone(String phone) {
        if (TextUtils.isEmpty(phone)) {
            ToastUtil.toast2(mContext, "手机号码不能为空");
            return;
        }

        if (!RegexUtils.isMobileExact(phone)) {
            ToastUtil.toast2(mContext, "请输入正确的手机号码");
            return;
        }
        mView.showLoadingDialog("正在上传手机号,请稍候...");

        Subscription subscription = mEngine.uploadPhone(phone).subscribe(new Subscriber<ResultInfo<String>>() {
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
                if (stringResultInfo != null && stringResultInfo.code == HttpConfig.STATUS_OK) {
                    mView.showUploadSuccess();
                }
            }
        });
        mSubscriptions.add(subscription);
    }
}
