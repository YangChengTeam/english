package com.yc.soundmark.category.activity;

import android.content.res.Configuration;
import android.graphics.Paint;
import android.hardware.SensorManager;
import android.os.Build;
import android.support.v4.content.ContextCompat;
import android.support.v7.widget.Toolbar;
import android.view.KeyEvent;
import android.view.View;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;
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
import com.jarvanmo.exoplayerview.media.SimpleMediaSource;
import com.jarvanmo.exoplayerview.ui.ExoVideoView;
import com.kk.securityhttp.net.contains.HttpConfig;

import com.yc.junior.english.R;
import com.yc.junior.english.base.view.StateView;
import com.yc.junior.english.main.hepler.UserInfoHelper;
import com.yc.junior.english.main.model.domain.Constant;
import com.yc.junior.english.main.model.domain.UserInfo;
import com.yc.junior.english.vip.utils.VipDialogHelper;
import com.yc.soundmark.category.contract.WeiKeDetailContract;
import com.yc.soundmark.category.model.domain.CourseInfo;
import com.yc.soundmark.category.presenter.WeiKeDetailPresenter;

import java.util.concurrent.TimeUnit;

import butterknife.BindView;
import rx.functions.Action1;
import yc.com.base.BaseActivity;
import yc.com.blankj.utilcode.util.LogUtils;
import yc.com.blankj.utilcode.util.NetworkUtils;
import yc.com.blankj.utilcode.util.SizeUtils;

import static com.jarvanmo.exoplayerview.orientation.OnOrientationChangedListener.SENSOR_LANDSCAPE;
import static com.jarvanmo.exoplayerview.orientation.OnOrientationChangedListener.SENSOR_PORTRAIT;







/**
 * Created by wanglin  on 2017/9/6 08:32.
 */

public class WeiKeDetailActivity extends BaseActivity<WeiKeDetailPresenter> implements WeiKeDetailContract.View {

    private static final String TAG = "NewsDetailActivity";
    @BindView(R.id.iv_back)
    ImageView ivBack;
    @BindView(R.id.tv_share)
    TextView tvShare;
    @BindView(R.id.exoVideoView)
    ExoVideoView exoVideoView;

    @BindView(R.id.tv_learn_count)
    TextView mLearnCountTextView;
    @BindView(R.id.layout_learn_count)
    LinearLayout layoutLearnCount;

    @BindView(R.id.tv_title)
    TextView mTextViewTitle;
    @BindView(R.id.tv_now_price)
    TextView mNowPriceTextView;
    @BindView(R.id.tv_old_price)
    TextView mOldPriceTextView;
    @BindView(R.id.layout_title_price)
    LinearLayout layoutTitlePrice;
    @BindView(R.id.layout_content)
    LinearLayout layoutContent;
    @BindView(R.id.webView)
    WebView webView;
    @BindView(R.id.nestedScrollView)
    ScrollView nestedScrollView;
    @BindView(R.id.layout_buy_now)
    LinearLayout mBuyNowLayout;
    @BindView(R.id.rl_container)
    RelativeLayout rlContainer;
    @BindView(R.id.ll_rootView)
    LinearLayout llRootView;
    @BindView(R.id.stateView)
    StateView stateView;
    @BindView(R.id.toolBar)
    Toolbar toolBar;


    private String title;


    private String id;

    private long startTime;

    private CourseInfo currentCourseInfo;

    private UserInfo userInfo;
    private SensorManager mSensorManager;

    @Override
    public int getLayoutId() {
        return R.layout.common_activity_weike_detail_soundmark;
    }

    @Override
    public void init() {

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


    }


    @Override
    public void showLoading() {
        stateView.showLoading(llRootView);
    }

    @Override
    public void showNoData() {
        stateView.showNoData(llRootView);
    }

    @Override
    public void showNoNet() {
        stateView.showNoNet(llRootView, HttpConfig.NET_ERROR, v -> mPresenter.getWeikeCategoryInfo(id));
    }

    @Override
    public void hide() {
        stateView.hide();
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

        RxView.clicks(ivBack).throttleFirst(200, TimeUnit.MILLISECONDS).subscribe(aVoid -> finish());

        RxView.clicks(tvShare).throttleFirst(200, TimeUnit.MILLISECONDS).subscribe(aVoid -> {
            //分享
        });
    }


    private void initWebView(final CourseInfo data) {

        webView.setBackgroundColor(ContextCompat.getColor(this, android.R.color.transparent));
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


    private void initVideoView(boolean isPlay) {
        exoVideoView.setPortrait(getResources().getConfiguration().orientation == Configuration.ORIENTATION_PORTRAIT);
        exoVideoView.setBackListener((view, isPortrait) -> {
            if (isPortrait) {
                finish();
            }
            return false;
        });

        if (isPlay) {
            exoVideoView.setOrientationListener(orientation -> {
                if (orientation == SENSOR_PORTRAIT) {
                    changeToPortrait();
                } else if (orientation == SENSOR_LANDSCAPE) {
                    changeToLandscape();
                }
            });
        }

    }

    @Override
    protected void updateUIToPortrait() {
        super.updateUIToPortrait();
        toolBar.setVisibility(View.VISIBLE);
        rlContainer.setVisibility(View.VISIBLE);
    }

    @Override
    protected void updateUIToLandscape() {
        super.updateUIToLandscape();
        toolBar.setVisibility(View.GONE);
        rlContainer.setVisibility(View.GONE);
    }


    /**
     * 播放视频
     *
     * @param courseInfo
     */
    private void playVideo(CourseInfo courseInfo) {

        Glide.with(this).load(courseInfo.getImg()).into(exoVideoView.artworkView);
        SimpleMediaSource mediaSource = new SimpleMediaSource(courseInfo.getUrl());//uri also supported

        boolean isPlay = false;

        if (judgeVip()) {
            if (NetworkUtils.getNetworkType() == NetworkUtils.NetworkType.NETWORK_WIFI)
                isPlay = true;
            else {
                click();
            }
        } else {
            click();
        }
        exoVideoView.setGestureEnabled(isPlay);
        exoVideoView.play(mediaSource, isPlay);
        initVideoView(isPlay);

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

        startOrBuy();
    }


    private void startOrBuy() {
        if (userInfo != null) {
            exoVideoView.startVideo(judgeVip(), v -> showBuyDialog());
        }
    }


    @Override
    protected void onStart() {
        super.onStart();
        if (Build.VERSION.SDK_INT > 23) {
            exoVideoView.resume();
        }
    }

    @Override
    protected void onResume() {
        super.onResume();
//
//        Sensor accelerometerSensor = mSensorManager.getDefaultSensor(Sensor.TYPE_ACCELEROMETER);
//        mSensorManager.registerListener(mSensorEventListener, accelerometerSensor, SensorManager.SENSOR_DELAY_NORMAL);

        if ((Build.VERSION.SDK_INT <= 23)) {
            exoVideoView.resume();
        }
    }


    @Override
    protected void onPause() {
        super.onPause();

        if (Build.VERSION.SDK_INT <= 23) {
            exoVideoView.pause();
        }
    }

    @Override
    protected void onStop() {
        super.onStop();
        if (Build.VERSION.SDK_INT > 23) {
            exoVideoView.pause();
        }
    }

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {

        if (keyCode == KeyEvent.KEYCODE_BACK) {
            return exoVideoView.onKeyDown(keyCode, event);
        }
        return super.onKeyDown(keyCode, event);
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
        exoVideoView.releasePlayer();
    }


    //显示支付弹窗

    private void showBuyDialog() {
//        BasePayFragment basePayFragment = new BasePayFragment();
//        basePayFragment.show(getSupportFragmentManager(), "");

        VipDialogHelper.showVipDialog(getSupportFragmentManager(), "", null);
    }


}
