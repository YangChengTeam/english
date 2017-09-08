package com.yc.english.main.view.fragments;

import android.content.Intent;
import android.graphics.Color;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentStatePagerAdapter;
import android.support.v4.content.ContextCompat;
import android.support.v4.view.ViewPager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.Gravity;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.ScrollView;
import android.widget.TextView;

import com.blankj.utilcode.util.SPUtils;
import com.chad.library.adapter.base.BaseQuickAdapter;
import com.hwangjr.rxbus.annotation.Subscribe;
import com.hwangjr.rxbus.annotation.Tag;
import com.hwangjr.rxbus.thread.EventThread;
import com.jakewharton.rxbinding.view.RxView;

import com.shizhefei.view.indicator.FixedIndicatorView;
import com.shizhefei.view.indicator.Indicator;
import com.shizhefei.view.indicator.slidebar.ColorBar;
import com.shizhefei.view.indicator.transition.OnTransitionTextListener;

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
import com.yc.english.union.view.activitys.UnionMainActivity;
import com.yc.english.weixin.views.activitys.CourseActivity;
import com.yc.english.weixin.views.activitys.CourseTypeActivity;
import com.youth.banner.Banner;
import com.youth.banner.listener.OnBannerListener;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.TimeUnit;

import butterknife.BindView;
import rx.functions.Action1;


/**
 * Created by zhangkai on 2017/7/24.
 */

public class IndexFragment extends BaseFragment<IndexPresenter> implements IndexContract.View {
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

    @BindView(R.id.iv_guild)
    ImageView mGuildImageView;

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

    @BindView(R.id.iv_english_work)
    ImageView mEnglishImageView;

    @BindView(R.id.iv_one_to_nine)
    ImageView mOTNImageView;

    @BindView(R.id.iv_one_to_to)
    ImageView mOneToToImageView;

    @BindView(R.id.banner)
    Banner mBanner;

    private FragmentAdapter mFragmentAdapter;
    @BindView(R.id.viewpager)
    ViewPager mViewPager;
    @BindView(R.id.fiv_indicator)
    FixedIndicatorView mFixedIndicatorView;

    @BindView(R.id.rv_hot)
    RecyclerView mHotMircoClassRecyclerView;
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

    @BindView(R.id.im_lex)
    IndexMenuView mLexIndexMenuView;

    @BindView(R.id.im_grammar)
    IndexMenuView mGrammarIndexMenuView;

    @BindView(R.id.im_sentence)
    IndexMenuView mSentenceIndexMenuView;

    @BindView(R.id.im_composition)
    IndexMenuView mCompositionIndexMenuView;

    @BindView(R.id.im_hearing)
    IndexMenuView mHearingIndexMenuView;


    @Override
    public void init() {
        mPresenter = new IndexPresenter(getActivity(), this);


        RxView.clicks(mLexIndexMenuView).throttleFirst(200, TimeUnit.MILLISECONDS).subscribe(new Action1<Void>() {
            @Override
            public void call(Void aVoid) {
                Intent intent = new Intent(getActivity(), CourseTypeActivity.class);
                intent.putExtra("index", 0);
                startActivity(intent);
            }
        });

        RxView.clicks(mGrammarIndexMenuView).throttleFirst(200, TimeUnit.MILLISECONDS).subscribe(new Action1<Void>() {
            @Override
            public void call(Void aVoid) {
                Intent intent = new Intent(getActivity(), CourseTypeActivity.class);
                intent.putExtra("index", 1);
                startActivity(intent);
            }
        });

        RxView.clicks(mSentenceIndexMenuView).throttleFirst(200, TimeUnit.MILLISECONDS).subscribe(new Action1<Void>() {
            @Override
            public void call(Void aVoid) {
                Intent intent = new Intent(getActivity(), CourseTypeActivity.class);
                intent.putExtra("index", 2);
                startActivity(intent);
            }
        });

        RxView.clicks(mCompositionIndexMenuView).throttleFirst(200, TimeUnit.MILLISECONDS).subscribe(new Action1<Void>() {
            @Override
            public void call(Void aVoid) {
                Intent intent = new Intent(getActivity(), CourseTypeActivity.class);
                intent.putExtra("index", 3);
                startActivity(intent);
            }
        });

        RxView.clicks(mHearingIndexMenuView).throttleFirst(200, TimeUnit.MILLISECONDS).subscribe(new Action1<Void>() {
            @Override
            public void call(Void aVoid) {
                Intent intent = new Intent(getActivity(), CourseTypeActivity.class);
                intent.putExtra("index", 4);
                startActivity(intent);
            }
        });

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

        RxView.clicks(mGuildImageView).throttleFirst(200, TimeUnit.MILLISECONDS).subscribe(new Action1<Void>() {
            @Override
            public void call(Void aVoid) {
                Intent intent = new Intent(getActivity(), UnionMainActivity.class);
                startActivity(intent);
            }
        });

        RxView.clicks(mEnglishImageView).throttleFirst(200, TimeUnit.MILLISECONDS).subscribe(new Action1<Void>() {
            @Override
            public void call(Void aVoid) {
                Intent intent = new Intent(getActivity(), CourseActivity.class);
                intent.putExtra("title", "英语练习题");
                intent.putExtra("type", "4");
                startActivity(intent);
            }
        });

        RxView.clicks(mOneToToImageView).throttleFirst(200, TimeUnit.MILLISECONDS).subscribe(new Action1<Void>() {
            @Override
            public void call(Void aVoid) {
                Intent intent = new Intent(getActivity(), CourseActivity.class);
                intent.putExtra("title", "初升高试卷");
                intent.putExtra("type", "5");
                startActivity(intent);
            }
        });

        RxView.clicks(mOTNImageView).throttleFirst(200, TimeUnit.MILLISECONDS).subscribe(new Action1<Void>() {
            @Override
            public void call(Void aVoid) {
                Intent intent = new Intent(getActivity(), CourseActivity.class);
                intent.putExtra("title", "小升初试卷");
                intent.putExtra("type", "6");
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
                Intent intent = new Intent(getActivity(), WebActivity.class);
                intent.putExtra("title", slideInfo.getTitle());
                intent.putExtra("url", slideInfo.getTypeValue());
                startActivity(intent);
            }
        });

        mFixedIndicatorView.setAdapter(new MyAdapter(5));

        mFixedIndicatorView.setScrollBar(new ColorBar(getActivity(), ContextCompat.getColor(getActivity(), R.color
                .primary), 8));

        float unSelectSize = 15;
        float selectSize = 15;
        int selectColor = ContextCompat.getColor(getActivity(), R.color.primary);
        int unSelectColor = ContextCompat.getColor(getActivity(), R.color.black_333);
        mFixedIndicatorView.setOnTransitionListener(new OnTransitionTextListener().setColor(selectColor, unSelectColor).setSize(selectSize, unSelectSize));
        mFixedIndicatorView.setOnIndicatorItemClickListener(new Indicator.OnIndicatorItemClickListener() {
            @Override
            public boolean onItemClick(View clickItemView, int position) {
                mViewPager.setCurrentItem(position);
                return false;
            }
        });
        mFixedIndicatorView.setCurrentItem(0, true);


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
        return R.layout.main_fragment_index;
    }

    @Override
    public void showLoading() {
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

        mFragmentAdapter = new FragmentAdapter(getChildFragmentManager(), indexInfo);
        mViewPager.setAdapter(mFragmentAdapter);
        mViewPager.setCurrentItem(0);
        mViewPager.setOffscreenPageLimit(3);
        mViewPager.addOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int i, float v, int i1) {

            }

            @Override
            public void onPageSelected(int i) {
                mFixedIndicatorView.setCurrentItem(i, true);
            }

            @Override
            public void onPageScrollStateChanged(int i) {

            }
        });
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

        if(indexInfo.getShequ() != null) {
            mHotCommunityAdapter.addData(indexInfo.getShequ());
        }

        if(indexInfo.getWeike()!= null) {
            mHotMircoClassAdapter.addData(indexInfo.getWeike());
        }


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


    class FragmentAdapter extends FragmentStatePagerAdapter {
        private List<ArticleFragment> articleFragments = new ArrayList<>();

        public FragmentAdapter(FragmentManager fm, IndexInfo indexInfo) {
            super(fm);
            for (int i = 0; i < 5; i++) {
                ArticleFragment articleFragment = new ArticleFragment();
                if (i == 0) {
                    articleFragment.setCourseInfos(indexInfo.getCihui());
                } else if (i == 1) {
                    articleFragment.setCourseInfos(indexInfo.getYufa());
                } else if (i == 2) {
                    articleFragment.setCourseInfos(indexInfo.getJuxing());
                } else if (i == 3) {
                    articleFragment.setCourseInfos(indexInfo.getZuowen());
                } else if (i == 4) {
                    articleFragment.setCourseInfos(indexInfo.getTingli());
                }
                articleFragments.add(articleFragment);
            }
        }

        @Override
        public Fragment getItem(int position) {
            return articleFragments.get(position);
        }

        @Override
        public int getCount() {
            return 5;
        }
    }

    private class MyAdapter extends Indicator.IndicatorAdapter {
        private final String[] titles = new String[]{"词汇", "语法", "句型", "作文", "听力"};

        private final int count;

        public MyAdapter(int count) {
            super();
            this.count = count;
        }

        @Override
        public int getCount() {
            return count;
        }

        @Override
        public View getView(int position, View convertView, ViewGroup parent) {
            if (convertView == null) {
                convertView = getActivity().getLayoutInflater().inflate(R.layout.index_tab, parent, false);
            }
            TextView textView = (TextView) convertView;
            textView.setText(titles[position]);
            return convertView;
        }
    }
}
