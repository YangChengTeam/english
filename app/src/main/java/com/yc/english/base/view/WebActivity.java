package com.yc.english.base.view;

import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.net.http.SslError;
import android.os.Build;
import android.support.annotation.RequiresApi;
import android.util.Log;
import android.view.KeyEvent;
import android.view.View;
import android.webkit.SslErrorHandler;
import android.webkit.WebChromeClient;
import android.webkit.WebResourceRequest;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;

import com.blankj.utilcode.util.UIUitls;
import com.kk.utils.LogUtil;
import com.yc.english.R;

import butterknife.BindView;

/**
 * Created by zhangkai on 2017/8/14.
 */

public class WebActivity extends FullScreenActivity {

    @BindView(R.id.wv_main)
    WebView mMainWebView;


    private String url;
    private String title;


    @Override
    public void init() {
        Intent intent = this.getIntent();
        if (intent != null) {
            url = intent.getStringExtra("url");
            title = intent.getStringExtra("title");
        }

        mToolbar.setTitle(title);
        mToolbar.showNavigationIcon();

        WebSettings webSettings = mMainWebView.getSettings();
        webSettings.setLoadsImagesAutomatically(false);
        webSettings.setAllowUniversalAccessFromFileURLs(true);
        webSettings.setJavaScriptEnabled(true);
        webSettings.setDomStorageEnabled(true);
        webSettings.setNeedInitialFocus(false);
        webSettings.setSupportZoom(false);
        webSettings.setBuiltInZoomControls(false);
        webSettings.setJavaScriptCanOpenWindowsAutomatically(true);
        webSettings.setAllowFileAccess(true);
        webSettings.setDefaultTextEncodingName("UTF-8");
        webSettings.setAppCacheEnabled(true);
        webSettings.setDatabaseEnabled(true);
        webSettings.setLoadsImagesAutomatically(true);
        webSettings.setLoadWithOverviewMode(true);
        webSettings.setAllowFileAccess(true);
        webSettings.setBlockNetworkImage(false);
        webSettings.setCacheMode(WebSettings.LOAD_DEFAULT);   // 默认使用缓存
        webSettings.setAllowFileAccess(true);   // 可以读取文件缓存(manifest生效)
        String appCaceDir = this.getDir("cache", Context.MODE_PRIVATE).getPath();
        webSettings.setAppCachePath(appCaceDir);
        //加载https网页包含http链接的资源，必须要加这句，否则图片资源加载不出来
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {

            webSettings.setMixedContentMode(WebSettings.MIXED_CONTENT_ALWAYS_ALLOW);
        }
        if ((!Build.MANUFACTURER.toLowerCase().contains("xiaomi")) && (Build.MANUFACTURER.toLowerCase().contains("huawei"))) {

        }

        if ((Build.VERSION.SDK_INT >= 11) && (Build.MANUFACTURER.toLowerCase().contains("lenovo")))
            mMainWebView.setLayerType(1, null);

        showLoadingDialog("正在加载...");
        mMainWebView.setWebChromeClient(new WebChromeClient() {
            @Override
            public void onProgressChanged(WebView view, final int newProgress) {
                super.onProgressChanged(view, newProgress);
                UIUitls.post(new Runnable() {
                    @Override
                    public void run() {
                        mLoadingDialog.setMessage("正在加载" + newProgress + "%...");
                    }
                });
            }


        });


        mMainWebView.setWebViewClient(new WebViewClient() {

            @Override
            public void onPageFinished(WebView view, String url) {
                super.onPageFinished(view, url);
                dismissLoadingDialog();
            }


        });

        mMainWebView.loadUrl(url);

        mMainWebView.setOnKeyListener(new View.OnKeyListener() {
            @Override
            public boolean onKey(View v, int keyCode, KeyEvent event) {
                if (event.getAction() == KeyEvent.ACTION_DOWN) {
                    if (keyCode == KeyEvent.KEYCODE_BACK && mMainWebView.canGoBack()) {
                        mMainWebView.goBack();
                        return true;
                    }
                }
                return false;
            }
        });
    }

    @Override
    public int getLayoutId() {
        return R.layout.base_web_activity;
    }

}
