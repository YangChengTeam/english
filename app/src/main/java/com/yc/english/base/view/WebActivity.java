package com.yc.english.base.view;

import android.content.Intent;
import android.os.Build;
import android.webkit.WebChromeClient;
import android.webkit.WebView;
import android.webkit.WebViewClient;

import com.yc.english.R;

import butterknife.BindView;
import yc.com.blankj.utilcode.util.UIUitls;

/**
 * Created by zhangkai on 2017/8/14.
 */

public class WebActivity extends FullScreenActivity {

    @BindView(R.id.wv_main)
    CommonWebView mMainWebView;


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

    }

    @Override
    public int getLayoutId() {
        return R.layout.base_web_activity;
    }

}
