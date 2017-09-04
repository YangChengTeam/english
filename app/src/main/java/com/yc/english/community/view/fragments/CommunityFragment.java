package com.yc.english.community.view.fragments;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;

import com.blankj.utilcode.util.ToastUtils;
import com.chad.library.adapter.base.BaseQuickAdapter;
import com.yc.english.R;
import com.yc.english.base.view.BaseFragment;
import com.yc.english.base.view.StateView;
import com.yc.english.community.contract.CommunityInfoContract;
import com.yc.english.community.model.domain.CommentInfo;
import com.yc.english.community.model.domain.CommunityInfo;
import com.yc.english.community.presenter.CommunityInfoPresenter;
import com.yc.english.community.view.activitys.CommunityDetailActivity;
import com.yc.english.community.view.adapter.CommunityItemClickAdapter;
import com.yc.english.main.hepler.UserInfoHelper;

import java.util.List;

import butterknife.BindView;

/**
 * Created by admin on 2017/7/25.
 */

public class CommunityFragment extends BaseFragment<CommunityInfoPresenter> implements CommunityInfoContract.View, SwipeRefreshLayout.OnRefreshListener {

    @BindView(R.id.sv_loading)
    StateView mStateView;

    @BindView(R.id.pull_to_refresh)
    SwipeRefreshLayout swipeLayout;

    @BindView(R.id.all_note_list)
    RecyclerView mAllNoteRecyclerView;

    CommunityItemClickAdapter mCommunityItemAdapter;

    private int currentChildPosition;

    LinearLayoutManager mLinearLayoutManager;

    private int currentPage = 1;

    private int currentHotPage = 1;

    private int currentFriendsPage = 1;

    private int currentEnglishPage = 1;

    private int type;

    @Override
    public int getLayoutId() {
        return R.layout.community_all_note;
    }

    @Override
    public void init() {

        Bundle bundle = getArguments();
        if (bundle != null) {
            type = getArguments().getInt("type");
        }

        swipeLayout.setColorSchemeResources(
                android.R.color.holo_red_light,
                android.R.color.holo_green_light,
                android.R.color.holo_orange_light,
                android.R.color.holo_blue_light);
        swipeLayout.setOnRefreshListener(this);

        mPresenter = new CommunityInfoPresenter(getActivity(), this);
        mLinearLayoutManager = new LinearLayoutManager(getActivity());
        mCommunityItemAdapter = new CommunityItemClickAdapter(getActivity(), null);
        mAllNoteRecyclerView.setLayoutManager(mLinearLayoutManager);
        mAllNoteRecyclerView.setAdapter(mCommunityItemAdapter);

        mCommunityItemAdapter.setOnItemClickListener(new BaseQuickAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(BaseQuickAdapter adapter, View view, int position) {
                Intent intent = new Intent(getActivity(), CommunityDetailActivity.class);
                intent.putExtra("community_info", mCommunityItemAdapter.getData().get(position));
                startActivity(intent);
            }
        });

        mCommunityItemAdapter.setOnItemChildClickListener(new BaseQuickAdapter.OnItemChildClickListener() {
            @Override
            public boolean onItemChildClick(BaseQuickAdapter adapter, View view, int position) {
                if (view.getId() == R.id.praise_count_layout && mCommunityItemAdapter.getData().get(position).getAgreed().equals("0")) {
                    currentChildPosition = position;
                    mPresenter.addAgreeInfo(UserInfoHelper.getUserInfo() != null ? UserInfoHelper.getUserInfo().getUid() : "", ((CommunityInfo) adapter.getData().get(position)).getId());
                }
                if (view.getId() == R.id.comment_layout) {
                    Intent intent = new Intent(getActivity(), CommunityDetailActivity.class);
                    intent.putExtra("community_info", mCommunityItemAdapter.getData().get(position));
                    startActivity(intent);
                }
                return false;
            }
        });

        mAllNoteRecyclerView.addOnScrollListener(new RecyclerView.OnScrollListener() {
            @Override
            public void onScrollStateChanged(RecyclerView recyclerView, int newState) {
                super.onScrollStateChanged(recyclerView, newState);
            }

            @Override
            public void onScrolled(RecyclerView recyclerView, int dx, int dy) {
                super.onScrolled(recyclerView, dx, dy);
                if (isSlideToBottom(recyclerView) && mCommunityItemAdapter.getData().size() >= 10) {
                    setCurrentPage();
                    mPresenter.communityInfoList(UserInfoHelper.getUserInfo() != null ? UserInfoHelper.getUserInfo().getUid() : "", type, getCurrentPageByType(type), 10);
                }
            }
        });

        mPresenter.communityInfoList(UserInfoHelper.getUserInfo() != null ? UserInfoHelper.getUserInfo().getUid() : "", type, getCurrentPageByType(type), 10);
    }

    public int getCurrentPageByType(int type) {
        switch (type) {
            case 1:
                currentPage = currentFriendsPage;
                break;
            case 2:
                currentPage = currentEnglishPage;
                break;
            case 3:
                currentPage = currentHotPage;
                break;
        }
        return currentPage;
    }

    public void setCurrentPage() {
        switch (type) {
            case 1:
                currentFriendsPage++;
                break;
            case 2:
                currentEnglishPage++;
                break;
            case 3:
                currentHotPage++;
                break;
        }
    }

    @Override
    public void onRefresh() {
        currentPage = 1;
        currentHotPage = 1;
        currentFriendsPage = 1;
        currentEnglishPage = 1;
        mPresenter.communityInfoList(UserInfoHelper.getUserInfo() != null ? UserInfoHelper.getUserInfo().getUid() : "", type, getCurrentPageByType(type), 10);
    }

    protected boolean isSlideToBottom(RecyclerView recyclerView) {
        if (recyclerView == null) return false;
        if (recyclerView.computeVerticalScrollExtent() + recyclerView.computeVerticalScrollOffset() >= recyclerView.computeVerticalScrollRange())
            return true;
        return false;
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
        if (getCurrentPageByType(type) > 1) {
            mCommunityItemAdapter.addData(list);
        } else {
            mCommunityItemAdapter.setNewData(list);
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
        ToastUtils.showLong("点赞成功");
        mCommunityItemAdapter.getData().get(currentChildPosition).setAgreed("1");
        //mCommunityItemAdapter.notifyItemChanged(currentChildPosition);
        mCommunityItemAdapter.changeView(mLinearLayoutManager.findViewByPosition(currentChildPosition), currentChildPosition);
    }
}
