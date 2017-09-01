package com.yc.english.community.presenter;

import android.content.Context;

import com.kk.securityhttp.domain.ResultInfo;
import com.yc.english.base.helper.ResultInfoHelper;
import com.yc.english.base.presenter.BasePresenter;
import com.yc.english.community.contract.CommunityInfoContract;
import com.yc.english.community.model.domain.CommunityInfo;
import com.yc.english.community.model.domain.CommunityInfoList;
import com.yc.english.community.model.engin.CommunityInfoEngin;

import rx.Subscriber;
import rx.Subscription;

/**
 * Created by admin on 2017/8/30.
 */

public class CommunityInfoPresenter extends BasePresenter<CommunityInfoEngin, CommunityInfoContract.View> implements CommunityInfoContract.Presenter {

    public CommunityInfoPresenter(Context context, CommunityInfoContract.View view) {
        super(context, view);
        mEngin = new CommunityInfoEngin(context);
    }

    @Override
    public void communityInfoList(int currentPage, int pageCount) {
        mView.showLoading();
        mView.showLoading();
        Subscription subscribe = mEngin.communityInfoList(currentPage, pageCount).subscribe(new Subscriber<ResultInfo<CommunityInfoList>>() {
            @Override
            public void onCompleted() {

            }

            @Override
            public void onError(Throwable e) {
                mView.showNoNet();
            }

            @Override
            public void onNext(final ResultInfo<CommunityInfoList> resultInfo) {

                ResultInfoHelper.handleResultInfo(resultInfo, new ResultInfoHelper.Callback() {
                    @Override
                    public void resultInfoEmpty(String message) {
                        mView.showNoNet();
                    }

                    @Override
                    public void resultInfoNotOk(String message) {
                        mView.showNoNet();
                    }

                    @Override
                    public void reulstInfoOk() {
                        if (resultInfo != null && resultInfo.data != null) {
                            mView.showCommunityInfoListData(resultInfo.data.list);
                            mView.hideStateView();
                        }
                    }
                });

            }
        });

        mSubscriptions.add(subscribe);

    }

    @Override
    public void addCommunityInfo(CommunityInfo communityInfo) {
    }

    @Override
    public void loadData(boolean forceUpdate, boolean showLoadingUI) {
        if (!forceUpdate) return;
    }
}
