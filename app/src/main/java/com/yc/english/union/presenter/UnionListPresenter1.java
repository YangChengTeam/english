package com.yc.english.union.presenter;

import android.content.Context;

import com.kk.securityhttp.domain.ResultInfo;
import com.yc.english.base.helper.ResultInfoHelper;
import com.yc.english.base.model.BaseEngin;
import com.yc.english.base.presenter.BasePresenter;
import com.yc.english.group.model.bean.ClassInfoList;
import com.yc.english.group.utils.EngineUtils;
import com.yc.english.union.contract.UnionListContract1;

import rx.Subscriber;
import rx.Subscription;

/**
 * Created by wanglin  on 2017/9/11 18:35.
 */

public class UnionListPresenter1 extends BasePresenter<BaseEngin, UnionListContract1.View> implements UnionListContract1.Presenter {
    public UnionListPresenter1(Context context, UnionListContract1.View view) {
        super(context, view);
    }

    @Override
    public void loadData(boolean forceUpdate, boolean showLoadingUI) {

    }

    public void getUnionList1(String user_id, String role, String type) {
        mView.showLoading();

        Subscription subscription = EngineUtils.getMyGroupList(mContext, user_id, role, type).subscribe(new Subscriber<ResultInfo<ClassInfoList>>() {
            @Override
            public void onCompleted() {

            }

            @Override
            public void onError(Throwable e) {
                mView.showNoNet();

            }

            @Override
            public void onNext(final ResultInfo<ClassInfoList> classInfo) {
                ResultInfoHelper.handleResultInfo(classInfo, new ResultInfoHelper.Callback() {
                    @Override
                    public void resultInfoEmpty(String message) {
                        mView.showNoNet();

                    }

                    @Override
                    public void resultInfoNotOk(String message) {
                        mView.showNoData();

                    }

                    @Override
                    public void reulstInfoOk() {
                        if (classInfo.data != null && classInfo.data.getList() != null && classInfo.data.getList().size() > 0) {
                            mView.showUnionList1(classInfo.data.getList());
                            mView.hideStateView();
                        } else {
                            mView.showNoData();
                        }


                    }
                });


            }
        });
        mSubscriptions.add(subscription);
    }

}
