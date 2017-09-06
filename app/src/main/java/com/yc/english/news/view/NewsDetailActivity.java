package com.yc.english.news.view;

import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.webkit.WebChromeClient;
import android.webkit.WebResourceRequest;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.LinearLayout;

import com.example.comm_recyclviewadapter.BaseItemDecoration;
import com.jakewharton.rxbinding.view.RxView;
import com.yc.english.R;
import com.yc.english.base.view.BaseToolBar;
import com.yc.english.base.view.FullScreenActivity;
import com.yc.english.base.view.SharePopupWindow;
import com.yc.english.news.adapter.NewsDetailAdapter;
import com.yc.english.news.bean.NewsInfo;

import java.io.Serializable;
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
    private NewsDetailAdapter newsDetailAdapter;

    @Override
    public void init() {
        mToolbar.setTitle("");
        mToolbar.setMenuTitle(getString(R.string.share));
        mToolbar.showNavigationIcon();

        if (getIntent() != null) {
            NewsInfo newsInfo = (NewsInfo) getIntent().getSerializableExtra("newsInfo");
        }


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
                // TODO: 2017/9/6 调转到更多新闻页面

            }
        });
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
        webSettings.setCacheMode(WebSettings.LOAD_CACHE_ELSE_NETWORK); //关闭webview中缓存
        webSettings.setAllowFileAccess(true); //设置可以访问文件
        webSettings.setJavaScriptCanOpenWindowsAutomatically(true); //支持通过JS打开新窗口
        webSettings.setLoadsImagesAutomatically(true); //支持自动加载图片
        webSettings.setDefaultTextEncodingName("utf-8");//设置编码格式

        //优先使用缓存:
        webSettings.setCacheMode(WebSettings.LOAD_CACHE_ELSE_NETWORK);
//        webView.loadUrl("http://www.baidu.com");
        webView.setWebViewClient(new WebViewClient(){
//            @Override
//            public boolean shouldOverrideUrlLoading(WebView view, WebResourceRequest request) {
//
//                return super.shouldOverrideUrlLoading(view, request);
//            }

            @Override
            public boolean shouldOverrideUrlLoading(WebView view, String url) {
                view.loadUrl("http://www.baidu.com");
                return true;
            }
        });

    }

    @Override
    public int getLayoutId() {

        return R.layout.common_activity_news_detail;
    }


}
