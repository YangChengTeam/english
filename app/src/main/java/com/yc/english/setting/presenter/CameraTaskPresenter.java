package com.yc.english.setting.presenter;

import android.content.Context;

import com.kk.securityhttp.domain.ResultInfo;
import com.yc.english.base.helper.ResultInfoHelper;

import com.yc.english.group.model.bean.TaskUploadInfo;
import com.yc.english.group.utils.EngineUtils;
import com.yc.english.setting.contract.CameraTaskContract;
import com.yc.english.setting.model.engin.CameraEngine;

import java.io.File;

import rx.Subscriber;
import rx.Subscription;
import yc.com.base.BasePresenter;

/**
 * Created by wanglin  on 2017/12/11 15:55.
 */

public class CameraTaskPresenter extends BasePresenter<CameraEngine, CameraTaskContract.View> implements CameraTaskContract.Presenter {


    public CameraTaskPresenter(Context context, CameraTaskContract.View view) {
        super(context, view);
        mEngine = new CameraEngine(context);
    }

    @Override
    public void loadData(boolean forceUpdate, boolean showLoadingUI) {

    }

    public void uploadFile(File file, String fileName, String name) {
        mView.showLoadingDialog("正在上传图片");
        Subscription subscription = EngineUtils.uploadFile(mContext, file, fileName, name).subscribe(new Subscriber<ResultInfo<TaskUploadInfo>>() {
            @Override
            public void onCompleted() {

            }

            @Override
            public void onError(Throwable e) {
                mView.dismissDialog();
            }

            @Override
            public void onNext(final ResultInfo<TaskUploadInfo> taskUploadInfoResultInfo) {
                ResultInfoHelper.handleResultInfo(taskUploadInfoResultInfo, new ResultInfoHelper.Callback() {
                    @Override
                    public void resultInfoEmpty(String message) {
                        mView.dismissDialog();
                    }

                    @Override
                    public void resultInfoNotOk(String message) {
                        mView.dismissDialog();
                    }

                    @Override
                    public void reulstInfoOk() {
                        mView.dismissDialog();
                        if (taskUploadInfoResultInfo.data != null)
                            mView.showUploadResult(taskUploadInfoResultInfo.data);

//                            pictureDiscern(taskUploadInfoResultInfo.data.getFile_path());
                    }
                });

            }
        });
        mSubscriptions.add(subscription);
    }


    public void uploadFile(File file) {
        mView.showLoadingDialog("正在上传图片");
        Subscription subscription = mEngine.uploadFile(file).subscribe(new Subscriber<String>() {
            @Override
            public void onCompleted() {

            }

            @Override
            public void onError(Throwable e) {
                mView.dismissDialog();
            }

            @Override
            public void onNext(final String string) {


            }
        });
        mSubscriptions.add(subscription);
    }

}
