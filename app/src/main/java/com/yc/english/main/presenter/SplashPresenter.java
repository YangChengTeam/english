package com.yc.english.main.presenter;

import android.content.Context;

import com.alibaba.fastjson.JSON;
import com.kk.securityhttp.domain.ResultInfo;
import com.kk.securityhttp.net.contains.HttpConfig;
import com.yc.english.main.contract.SplashContract;
import com.yc.english.main.model.domain.Constant;
import com.yc.english.main.model.domain.IndexDialogInfoWrapper;
import com.yc.english.main.model.engin.SplashEngin;

import rx.Subscriber;
import rx.Subscription;
import yc.com.base.BasePresenter;
import yc.com.blankj.utilcode.util.SPUtils;

/**
 * Created by zhangkai on 2017/8/1.
 */

public class SplashPresenter extends BasePresenter<SplashEngin, SplashContract.View> implements SplashContract.Presenter {

    public SplashPresenter(Context context, SplashContract.View view) {
        super(context, view);
        mEngine = new SplashEngin(context);
    }

    @Override
    public void loadData(boolean forceUpdate, boolean showLoadingUI) {
        if (!forceUpdate) return;
//        mView.gotToMain();
        getDialogInfo();
        getIndexMenuInfo();
    }


    @Override
    public void getDialogInfo() {
        final long startTime = System.currentTimeMillis();

        Subscription subscription = mEngine.getDialogInfo().subscribe(new Subscriber<ResultInfo<IndexDialogInfoWrapper>>() {
            @Override
            public void onCompleted() {

            }

            @Override
            public void onError(Throwable e) {
                long delay = System.currentTimeMillis() - startTime;

                mView.gotToMain(delay);
            }

            @Override
            public void onNext(ResultInfo<IndexDialogInfoWrapper> indexDialogInfoWrapperResultInfo) {
                long delay = System.currentTimeMillis() - startTime;

                if (indexDialogInfoWrapperResultInfo != null && indexDialogInfoWrapperResultInfo.code == HttpConfig.STATUS_OK) {
                    IndexDialogInfoWrapper infoWrapper = indexDialogInfoWrapperResultInfo.data;
                    mView.showAdvInfo(infoWrapper.info, delay);
                    SPUtils.getInstance().put(Constant.INDEX_DIALOG_INFO, JSON.toJSONString(infoWrapper.info));
                } else {
                    mView.gotToMain(delay);
                }

            }
        });
        mSubscriptions.add(subscription);
    }

    @Override
    public void getIndexMenuInfo() {
        Subscription subscription = mEngine.getIndexMenuInfo().subscribe(new Subscriber<ResultInfo<IndexDialogInfoWrapper>>() {
            @Override
            public void onCompleted() {

            }

            @Override
            public void onError(Throwable e) {


            }

            @Override
            public void onNext(ResultInfo<IndexDialogInfoWrapper> indexDialogInfoWrapperResultInfo) {

                if (indexDialogInfoWrapperResultInfo != null && indexDialogInfoWrapperResultInfo.code == HttpConfig.STATUS_OK) {
//                    mView.hide();
                    IndexDialogInfoWrapper infoWrapper = indexDialogInfoWrapperResultInfo.data;
                    SPUtils.getInstance().put(Constant.INDEX_MENU_STATICS, JSON.toJSONString(infoWrapper.info));
                }
            }
        });
        mSubscriptions.add(subscription);
    }
}
