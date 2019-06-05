package com.yc.english.news.view.activity;

import android.content.Intent;
import android.content.res.Configuration;
import android.graphics.Typeface;
import android.os.Build;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.util.Log;
import android.view.KeyEvent;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.FrameLayout;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.hwangjr.rxbus.annotation.Subscribe;
import com.hwangjr.rxbus.annotation.Tag;
import com.hwangjr.rxbus.thread.EventThread;
import com.jarvanmo.exoplayerview.media.SimpleMediaSource;
import com.jarvanmo.exoplayerview.ui.ExoVideoView;
import com.kk.securityhttp.net.contains.HttpConfig;
import com.kk.utils.LogUtil;
import com.yc.english.R;
import com.yc.english.base.view.FullScreenActivity;
import com.yc.english.base.view.SharePopupWindow;
import com.yc.english.base.view.StateView;
import com.yc.english.group.activitys.GroupPictureDetailActivity;
import com.yc.english.main.hepler.UserInfoHelper;
import com.yc.english.main.model.domain.Constant;
import com.yc.english.main.model.domain.UserInfo;
import com.yc.english.news.adapter.NewsDetailAdapter;
import com.yc.english.news.bean.CourseInfoWrapper;
import com.yc.english.news.contract.NewsDetailContract;
import com.yc.english.news.presenter.NewsDetailPresenter;
import com.yc.english.news.utils.ViewUtil;
import com.yc.english.news.view.fragment.QRCodeScanFragment;
import com.yc.english.news.view.widget.MediaPlayerView;
import com.yc.english.news.view.widget.NewsScrollView;
import com.yc.english.vip.model.bean.GoodsType;
import com.yc.english.vip.utils.VipDialogHelper;
import com.yc.english.vip.views.fragments.BasePayItemView;
import com.yc.english.weixin.model.domain.CourseInfo;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Locale;

import butterknife.BindView;
import yc.com.base.StatusBarCompat;
import yc.com.blankj.utilcode.util.LogUtils;
import yc.com.blankj.utilcode.util.NetworkUtils;
import yc.com.blankj.utilcode.util.TimeUtils;

import static com.jarvanmo.exoplayerview.orientation.OnOrientationChangedListener.SENSOR_LANDSCAPE;
import static com.jarvanmo.exoplayerview.orientation.OnOrientationChangedListener.SENSOR_PORTRAIT;

/**
 * Created by wanglin  on 2017/9/6 08:32.
 */

public class NewsDetailActivity extends FullScreenActivity<NewsDetailPresenter> implements NewsDetailContract.View {
    private static final String TAG = "NewsDetailActivity";

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
    @BindView(R.id.baseItemView_textbook_read)
    BasePayItemView baseItemViewTextbookRead;
    @BindView(R.id.baseItemView_word_valuable)
    BasePayItemView baseItemViewWordValuable;
    @BindView(R.id.baseItemView_brainpower_appraisal)
    BasePayItemView baseItemViewBrainpowerAppraisal;
    @BindView(R.id.baseItemView_score_tutorship)
    BasePayItemView baseItemViewScoreTutorship;
    @BindView(R.id.exoVideoView)
    ExoVideoView exoVideoView;


    private NewsDetailAdapter newsDetailAdapter;
    private String title;
    private String id;
    private long startTime;
    private CourseInfo currentCourseInfo;

    private UserInfo userInfo;

    private boolean isVideo = false;

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

    }


    private void initData(CourseInfo courseInfo) {
        title = courseInfo.getTitle();
        mTextViewTitle.setText(title);

        String str = getString(R.string.from_author,
                TextUtils.isEmpty(courseInfo.getAuthor()) ? getString(R.string.app_name) : courseInfo.getAuthor());

        mTextViewFrom.setText(str);

        String time = null;
        if (!TextUtils.isEmpty(courseInfo.getAdd_time())) {

            time = TimeUtils.millis2String(Long.parseLong(courseInfo.getAdd_time()) * 1000, new SimpleDateFormat("yyyy-MM-dd HH:mm:ss", Locale.getDefault()));
        }

        mTextViewTime.setText(time);

        String url = courseInfo.getUrl();
        if (courseInfo.getUrl_type() == 1) {
            playAudio(url);
        } else if (courseInfo.getUrl_type() == 2) {
            isVideo = true;

            playVideo(url, courseInfo.getImg());
        } else {
            flPlayer.setVisibility(View.GONE);
        }

    }

    private void initListener() {
        mToolbar.setOnItemClickLisener(() -> {
            SharePopupWindow sharePopupWindow = new SharePopupWindow(NewsDetailActivity.this);
            sharePopupWindow.show(llRootView);
        });
        nestedScrollView.setOnScrollChangeListener((l, t, oldl, oldt) -> {
            if (t > mTextViewTitle.getMeasuredHeight()) {
                mToolbar.setTitle(title);
            } else {
                mToolbar.setTitle("");
            }
        });

        ViewUtil.switchActivity(NewsDetailActivity.this, baseItemViewTextbookRead, 0);
        ViewUtil.switchActivity(NewsDetailActivity.this, baseItemViewWordValuable, 1);
        ViewUtil.switchActivity(NewsDetailActivity.this, baseItemViewBrainpowerAppraisal, 2);
        ViewUtil.switchActivity(NewsDetailActivity.this, baseItemViewScoreTutorship, 3);
    }


    private void initRecycleView() {

        mRecyclerView.setHasFixedSize(true);
        mRecyclerView.setLayoutManager(new LinearLayoutManager(this));
        newsDetailAdapter = new NewsDetailAdapter(null);
        mRecyclerView.setAdapter(newsDetailAdapter);

    }


    private void initWebView(final CourseInfoWrapper data) {

        final WebSettings webSettings = webView.getSettings();
        webSettings.setJavaScriptEnabled(true);

        //设置自适应屏幕，两者合用
        webSettings.setUseWideViewPort(true); //将图片调整到适合webview的大小
        webSettings.setLoadWithOverviewMode(true); // 缩放至屏幕的大小
        if (Build.VERSION.SDK_INT > Build.VERSION_CODES.JELLY_BEAN_MR1)
            webView.addJavascriptInterface(new JavascriptInterface(), "HTML");

        //其他细节操作
        webSettings.setCacheMode(WebSettings.LOAD_CACHE_ELSE_NETWORK); //关闭webview中缓存 //优先使用缓存:
        webSettings.setAllowFileAccess(true); //设置可以访问本地文件
        webSettings.setJavaScriptCanOpenWindowsAutomatically(true); //支持通过JS打开新窗口
//        webSettings.setLoadsImagesAutomatically(true); //支持自动加载图片
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


        webView.setOnLongClickListener(v -> {
            // 长按事件监听（注意：需要实现LongClickCallBack接口并传入对象）

            final WebView.HitTestResult htr = webView.getHitTestResult();//获取所点击的内容
            if (htr.getType() == WebView.HitTestResult.IMAGE_TYPE
                    || htr.getType() == WebView.HitTestResult.IMAGE_ANCHOR_TYPE
                    || htr.getType() == WebView.HitTestResult.SRC_IMAGE_ANCHOR_TYPE) {
                //判断被点击的类型为图片

                showQRCodeDialog(htr.getExtra());

            }

            LogUtil.msg("url: " + htr.getExtra());

            return false;
        });
    }


    public void showQRCodeDialog(String url) {
        QRCodeScanFragment scanFragment = new QRCodeScanFragment();
        Bundle bundle = new Bundle();
        bundle.putString("imgurl", url);
        scanFragment.setArguments(bundle);
        scanFragment.show(getSupportFragmentManager(), "");
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
        exoVideoView.setVisibility(View.GONE);
        mMediaPlayerView.setPath(path);
        mMediaPlayerView.setOnMediaClickListener(() -> mPresenter.statisticsStudyTotal(id));
    }

    /**
     * 播放视频
     *
     * @param url
     */
    private void playVideo(String url, String imgUrl) {

        Glide.with(this).load(imgUrl).into(exoVideoView.artworkView);
        SimpleMediaSource mediaSource = new SimpleMediaSource(url);//uri also supported
//        mJCVideoPlayer.batteryLevel.setVisibility(View.GONE);


        boolean isPlay = false;

        if (judgeVip()) {
            if (NetworkUtils.getNetworkType() == NetworkUtils.NetworkType.NETWORK_WIFI)
//                mJCVideoPlayer.startVideo();
                isPlay = true;
//            videoView.play(mediaSource, true);
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
        mToolbar.setVisibility(View.VISIBLE);
    }

    @Override
    protected void updateUIToLandscape() {
        super.updateUIToLandscape();
        mToolbar.setVisibility(View.GONE);
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


        startOrBuy();
    }

    private void startOrBuy() {
        if (userInfo != null) {
            exoVideoView.startVideo(judgeVip(), v -> showBuyDialog());
        } else {
            UserInfoHelper.isGotoLogin(NewsDetailActivity.this);
        }
    }

    private void showBuyDialog() {
        currentCourseInfo.setUserId(UserInfoHelper.getUserInfo().getUid());
        Bundle bundle = new Bundle();
        bundle.putParcelable("courseInfo", currentCourseInfo);
        bundle.putInt(GoodsType.GOODS_KEY, GoodsType.TYPE_GENERAL_VIP);
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
    public void hide() {
        stateView.hide();
    }

    @Override
    public void showNoNet() {
        stateView.showNoNet(nestedScrollView, HttpConfig.NET_ERROR, v -> mPresenter.getWeixinInfo(id));
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
        webView.onPause();
    }

    @Override
    public void onStop() {
        super.onStop();
        if (Build.VERSION.SDK_INT > 23) {
            exoVideoView.pause();
        }
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
        exoVideoView.releasePlayer();
    }

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {

        if (keyCode == KeyEvent.KEYCODE_BACK && isVideo) {
            return exoVideoView.onKeyDown(keyCode, event);
        }
        return super.onKeyDown(keyCode, event);
    }


    @Override
    protected boolean isSupportSwipeBack() {
        return false;
    }
}
