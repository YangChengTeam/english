package com.yc.english.community.view.fragments;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;

import com.blankj.utilcode.util.StringUtils;
import com.chad.library.adapter.base.BaseQuickAdapter;
import com.hwangjr.rxbus.RxBus;
import com.hwangjr.rxbus.annotation.Subscribe;
import com.hwangjr.rxbus.annotation.Tag;
import com.hwangjr.rxbus.thread.EventThread;
import com.yc.english.R;
import com.yc.english.base.view.AlertDialog;
import com.yc.english.base.view.BaseFragment;
import com.yc.english.base.view.StateView;
import com.yc.english.community.contract.CommunityInfoContract;
import com.yc.english.community.model.domain.CommentInfo;
import com.yc.english.community.model.domain.CommunityInfo;
import com.yc.english.community.presenter.CommunityInfoPresenter;
import com.yc.english.community.view.activitys.CommunityDetailActivity;
import com.yc.english.community.view.adapter.CommunityItemClickAdapter;
import com.yc.english.main.hepler.UserInfoHelper;
import com.yc.english.main.model.domain.Constant;

import java.util.List;

import butterknife.BindView;

/**
 * Created by admin on 2017/7/25.
 */

public class CommunityFragment extends BaseFragment<CommunityInfoPresenter> implements CommunityInfoContract.View, SwipeRefreshLayout.OnRefreshListener, CommunityItemClickAdapter.PraiseListener {

    @BindView(R.id.sv_loading)
    StateView mStateView;

    @BindView(R.id.pull_to_refresh)
    SwipeRefreshLayout swipeLayout;

    @BindView(R.id.all_note_list)
    RecyclerView mAllNoteRecyclerView;

    CommunityItemClickAdapter mCommunityItemAdapter;

    LinearLayoutManager mLinearLayoutManager;

    private int currentPage = 1;

    private int currentHotPage = 1;

    private int currentFriendsPage = 1;

    private int currentEnglishPage = 1;

    private int currentMyPage = 1;

    private int type;

    private int currentItemPosition;

    private int pageSize = 10;

    private boolean isShowDelete = false;

    @Override
    public int getLayoutId() {
        return R.layout.community_all_note;
    }

    @Override
    public void init() {

        Bundle bundle = getArguments();
        if (bundle != null) {
            type = getArguments().getInt("type");
            switch (type) {
                case 1:
                case 2:
                case 3:
                    isShowDelete = false;
                    break;
                case 4:
                    isShowDelete = true;
                    break;
            }
        }

        swipeLayout.setColorSchemeResources(
                R.color.primary,
                android.R.color.holo_green_light,
                android.R.color.holo_orange_light,
                R.color.primaryDark);
        swipeLayout.setOnRefreshListener(this);

        mPresenter = new CommunityInfoPresenter(getActivity(), this);
        mLinearLayoutManager = new LinearLayoutManager(getActivity());
        mCommunityItemAdapter = new CommunityItemClickAdapter(getActivity(), null, isShowDelete);
        mCommunityItemAdapter.setPraiseListener(this);
        mAllNoteRecyclerView.setLayoutManager(mLinearLayoutManager);
        mAllNoteRecyclerView.setAdapter(mCommunityItemAdapter);

        mCommunityItemAdapter.setOnItemClickListener(new BaseQuickAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(BaseQuickAdapter adapter, View view, int position) {
                currentItemPosition = position;
                Intent intent = new Intent(getActivity(), CommunityDetailActivity.class);
                intent.putExtra("community_info", mCommunityItemAdapter.getData().get(position));
                startActivity(intent);
            }
        });

        mCommunityItemAdapter.setOnItemChildClickListener(new BaseQuickAdapter.OnItemChildClickListener() {
            @Override
            public boolean onItemChildClick(final BaseQuickAdapter adapter, View view, final int position) {

                if (view.getId() == R.id.tv_comment_count) {
                    Intent intent = new Intent(getActivity(), CommunityDetailActivity.class);
                    intent.putExtra("community_info", mCommunityItemAdapter.getData().get(position));
                    startActivity(intent);
                }
                if (view.getId() == R.id.iv_delete) {
                    final AlertDialog alertDialog = new AlertDialog(getActivity());
                    alertDialog.setDesc("确认删除该帖子？");
                    alertDialog.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            alertDialog.dismiss();
                            mPresenter.deleteNote(UserInfoHelper.getUserInfo() != null ? UserInfoHelper.getUserInfo().getUid() : "", ((CommunityInfo) adapter.getData().get(position)).getId());
                        }
                    });
                    alertDialog.show();
                }
                return false;
            }
        });

        mCommunityItemAdapter.setOnLoadMoreListener(new BaseQuickAdapter.RequestLoadMoreListener() {
            @Override
            public void onLoadMoreRequested() {
                mPresenter.communityInfoList(UserInfoHelper.getUserInfo() != null ? UserInfoHelper.getUserInfo().getUid() : "", type, getCurrentPageByType(type), pageSize);
            }
        }, mAllNoteRecyclerView);

        mPresenter.communityInfoList(UserInfoHelper.getUserInfo() != null ? UserInfoHelper.getUserInfo().getUid() : "", type, getCurrentPageByType(type), pageSize);
    }

    @Override
    public void praiseItem(int parentPosition) {
        if (mCommunityItemAdapter.getData().get(parentPosition).getAgreed().equals("0")) {
            currentItemPosition = parentPosition;
            if (UserInfoHelper.getUserInfo() == null) {
                UserInfoHelper.isGotoLogin(getActivity());
            } else {
                mPresenter.addAgreeInfo(UserInfoHelper.getUserInfo() != null ? UserInfoHelper.getUserInfo().getUid() : "", ((CommunityInfo) mCommunityItemAdapter.getData().get(currentItemPosition)).getId());
            }
        }
    }

    public int getCurrentPageByType(int type) {
        switch (type) {
            case 1:
                currentPage = currentEnglishPage;
                break;
            case 2:
                currentPage = currentFriendsPage;
                break;
            case 3:
                currentPage = currentHotPage;
                break;
            case 4:
                currentPage = currentMyPage;
                break;
        }
        return currentPage;
    }

    public void setCurrentPage() {
        switch (type) {
            case 1:
                currentEnglishPage++;
                break;
            case 2:
                currentFriendsPage++;
                break;
            case 3:
                currentHotPage++;
                break;
            case 4:
                currentMyPage++;
                break;
        }
    }


    @Override
    public void onRefresh() {
        currentPage = 1;
        currentHotPage = 1;
        currentEnglishPage = 1;
        currentFriendsPage = 1;
        currentMyPage = 1;
        mPresenter.communityInfoList(UserInfoHelper.getUserInfo() != null ? UserInfoHelper.getUserInfo().getUid() : "", type, getCurrentPageByType(type), pageSize);
    }

    @Subscribe(
            thread = EventThread.MAIN_THREAD,
            tags = {
                    @Tag(Constant.COMMUNITY_REFRESH)
            }
    )
    public void rxRefresh(String tag) {
        onRefresh();
    }

    @Override
    public void showLoadingDialog(String msg) {

    }

    @Override
    public void finish() {

    }

    @Override
    public void dismissLoadingDialog() {

    }

    @Override
    public void hideStateView() {
        mStateView.hide();
    }

    @Override
    public void showNoNet() {
        mStateView.showNoNet(swipeLayout, "网络不给力", new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mPresenter.communityInfoList(UserInfoHelper.getUserInfo() != null ? UserInfoHelper.getUserInfo().getUid() : "", type, getCurrentPageByType(type), 10);
            }
        });
    }

    @Override
    public void showNoData() {
        mStateView.showNoData(swipeLayout);
    }

    @Override
    public void showLoading() {
        mStateView.showLoading(swipeLayout);
    }

    @Override
    public void showCommunityInfoListData(List<CommunityInfo> list) {
        swipeLayout.setRefreshing(false);
        if (getCurrentPageByType(type) == 1) {
            mCommunityItemAdapter.setNewData(list);
        } else {
            mCommunityItemAdapter.addData(list);
        }

        if (list.size() == pageSize) {
            setCurrentPage();
            mCommunityItemAdapter.loadMoreComplete();
        } else {
            mCommunityItemAdapter.loadMoreEnd();
        }
    }

    @Override
    public void showAddCommunityInfo(CommunityInfo communityInfo) {

    }

    @Override
    public void showCommentList(List<CommentInfo> list) {

    }


    @Override
    public void showAddComment(CommentInfo commentInfo) {

    }

    @Override
    public void showAgreeInfo(boolean flag) {
        mCommunityItemAdapter.getData().get(currentItemPosition).setAgreed("1");
        //mCommunityItemAdapter.notifyItemChanged(currentChildPosition);(Integer.parseInt(this.getData().get(pos).getAgreeCount()) + 1) + "");
        if (!StringUtils.isEmpty(mCommunityItemAdapter.getData().get(currentItemPosition).getAgreeCount())) {
            mCommunityItemAdapter.getData().get(currentItemPosition).setAgreeCount((Integer.parseInt(mCommunityItemAdapter.getData().get(currentItemPosition).getAgreeCount()) + 1) + "");
        }
        mCommunityItemAdapter.changeView(mLinearLayoutManager.findViewByPosition(currentItemPosition), currentItemPosition);
    }

    @Subscribe(
            thread = EventThread.MAIN_THREAD,
            tags = {
                    @Tag(Constant.PRAISE_REFRESH)
            }
    )
    public void rxRefreshItemPraise(String tag) {
        showAgreeInfo(true);
    }

    @Override
    public void showNoteDelete(boolean flag) {
        if (flag) {
            if (mCommunityItemAdapter.getData() != null && currentItemPosition < mCommunityItemAdapter.getData().size()) {
                mCommunityItemAdapter.getData().remove(currentItemPosition);
                if (mCommunityItemAdapter.getData().size() == 0) {
                    mStateView.showNoData(swipeLayout);
                } else {
                    mCommunityItemAdapter.notifyDataSetChanged();
                }
                RxBus.get().post(Constant.COMMUNITY_REFRESH, "from add communityInfo");
            }
        }
    }
}
