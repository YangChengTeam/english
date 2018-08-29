package com.yc.english.main.view.fragments;

import android.content.Intent;
import android.graphics.Color;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.content.ContextCompat;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.Html;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ScrollView;
import android.widget.TextView;

import com.blankj.utilcode.util.EmptyUtils;
import com.blankj.utilcode.util.SPUtils;
import com.chad.library.adapter.base.BaseQuickAdapter;
import com.hwangjr.rxbus.annotation.Subscribe;
import com.hwangjr.rxbus.annotation.Tag;
import com.hwangjr.rxbus.thread.EventThread;
import com.jakewharton.rxbinding.view.RxView;
import com.kk.utils.LogUtil;
import com.kk.utils.ToastUtil;
import com.tencent.mm.opensdk.modelbiz.WXLaunchMiniProgram;
import com.tencent.mm.opensdk.openapi.IWXAPI;
import com.tencent.mm.opensdk.openapi.WXAPIFactory;
import com.umeng.analytics.MobclickAgent;
import com.yc.english.EnglishApp;
import com.yc.english.R;
import com.yc.english.base.helper.GlideHelper;
import com.yc.english.base.utils.StatusBarCompat;
import com.yc.english.base.view.BaseActivity;
import com.yc.english.base.view.BaseFragment;
import com.yc.english.base.view.IndexVipKidDialog;
import com.yc.english.base.view.SharePopupWindow;
import com.yc.english.base.view.StateView;
import com.yc.english.base.view.WebActivity;
import com.yc.english.group.constant.GroupConstant;
import com.yc.english.group.view.activitys.CoachScoreActivity;
import com.yc.english.main.contract.IndexContract;
import com.yc.english.main.hepler.BannerImageLoader;
import com.yc.english.main.model.domain.Constant;
import com.yc.english.main.model.domain.IndexInfo;
import com.yc.english.main.model.domain.SlideInfo;
import com.yc.english.main.model.domain.UserInfo;
import com.yc.english.main.presenter.IndexPresenter;
import com.yc.english.main.view.activitys.MainActivity;
import com.yc.english.main.view.adapters.AritleAdapter;
import com.yc.english.news.view.activity.NewsDetailActivity;
import com.yc.english.read.common.ReadApp;
import com.yc.english.read.view.activitys.BookActivity;
import com.yc.english.speak.view.activity.SpeakMainActivity;
import com.yc.english.speak.view.adapter.IndexRecommendAdapter;
import com.yc.english.vip.views.activity.VipScoreTutorshipActivity;
import com.yc.english.weixin.model.domain.CourseInfo;
import com.yc.english.weixin.views.activitys.CourseActivity;
import com.yc.english.weixin.views.activitys.CourseClassifyActivity;
import com.yc.english.weixin.views.activitys.CourseTypeActivity;
import com.yc.english.weixin.views.activitys.WeikeUnitActivity;
import com.youth.banner.Banner;
import com.youth.banner.listener.OnBannerListener;

import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.concurrent.TimeUnit;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.Unbinder;
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

    @BindView(R.id.iv_speak)
    ImageView mSpeakImageView;

    @BindView(R.id.iv_word)
    ImageView mWordImageView;

    @BindView(R.id.iv_task)
    ImageView mTaskImageView;

    @BindView(R.id.iv_homework_answer)
    ImageView mHomeworkAnswer;

    @BindView(R.id.iv_exam)
    ImageView mExamImageView;

    @BindView(R.id.iv_ad)
    ImageView mAd;

    @BindView(R.id.iv_teacher_task)
    ImageView mTeacherTask;

    @BindView(R.id.banner)
    Banner mBanner;

    @BindView(R.id.rv_hot)
    RecyclerView mHotMircoClassRecyclerView;

    @BindView(R.id.ll_recommend_more)
    LinearLayout mllRecommendMore;
    @BindView(R.id.rv_recommend)
    RecyclerView mRvRecommend;
    @BindView(R.id.iv_weike)
    ImageView ivWeike;
    @BindView(R.id.iv_spoken)
    ImageView ivSpoken;


    private AritleAdapter mHotMircoClassAdapter;

    @BindView(R.id.ll_morcoclass_more)
    LinearLayout mMorcoclassMoreLinearLayout;

    @BindView(R.id.tv_hot_title)
    TextView mHotTitleTextView;

    @BindView(R.id.tv_more)
    TextView mMoreTextView;

    @BindView(R.id.status_bar)
    View mStatusBar;

    @BindView(R.id.toolbar)
    LinearLayout mToolBar;

    @BindView(R.id.toolbarWarpper)
    FrameLayout mToolbarWarpper;

    private IndexRecommendAdapter mRecommendAdapter;

//    @BindView(R.id.refresh)
//    SwipeRefreshLayout mRefreshSwipeRefreshLayout;


    @Override
    public void init() {
        StatusBarCompat.compat((BaseActivity) getActivity(), mToolbarWarpper, mToolBar, mStatusBar);
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

        //课本点读入口
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
                MobclickAgent.onEvent(getActivity(), "word_books", "单词宝典");
                ReadApp.READ_COMMON_TYPE = 2;
                Intent intent = new Intent(getActivity(), BookActivity.class);
                intent.putExtra("tag", "word");
                startActivity(intent);
            }
        });

        RxView.clicks(mAd).throttleFirst(200, TimeUnit.MILLISECONDS).subscribe(new Action1<Void>() {
            @Override
            public void call(Void aVoid) {
                Intent intent = new Intent(getActivity(), VipScoreTutorshipActivity.class);

                startActivity(intent);
            }
        });

        RxView.clicks(mTaskImageView).throttleFirst(200, TimeUnit.MILLISECONDS).subscribe(new Action1<Void>() {
            @Override
            public void call(Void aVoid) {
                MobclickAgent.onEvent(getActivity(), "listen_and_speak", "配音听力");
                Intent intent = new Intent(getActivity(), SpeakMainActivity.class);
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

        RxView.clicks(mTeacherTask).throttleFirst(200, TimeUnit.MILLISECONDS).subscribe(new Action1<Void>() {
            @Override
            public void call(Void aVoid) {
                Intent intent = new Intent(getActivity(), CoachScoreActivity.class);
                startActivity(intent);
            }
        });

        RxView.clicks(mHomeworkAnswer).throttleFirst(200, TimeUnit.MILLISECONDS).subscribe(new Action1<Void>() {
            @Override
            public void call(Void aVoid) {
                MobclickAgent.onEvent(getActivity(), "teacher_answer_in", "教材答案入口点击");
//                Intent intent = new Intent(getActivity(), CourseActivity.class);
//                intent.putExtra("title", "教材答案");
//                intent.putExtra("type", "17");
//                startActivity(intent);
                switchSmallProcedure(GroupConstant.originid, GroupConstant.appid);
            }
        });

//"http:\\/\\/a.app.qq.com\\/o\\/simple.jsp?pkgname=com.yc.phonogram"
        RxView.clicks(mExamImageView).throttleFirst(200, TimeUnit.MILLISECONDS).subscribe(new Action1<Void>() {
            @Override
            public void call(Void aVoid) {
                if (advInfo == null) return;
                MobclickAgent.onEvent(getActivity(), advInfo.getStatistics());
                Intent intent = new Intent(Intent.ACTION_VIEW);
                intent.setData(Uri.parse(advInfo.getTypeValue()));
                startActivity(intent);

//                Intent intent = new Intent(getActivity(), WebActivity.class);
//                intent.putExtra("title", "小说阅读");
//                intent.putExtra("url", advInfo.getTypeValue());
//                startActivity(intent);
            }
        });

        RxView.clicks(mSpeakImageView).throttleFirst(200, TimeUnit.MILLISECONDS).subscribe(new Action1<Void>() {
            @Override
            public void call(Void aVoid) {
                MobclickAgent.onEvent(getActivity(), "task_online", "在线作业");
                // todo 这里是在线作业 融云im模块
//                Intent intent = new Intent(getActivity(), GroupMainActivity.class);
//                startActivity(intent);


//                ToastUtil.toast2(getActivity(), "功能正在开发中...");
                switchSmallProcedure(GroupConstant.assistant_originid, GroupConstant.appid);

//                SlideInfo slideInfo = mPresenter.getSlideInfo(0);
//                if (slideInfo.getType().equals("2")) {
//                    try {
//                        String typeValue = slideInfo.getTypeValue();
//                        if (TextUtils.isEmpty(typeValue)) return;
//                        String[] strs = typeValue.split("\\|");
//                        LogUtil.msg("tag: " + strs[0] + "---" + strs[1]);
//                        if (strs.length > 1) {
//                            // 填应用AppId
//                            String appId = strs[1];
//                            String originId = strs[0];
//                            switchSmallProcedure(originId, appId);
//                        }
//                    } catch (Exception e) {
//                        LogUtil.msg("e :" + e.getMessage());
//                        ToastUtil.toast(getActivity(), "");
//                    }
//                }
            }
        });


        RxView.clicks(mMorcoclassMoreLinearLayout).throttleFirst(200, TimeUnit.MILLISECONDS).subscribe(new Action1<Void>() {
            @Override
            public void call(Void aVoid) {
                MainActivity mainActivity = (MainActivity) getActivity();
                mainActivity.goToTask();
            }
        });

        RxView.clicks(mllRecommendMore).throttleFirst(200, TimeUnit.MILLISECONDS).subscribe(new Action1<Void>() {
            @Override
            public void call(Void aVoid) {
                Intent intent = new Intent(getActivity(), CourseTypeActivity.class);
                startActivity(intent);
            }
        });
        mBanner.setFocusable(false);
        mBanner.setOnBannerListener(new OnBannerListener() {


            @Override
            public void OnBannerClick(int position) {
                SlideInfo slideInfo = mPresenter.getSlideInfo(position);
                //友盟统计各个幻灯点击数
                MobclickAgent.onEvent(getActivity(), slideInfo.getStatistics());
                if (slideInfo.getType().equals("0")) {
                    if (EmptyUtils.isEmpty(slideInfo.getTypeValue())) {
                        return;
                    }
                    Intent intent = new Intent(getActivity(), WebActivity.class);
                    intent.putExtra("title", slideInfo.getTitle());
                    intent.putExtra("url", slideInfo.getTypeValue());
                    startActivity(intent);
                } else if (slideInfo.getType().equals("1")) {
                    try {
                        String typeValue = slideInfo.getTypeValue();
                        if (TextUtils.isEmpty(typeValue)) return;
                        String[] split = typeValue.split("\\|");
                        Class clazz = Class.forName(split[0]);
                        Intent intent = new Intent(getActivity(), clazz);
                        if (split.length == 2) {
                            CourseInfo courseInfo = new CourseInfo();
                            courseInfo.setId(split[1]);
                            intent.putExtra("info", courseInfo);
                        }
                        startActivity(intent);
                    } catch (Exception e) {

                    }
                } else if (slideInfo.getType().equals("2")) {
                    try {
                        String typeValue = slideInfo.getTypeValue();
                        if (TextUtils.isEmpty(typeValue)) return;
                        String[] strs = typeValue.split("\\|");
                        LogUtil.msg("tag: " + strs[0] + "---" + strs[1]);
                        if (strs.length > 1) {
                            // 填应用AppId
                            String appId = strs[1];
                            String originId = strs[0];
                            switchSmallProcedure(originId, appId);
                        }
                    } catch (Exception e) {
                        LogUtil.msg("e :" + e.getMessage());
                        ToastUtil.toast(getActivity(), "");
                    }
                }
            }
        });

        mHotMircoClassRecyclerView.setFocusable(false);
        mHotMircoClassRecyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));
        mHotMircoClassAdapter = new AritleAdapter(null, 1);
        mHotMircoClassRecyclerView.setAdapter(mHotMircoClassAdapter);


        mHotMircoClassAdapter.setOnItemClickListener(new BaseQuickAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(BaseQuickAdapter adapter, View view, int position) {
                Intent intent = new Intent(getActivity(), WeikeUnitActivity.class);
                intent.putExtra("pid", mHotMircoClassAdapter.getData().get(position).getId());
                intent.putExtra("type", mHotMircoClassAdapter.getData().get(position).getTypeId());
                startActivity(intent);
            }
        });

        mRvRecommend.setFocusable(false);
        mRvRecommend.setLayoutManager(new LinearLayoutManager(getActivity()));
        mRecommendAdapter = new IndexRecommendAdapter(null);
        mRvRecommend.setAdapter(mRecommendAdapter);

        mRecommendAdapter.setOnItemClickListener(new BaseQuickAdapter.OnItemClickListener() {

            @Override
            public void onItemClick(BaseQuickAdapter adapter, View view, int position) {
                MobclickAgent.onEvent(getActivity(), "fine_read_click", "精品推荐");
                CourseInfo courseInfo = (CourseInfo) adapter.getItem(position);
                Intent intent = new Intent(getActivity(), NewsDetailActivity.class);
                intent.putExtra("info", courseInfo);
                startActivity(intent);
            }
        });

        RxView.clicks(ivWeike).throttleFirst(200, TimeUnit.MILLISECONDS).subscribe(new Action1<Void>() {
            @Override
            public void call(Void aVoid) {
                MobclickAgent.onEvent(getActivity(), "synchronous_weike", "同步微课");
                Intent intent = new Intent(getActivity(), CourseClassifyActivity.class);
                intent.putExtra("type", 8);
                startActivity(intent);
            }
        });
        RxView.clicks(ivSpoken).throttleFirst(200, TimeUnit.MILLISECONDS).subscribe(new Action1<Void>() {
            @Override
            public void call(Void aVoid) {
                MobclickAgent.onEvent(getActivity(), "spoken_teach", "口语学习");
                Intent intent = new Intent(getActivity(), CourseClassifyActivity.class);
                intent.putExtra("type", 7);
                startActivity(intent);
            }
        });


        if (SPUtils.getInstance().getString("period", "").isEmpty()) {

            SPUtils.getInstance().put("grade", 4);
            SPUtils.getInstance().put("period", "0");
            EnglishApp.get().setHttpDefaultParams();
//            mContextScrollView.post(new Runnable() {
//                @Override
//                public void run() {
//                    SelectGradePopupWindow selectGradePopupWindow = new SelectGradePopupWindow(getActivity());
//                    selectGradePopupWindow.show(mContextScrollView, Gravity.CENTER);
//                }
//            });

        }
        if (isShowDialog()) {

            IndexVipKidDialog indexVipKidDialog = new IndexVipKidDialog(getActivity());
            indexVipKidDialog.show();
        }
    }

    //1.当天是否弹出过
    //2.当天时间过后替换成最新时间
    private boolean isShowDialog() {
        boolean isShow = false;
        int days = SPUtils.getInstance().getInt(GroupConstant.EVERY_DAY_DIALOG, 0);//保存的时间
        LogUtil.msg("days:  " + days);
//        Date date = new Date();
        int day = Calendar.getInstance().get(Calendar.DAY_OF_YEAR);
        if (days < day) {
            isShow = true;
            SPUtils.getInstance().put(GroupConstant.EVERY_DAY_DIALOG, day);
        }

        return isShow;


    }

    private SlideInfo advInfo;//广告页

    private void switchSmallProcedure(String strs, String appId) {
        IWXAPI api = WXAPIFactory.createWXAPI(getActivity(), appId);

        WXLaunchMiniProgram.Req req = new WXLaunchMiniProgram.Req();
        req.userName = strs; // 填小程序原始id
//                    req.path = path;                  //拉起小程序页面的可带参路径，不填默认拉起小程序首页
        req.miniprogramType = WXLaunchMiniProgram.Req.MINIPTOGRAM_TYPE_RELEASE;// 可选打开 开发版，体验版和正式版
        api.sendReq(req);
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
                    MobclickAgent.onEvent(getActivity(), "toady_hot_click", "今日热点");
                    Intent intent = new Intent(getActivity(), NewsDetailActivity.class);
                    intent.putExtra("info", indexInfo.getRedian().get(0));
                    startActivity(intent);
                }
            });

        }

        if (indexInfo.getWeike() != null) {
            mHotMircoClassAdapter.addData(indexInfo.getWeike());
        }
        if (indexInfo.getTuijian() != null) {
            List<CourseInfo> tuijians = indexInfo.getTuijian();
            if (tuijians.size() > 5) {
                tuijians = indexInfo.getTuijian().subList(0, 5);
            }
            mRecommendAdapter.setNewData(tuijians);
        }
        if (indexInfo.getAdvInfo() != null && indexInfo.getAdvInfo().size() > 0) {
            SlideInfo slideInfo = indexInfo.getAdvInfo().get(0);
            advInfo = slideInfo;
            GlideHelper.imageView(getContext(), mExamImageView, slideInfo.getImg(), R.mipmap.xiaoxueyinbbiao_ad);
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
