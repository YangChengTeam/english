package com.yc.english.news.view.activity;

import android.content.Intent;
import android.graphics.Typeface;
import android.hardware.Sensor;
import android.hardware.SensorManager;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.FrameLayout;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.blankj.utilcode.util.LogUtils;
import com.blankj.utilcode.util.NetworkUtils;
import com.blankj.utilcode.util.TimeUtils;
import com.bumptech.glide.Glide;
import com.example.comm_recyclviewadapter.BaseItemDecoration;
import com.hwangjr.rxbus.annotation.Subscribe;
import com.hwangjr.rxbus.annotation.Tag;
import com.hwangjr.rxbus.thread.EventThread;
import com.jakewharton.rxbinding.view.RxView;
import com.kk.securityhttp.net.contains.HttpConfig;
import com.yc.english.R;
import com.yc.english.base.utils.StatusBarCompat;
import com.yc.english.base.view.BaseToolBar;
import com.yc.english.base.view.FullScreenActivity;
import com.yc.english.base.view.SharePopupWindow;
import com.yc.english.base.view.StateView;
import com.yc.english.group.view.activitys.GroupPictureDetailActivity;
import com.yc.english.main.hepler.UserInfoHelper;
import com.yc.english.main.model.domain.Constant;
import com.yc.english.main.model.domain.UserInfo;
import com.yc.english.news.adapter.NewsDetailAdapter;
import com.yc.english.news.bean.CourseInfoWrapper;
import com.yc.english.news.contract.NewsDetailContract;
import com.yc.english.news.presenter.NewsDetailPresenter;
import com.yc.english.news.view.widget.MediaPlayerView;
import com.yc.english.news.view.widget.NewsScrollView;
import com.yc.english.vip.model.bean.GoodsType;
import com.yc.english.vip.utils.VipDialogHelper;
import com.yc.english.weixin.model.domain.CourseInfo;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Locale;
import java.util.concurrent.TimeUnit;

import butterknife.BindView;
import cn.jzvd.JZVideoPlayer;
import cn.jzvd.JZVideoPlayerStandard;
import rx.functions.Action1;

/**
 * Created by wanglin  on 2017/9/6 08:32.
 */

public class NewsDetailActivity extends FullScreenActivity<NewsDetailPresenter> implements NewsDetailContract.View {
    private static final String TAG = "NewsDetailActivity";
    @BindView(R.id.mJCVideoPlayer)
    JZVideoPlayerStandard mJCVideoPlayer;
    @BindView(R.id.webView)
    WebView webView;
    @BindView(R.id.mLinearLayoutMore)
    LinearLayout mLinearLayoutMore;
    @BindView(R.id.mRecyclerView)
    RecyclerView mRecyclerView;
    @BindView(R.id.ll_rootView)
    LinearLayout llRootView;

    @BindView(R.id.m_ll_recommend)
    LinearLayout mLlRecommend;
    @BindView(R.id.mMediaPlayerView)
    MediaPlayerView mMediaPlayerView;
    @BindView(R.id.mTextViewTitle)
    TextView mTextViewTitle;
    @BindView(R.id.mTextViewTime)
    TextView mTextViewTime;
    @BindView(R.id.nestedScrollView)
    NewsScrollView nestedScrollView;
    @BindView(R.id.stateView)
    StateView stateView;
    @BindView(R.id.mTextViewFrom)
    TextView mTextViewFrom;
    @BindView(R.id.fl_player)
    FrameLayout flPlayer;


    private NewsDetailAdapter newsDetailAdapter;
    private String title;
    private String id;
    private long startTime;
    private CourseInfo currentCourseInfo;

    private UserInfo userInfo;

    @Override
    public void init() {
        mPresenter = new NewsDetailPresenter(this, this);
        mToolbar.setTitle("");
        mToolbar.setMenuTitle(getString(R.string.share));
        mToolbar.showNavigationIcon();
        mToolbar.setMenuTitleColor(R.color.black_333333);
        StatusBarCompat.light(this);
        startTime = System.currentTimeMillis();

        if (getIntent() != null) {
            CourseInfo courseInfo = getIntent().getParcelableExtra("info");
            if (courseInfo != null) {
                id = courseInfo.getId();
            } else {
                id = getIntent().getStringExtra("id");
            }
            mPresenter.getWeixinInfo(id);
        }


        mTextViewTitle.setTypeface(Typeface.DEFAULT);

        initRecycleView();
        initListener();
        mSensorManager = (SensorManager) getSystemService(SENSOR_SERVICE);
        mSensorEventListener = new JZVideoPlayer.JZAutoFullscreenListener();
        userInfo = UserInfoHelper.getUserInfo();
    }


    private void initData(CourseInfo courseInfo) {
        title = courseInfo.getTitle();
        mTextViewTitle.setText(title);

        String str = getString(R.string.from_author);
        mTextViewFrom.setText(String.format(str,
                TextUtils.isEmpty(courseInfo.getAuthor()) ? getString(R.string.app_name) : courseInfo.getAuthor()));

        String time = null;
        if (!TextUtils.isEmpty(courseInfo.getAdd_time())) {

            time = TimeUtils.millis2String(Long.parseLong(courseInfo.getAdd_time()) * 1000, new SimpleDateFormat("yyyy-MM-dd HH:mm:ss", Locale.getDefault()));
        }

        mTextViewTime.setText(time);

        String url = courseInfo.getUrl();
        if (courseInfo.getUrl_type() == 1) {
            playAudio(url);
        } else if (courseInfo.getUrl_type() == 2) {
            playVideo(url, courseInfo.getImg());
        } else {
            flPlayer.setVisibility(View.GONE);
        }

    }

    private void initListener() {
        mToolbar.setOnItemClickLisener(new BaseToolBar.OnItemClickLisener() {
            @Override
            public void onClick() {
                SharePopupWindow sharePopupWindow = new SharePopupWindow(NewsDetailActivity.this);
                sharePopupWindow.show(llRootView);
            }
        });
        nestedScrollView.setOnScrollChangeListener(new NewsScrollView.onScrollChangeListener() {
            @Override
            public void onScrollChange(int l, int t, int oldl, int oldt) {
                if (t > mTextViewTitle.getMeasuredHeight()) {
                    mToolbar.setTitle(title);
                } else {
                    mToolbar.setTitle("");
                }
            }
        });
    }


    private void initRecycleView() {

        mRecyclerView.setHasFixedSize(true);
        mRecyclerView.setLayoutManager(new LinearLayoutManager(this));
        newsDetailAdapter = new NewsDetailAdapter(null);
        mRecyclerView.setAdapter(newsDetailAdapter);

        RecyclerView.ItemDecoration itemDecoration = new BaseItemDecoration(this);
        mRecyclerView.addItemDecoration(itemDecoration);

    }


    private void initWebView(final CourseInfoWrapper data) {

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
                        + "    window.HTML.openImg(this.src);  "
                        + "   }  " + "}" + "}())");


//                view.loadUrl("javascript:(function(){"
//                        + "var imgs=document.getElementsByTagName(\"img\");"
//                        + "var imgPaths= \"\";"
//                        + "for(var i=0;i<imgs.length;i++) " + "{"
//                        + "   imgPaths+=  imgs[i].src+\",\"}"
//                        + "  window.HTML.getImgs(imgPaths) "
//                        + "}())");

            }


        });


    }

    @Override
    public int getLayoutId() {
        return R.layout.common_activity_news_detail;
    }

    /**
     * 播放音频
     *
     * @param path
     */
    private void playAudio(String path) {
        mMediaPlayerView.setVisibility(View.VISIBLE);
        mJCVideoPlayer.setVisibility(View.GONE);
        mMediaPlayerView.setPath(path);
        mMediaPlayerView.setOnMediaClickListener(new MediaPlayerView.onMediaClickListener() {
            @Override
            public void onMediaClick() {
                mPresenter.statisticsStudyTotal(id);
            }
        });
    }

    /**
     * 播放视频
     *
     * @param url
     */
    private void playVideo(String url, String imgUrl) {
        mJCVideoPlayer.setVisibility(View.VISIBLE);
        mMediaPlayerView.setVisibility(View.GONE);

        mJCVideoPlayer.setUp(url, JZVideoPlayerStandard.SCREEN_WINDOW_NORMAL);
        Glide.with(this).load(imgUrl).into(mJCVideoPlayer.thumbImageView);
        mJCVideoPlayer.backButton.setVisibility(View.GONE);
        mJCVideoPlayer.tinyBackImageView.setVisibility(View.GONE);
        mJCVideoPlayer.batteryLevel.setVisibility(View.GONE);
        

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
        boolean isPlay = true;
        if (currentCourseInfo != null) {
            if (currentCourseInfo.getIsPay() == 0) {
                //未购买
                if (currentCourseInfo.getUserHas() == 0) {
                    if (userInfo != null) {
                        if (userInfo.getIsVip() == 0) {
                            isPlay = false;
                        } else {
                            if (currentCourseInfo.getIs_vip() == 0) {
                                isPlay = false;
                            }
                        }
                    } else {
                        isPlay = false;
                    }
                } else {
                    isPlay = true;
                }
            }
        }

        return isPlay;
    }


    public void click() {

        RxView.clicks(mJCVideoPlayer.thumbImageView).throttleFirst(1000, TimeUnit.MICROSECONDS).subscribe(new Action1<Void>() {
            @Override
            public void call(Void aVoid) {
                startOrBuy();
            }
        });
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
        } else {
            UserInfoHelper.isGotoLogin(NewsDetailActivity.this);
        }
    }

    private void showBuyDialog() {
        currentCourseInfo.setUserId(UserInfoHelper.getUserInfo().getUid());
        Bundle bundle = new Bundle();
        bundle.putParcelable("courseInfo", currentCourseInfo);
        bundle.putInt(GoodsType.GOODS_KEY, GoodsType.TYPE_SINGLE_WEIKE);
        VipDialogHelper.showVipDialog(getSupportFragmentManager(), null, bundle);
    }

    @Override
    public void showCourseResult(CourseInfoWrapper data) {
        initWebView(data);
        currentCourseInfo = data.getInfo();
        initData(currentCourseInfo);
        if (data.getRecommend() != null && data.getRecommend().size() > 0) {
            newsDetailAdapter.setNewData(data.getRecommend());
            mLlRecommend.setVisibility(View.VISIBLE);
        } else {
            mLlRecommend.setVisibility(View.GONE);
        }
    }

    @Override
    public void hideStateView() {
        stateView.hide();
    }

    @Override
    public void showNoNet() {
        stateView.showNoNet(nestedScrollView, HttpConfig.NET_ERROR, new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mPresenter.getWeixinInfo(id);
            }
        });
    }

    @Override
    public void showNoData() {
        stateView.showNoData(nestedScrollView);
    }

    @Override
    public void showLoading() {
        stateView.showLoading(nestedScrollView);
    }

    private ArrayList<String> imageList = new ArrayList<>();



    private class JavascriptInterface {

        @android.webkit.JavascriptInterface
        public void openImg(final String imgPath) {
            if (imageList.indexOf(imgPath) == -1) {
                imageList.add(imgPath);
            }
            Log.e(TAG, "openImg: " + imgPath);
            Intent intent = new Intent(NewsDetailActivity.this, GroupPictureDetailActivity.class);
            intent.putExtra("mList", imageList);
            intent.putExtra("position", imageList.indexOf(imgPath));
            startActivity(intent);

        }

        @android.webkit.JavascriptInterface
        public void getImgs(String imgPaths) {

            LogUtils.e("getImgs " + imgPaths);
        }

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

    private JZVideoPlayer.JZAutoFullscreenListener mSensorEventListener;

    private SensorManager mSensorManager;


    @Override
    protected void onResume() {
        super.onResume();
        Sensor accelerometerSensor = mSensorManager.getDefaultSensor(Sensor.TYPE_ACCELEROMETER);
        mSensorManager.registerListener(mSensorEventListener, accelerometerSensor, SensorManager.SENSOR_DELAY_NORMAL);
    }


    @Override
    protected void onPause() {
        super.onPause();
        JZVideoPlayer.releaseAllVideos();
        mSensorManager.unregisterListener(mSensorEventListener);
        JZVideoPlayer.clearSavedProgress(this, null);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        mMediaPlayerView.destroy();
        if (webView != null) {
            webView.loadDataWithBaseURL(null, "", "text/html", "utf-8", null);
            webView.clearHistory();
            llRootView.removeView(webView);
            webView.destroy();
        }
    }

    @Override
    public void onBackPressed() {
        if (JZVideoPlayer.backPress()) {
            return;
        }
        super.onBackPressed();
    }
}
