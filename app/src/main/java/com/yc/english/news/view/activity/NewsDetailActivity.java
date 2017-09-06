package com.yc.english.news.view.activity;

import android.net.Uri;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.MotionEvent;
import android.view.View;
import android.webkit.WebChromeClient;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.LinearLayout;
import android.widget.ScrollView;

import com.example.comm_recyclviewadapter.BaseItemDecoration;
import com.jakewharton.rxbinding.view.RxView;
import com.yc.english.R;
import com.yc.english.base.view.BaseToolBar;
import com.yc.english.base.view.FullScreenActivity;
import com.yc.english.base.view.SharePopupWindow;
import com.yc.english.news.adapter.NewsDetailAdapter;
import com.yc.english.news.bean.NewsInfo;
import com.yc.english.news.view.widget.MediaPlayerView;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.TimeUnit;

import butterknife.BindView;
import fm.jiecao.jcvideoplayer_lib.JCVideoPlayerStandard;
import rx.functions.Action1;

/**
 * Created by wanglin  on 2017/9/6 08:32.
 */

public class NewsDetailActivity extends FullScreenActivity {
    @BindView(R.id.mJCVideoPlayer)
    JCVideoPlayerStandard mJCVideoPlayer;
    @BindView(R.id.webView)
    WebView webView;
    @BindView(R.id.mLinearLayoutMore)
    LinearLayout mLinearLayoutMore;
    @BindView(R.id.mRecyclerView)
    RecyclerView mRecyclerView;
    @BindView(R.id.ll_rootView)
    LinearLayout llRootView;
    @BindView(R.id.nestedScrollView)
    ScrollView nestedScrollView;
    @BindView(R.id.m_ll_recommend)
    LinearLayout mLlRecommend;
    @BindView(R.id.mMediaPlayerView)
    MediaPlayerView mMediaPlayerView;

    private NewsDetailAdapter newsDetailAdapter;

    @Override
    public void init() {
        mToolbar.setTitle("");
        mToolbar.setMenuTitle(getString(R.string.share));
        mToolbar.showNavigationIcon();

        if (getIntent() != null) {
            NewsInfo newsInfo = (NewsInfo) getIntent().getSerializableExtra("newsInfo");
//            mMediaPlayerView.setPath();
//            mMediaPlayerView.startPlay();
        }
        playAudio("http://play.baidu.com/?__m=mboxCtrl.playSong&__a=100575177&__o=song/100575177||playBtn&fr=-1||www.baidu.com#");

        initWebView();
        initRecycleView();
        initListener();

    }

    private void initListener() {
        mToolbar.setOnItemClickLisener(new BaseToolBar.OnItemClickLisener() {
            @Override
            public void onClick() {
                SharePopupWindow sharePopupWindow = new SharePopupWindow(NewsDetailActivity.this);
                sharePopupWindow.show(llRootView);
            }
        });
        RxView.clicks(mLinearLayoutMore).throttleFirst(200, TimeUnit.MILLISECONDS).subscribe(new Action1<Void>() {
            @Override
            public void call(Void aVoid) {
                // TODO: 2017/9/6 跳转到更多新闻页面

            }
        });

//        webView.setOnTouchListener(new View.OnTouchListener() {
//            @Override
//            public boolean onTouch(View v, MotionEvent event) {
//                if (event.getAction() == MotionEvent.ACTION_UP) {
//                    nestedScrollView.requestDisallowInterceptTouchEvent(false);
//                } else {
//                    nestedScrollView.requestDisallowInterceptTouchEvent(true);
//                }
//                return false;
//            }
//        });

    }


    private void initData() {
        List<NewsInfo> list = new ArrayList<>();
        list.add(new NewsInfo("1.12生肖的英文怎么说", "http://www.baidu.com"));
        list.add(new NewsInfo("2.这么积极向上的‘kill’你见过吗", "http://www.baidu.com"));
        list.add(new NewsInfo("3.一组英文漫画，戳中了多少家庭的痛点", "http://www.baidu.com"));
        list.add(new NewsInfo("4.每一个结局，都会是一个新的起点", "http://www.baidu.com"));
        list.add(new NewsInfo("5.醒来见到你，我心便安然", "http://www.baidu.com"));
        newsDetailAdapter.setData(list);
    }

    private void initRecycleView() {
        mRecyclerView.setLayoutManager(new LinearLayoutManager(this));
        newsDetailAdapter = new NewsDetailAdapter(this, null);
        mRecyclerView.setAdapter(newsDetailAdapter);
        RecyclerView.ItemDecoration itemDecoration = new BaseItemDecoration(this);
        mRecyclerView.addItemDecoration(itemDecoration);
        initData();
    }

    private void initWebView() {
        final LinearLayout.LayoutParams layoutParams = (LinearLayout.LayoutParams) webView.getLayoutParams();

        WebSettings webSettings = webView.getSettings();
        webSettings.setJavaScriptEnabled(true);


        //设置自适应屏幕，两者合用
        webSettings.setUseWideViewPort(true); //将图片调整到适合webview的大小
        webSettings.setLoadWithOverviewMode(true); // 缩放至屏幕的大小

        //缩放操作
        webSettings.setSupportZoom(true); //支持缩放，默认为true。是下面那个的前提。
        webSettings.setBuiltInZoomControls(true); //设置内置的缩放控件。若为false，则该WebView不可缩放
        webSettings.setDisplayZoomControls(false); //隐藏原生的缩放控件

        //其他细节操作
        webSettings.setCacheMode(WebSettings.LOAD_NO_CACHE); //关闭webview中缓存 //优先使用缓存:
        webSettings.setAllowFileAccess(true); //设置可以访问文件
        webSettings.setJavaScriptCanOpenWindowsAutomatically(true); //支持通过JS打开新窗口
        webSettings.setLoadsImagesAutomatically(true); //支持自动加载图片
        webSettings.setDefaultTextEncodingName("utf-8");//设置编码格式

        webView.loadUrl("http://www.cnblogs.com/xiaoQLu/archive/2011/04/24/2026520.html");
        webView.setWebViewClient(new WebViewClient() {

            @Override
            public boolean shouldOverrideUrlLoading(WebView view, String url) {
                view.loadUrl(url);
                return true;
            }

            @Override
            public void onPageFinished(WebView view, String url) {
                super.onPageFinished(view, url);
                layoutParams.height = view.getContentHeight();
                view.setLayoutParams(layoutParams);

            }
        });
        webView.setWebChromeClient(new WebChromeClient() {
            @Override
            public void onProgressChanged(WebView view, int newProgress) {
                super.onProgressChanged(view, newProgress);
                if (newProgress == 100) {

                    mLlRecommend.setVisibility(View.VISIBLE);
                }
            }

            @Override
            public void onReceivedTitle(WebView view, String title) {

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
        mMediaPlayerView.startPlay();
    }

    /**
     * 播放视频
     *
     * @param url
     */
    private void playVideo(String url) {
        mJCVideoPlayer.setVisibility(View.VISIBLE);
        mMediaPlayerView.setVisibility(View.GONE);
        mJCVideoPlayer.setUp(url, JCVideoPlayerStandard.SCREEN_LAYOUT_NORMAL);
        mJCVideoPlayer.thumbImageView.setImageURI(Uri.parse("http://p.qpic.cn/videoyun/0/2449_43b6f696980311e59ed467f22794e792_1/640"));
    }

}
