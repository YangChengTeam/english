package com.yc.english.main.view.fragments;

import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentStatePagerAdapter;
import android.support.v4.content.ContextCompat;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.ScrollView;
import android.widget.TextView;

import com.blankj.utilcode.util.EmptyUtils;
import com.blankj.utilcode.util.SPUtils;
import com.chad.library.adapter.base.BaseQuickAdapter;
import com.hwangjr.rxbus.annotation.Subscribe;
import com.hwangjr.rxbus.annotation.Tag;
import com.hwangjr.rxbus.thread.EventThread;
import com.jakewharton.rxbinding.view.RxView;
import com.yc.english.R;
import com.yc.english.base.helper.GlideHelper;
import com.yc.english.base.view.BaseFragment;
import com.yc.english.base.view.SelectGradePopupWindow;
import com.yc.english.base.view.SharePopupWindow;
import com.yc.english.base.view.StateView;
import com.yc.english.base.view.WebActivity;
import com.yc.english.community.view.activitys.CommunityActivity;
import com.yc.english.community.view.activitys.CommunityDetailActivity;
import com.yc.english.group.view.activitys.GroupCommonClassActivity;
import com.yc.english.group.view.activitys.GroupMainActivity;
import com.yc.english.main.contract.IndexContract;
import com.yc.english.main.hepler.BannerImageLoader;
import com.yc.english.main.model.domain.Constant;
import com.yc.english.main.model.domain.IndexInfo;
import com.yc.english.main.model.domain.SlideInfo;
import com.yc.english.main.model.domain.UserInfo;
import com.yc.english.main.presenter.IndexPresenter;
import com.yc.english.main.view.activitys.MainActivity;
import com.yc.english.main.view.adapters.AritleAdapter;
import com.yc.english.main.view.adapters.CommunityAdapter;
import com.yc.english.main.view.wdigets.IndexMenuView;
import com.yc.english.news.view.activity.NewsDetailActivity;
import com.yc.english.read.common.ReadApp;
import com.yc.english.read.view.activitys.BookActivity;
import com.yc.english.speak.view.activity.SpeakMainActivity;
import com.yc.english.union.view.activitys.UnionMainActivity;
import com.yc.english.weixin.views.activitys.CourseActivity;
import com.yc.english.weixin.views.activitys.CourseTypeActivity;
import com.youth.banner.Banner;
import com.youth.banner.listener.OnBannerListener;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.TimeUnit;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.Unbinder;
import rx.functions.Action1;


/**
 * Created by zhangkai on 2017/7/24.
 */

public class IndexFragmentNew extends BaseFragment<IndexPresenter> implements IndexContract.View {
    @BindView(R.id.sv_content)
    ScrollView mContextScrollView;

    @BindView(R.id.sv_loading)
    StateView mLoadingStateView;

    @BindView(R.id.iv_avatar)
    ImageView mAvatarImageView;

    @BindView(R.id.ll_share)
    LinearLayout mShareLinearLayout;

    @BindView(R.id.iv_book_read)
    ImageView mReadImageView;

    @BindView(R.id.iv_speak)
    ImageView mSpeakImageView;

    @BindView(R.id.iv_microclass)
    ImageView mMicroclassImageView;

    @BindView(R.id.iv_word)
    ImageView mWordImageView;

    @BindView(R.id.iv_task)
    ImageView mTaskImageView;

    @BindView(R.id.iv_community)
    ImageView mCommunityImageView;

    @BindView(R.id.iv_tutor)
    ImageView mTutorImageView;


    @BindView(R.id.banner)
    Banner mBanner;


    @BindView(R.id.rv_hot)
    RecyclerView mHotMircoClassRecyclerView;

    @BindView(R.id.ll_recommend_more)
    LinearLayout mllRecommandMore;
    @BindView(R.id.rv_recommend)
    RecyclerView mRvRecommend;

    @BindView(R.id.iv_union)
    ImageView mIvUnion;

    private AritleAdapter mHotMircoClassAdapter;


    @BindView(R.id.rv_community)
    RecyclerView mCommunityRecyclerView;
    private CommunityAdapter mHotCommunityAdapter;

    @BindView(R.id.rl_use_more)
    RelativeLayout mUserMoreRelativeLayout;

    @BindView(R.id.ll_community_more)
    LinearLayout mCommunityMoreLinearLayout;

    @BindView(R.id.ll_morcoclass_more)
    LinearLayout mMorcoclassMoreLinearLayout;

    @BindView(R.id.tv_hot_title)
    TextView mHotTitleTextView;

    @BindView(R.id.tv_more)
    TextView mMoreTextView;


//    @BindView(R.id.refresh)
//    SwipeRefreshLayout mRefreshSwipeRefreshLayout;


    @Override
    public void init() {
        mPresenter = new IndexPresenter(getActivity(), this);


        RxView.clicks(mMoreTextView).throttleFirst(200, TimeUnit.MILLISECONDS).subscribe(new Action1<Void>() {
            @Override
            public void call(Void aVoid) {
                Intent intent = new Intent(getActivity(), CourseActivity.class);
                intent.putExtra("title", "今日热点");
                intent.putExtra("type", "3");
                startActivity(intent);
            }
        });

        RxView.clicks(mReadImageView).throttleFirst(200, TimeUnit.MILLISECONDS).subscribe(new Action1<Void>() {
            @Override
            public void call(Void aVoid) {
                ReadApp.READ_COMMON_TYPE = 1;
                Intent intent = new Intent(getActivity(), BookActivity.class);
                intent.putExtra("tag", "read");
                startActivity(intent);
            }
        });

        RxView.clicks(mWordImageView).throttleFirst(200, TimeUnit.MILLISECONDS).subscribe(new Action1<Void>() {
            @Override
            public void call(Void aVoid) {
                ReadApp.READ_COMMON_TYPE = 2;
                Intent intent = new Intent(getActivity(), BookActivity.class);
                intent.putExtra("tag", "word");
                startActivity(intent);
            }
        });

        RxView.clicks(mTutorImageView).throttleFirst(200, TimeUnit.MILLISECONDS).subscribe(new Action1<Void>() {
            @Override
            public void call(Void aVoid) {
                Intent intent = new Intent(getActivity(), GroupCommonClassActivity.class);
                startActivity(intent);
            }
        });

        RxView.clicks(mTaskImageView).throttleFirst(200, TimeUnit.MILLISECONDS).subscribe(new Action1<Void>() {
            @Override
            public void call(Void aVoid) {
                Intent intent = new Intent(getActivity(), GroupMainActivity.class);

                startActivity(intent);
            }
        });

        RxView.clicks(mAvatarImageView).throttleFirst(200, TimeUnit.MILLISECONDS).subscribe(new Action1<Void>() {
            @Override
            public void call(Void aVoid) {
                MainActivity mainActivity = (MainActivity) getActivity();
                mainActivity.goToMy();
            }
        });

        RxView.clicks(mShareLinearLayout).throttleFirst(200, TimeUnit.MILLISECONDS).subscribe(new Action1<Void>() {
            @Override
            public void call(Void aVoid) {
                SharePopupWindow sharePopupWindow = new SharePopupWindow(getActivity());
                sharePopupWindow.show(mRootView);
            }
        });

        RxView.clicks(mMicroclassImageView).throttleFirst(200, TimeUnit.MILLISECONDS).subscribe(new Action1<Void>() {
            @Override
            public void call(Void aVoid) {
                MainActivity mainActivity = (MainActivity) getActivity();
                mainActivity.goToTask();
            }
        });

        RxView.clicks(mCommunityImageView).throttleFirst(200, TimeUnit.MILLISECONDS).subscribe(new Action1<Void>() {
            @Override
            public void call(Void aVoid) {
                Intent intent = new Intent(getActivity(), CommunityActivity.class);
                startActivity(intent);
            }
        });

        RxView.clicks(mSpeakImageView).throttleFirst(200, TimeUnit.MILLISECONDS).subscribe(new Action1<Void>() {
            @Override
            public void call(Void aVoid) {
                Intent intent = new Intent(getActivity(), SpeakMainActivity.class);
                startActivity(intent);
            }
        });

        RxView.clicks(mIvUnion).throttleFirst(200,TimeUnit.MILLISECONDS).subscribe(new Action1<Void>() {
            @Override
            public void call(Void aVoid) {
                Intent intent = new Intent(getActivity(), UnionMainActivity.class);
                startActivity(intent);
            }
        });


        RxView.clicks(mCommunityMoreLinearLayout).throttleFirst(200, TimeUnit.MILLISECONDS).subscribe(new Action1<Void>() {
            @Override
            public void call(Void aVoid) {
                Intent intent = new Intent(getActivity(), CommunityActivity.class);
                startActivity(intent);
            }
        });

        RxView.clicks(mMorcoclassMoreLinearLayout).throttleFirst(200, TimeUnit.MILLISECONDS).subscribe(new Action1<Void>() {
            @Override
            public void call(Void aVoid) {
                MainActivity mainActivity = (MainActivity) getActivity();
                mainActivity.goToTask();
            }
        });

        RxView.clicks(mUserMoreRelativeLayout).throttleFirst(200, TimeUnit.MILLISECONDS).subscribe(new Action1<Void>() {
            @Override
            public void call(Void aVoid) {
                Intent intent = new Intent(getActivity(), CourseTypeActivity.class);
                startActivity(intent);
            }
        });

        mBanner.setOnBannerListener(new OnBannerListener() {
            @Override
            public void OnBannerClick(int position) {
                SlideInfo slideInfo = mPresenter.getSlideInfo(position);
                if (slideInfo.getType().equals("0")) {
                    if (slideInfo == null || EmptyUtils.isEmpty(slideInfo.getTypeValue())) {
                        return;
                    }
                    Intent intent = new Intent(getActivity(), WebActivity.class);
                    intent.putExtra("title", slideInfo.getTitle());
                    intent.putExtra("url", slideInfo.getTypeValue());
                    startActivity(intent);
                } else if (slideInfo.getType().equals("1")) {
                    try {
                        Class clazz = Class.forName(slideInfo.getTypeValue());
                        Intent intent = new Intent(getActivity(), clazz);
                        startActivity(intent);
                    } catch (Exception e) {

                    }
                }
            }
        });


        mHotMircoClassRecyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));
        mHotMircoClassAdapter = new AritleAdapter(null, 1);
        mHotMircoClassRecyclerView.setAdapter(mHotMircoClassAdapter);

        mHotMircoClassAdapter.setOnItemClickListener(new BaseQuickAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(BaseQuickAdapter adapter, View view, int position) {
                Intent intent = new Intent(getActivity(), NewsDetailActivity.class);
                intent.putExtra("info", mHotMircoClassAdapter.getData().get(position));
                startActivity(intent);
            }
        });

        mCommunityRecyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));
        mHotCommunityAdapter = new CommunityAdapter(null);
        mCommunityRecyclerView.setAdapter(mHotCommunityAdapter);
        mHotCommunityAdapter.setOnItemClickListener(new BaseQuickAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(BaseQuickAdapter adapter, View view, int position) {
                Intent intent = new Intent(getActivity(), CommunityDetailActivity.class);
                intent.putExtra("community_info", mHotCommunityAdapter.getData().get(position));
                startActivity(intent);
            }
        });

        if (SPUtils.getInstance().getString("period", "").isEmpty()) {
            SelectGradePopupWindow selectGradePopupWindow = new SelectGradePopupWindow(getActivity());
            selectGradePopupWindow.show(mContextScrollView, Gravity.CENTER);
        }

//        mRefreshSwipeRefreshLayout.setColorSchemeColors(ContextCompat.getColor(getActivity(), R.color.primaryDark), ContextCompat.getColor(getActivity(), R.color.primaryDark));
//        mRefreshSwipeRefreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
//            @Override
//            public void onRefresh() {
//                mPresenter.getIndexInfo();
//            }
//        });
    }

    @Override
    public void onStart() {
        super.onStart();
        mBanner.startAutoPlay();
    }

    @Override
    public void onStop() {
        super.onStop();
        mBanner.stopAutoPlay();
    }

    @Override
    public int getLayoutId() {
        return R.layout.main_fragment_index_new;
    }

    @Override
    public void showLoading() {
//        if (!mRefreshSwipeRefreshLayout.isRefreshing())
        mLoadingStateView.showLoading(mContextScrollView);
    }

    @Override
    public void hideStateView() {
        mLoadingStateView.hide();
    }

    @Override
    public void showBanner(List<String> images) {
        mBanner.isAutoPlay(true)
                .setDelayTime(3000)
                .setImageLoader(new BannerImageLoader())
                .setImages(images)
                .start();
    }


    @Override
    public void showInfo(final IndexInfo indexInfo) {
        if (indexInfo.getRedian() != null && indexInfo.getRedian().size() > 0) {
            mHotTitleTextView.setText(indexInfo.getRedian().get(0).getTitle());
            RxView.clicks(mHotTitleTextView).throttleFirst(200, TimeUnit.MILLISECONDS).subscribe(new Action1<Void>() {
                @Override
                public void call(Void aVoid) {
                    Intent intent = new Intent(getActivity(), NewsDetailActivity.class);
                    intent.putExtra("info", indexInfo.getRedian().get(0));
                    startActivity(intent);
                }
            });

        }

        if (indexInfo.getShequ() != null) {
            mHotCommunityAdapter.addData(indexInfo.getShequ());
        }

        if (indexInfo.getWeike() != null) {
            mHotMircoClassAdapter.addData(indexInfo.getWeike());
        }
//        mRefreshSwipeRefreshLayout.setRefreshing(false);
    }

    @Subscribe(
            thread = EventThread.MAIN_THREAD,
            tags = {
                    @Tag(Constant.GRADE_REFRESH)
            }
    )
    public void refresh(String tag) {
        mPresenter.loadData(true, false);
    }

    @Subscribe(
            thread = EventThread.MAIN_THREAD,
            tags = {
                    @Tag(Constant.USER_INFO)
            }
    )
    @Override
    public void showAvatar(UserInfo userInfo) {
        GlideHelper.circleBorderImageView(getActivity(), mAvatarImageView, userInfo.getAvatar(), R.mipmap
                .default_avatar, 0.5f, Color.parseColor("#dbdbe0"));
    }

    @Subscribe(
            thread = EventThread.MAIN_THREAD,
            tags = {
                    @Tag(Constant.NO_LOGIN)
            }
    )
    public void showNoLogin(Boolean flag) {
        mAvatarImageView.setImageDrawable(ContextCompat.getDrawable(getActivity(), R.mipmap.default_big_avatar));
    }

    @Override
    public void showNoNet() {
        mLoadingStateView.showNoNet(mContextScrollView, "网络不给力", new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mPresenter.loadData(true);
            }
        });
    }

    @Override
    public void showNoData() {
        mLoadingStateView.showNoData(mContextScrollView);
    }


}
