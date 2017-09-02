package com.yc.english.community.view.activitys;

import android.content.Intent;
import android.graphics.Matrix;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.view.animation.DecelerateInterpolator;
import android.view.animation.Transformation;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.blankj.utilcode.util.ToastUtils;
import com.chad.library.adapter.base.BaseQuickAdapter;
import com.jakewharton.rxbinding.view.RxView;
import com.yc.english.R;
import com.yc.english.base.view.BaseToolBar;
import com.yc.english.base.view.FullScreenActivity;
import com.yc.english.community.contract.CommunityInfoContract;
import com.yc.english.community.model.domain.CommentInfo;
import com.yc.english.community.model.domain.CommunityInfo;
import com.yc.english.community.presenter.CommunityInfoPresenter;
import com.yc.english.community.view.adapter.CommunityItemClickAdapter;

import java.util.List;
import java.util.concurrent.TimeUnit;

import butterknife.BindView;
import rx.functions.Action1;

/**
 * Created by admin on 2017/7/25.
 */

public class CommunityActivity extends FullScreenActivity<CommunityInfoPresenter> implements CommunityInfoContract.View {

    @BindView(R.id.all_note_list)
    RecyclerView mAllNoteRecyclerView;

    @BindView(R.id.menu_layout)
    LinearLayout menuLayout;

    @BindView(R.id.ground_view)
    View groundView;

    @BindView(R.id.tv_english_circle)
    TextView mEnglishCircleTextView;

    @BindView(R.id.tv_friends_circle)
    TextView mFriendsCircleTextView;

    CommunityItemClickAdapter mCommunityItemAdapter;

    private int[] location;

    private float maxSize = 0;

    //子菜单打开，关闭时的动画
    private Animation qq_friend_in, take_photo_in, qq_friend_out, take_photo_out;

    float lastSize = 0;

    public boolean isShow = false;

    @Override
    public int getLayoutId() {
        return R.layout.community_all_note;
    }

    @Override
    public void init() {
        mToolbar.setTitle(getString(R.string.community_name));
        mToolbar.showNavigationIcon();
        mToolbar.setMenuIcon(R.mipmap.add_note_icon);
        mToolbar.setOnItemClickLisener(new BaseToolBar.OnItemClickLisener() {
            @Override
            public void onClick() {
                if (menuLayout.getVisibility() == View.INVISIBLE) {
                    menuLayout.setVisibility(View.VISIBLE);
                    menuLayout.setFocusable(true);
                    menuLayout.setClickable(true);

                    animationOpen.setDuration(600);
                    animationOpen.setInterpolator(new DecelerateInterpolator());
                    animationOpen.setFillAfter(true);
                    groundView.startAnimation(animationOpen);
                    mFriendsCircleTextView.startAnimation(qq_friend_in);
                    mEnglishCircleTextView.startAnimation(take_photo_in);

                    isShow = true;
                    //showAddIv.setClickable(false);
                } else {
                    closeMenu();
                }
            }
        });

        location = new int[]{1000, 0};
        maxSize = 2.5f * 15;

        qq_friend_in = AnimationUtils.loadAnimation(this, R.anim.english_in);
        take_photo_in = AnimationUtils.loadAnimation(this, R.anim.friends_in);
        qq_friend_out = AnimationUtils.loadAnimation(this, R.anim.english_out);
        take_photo_out = AnimationUtils.loadAnimation(this, R.anim.friends_out);

        mPresenter = new CommunityInfoPresenter(this, this);
        mCommunityItemAdapter = new CommunityItemClickAdapter(this, null);
        mAllNoteRecyclerView.setLayoutManager(new LinearLayoutManager(this));
        mAllNoteRecyclerView.setAdapter(mCommunityItemAdapter);

        RxView.clicks(menuLayout).throttleFirst(200, TimeUnit.MILLISECONDS).subscribe(new Action1<Void>() {
            @Override
            public void call(Void aVoid) {
                closeMenu();
            }
        });

        //英语圈
        RxView.clicks(mEnglishCircleTextView).throttleFirst(200, TimeUnit.MILLISECONDS).subscribe(new Action1<Void>() {
            @Override
            public void call(Void aVoid) {
                Intent intent = new Intent(CommunityActivity.this, CommunityAddActivity.class);
                startActivity(intent);
                closeMenu();
            }
        });

        mCommunityItemAdapter.setOnItemClickListener(new BaseQuickAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(BaseQuickAdapter adapter, View view, int position) {
                Intent intent = new Intent(CommunityActivity.this, CommunityDetailActivity.class);
                intent.putExtra("community_info", mCommunityItemAdapter.getData().get(position));
                startActivity(intent);
            }
        });

        mCommunityItemAdapter.setOnItemChildClickListener(new BaseQuickAdapter.OnItemChildClickListener() {
            @Override
            public boolean onItemChildClick(BaseQuickAdapter adapter, View view, int position) {
                if (view.getId() == R.id.praise_count_layout) {
                    mPresenter.addAgreeInfo("35", ((CommunityInfo) adapter.getData().get(position)).getId());
                }
                return false;
            }
        });

        mPresenter.communityInfoList(1, 1, 10);
    }

    @Override
    public void hideStateView() {
        //mStateView.hide();
    }

    @Override
    public void showNoNet() {
        /*mStateView.showNoNet(mLayoutContext, "网络不给力", new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //mPresenter.getBookInfoById(bookId);
            }
        });*/
    }

    @Override
    public void showNoData() {
        //mStateView.showNoData(mLayoutContext);
    }

    @Override
    public void showLoading() {
        //mStateView.showLoading(mLayoutContext);
    }

    @Override
    public void showCommunityInfoListData(List<CommunityInfo> list) {
        mCommunityItemAdapter.setNewData(list);
    }

    @Override
    public void showAddCommunityInfo(CommunityInfo communityInfo) {

    }

    @Override
    public void showCommentList(List<CommentInfo> list) {

    }

    private Animation animationOpen = new Animation() {
        @Override
        protected void applyTransformation(float interpolatedTime, Transformation t) {
            float size = (maxSize - 1) * interpolatedTime + 1;
            lastSize = size;
            Matrix matrix = t.getMatrix();

            matrix.postTranslate(location[0] - groundView.getWidth() / 2, location[1] - groundView.getHeight() / 2);
            matrix.postScale(size, size, location[0], location[1]);
            if (interpolatedTime == 1) {

            }
        }
    };

    private Animation animationClose = new Animation() {
        @Override
        protected void applyTransformation(float interpolatedTime, Transformation t) {

            float size = lastSize - (maxSize - 1) * interpolatedTime + 1;
            Matrix matrix = t.getMatrix();
            matrix.postTranslate(location[0] - groundView.getWidth() / 2, location[1] - groundView.getHeight() / 2);
            matrix.postScale(size, size, location[0], location[1]);
            if (interpolatedTime == 1) {
                menuLayout.setVisibility(View.INVISIBLE);
            }
        }
    };

    /**
     * 关闭菜单
     */
    public void closeMenu() {
        mEnglishCircleTextView.startAnimation(qq_friend_out);
        mFriendsCircleTextView.startAnimation(take_photo_out);

        animationClose.setDuration(500);
        animationClose.setInterpolator(new DecelerateInterpolator());
        animationClose.setFillAfter(true);
        groundView.startAnimation(animationClose);
        isShow = false;
    }

    @Override
    public void showAddComment(CommentInfo commentInfo) {

    }

    @Override
    public void showAgreeInfo(boolean flag) {
        ToastUtils.showLong("点赞成功");
    }
}
