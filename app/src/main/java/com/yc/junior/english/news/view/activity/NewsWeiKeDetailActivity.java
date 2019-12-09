package com.yc.junior.english.news.view.activity;

import android.content.Intent;
import android.content.res.Configuration;
import android.graphics.Paint;
import android.os.Build;
import android.os.Bundle;
import android.util.Log;
import android.view.KeyEvent;
import android.view.View;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;
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
import com.umeng.analytics.MobclickAgent;
import com.yc.junior.english.R;
import com.yc.junior.english.base.view.FullScreenActivity;
import com.yc.junior.english.base.view.SharePopupWindow;
import com.yc.junior.english.base.view.StateView;
import com.yc.junior.english.group.activitys.GroupPictureDetailActivity;
import com.yc.junior.english.main.hepler.UserInfoHelper;
import com.yc.junior.english.main.model.domain.Constant;
import com.yc.junior.english.main.model.domain.UserInfo;
import com.yc.junior.english.news.bean.CourseInfoWrapper;
import com.yc.junior.english.news.contract.NewsDetailContract;
import com.yc.junior.english.news.presenter.NewsDetailPresenter;
import com.yc.junior.english.news.utils.ViewUtil;
import com.yc.junior.english.vip.model.bean.GoodsType;
import com.yc.junior.english.vip.utils.VipDialogHelper;
import com.yc.junior.english.vip.utils.VipInfoHelper;
import com.yc.junior.english.vip.views.fragments.BasePayItemView;
import com.yc.junior.english.weixin.model.domain.CourseInfo;

import java.util.ArrayList;
import java.util.concurrent.TimeUnit;

import androidx.core.content.ContextCompat;
import butterknife.BindView;
import yc.com.blankj.utilcode.util.LogUtils;
import yc.com.blankj.utilcode.util.NetworkUtils;
import yc.com.blankj.utilcode.util.SizeUtils;

import static com.jarvanmo.exoplayerview.orientation.OnOrientationChangedListener.SENSOR_LANDSCAPE;
import static com.jarvanmo.exoplayerview.orientation.OnOrientationChangedListener.SENSOR_PORTRAIT;


/**
 * Created by wanglin  on 2017/9/6 08:32.
 */

public class NewsWeiKeDetailActivity extends FullScreenActivity<NewsDetailPresenter> implements NewsDetailContract.View {

    private static final String TAG = "NewsDetailActivity";

    @BindView(R.id.stateView)
    StateView stateView;

    @BindView(R.id.webView)
    WebView webView;

    @BindView(R.id.ll_rootView)
    LinearLayout llRootView;

    @BindView(R.id.tv_title)
    TextView mTextViewTitle;

    @BindView(R.id.layout_buy_now)
    LinearLayout mBuyNowLayout;

    @BindView(R.id.tv_learn_count)
    TextView mLearnCountTextView;

    @BindView(R.id.tv_now_price)
    TextView mNowPriceTextView;

    @BindView(R.id.tv_old_price)
    TextView mOldPriceTextView;

    @BindView(R.id.nestedScrollView)
    ScrollView scrollView;

    @BindView(R.id.view_synchronization_teach)
    View mSynchronizationTeachView;
    @BindView(R.id.baseItemView_textbook_read)
    BasePayItemView baseItemViewTextbookRead;
    @BindView(R.id.baseItemView_word_valuable)
    BasePayItemView baseItemViewWordValuable;
    @BindView(R.id.baseItemView_brainpower_appraisal)
    BasePayItemView baseItemViewBrainpowerAppraisal;
    @BindView(R.id.baseItemView_score_tutorship)
    BasePayItemView baseItemViewScoreTutorship;
    @BindView(R.id.exoVideoView)
    ExoVideoView videoView;
    @BindView(R.id.rl_container)
    RelativeLayout rlContainer;


    private String title;


    private String id;

    private long startTime;

    private CourseInfo currentCourseInfo;

    private UserInfo userInfo;

    @Override
    public int getLayoutId() {
        return R.layout.common_activity_weike_detail;
    }

    @Override
    public void init() {
        mPresenter = new NewsDetailPresenter(this, this);
        mToolbar.setTitle("视频微课学习");
        mToolbar.setMenuTitle(getString(R.string.share));
        mToolbar.showNavigationIcon();
        mToolbar.setMenuTitleColor(R.color.black_333333);
        mOldPriceTextView.getPaint().setFlags(Paint.STRIKE_THRU_TEXT_FLAG);

        startTime = System.currentTimeMillis();

        if (getIntent() != null) {
            id = getIntent().getStringExtra("id");
        }
        userInfo = UserInfoHelper.getUserInfo();
        mPresenter.getWeiKeDetail(id, userInfo != null ? userInfo.getUid() : "");
        initListener();
//        mSensorManager = (SensorManager) getSystemService(SENSOR_SERVICE);
//        mSensorEventListener = new XinQuVideoPlayer.XinQuAutoFullscreenListener();


    }


    private void initData(CourseInfo courseInfo) {

        if (courseInfo != null) {
            currentCourseInfo = courseInfo;

            title = courseInfo.getTitle();
            mTextViewTitle.setText(title);

            String url = courseInfo.getUrl();

            playVideo(url, courseInfo.getImg());

            mLearnCountTextView.setText(courseInfo.getUserNum());
            if (VipInfoHelper.getGoodInfoList() != null) {
                if (VipInfoHelper.getGoodInfoList().size() > 0) {
                    mNowPriceTextView.setText("会员 ¥" + VipInfoHelper.getGoodInfoList().get(0).getVip_price());
                    mOldPriceTextView.setText("会员 原价:¥" + VipInfoHelper.getGoodInfoList().get(0).getPrice());
                }
            }


        }
    }

    private void initListener() {
        mToolbar.setOnItemClickLisener(() -> {
            SharePopupWindow sharePopupWindow = new SharePopupWindow(NewsWeiKeDetailActivity.this);
            sharePopupWindow.show(llRootView);
        });

        RxView.clicks(mBuyNowLayout).throttleFirst(200, TimeUnit.MILLISECONDS).subscribe(aVoid -> {
            if (currentCourseInfo != null) {
                if (UserInfoHelper.getUserInfo() != null) {
                    currentCourseInfo.setUserId(UserInfoHelper.getUserInfo().getUid());
                    showBuyDialog();
                } else {
                    UserInfoHelper.isGotoLogin(NewsWeiKeDetailActivity.this);
                }
            }
        });

        ViewUtil.switchActivity(NewsWeiKeDetailActivity.this, baseItemViewTextbookRead, 0);
        ViewUtil.switchActivity(NewsWeiKeDetailActivity.this, baseItemViewWordValuable, 1);
        ViewUtil.switchActivity(NewsWeiKeDetailActivity.this, baseItemViewBrainpowerAppraisal, 2);
        ViewUtil.switchActivity(NewsWeiKeDetailActivity.this, baseItemViewScoreTutorship, 3);
    }


    private void initWebView(final CourseInfoWrapper data) {

        webView.setBackgroundColor(ContextCompat.getColor(this, android.R.color.transparent));
        final WebSettings webSettings = webView.getSettings();
        webSettings.setJavaScriptEnabled(true);

        //设置自适应屏幕，两者合用
        webSettings.setUseWideViewPort(true); //将图片调整到适合webview的大小
        webSettings.setLoadWithOverviewMode(true); // 缩放至屏幕的大小

        webView.addJavascriptInterface(new JavascriptInterface(), "HTML");

        //其他细节操作
        webSettings.setCacheMode(WebSettings.LOAD_CACHE_ELSE_NETWORK); //关闭webview中缓存 //优先使用缓存:
        webSettings.setAllowFileAccess(true); //设置可以访问文件
        webSettings.setJavaScriptCanOpenWindowsAutomatically(true); //支持通过JS打开新窗口
        webSettings.setLoadsImagesAutomatically(true); //支持自动加载图片
        webSettings.setDefaultTextEncodingName("utf-8");//设置编码格式
        webSettings.setBlockNetworkImage(true);//设置是否加载网络图片 true 为不加载 false 为加载

        String body = data.getInfo().getBody();
        webView.loadDataWithBaseURL(null, body, "text/html", "utf-8", null);

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
     * @param url
     */
    private void playVideo(String url, String imgUrl) {
//        LogUtil.msg("url: " + url + "   imgUrl: " + imgUrl);
        Glide.with(this).load(imgUrl).into(videoView.artworkView);
        SimpleMediaSource mediaSource = new SimpleMediaSource(url);//uri also supported


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
        videoView.setGestureEnabled(isPlay);
        videoView.play(mediaSource, isPlay);
        initVideoView(isPlay);

    }

    private void initVideoView(boolean isPlay) {
        videoView.setPortrait(getResources().getConfiguration().orientation == Configuration.ORIENTATION_PORTRAIT);
        videoView.setBackListener((view, isPortrait) -> {
            if (isPortrait) {
                finish();
            }
            return false;
        });
        if (isPlay) {
            videoView.setOrientationListener(orientation -> {
                if (orientation == SENSOR_PORTRAIT) {
                    changeToPortrait();
                } else if (orientation == SENSOR_LANDSCAPE) {
                    changeToLandscape();
                }
            });
        }


    }

    @Override
    protected void updateUIToLandscape() {
        super.updateUIToLandscape();
        mToolbar.setVisibility(View.GONE);
        rlContainer.setVisibility(View.GONE);
    }

    @Override
    protected void updateUIToPortrait() {
        super.updateUIToPortrait();

        mToolbar.setVisibility(View.VISIBLE);
        rlContainer.setVisibility(View.VISIBLE);
    }

    private boolean judgeVip() {
        boolean isPlay = false;

        if (UserInfoHelper.isVip(userInfo) || currentCourseInfo.getIsPay() == 1) {
            isPlay = true;
        }
        if (isPlay) {
            mBuyNowLayout.setVisibility(View.GONE);
        } else {
            mBuyNowLayout.setVisibility(View.VISIBLE);
            LinearLayout.LayoutParams layoutParams = (LinearLayout.LayoutParams) mSynchronizationTeachView.getLayoutParams();
            layoutParams.bottomMargin = SizeUtils.dp2px(45);
            mSynchronizationTeachView.setLayoutParams(layoutParams);
        }

        return isPlay;
    }

    @Subscribe(
            thread = EventThread.MAIN_THREAD,
            tags = {
                    @Tag(Constant.COMMUNITY_ACTIVITY_REFRESH)
            }
    )
    public void getInfo(String loginInfo) {
        userInfo = UserInfoHelper.getUserInfo();
        judgeVip();
    }

    @Subscribe(
            thread = EventThread.MAIN_THREAD,
            tags = {
                    @Tag(Constant.PAY_SIGNAL_SUCCESS)
            }
    )
    public void getSignalInfo(String info) {
        mPresenter.getWeiKeDetail(id, userInfo != null ? userInfo.getUid() : "");
        currentCourseInfo.setUserHas(1);
        judgeVip();
    }


    private void click() {

        startOrBuy();

    }

    private void startOrBuy() {
        if (userInfo != null) {

            videoView.startVideo(judgeVip(), v -> showBuyDialog());

        } else {
            UserInfoHelper.isGotoLogin(NewsWeiKeDetailActivity.this);
        }
    }

    @Override
    public void showCourseResult(CourseInfoWrapper data) {
        initWebView(data);
        initData(data.getInfo());
    }

    @Override
    public void hide() {
        stateView.hide();
    }

    @Override
    public void showNoNet() {
        stateView.showNoNet(llRootView, HttpConfig.NET_ERROR, v -> mPresenter.getWeiKeDetail(id, UserInfoHelper.getUserInfo() != null ? UserInfoHelper.getUserInfo().getUid() : ""));
    }

    @Override
    public void showNoData() {
        stateView.showNoData(llRootView);
    }

    @Override
    public void showLoading() {
        stateView.showLoading(llRootView);
    }

    private ArrayList<String> imageList = new ArrayList<>();


    private class JavascriptInterface {

        @android.webkit.JavascriptInterface
        public void openImg(final String imgPath) {
            if (imageList.indexOf(imgPath) == -1) {
                imageList.add(imgPath);
            }
            Log.e(TAG, "openImg: " + imgPath);
            Intent intent = new Intent(NewsWeiKeDetailActivity.this, GroupPictureDetailActivity.class);
            intent.putExtra("mList", imageList);
            intent.putExtra("position", imageList.indexOf(imgPath));
            startActivity(intent);

        }

    }


    @Override
    protected void onResume() {
        super.onResume();

//        Sensor accelerometerSensor = mSensorManager.getDefaultSensor(Sensor.TYPE_ACCELEROMETER);
//        mSensorManager.registerListener(mSensorEventListener, accelerometerSensor, SensorManager.SENSOR_DELAY_NORMAL);
        if ((Build.VERSION.SDK_INT <= 23)) {
            videoView.resume();
        }

    }

    @Override
    protected void onStart() {
        super.onStart();
        if (Build.VERSION.SDK_INT > 23) {
            videoView.resume();
        }
    }

    @Override
    protected void onPause() {
        super.onPause();

        if (Build.VERSION.SDK_INT <= 23) {
            videoView.pause();
        }
    }


    @Override
    public void onStop() {
        super.onStop();
        if (Build.VERSION.SDK_INT > 23) {
            videoView.pause();
        }
    }


    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {

        if (keyCode == KeyEvent.KEYCODE_BACK) {
            return videoView.onKeyDown(keyCode, event);
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
        videoView.releasePlayer();
    }


    private void showBuyDialog() {
        VipDialogHelper.dismissVipDialog();
        Bundle bundle = new Bundle();
        bundle.putParcelable("courseInfo", currentCourseInfo);
        bundle.putInt(GoodsType.GOODS_KEY, GoodsType.TYPE_GENERAL_VIP);
        VipDialogHelper.showVipDialog(getSupportFragmentManager(), null, bundle);
        MobclickAgent.onEvent(this, "weike_video", "视频微课学习");

    }


}
