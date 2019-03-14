package com.yc.soundmark.category.activity;

import android.graphics.Paint;
import android.hardware.Sensor;
import android.hardware.SensorManager;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.ScrollView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.hwangjr.rxbus.annotation.Subscribe;
import com.hwangjr.rxbus.annotation.Tag;
import com.hwangjr.rxbus.thread.EventThread;
import com.jakewharton.rxbinding.view.RxView;
import com.xinqu.videoplayer.XinQuVideoPlayer;
import com.xinqu.videoplayer.XinQuVideoPlayerStandard;
import com.yc.junior.english.R;
import com.yc.junior.english.main.hepler.UserInfoHelper;
import com.yc.junior.english.main.model.domain.Constant;
import com.yc.junior.english.main.model.domain.UserInfo;
import com.yc.soundmark.base.fragment.BasePayFragment;
import com.yc.soundmark.category.contract.WeiKeDetailContract;
import com.yc.soundmark.category.model.domain.CourseInfo;
import com.yc.soundmark.category.presenter.WeiKeDetailPresenter;

import java.util.concurrent.TimeUnit;

import rx.functions.Action1;
import yc.com.base.BaseActivity;
import yc.com.blankj.utilcode.util.LogUtils;
import yc.com.blankj.utilcode.util.NetworkUtils;
import yc.com.blankj.utilcode.util.SizeUtils;




/**
 * Created by wanglin  on 2017/9/6 08:32.
 */

public class WeiKeDetailActivity extends BaseActivity<WeiKeDetailPresenter> implements WeiKeDetailContract.View {

    private static final String TAG = "NewsDetailActivity";


    private FrameLayout layoutVideo;

    private TextView mLearnCountTextView;

    private LinearLayout layoutLearnCount;

    private View lineView;

    private TextView mTextViewTitle;

    private TextView mNowPriceTextView;

    private TextView mOldPriceTextView;

    private LinearLayout layoutTitlePrice;

    private LinearLayout layoutContent;

    private WebView webView;

    private ScrollView nestedScrollView;
    private LinearLayout mBuyNowLayout;

    private RelativeLayout llRootView;
    private XinQuVideoPlayerStandard mJCVideoPlayer;
    private Toolbar toolBar;
    private ImageView ivBack;

    private TextView tvShare;


    private String title;


    private String id;

    private long startTime;

    private CourseInfo currentCourseInfo;

    private UserInfo userInfo;
    private SensorManager mSensorManager;
    private XinQuVideoPlayer.XinQuAutoFullscreenListener mSensorEventListener;

    @Override
    public int getLayoutId() {
        return R.layout.common_activity_weike_detail_soundmark;
    }

    @Override
    public void init() {
        initView();
        mPresenter = new WeiKeDetailPresenter(this, this);

        mOldPriceTextView.getPaint().setFlags(Paint.STRIKE_THRU_TEXT_FLAG);

        startTime = System.currentTimeMillis();


        if (getIntent() != null) {
            id = getIntent().getStringExtra("pid");
        }
        userInfo = UserInfoHelper.getUserInfo();
        mPresenter.getWeikeCategoryInfo(id);
        initListener();
        mSensorManager = (SensorManager) getSystemService(SENSOR_SERVICE);

        mSensorEventListener = new XinQuVideoPlayer.XinQuAutoFullscreenListener();
        mJCVideoPlayer.widthRatio = 16;
        mJCVideoPlayer.heightRatio = 9;


    }

    private void initView() {
        layoutVideo = findViewById(R.id.layout_video);
        mLearnCountTextView = findViewById(R.id.tv_learn_count);
        layoutLearnCount = findViewById(R.id.layout_learn_count);
        lineView = findViewById(R.id.line_view);
        mTextViewTitle = findViewById(R.id.tv_title);
        mNowPriceTextView = findViewById(R.id.tv_now_price);
        mOldPriceTextView = findViewById(R.id.tv_old_price);
        layoutTitlePrice = findViewById(R.id.layout_title_price);
        layoutContent = findViewById(R.id.layout_content);
        webView = findViewById(R.id.webView);
        nestedScrollView = findViewById(R.id.nestedScrollView);
        mBuyNowLayout = findViewById(R.id.layout_buy_now);
        llRootView = findViewById(R.id.ll_rootView);
        mJCVideoPlayer = findViewById(R.id.mJCVideoPlayer);
        toolBar = findViewById(R.id.toolBar);
        ivBack = findViewById(R.id.iv_back);
        tvShare = findViewById(R.id.tv_share);
    }

    @Override
    public void showLoading() {

    }

    @Override
    public void showNoData() {

    }

    @Override
    public void showNoNet() {

    }

    @Override
    public void hide() {

    }

    @Override
    public void showWeikeInfo(CourseInfo courseInfo) {

        initData(courseInfo);
    }


    private void initData(CourseInfo courseInfo) {

        if (courseInfo != null) {
            currentCourseInfo = courseInfo;

            title = courseInfo.getTitle();
            mTextViewTitle.setText(title);

            playVideo(courseInfo);
            initWebView(courseInfo);
            mNowPriceTextView.setText("微课 ¥" + courseInfo.getVipPrice());
            mOldPriceTextView.setText("微课 原价:¥" + courseInfo.getMPrice());
            mLearnCountTextView.setText(courseInfo.getUserNum());


        }
    }

    @Override
    public boolean isStatusBarMateria() {
        return false;
    }


    private void initListener() {
//        mToolbar.setOnItemClickLisener(new BaseToolBar.OnItemClickLisener() {
//            @Override
//            public void onClick() {
//                SharePopupWindow sharePopupWindow = new SharePopupWindow(NewsWeiKeDetailActivity.this);
//                sharePopupWindow.show(llRootView);
//            }
//        });

        RxView.clicks(mBuyNowLayout).throttleFirst(200, TimeUnit.MILLISECONDS).subscribe(new Action1<Void>() {
            @Override
            public void call(Void aVoid) {
                if (currentCourseInfo != null) {
                    if (UserInfoHelper.getUserInfo() != null) {
                        currentCourseInfo.setUserId(UserInfoHelper.getUserInfo().getUid());
                        showBuyDialog();
                    }
                }
            }
        });

        RxView.clicks(ivBack).throttleFirst(200, TimeUnit.MILLISECONDS).subscribe(new Action1<Void>() {
            @Override
            public void call(Void aVoid) {
                finish();
            }
        });

        RxView.clicks(tvShare).throttleFirst(200, TimeUnit.MILLISECONDS).subscribe(new Action1<Void>() {
            @Override
            public void call(Void aVoid) {
                //分享
            }
        });
    }


    private void initWebView(final CourseInfo data) {

        final WebSettings webSettings = webView.getSettings();
        webSettings.setJavaScriptEnabled(true);

        //设置自适应屏幕，两者合用
        webSettings.setUseWideViewPort(true); //将图片调整到适合webview的大小
        webSettings.setLoadWithOverviewMode(true); // 缩放至屏幕的大小

//        webView.addJavascriptInterface(new JavascriptInterface(), "HTML");

        //其他细节操作
        webSettings.setCacheMode(WebSettings.LOAD_CACHE_ELSE_NETWORK); //关闭webview中缓存 //优先使用缓存:
        webSettings.setAllowFileAccess(true); //设置可以访问文件
        webSettings.setJavaScriptCanOpenWindowsAutomatically(true); //支持通过JS打开新窗口
        webSettings.setLoadsImagesAutomatically(true); //支持自动加载图片
        webSettings.setDefaultTextEncodingName("utf-8");//设置编码格式
        webSettings.setBlockNetworkImage(true);//设置是否加载网络图片 true 为不加载 false 为加载

//        String body = data.getInfo().getBody();
        webView.loadDataWithBaseURL(null, data.getDesp(), "text/html", "utf-8", null);

        webView.setWebViewClient(new WebViewClient() {

            @Override
            public void onPageFinished(WebView view, String url) {
//                view.loadUrl("javascript:window.HTML.getContentHeight(document.getElementsByTagName('html')[0].scrollHeight);");

                llRootView.setVisibility(View.VISIBLE);

                webSettings.setBlockNetworkImage(false);

                LogUtils.e("startTime-->" + (System.currentTimeMillis() - startTime));

                view.loadUrl("javascript:(function(){"
                        + "var imgs=document.getElementsByTagName(\"img\");"
                        + "for(var i=0;i<imgs.length;i++) " + "{"
                        + "  imgs[i].onclick=function() " + "{ "
                        + "    window.HTML.openImg(this.src); "
                        + "   }  " + "}" + "}())");

            }
        });
    }


    /**
     * 播放视频
     *
     * @param courseInfo
     */
    private void playVideo(CourseInfo courseInfo) {
        Glide.with(this).load(courseInfo.getImg()).thumbnail(0.1f).into(mJCVideoPlayer.thumbImageView);

        mJCVideoPlayer.setUp(courseInfo.getUrl(), XinQuVideoPlayer.SCREEN_WINDOW_LIST, false, null == courseInfo.getTitle() ? "" : courseInfo.getTitle());

        mJCVideoPlayer.backButton.setVisibility(View.GONE);
        mJCVideoPlayer.tinyBackImageView.setVisibility(View.GONE);


        if (judgeVip()) {
            if (NetworkUtils.getNetworkType() == NetworkUtils.NetworkType.NETWORK_WIFI)
                mJCVideoPlayer.startVideo();
            else {
                click();
            }
        } else {
            click();
        }


    }


    private boolean judgeVip() {
        boolean isPlay = false;

        if (UserInfoHelper.isVip(UserInfoHelper.getUserInfo()) || currentCourseInfo.getIs_vip() == 0) {
            isPlay = true;
        }

        if (isPlay) {
            mBuyNowLayout.setVisibility(View.GONE);
        } else {
            mBuyNowLayout.setVisibility(View.VISIBLE);
            LinearLayout.LayoutParams layoutParams = (LinearLayout.LayoutParams) webView.getLayoutParams();
            layoutParams.bottomMargin = SizeUtils.dp2px(45);
            webView.setLayoutParams(layoutParams);
        }

        return isPlay;
    }

    @Subscribe(
            thread = EventThread.MAIN_THREAD,
            tags = {
                    @Tag(Constant.COMMUNITY_ACTIVITY_REFRESH)
            }
    )
    public void paySuccess(String info) {
        judgeVip();
    }

    private void click() {
        RxView.clicks(mJCVideoPlayer.startButton).throttleFirst(1000, TimeUnit.MICROSECONDS).subscribe(new Action1<Void>() {
            @Override
            public void call(Void aVoid) {
                startOrBuy();
            }
        });
    }


    private void startOrBuy() {
        if (userInfo != null) {
            if (judgeVip()) {
                mJCVideoPlayer.startVideo();
            } else {
                showBuyDialog();
            }
        }
    }


    @Override
    protected void onResume() {
        super.onResume();

        Sensor accelerometerSensor = mSensorManager.getDefaultSensor(Sensor.TYPE_ACCELEROMETER);
        mSensorManager.registerListener(mSensorEventListener, accelerometerSensor, SensorManager.SENSOR_DELAY_NORMAL);
    }


    @Override
    protected void onPause() {
        super.onPause();

        XinQuVideoPlayerStandard.releaseAllVideos();

        mSensorManager.unregisterListener(mSensorEventListener);
        XinQuVideoPlayerStandard.clearSavedProgress(this, null);
    }


    @Override
    protected void onDestroy() {
        super.onDestroy();
        if (webView != null && llRootView != null) {
            webView.loadDataWithBaseURL(null, "", "text/html", "utf-8", null);
            webView.clearHistory();

            llRootView.removeView(webView);
            webView.destroy();
        }
    }


    @Override
    public void onBackPressed() {
        if (XinQuVideoPlayer.backPress()) {
            return;
        }
        super.onBackPressed();
    }

    //显示支付弹窗

    private void showBuyDialog() {
        BasePayFragment basePayFragment = new BasePayFragment();
        basePayFragment.show(getSupportFragmentManager(), "");
    }


}
