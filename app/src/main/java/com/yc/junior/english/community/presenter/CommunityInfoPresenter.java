package com.yc.junior.english.community.presenter;

import android.content.Context;

import com.kk.securityhttp.domain.ResultInfo;
import com.kk.securityhttp.net.contains.HttpConfig;
import com.kk.securityhttp.net.entry.UpFileInfo;
import com.yc.junior.english.base.helper.ResultInfoHelper;
import com.yc.junior.english.base.helper.TipsHelper;
import com.yc.junior.english.community.contract.CommunityInfoContract;
import com.yc.junior.english.community.model.domain.CommentInfo;
import com.yc.junior.english.community.model.domain.CommentInfoList;
import com.yc.junior.english.community.model.domain.CommunityInfo;
import com.yc.junior.english.community.model.domain.CommunityInfoList;
import com.yc.junior.english.community.model.engin.CommunityInfoEngin;

import rx.Subscriber;
import rx.Subscription;
import yc.com.base.BasePresenter;
import yc.com.blankj.utilcode.util.UIUitls;


/**
 * Created by admin on 2017/8/30.
 */

public class CommunityInfoPresenter extends BasePresenter<CommunityInfoEngin, CommunityInfoContract.View> implements CommunityInfoContract.Presenter {



    public CommunityInfoPresenter(Context context, CommunityInfoContract.View view) {
        super(context, view);
        mEngine = new CommunityInfoEngin(context);
    }

    @Override
    public void communityInfoList(String userId, int type, final int currentPage, int pageCount) {
        if (currentPage == 1 && mFirstLoad) {
            mView.showLoading();
        }
        Subscription subscribe = mEngine.communityInfoList(userId, type, currentPage, pageCount).subscribe(new Subscriber<ResultInfo<CommunityInfoList>>() {
            @Override
            public void onCompleted() {

            }

            @Override
            public void onError(Throwable e) {
                if (currentPage == 1 && mFirstLoad) {
                    mView.showNoNet();
                }
            }

            @Override
            public void onNext(final ResultInfo<CommunityInfoList> resultInfo) {

                ResultInfoHelper.handleResultInfo(resultInfo, new ResultInfoHelper.Callback() {
                    @Override
                    public void resultInfoEmpty(String message) {
                        if (currentPage == 1 && !mFirstLoad) {
                            mView.showNoNet();
                        }
                    }

                    @Override
                    public void resultInfoNotOk(String message) {
                        if (currentPage == 1 && !mFirstLoad) {
                            mView.showNoData();
                        }
                    }

                    @Override
                    public void reulstInfoOk() {
                        if (resultInfo != null && resultInfo.data != null) {
                            if (currentPage == 1 && !mFirstLoad) {
                                mView.hide();
                            }

                            if (resultInfo.data.list != null && resultInfo.data.list.size() > 0) {
                                mView.showCommunityInfoListData(resultInfo.data.list);
                            } else {
                                if (currentPage == 1 && !mFirstLoad) {
                                    mView.showNoData();
                                }
                            }
                        } else {
                            if (currentPage == 1 && !mFirstLoad) {
                                mView.showNoData();
                            }
                        }
                    }
                });
            }
        });

        mSubscriptions.add(subscribe);
    }

    @Override
    public void addCommunityInfo(CommunityInfo communityInfo, UpFileInfo upFileInfo) {
        mView.showLoadingDialog("发布中");
        Subscription subscribe = mEngine.addCommunityInfo(communityInfo, upFileInfo).subscribe(new Subscriber<ResultInfo<CommunityInfo>>() {
            @Override
            public void onCompleted() {
                mView.dismissDialog();
            }

            @Override
            public void onError(Throwable e) {
                TipsHelper.tips(mContext, HttpConfig.NET_ERROR);
                mView.dismissDialog();
            }

            @Override
            public void onNext(final ResultInfo<CommunityInfo> resultInfo) {

                ResultInfoHelper.handleResultInfo(resultInfo, new ResultInfoHelper.Callback() {
                    @Override
                    public void resultInfoEmpty(String message) {
                        TipsHelper.tips(mContext, message);
                    }

                    @Override
                    public void resultInfoNotOk(String message) {
                        TipsHelper.tips(mContext, message);
                    }

                    @Override
                    public void reulstInfoOk() {
                        if (resultInfo != null && resultInfo.data != null) {
                            mView.showAddCommunityInfo(resultInfo.data);
                        }
                    }
                });

            }
        });

        mSubscriptions.add(subscribe);
    }

    @Override
    public void commentInfoList(int nid, final int currentPage, int pageCount) {
        if (currentPage == 1 && mFirstLoad) {
            mView.showLoading();
        }
        Subscription subscribe = mEngine.commentInfoList(nid, currentPage, pageCount).subscribe(new Subscriber<ResultInfo<CommentInfoList>>() {
            @Override
            public void onCompleted() {

            }

            @Override
            public void onError(Throwable e) {
                if (currentPage == 1 && mFirstLoad) {
                    mView.showNoNet();
                }
            }

            @Override
            public void onNext(final ResultInfo<CommentInfoList> resultInfo) {

                ResultInfoHelper.handleResultInfo(resultInfo, new ResultInfoHelper.Callback() {
                    @Override
                    public void resultInfoEmpty(String message) {
                        if (currentPage == 1 && !mFirstLoad) {
                            mView.showNoNet();
                        }
                    }

                    @Override
                    public void resultInfoNotOk(String message) {
                        if (currentPage == 1 && !mFirstLoad) {
                            mView.showNoData();
                        }
                    }

                    @Override
                    public void reulstInfoOk() {
                        if (currentPage == 1 && !mFirstLoad) {
                            mView.hide();
                        }

                        if (resultInfo != null && resultInfo.data != null) {
                            mView.showCommentList(resultInfo.data.list);
                        }
                    }
                });

            }
        });

        mSubscriptions.add(subscribe);
    }

    @Override
    public void addCommentInfo(CommentInfo commentInfo) {

        mView.showLoadingDialog("回复中");
        Subscription subscribe = mEngine.addCommentInfo(commentInfo).subscribe(new Subscriber<ResultInfo<CommentInfo>>() {
            @Override
            public void onCompleted() {
                mView.dismissDialog();
            }

            @Override
            public void onError(Throwable e) {
                TipsHelper.tips(mContext, HttpConfig.NET_ERROR);
            }

            @Override
            public void onNext(final ResultInfo<CommentInfo> resultInfo) {

                ResultInfoHelper.handleResultInfo(resultInfo, new ResultInfoHelper.Callback() {
                    @Override
                    public void resultInfoEmpty(String message) {
                        TipsHelper.tips(mContext, message);
                    }

                    @Override
                    public void resultInfoNotOk(String message) {
                        TipsHelper.tips(mContext, message);
                    }

                    @Override
                    public void reulstInfoOk() {
                        if (resultInfo != null && resultInfo.data != null) {
                            mView.showAddComment(resultInfo.data);
                        }
                    }
                });

            }
        });

        mSubscriptions.add(subscribe);
    }

    @Override
    public void addAgreeInfo(String userId, String noteId) {
        Subscription subscribe = mEngine.addAgreeInfo(userId, noteId).subscribe(new Subscriber<ResultInfo>() {
            @Override
            public void onCompleted() {
            }

            @Override
            public void onError(Throwable e) {
                TipsHelper.tips(mContext, HttpConfig.SERVICE_ERROR);
            }

            @Override
            public void onNext(final ResultInfo resultInfo) {

                ResultInfoHelper.handleResultInfo(resultInfo, new ResultInfoHelper.Callback() {
                    @Override
                    public void resultInfoEmpty(String message) {
                        TipsHelper.tips(mContext, message);
                    }

                    @Override
                    public void resultInfoNotOk(final String message) {
                        UIUitls.post(new Runnable() {
                            @Override
                            public void run() {
                                TipsHelper.tips(mContext, message);
                            }
                        });
                    }

                    @Override
                    public void reulstInfoOk() {
                        if (resultInfo != null) {
                            mView.showAgreeInfo(true);
                        } else {
                            mView.showAgreeInfo(false);
                        }
                    }
                });
            }
        });

        mSubscriptions.add(subscribe);
    }

    @Override
    public void deleteNote(String userId, String noteId) {
        Subscription subscribe = mEngine.deleteNote(userId, noteId).subscribe(new Subscriber<ResultInfo>() {
            @Override
            public void onCompleted() {
            }

            @Override
            public void onError(Throwable e) {
                TipsHelper.tips(mContext, HttpConfig.SERVICE_ERROR);
            }

            @Override
            public void onNext(final ResultInfo resultInfo) {

                ResultInfoHelper.handleResultInfo(resultInfo, new ResultInfoHelper.Callback() {
                    @Override
                    public void resultInfoEmpty(String message) {
                        TipsHelper.tips(mContext, message);
                    }

                    @Override
                    public void resultInfoNotOk(final String message) {
                        UIUitls.post(new Runnable() {
                            @Override
                            public void run() {
                                mView.showNoteDelete(false);
                                TipsHelper.tips(mContext, message);
                            }
                        });
                    }

                    @Override
                    public void reulstInfoOk() {
                        if (resultInfo != null) {
                            mView.showNoteDelete(true);
                        } else {
                            mView.showNoteDelete(false);
                        }
                    }
                });
            }
        });

        mSubscriptions.add(subscribe);
    }

    @Override
    public void loadData(boolean forceUpdate, boolean showLoadingUI) {
        if (!forceUpdate) return;
    }
}
