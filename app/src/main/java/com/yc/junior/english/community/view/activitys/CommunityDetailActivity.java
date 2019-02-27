package com.yc.junior.english.community.view.activitys;

import android.content.Intent;
import android.graphics.drawable.Drawable;
import android.support.v4.content.ContextCompat;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.hwangjr.rxbus.RxBus;
import com.jakewharton.rxbinding.view.RxView;
import com.yc.english.R;
import com.yc.english.base.helper.TipsHelper;
import com.yc.english.base.view.FullScreenActivity;
import com.yc.english.base.view.StateView;
import com.yc.english.community.contract.CommunityInfoContract;
import com.yc.english.community.model.domain.CommentInfo;
import com.yc.english.community.model.domain.CommunityInfo;
import com.yc.english.community.presenter.CommunityInfoPresenter;
import com.yc.english.community.view.adapter.CommentItemAdapter;
import com.yc.english.community.view.wdigets.CommunityHeadView;
import com.yc.english.main.hepler.UserInfoHelper;
import com.yc.english.main.model.domain.Constant;
import com.yc.junior.english.community.contract.CommunityInfoContract;
import com.yc.junior.english.community.model.domain.CommentInfo;
import com.yc.junior.english.community.model.domain.CommunityInfo;
import com.yc.junior.english.community.presenter.CommunityInfoPresenter;
import com.yc.junior.english.community.view.wdigets.CommunityHeadView;

import java.io.Serializable;
import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.util.List;
import java.util.concurrent.TimeUnit;

import butterknife.BindView;
import rx.functions.Action1;
import yc.com.blankj.utilcode.util.StringUtils;


/**
 * Created by admin on 2017/9/1.
 */

public class CommunityDetailActivity extends FullScreenActivity<CommunityInfoPresenter> implements CommunityInfoContract.View, CommunityHeadView.CommunityDetailListener {

    @BindView(R.id.sv_loading)
    StateView mStateView;

    @BindView(R.id.comment_list)
    RecyclerView mCommentRecyclerView;

    @BindView(R.id.et_comment_content)
    EditText mCommentContentEditText;

    @BindView(R.id.tv_send_comment)
    TextView mSendCommentTextView;

    private CommentItemAdapter mCommentItemAdapter;

    private CommunityInfo communityInfo;

    private int currentPage = 1;

    private int pageSize = 10;

    private CommunityHeadView headView;

    @Override
    public int getLayoutId() {
        return R.layout.community_note_detail;
    }

    @Override
    public void init() {

        Intent intent = getIntent();
        communityInfo = (CommunityInfo) intent.getSerializableExtra("community_info");

        String titleName = "";
        if (communityInfo.getcType().equals("1")) {
            titleName = getResources().getString(R.string.english_name);
        } else if (communityInfo.getcType().equals("2")) {
            titleName = getResources().getString(R.string.friends_name);
        } else {
            titleName = getResources().getString(R.string.hot_data_name);
        }
        mToolbar.setTitle(titleName);
        mToolbar.showNavigationIcon();

        mPresenter = new CommunityInfoPresenter(this, this);

        if (communityInfo != null) {

            mCommentItemAdapter = new CommentItemAdapter(this, null);

            headView = new CommunityHeadView(this);

            mCommentItemAdapter.setHeaderView(headView);
            headView.setListener(this);

            headView.showHeadInfo(communityInfo);

            mCommentRecyclerView.setLayoutManager(new LinearLayoutManager(this));
            mCommentRecyclerView.setAdapter(mCommentItemAdapter);

            setPraiseStatus(communityInfo.getAgreed());

            mPresenter.commentInfoList(Integer.parseInt(communityInfo.getId()), currentPage, 10);
        }

        mCommentItemAdapter.setOnLoadMoreListener(new BaseQuickAdapter.RequestLoadMoreListener() {
            @Override
            public void onLoadMoreRequested() {
                mPresenter.commentInfoList(Integer.parseInt(communityInfo.getId()), currentPage, pageSize);
            }
        }, mCommentRecyclerView);


        RxView.clicks(mSendCommentTextView).throttleFirst(200, TimeUnit.MILLISECONDS).subscribe(new Action1<Void>() {
            @Override
            public void call(Void aVoid) {

                if (StringUtils.isEmpty(mCommentContentEditText.getText())) {
                    TipsHelper.tips(CommunityDetailActivity.this, "请输入回复内容");
                    return;
                }

                if (UserInfoHelper.getUserInfo() == null) {
                    UserInfoHelper.isGotoLogin(CommunityDetailActivity.this);
                    return;
                }

                if (communityInfo != null) {
                    CommentInfo commentInfo = new CommentInfo();
                    commentInfo.setNoteId(communityInfo.getId());
                    commentInfo.setUserId(UserInfoHelper.getUserInfo() != null ? UserInfoHelper.getUserInfo().getUid() : "");
                    try {
                        commentInfo.setContent(URLEncoder.encode(mCommentContentEditText.getText().toString(), "UTF-8"));
                    } catch (UnsupportedEncodingException e) {
                        e.printStackTrace();
                    }

                    mPresenter.addCommentInfo(commentInfo);
                }
            }
        });

    }

    @Override
    public void praiseClick() {
        if (UserInfoHelper.getUserInfo() == null) {
            UserInfoHelper.isGotoLogin(CommunityDetailActivity.this);
            return;
        }

        if (communityInfo != null) {
            mPresenter.addAgreeInfo(UserInfoHelper.getUserInfo() != null ? UserInfoHelper.getUserInfo().getUid() : "", communityInfo.getId());
        } else {
            TipsHelper.tips(CommunityDetailActivity.this, "数据异常");
        }
    }

    @Override
    public void imageShow(int position) {
        if (communityInfo != null) {
            Intent intent = new Intent(CommunityDetailActivity.this, CommunityImageShowActivity.class);
            intent.putExtra("current_position", position);
            intent.putExtra("images", (Serializable) communityInfo.getImages());
            startActivity(intent);
        } else {
            TipsHelper.tips(CommunityDetailActivity.this, "数据异常");
        }
    }

    @Override
    public void hide() {
        mStateView.hide();
    }

    @Override
    public void showNoNet() {
        mStateView.showNoNet(mCommentRecyclerView, "网络不给力", new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                currentPage = 1;
                mPresenter.commentInfoList(Integer.parseInt(communityInfo.getId()), currentPage, 10);
            }
        });
    }

    @Override
    public void showNoData() {
        mStateView.showNoData(mCommentRecyclerView);
    }

    @Override
    public void showLoading() {
        mStateView.showLoading(mCommentRecyclerView);
    }

    @Override
    public void showCommunityInfoListData(List<CommunityInfo> list) {

    }

    @Override
    public void showAddCommunityInfo(CommunityInfo communityInfo) {

    }

    @Override
    public void showCommentList(List<CommentInfo> list) {
        if (currentPage > 1) {
            mCommentItemAdapter.addData(list);
        } else {
            mCommentItemAdapter.setNewData(list);
        }

        if (list.size() == pageSize) {
            currentPage++;
            mCommentItemAdapter.loadMoreComplete();
        } else {
            mCommentItemAdapter.loadMoreEnd();
        }
    }

    @Override
    public void showAddComment(CommentInfo commentInfo) {

        mCommentContentEditText.setText("");
        if (UserInfoHelper.getUserInfo() != null) {
            commentInfo.setUserName(UserInfoHelper.getUserInfo().getNickname());
            commentInfo.setFace(UserInfoHelper.getUserInfo().getAvatar());
        }


        if (communityInfo != null && !StringUtils.isEmpty(communityInfo.getFollowCount())) {
            try {
                int resCount = Integer.parseInt(communityInfo.getFollowCount()) + 1;
                headView.updateCommentCount(resCount);
            } catch (NumberFormatException e) {
                e.printStackTrace();
            }
        }

        mCommentItemAdapter.addData(0, commentInfo);
        RxBus.get().post(Constant.COMMUNITY_REFRESH, "from add communityInfo");
    }


    @Override
    public void showAgreeInfo(boolean flag) {
        setPraiseStatus("1");

        if (communityInfo != null && !StringUtils.isEmpty(communityInfo.getAgreeCount())) {
            try {
                int resCount = Integer.parseInt(communityInfo.getAgreeCount()) + 1;

                headView.updatePraiseCount(resCount);
            } catch (NumberFormatException e) {
                e.printStackTrace();
            }
        }
        RxBus.get().post(Constant.COMMUNITY_REFRESH, "from add communityInfo");
    }

    public void setPraiseStatus(String type) {
        if (type != null && type.equals("1")) {
            Drawable isZan = ContextCompat.getDrawable(CommunityDetailActivity.this, R.mipmap.is_zan_icon);
            isZan.setBounds(0, 0, isZan.getMinimumWidth(), isZan.getMinimumHeight());
            headView.updatePraiseState(isZan);
        } else {
            Drawable noZan = ContextCompat.getDrawable(CommunityDetailActivity.this, R.mipmap.no_zan_icon);
            noZan.setBounds(0, 0, noZan.getMinimumWidth(), noZan.getMinimumHeight());
            headView.updatePraiseState(noZan);
        }
    }

    @Override
    public void showNoteDelete(boolean flag) {

    }
}