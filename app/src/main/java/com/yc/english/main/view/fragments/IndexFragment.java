package com.yc.english.main.view.fragments;

import android.Manifest;
import android.content.Intent;
import android.graphics.Color;
import android.os.Build;
import android.support.annotation.NonNull;
import android.support.v4.content.ContextCompat;
import android.support.v4.widget.NestedScrollView;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.ScrollView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.chad.library.adapter.base.BaseQuickAdapter;
import com.hwangjr.rxbus.annotation.Subscribe;
import com.hwangjr.rxbus.annotation.Tag;
import com.hwangjr.rxbus.thread.EventThread;
import com.jakewharton.rxbinding.view.RxView;
import com.kk.utils.LogUtil;
import com.kk.utils.ToastUtil;
import com.qq.e.ads.cfg.MultiProcessFlag;
import com.qq.e.ads.nativ.ADSize;
import com.qq.e.ads.nativ.NativeExpressAD;
import com.qq.e.ads.nativ.NativeExpressADView;
import com.qq.e.comm.pi.AdData;
import com.qq.e.comm.util.AdError;
import com.qq.e.comm.util.GDTLogger;
import com.scwang.smartrefresh.layout.SmartRefreshLayout;
import com.scwang.smartrefresh.layout.api.RefreshLayout;
import com.scwang.smartrefresh.layout.header.ClassicsHeader;
import com.scwang.smartrefresh.layout.listener.OnRefreshListener;
import com.umeng.analytics.MobclickAgent;
import com.yc.english.EnglishApp;
import com.yc.english.R;
import com.yc.english.base.helper.GlideHelper;
import com.yc.english.base.utils.BrandUtils;
import com.yc.english.base.utils.PermissionGroup;
import com.yc.english.base.utils.PermissionManager;
import com.yc.english.base.utils.PermissionUIListener;
import com.yc.english.base.view.MyScrollview;
import com.yc.english.base.view.SharePopupWindow;
import com.yc.english.base.view.StateView;
import com.yc.english.base.view.UserLoginDialog;
import com.yc.english.base.view.WebActivity;
import com.yc.english.composition.activity.CompositionMainActivity;
import com.yc.english.group.constant.GroupConstant;
import com.yc.english.main.contract.IndexContract;
import com.yc.english.main.hepler.BannerImageLoader;
import com.yc.english.main.hepler.UserInfoHelper;
import com.yc.english.main.model.domain.Constant;
import com.yc.english.main.model.domain.IndexInfo;
import com.yc.english.main.model.domain.SlideInfo;
import com.yc.english.main.model.domain.UserInfo;
import com.yc.english.main.presenter.IndexPresenter;
import com.yc.english.main.view.activitys.MainActivity;
import com.yc.english.main.view.adapters.AritleAdapter;
import com.yc.english.news.utils.SmallProcedureUtils;
import com.yc.english.news.view.activity.NewsDetailActivity;
import com.yc.english.read.common.ReadApp;
import com.yc.english.read.view.activitys.BookActivity;
import com.yc.english.speak.view.activity.SpeakMainActivity;
import com.yc.english.speak.view.adapter.IndexRecommendAdapter;
import com.yc.english.vip.views.activity.VipScoreTutorshipActivity;
import com.yc.english.vip.views.fragments.BindPhoneFragment;
import com.yc.english.vip.views.fragments.BindPhoneSuccessFragment;
import com.yc.english.weixin.model.domain.CourseInfo;
import com.yc.english.weixin.views.activitys.CourseActivity;
import com.yc.english.weixin.views.activitys.CourseClassifyActivity;
import com.yc.english.weixin.views.activitys.CourseTypeActivity;
import com.yc.english.weixin.views.activitys.WeikeUnitActivity;
import com.yc.soundmark.study.activity.StudyActivity;
import com.youth.banner.Banner;
import com.youth.banner.listener.OnBannerListener;

import java.security.Permission;
import java.util.Calendar;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.TimeUnit;

import butterknife.BindView;
import rx.functions.Action1;
import yc.com.base.BaseActivity;
import yc.com.base.BaseFragment;
import yc.com.base.EmptyUtils;
import yc.com.base.StatusBarCompat;
import yc.com.blankj.utilcode.util.SPUtils;
import yc.com.tencent_adv.AdvDispatchManager;
import yc.com.tencent_adv.AdvType;
import yc.com.tencent_adv.OnAdvStateListener;


/**
 * Created by zhangkai on 2017/7/24.
 */

public class IndexFragment extends BaseFragment<IndexPresenter> implements IndexContract.View, NativeExpressAD.NativeExpressADListener, OnAdvStateListener, PermissionUIListener {
    @BindView(R.id.sv_content)
    MyScrollview mContextScrollView;

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
    @BindView(R.id.bannerContainer)
    FrameLayout bannerContainer;
    @BindView(R.id.bannerBottomContainer)
    FrameLayout bannerBottomContainer;
    @BindView(R.id.refresh)
    SmartRefreshLayout mRefreshSwipeRefreshLayout;
    @BindView(R.id.iv_topbanner_close)
    ImageView ivTopbannerClose;
    @BindView(R.id.rl_top_banner)
    RelativeLayout rlTopBanner;
    @BindView(R.id.iv_bottombanner_close)
    ImageView ivBottombannerClose;
    @BindView(R.id.rl_bottom_banner)
    RelativeLayout rlBottomBanner;

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


    private SlideInfo dialogInfo;

    public static final int AD_COUNT = 1;// 加载广告的条数，取值范围为[1, 10]
    public static int FIRST_AD_POSITION = 2; // 第一条广告的位置
    private List<NativeExpressADView> mAdViewList;
    private HashMap<NativeExpressADView, Integer> mAdViewPositionMap = new HashMap<>();
    public static final String TAG = "IndexFragment";

    //TODO 添加TODO标识的都是隐藏的

    @Override
    public void init() {

        PermissionManager.getInstance().addPermissions(getActivity(), this,
                new String[]{Manifest.permission.READ_PHONE_STATE}, PermissionGroup.getPermissionGroup(PermissionGroup.GroupType.STORAGE_GROUP),
                PermissionGroup.getPermissionGroup(PermissionGroup.GroupType.MICROPHONE_GROUP),
                new String[]{Manifest.permission.CAMERA});


        if (BrandUtils.isRelatedBrand() || UserInfoHelper.isVip(UserInfoHelper.getUserInfo())) {
            rlTopBanner.setVisibility(View.GONE);
            rlBottomBanner.setVisibility(View.GONE);
        } else {

            AdvDispatchManager.getManager().init(getActivity(), AdvType.BANNER, bannerBottomContainer, null, Constant.TENCENT_ADV_ID, Constant.BANNER_ADV2, this);

            initNativeExpressAD();
        }

        StatusBarCompat.compat((BaseActivity) getActivity(), mToolbarWarpper, mToolBar, mStatusBar);
        mPresenter = new IndexPresenter(getActivity(), this);

        //最上面今日热点
        RxView.clicks(mMoreTextView).throttleFirst(200, TimeUnit.MILLISECONDS).subscribe(aVoid -> {
            Intent intent = new Intent(getActivity(), CourseActivity.class);
            intent.putExtra("title", "今日热点");
            intent.putExtra("type", "3");
            startActivity(intent);
        });

        //课本点读入口
        RxView.clicks(mReadImageView).throttleFirst(200, TimeUnit.MILLISECONDS).subscribe(aVoid -> {
            MobclickAgent.onEvent(getActivity(), "book_read", "教材点读");
            ReadApp.READ_COMMON_TYPE = 1;
            Intent intent = new Intent(getActivity(), BookActivity.class);
            intent.putExtra("tag", "read");
            startActivity(intent);
        });
        //单词宝典
        RxView.clicks(mWordImageView).throttleFirst(200, TimeUnit.MILLISECONDS).subscribe(aVoid -> {
            MobclickAgent.onEvent(getActivity(), "word_books", "单词宝典");
            ReadApp.READ_COMMON_TYPE = 2;
            Intent intent = new Intent(getActivity(), BookActivity.class);
            intent.putExtra("tag", "word");
            startActivity(intent);
        });

        //todo 已隐藏 最下边广告位
        RxView.clicks(mAd).throttleFirst(200, TimeUnit.MILLISECONDS).subscribe(aVoid -> {
            Intent intent = new Intent(getActivity(), VipScoreTutorshipActivity.class);

            startActivity(intent);
        });

        //跳转到配音听力
        RxView.clicks(mTaskImageView).throttleFirst(200, TimeUnit.MILLISECONDS).subscribe(aVoid -> {
            MobclickAgent.onEvent(getActivity(), "listen_and_speak", "配音听力");
            Intent intent = new Intent(getActivity(), SpeakMainActivity.class);
            startActivity(intent);

        });

        //跳转到我的 个人中心
        RxView.clicks(mAvatarImageView).throttleFirst(200, TimeUnit.MILLISECONDS).subscribe(aVoid -> {
            MainActivity mainActivity = (MainActivity) getActivity();
            mainActivity.goToMy();
        });

        //分享
        RxView.clicks(mShareLinearLayout).throttleFirst(200, TimeUnit.MILLISECONDS).subscribe(aVoid -> {
            SharePopupWindow sharePopupWindow = new SharePopupWindow(getActivity());
            sharePopupWindow.show(rootView);
        });

        //学习工具
        //音标学习小助手
        RxView.clicks(mTeacherTask).throttleFirst(200, TimeUnit.MILLISECONDS).subscribe(aVoid -> {
            MobclickAgent.onEvent(getActivity(), "yinbiao_xiaozhushou", "音标学习小助手");
            Intent intent = new Intent(getActivity(), CompositionMainActivity.class);
            startActivity(intent);


        });
        //51答案小程序
        RxView.clicks(mHomeworkAnswer).throttleFirst(200, TimeUnit.MILLISECONDS).subscribe(aVoid -> {
            MobclickAgent.onEvent(getActivity(), "teacher_answer_in", "教材答案入口点击");
//                Intent intent = new Intent(getActivity(), CourseActivity.class);
//                intent.putExtra("title", "教材答案");
//                intent.putExtra("type", "17");
//                startActivity(intent);
            SmallProcedureUtils.switchSmallProcedure(getActivity(), GroupConstant.originid, GroupConstant.appid);
        });
        //todo 下面广告位
        RxView.clicks(mExamImageView).throttleFirst(200, TimeUnit.MILLISECONDS).subscribe(aVoid -> {
            if (advInfo == null) return;
//                MobclickAgent.onEvent(getActivity(), advInfo.getStatistics());
//                Intent intent = new Intent(Intent.ACTION_VIEW);
//                intent.setData(Uri.parse(advInfo.getTypeValue()));
//                startActivity(intent);

            Intent intent = new Intent(getActivity(), WebActivity.class);
            intent.putExtra("title", advInfo.getTitle());
            intent.putExtra("url", advInfo.getTypeValue());
            startActivity(intent);
        });

        //音标点读
        RxView.clicks(mSpeakImageView).throttleFirst(200, TimeUnit.MILLISECONDS).subscribe(aVoid -> {
            MobclickAgent.onEvent(getActivity(), "photograp_read", "音标点读");
            // todo 这里是在线作业 融云im模块

            Intent intent = new Intent(getActivity(), StudyActivity.class);
            startActivity(intent);


        });

        //外接或自身广告
        RxView.clicks(mMorcoclassMoreLinearLayout).throttleFirst(200, TimeUnit.MILLISECONDS).subscribe(aVoid -> {
            MainActivity mainActivity = (MainActivity) getActivity();
            mainActivity.goToTask();
        });
        //精品推荐更多
        RxView.clicks(mllRecommendMore).throttleFirst(200, TimeUnit.MILLISECONDS).subscribe(aVoid -> {
            Intent intent = new Intent(getActivity(), CourseTypeActivity.class);
            startActivity(intent);
        });
        mBanner.setFocusable(false);
        mBanner.setOnBannerListener(position -> {
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
//                        Class clazz = Class.forName(split[0]);
                    Class clazz = Class.forName(getActivity().getPackageName() + split[0]);

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
//                        LogUtil.msg("tag: " + strs[0] + "---" + strs[1]);
                    if (strs.length > 1) {
                        // 填应用AppId
                        String appId = strs[1];
                        String originId = strs[0];
                        SmallProcedureUtils.switchSmallProcedure(getActivity(), originId, appId);
                    }
                } catch (Exception e) {
                    LogUtil.msg("e :" + e.getMessage());
                    ToastUtil.toast(getActivity(), "");
                }
            }
        });

        mHotMircoClassRecyclerView.setFocusable(false);
        mHotMircoClassRecyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));
        mHotMircoClassAdapter = new AritleAdapter(null, 1);
        mHotMircoClassRecyclerView.setAdapter(mHotMircoClassAdapter);

        //TODO 热门推荐条目点击
        mHotMircoClassAdapter.setOnItemClickListener((adapter, view, position) -> {
            Intent intent = new Intent(getActivity(), WeikeUnitActivity.class);
            intent.putExtra("pid", mHotMircoClassAdapter.getData().get(position).getId());
            intent.putExtra("type", mHotMircoClassAdapter.getData().get(position).getTypeId());
            startActivity(intent);
        });

        mRvRecommend.setFocusable(false);
        mRvRecommend.setLayoutManager(new LinearLayoutManager(getActivity()));
        mRvRecommend.setHasFixedSize(true);
        mRecommendAdapter = new IndexRecommendAdapter(getActivity(), null, mAdViewPositionMap);
        mRvRecommend.setAdapter(mRecommendAdapter);

        //精品推荐条目点击
        mRecommendAdapter.setOnItemClickListener((view, position) -> {
            MobclickAgent.onEvent(getActivity(), "fine_read_click", "精品推荐");
            CourseInfo courseInfo = mRecommendAdapter.getItem(position);
            Intent intent = new Intent(getActivity(), NewsDetailActivity.class);
            intent.putExtra("info", courseInfo);
            startActivity(intent);
            mPresenter.statisticsNewsCount(courseInfo.getId());
        });

        //同步微课点击
        RxView.clicks(ivWeike).throttleFirst(200, TimeUnit.MILLISECONDS).subscribe(aVoid -> {
            MobclickAgent.onEvent(getActivity(), "synchronous_weike", "同步微课");
            Intent intent = new Intent(getActivity(), CourseClassifyActivity.class);
            intent.putExtra("type", 8);
            intent.putExtra("cate", 1);
            startActivity(intent);
        });
        //口语学习点击
        RxView.clicks(ivSpoken).throttleFirst(200, TimeUnit.MILLISECONDS).subscribe(aVoid -> {
            MobclickAgent.onEvent(getActivity(), "spoken_teach", "口语学习");
            Intent intent = new Intent(getActivity(), CourseClassifyActivity.class);
            intent.putExtra("type", 7);
            intent.putExtra("cate", 2);
            startActivity(intent);
        });

        RxView.clicks(ivTopbannerClose).throttleFirst(200, TimeUnit.MILLISECONDS).subscribe(aVoid -> rlTopBanner.setVisibility(View.GONE));

        RxView.clicks(ivBottombannerClose).throttleFirst(200, TimeUnit.MILLISECONDS).subscribe(aVoid -> rlBottomBanner.setVisibility(View.GONE));


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
//        if (isShowDialog()) {
//
//            IndexVipKidDialog indexVipKidDialog = new IndexVipKidDialog(getActivity(), getDialogInfo());
//            indexVipKidDialog.show();
//        }



        initRefresh();

//


    }


    private void initRefresh() {
        //设置 Header 为 贝塞尔雷达 样式
        mRefreshSwipeRefreshLayout.setRefreshHeader(new ClassicsHeader(getActivity()));
        mRefreshSwipeRefreshLayout.setPrimaryColorsId(R.color.primaryDark);
        mRefreshSwipeRefreshLayout.setEnableLoadMore(false);
        mRefreshSwipeRefreshLayout.autoRefresh();
        mRefreshSwipeRefreshLayout.setOnRefreshListener(refreshLayout -> mPresenter.getIndexInfo(true));
    }

    //1.当天是否弹出过
    //2.当天时间过后替换成最新时间
    private boolean isShowDialog() {
        boolean isShow = false;
        int days = SPUtils.getInstance().getInt(GroupConstant.EVERY_DAY_DIALOG, 0);//保存的时间

        int day = Calendar.getInstance().get(Calendar.DAY_OF_YEAR);
        if (days < day) {
            isShow = true;
            SPUtils.getInstance().put(GroupConstant.EVERY_DAY_DIALOG, day);
        }

        return isShow;


    }

    private SlideInfo advInfo;//广告页


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
    public void hide() {
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

    private List<CourseInfo> tuijians;

    @Override
    public void showInfo(final IndexInfo indexInfo, boolean isFresh) {
        if (indexInfo.getRedian() != null && indexInfo.getRedian().size() > 0) {
            mHotTitleTextView.setText(indexInfo.getRedian().get(0).getTitle());
            RxView.clicks(mHotTitleTextView).throttleFirst(200, TimeUnit.MILLISECONDS).subscribe(aVoid -> {
                MobclickAgent.onEvent(getActivity(), "toady_hot_click", "今日热点");
                Intent intent = new Intent(getActivity(), NewsDetailActivity.class);
                intent.putExtra("info", indexInfo.getRedian().get(0));
                startActivity(intent);
            });

        }

        if (indexInfo.getWeike() != null) {
            mHotMircoClassAdapter.addData(indexInfo.getWeike());
        }
        if (indexInfo.getTuijian() != null) {
            tuijians = indexInfo.getTuijian();
            if (tuijians.size() > 4) {
                tuijians = indexInfo.getTuijian().subList(0, 4);
            }
            mRecommendAdapter.addNewData(tuijians);

            if (isFresh) {
                onADLoaded(mAdViewList);
            }
        }
        if (indexInfo.getAdvInfo() != null && indexInfo.getAdvInfo().size() > 0) {
            SlideInfo slideInfo = indexInfo.getAdvInfo().get(0);
            advInfo = slideInfo;
            GlideHelper.imageView(getContext(), mExamImageView, slideInfo.getImg(), R.mipmap.xiaoxueyinbbiao_ad);
        }

        mRefreshSwipeRefreshLayout.finishRefresh();
    }

    private void initNativeExpressAD() {//com.qq.e.ads.nativ.ADSize.FULL_WIDTH
        MultiProcessFlag.setMultiProcess(true);
        ADSize adSize = new ADSize(ADSize.FULL_WIDTH, ADSize.AUTO_HEIGHT); // 消息流中用AUTO_HEIGHT // 消息流中用AUTO_HEIGHT
        NativeExpressAD mADManager = new NativeExpressAD(getActivity(), adSize, Constant.TENCENT_ADV_ID, Constant.NATIVE_ADV_ID, this);
        mADManager.loadAD(AD_COUNT);
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


        mRefreshSwipeRefreshLayout.finishRefresh();

        mLoadingStateView.showNoNet(mContextScrollView, "网络不给力", v -> mPresenter.loadData(true));
    }

    @Override
    public void showNoData() {

        mRefreshSwipeRefreshLayout.finishRefresh();
        mLoadingStateView.showNoData(mContextScrollView);

    }


    public void setDialogInfo(SlideInfo dialogInfo) {
        this.dialogInfo = dialogInfo;
    }

    public SlideInfo getDialogInfo() {
        return dialogInfo;
    }


    @Override
    public void onNoAD(AdError adError) {
        Log.i(TAG,
                String.format("onNoAD, error code: %d, error msg: %s", adError.getErrorCode(),
                        adError.getErrorMsg()));
    }

    private NativeExpressADView view;

    //去掉广告
    @Override
    public void onADLoaded(List<NativeExpressADView> adList) {
        if (adList != null && adList.size() > 0) {
            mAdViewList = adList;
//            if (view != null) {
//                view.destroy();
//            }
            int position = FIRST_AD_POSITION;
            view = mAdViewList.get(0);
            GDTLogger.i("ad load[" + 0 + "]: " + getAdInfo(view));
            mAdViewPositionMap.put(view, FIRST_AD_POSITION);
            if (!(BrandUtils.isRelatedBrand() || UserInfoHelper.isVip(UserInfoHelper.getUserInfo())))
                mRecommendAdapter.addADViewToPosition(position, mAdViewList.get(0));
        }
    }

    private String getAdInfo(NativeExpressADView nativeExpressADView) {
        AdData adData = nativeExpressADView.getBoundData();
        if (adData != null) {
            StringBuilder infoBuilder = new StringBuilder();
            infoBuilder.append("title:").append(adData.getTitle()).append(",")
                    .append("desc:").append(adData.getDesc()).append(",")
                    .append("patternType:").append(adData.getAdPatternType());
            return infoBuilder.toString();
        }
        return null;
    }

    @Override
    public void onRenderFail(NativeExpressADView nativeExpressADView) {

    }

    @Override
    public void onRenderSuccess(NativeExpressADView nativeExpressADView) {

    }

    @Override
    public void onADExposure(NativeExpressADView nativeExpressADView) {

    }

    @Override
    public void onADClicked(NativeExpressADView nativeExpressADView) {

    }

    @Override
    public void onADClosed(NativeExpressADView adView) {
        if (mRecommendAdapter != null) {
            int removedPosition = mAdViewPositionMap.get(adView);
            mRecommendAdapter.removeADView(removedPosition, adView);
        }
    }

    @Override
    public void onADLeftApplication(NativeExpressADView nativeExpressADView) {

    }

    @Override
    public void onADOpenOverlay(NativeExpressADView nativeExpressADView) {

    }

    @Override
    public void onADCloseOverlay(NativeExpressADView nativeExpressADView) {

    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        if (view != null) view.destroy();
//        if (banner != null) banner.destroy();
    }

    @Override
    public void onShow() {
//        ivTopbannerClose.setVisibility(View.VISIBLE);
        if (!(BrandUtils.isRelatedBrand() || UserInfoHelper.isVip(UserInfoHelper.getUserInfo()))) {
            ivBottombannerClose.setVisibility(View.VISIBLE);
        }
    }

    @Override
    public void onDismiss(long delayTime) {

    }

    @Override
    public void onError() {

    }

    @Override
    public void onNativeExpressDismiss(NativeExpressADView view) {

    }

    @Override
    public void onNativeExpressShow(Map<NativeExpressADView, Integer> mDatas) {

    }

    @Subscribe(
            thread = EventThread.MAIN_THREAD,
            tags = {
                    @Tag(Constant.COMMUNITY_ACTIVITY_REFRESH)
            }
    )
    public void paySuccess(String info) {
        rlBottomBanner.setVisibility(View.GONE);
        try {
            if (mRecommendAdapter != null) {
                int removedPosition = mAdViewPositionMap.get(view);
                mRecommendAdapter.removeADView(removedPosition, view);
            }
        } catch (Exception e) {
            LogUtil.msg("e:  " + e.getMessage());
        }


    }

    @Override
    public void onPermissionGranted() {

    }

    @Override
    public void onPermissionDenyed() {

    }
}
