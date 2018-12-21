package com.yc.english.setting.view.activitys;

import android.view.View;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.ImageView;

import com.blankj.utilcode.util.LogUtils;
import com.blankj.utilcode.util.SizeUtils;
import com.bumptech.glide.Glide;
import com.bumptech.glide.request.RequestOptions;
import com.yc.english.R;
import com.yc.english.base.view.FullScreenActivity;

import butterknife.BindView;

/**
 * Created by wanglin  on 2017/12/11 16:16.
 */

public class TaskShowActivity extends FullScreenActivity {

    @BindView(R.id.webView)
    WebView webView;
    @BindView(R.id.iv_loading)
    ImageView mIvLoading;

    @Override
    public void init() {
        String data = getIntent().getStringExtra("data");
        LogUtils.e(data);
        final WebSettings webSettings = webView.getSettings();

        //设置自适应屏幕，两者合用
        webSettings.setUseWideViewPort(true); //将图片调整到适合webview的大小
        webSettings.setLoadWithOverviewMode(true); // 缩放至屏幕的大小
//        Glide.with(this).load(R.mipmap.base_loading)
//                .apply(new RequestOptions().override(SizeUtils.dp2px(1080 / 3), SizeUtils.dp2px(408 / 3))).into(mIvLoading);
        webView.setVisibility(View.GONE);


        //其他细节操作
        webSettings.setCacheMode(WebSettings.LOAD_CACHE_ELSE_NETWORK); //关闭webview中缓存 //优先使用缓存:
        webSettings.setAllowFileAccess(true); //设置可以访问文件
        webSettings.setJavaScriptCanOpenWindowsAutomatically(true); //支持通过JS打开新窗口
        webSettings.setLoadsImagesAutomatically(true); //支持自动加载图片
        webSettings.setDefaultTextEncodingName("utf-8");//设置编码格式
        webSettings.setBlockNetworkImage(true);//设置是否加载网络图片 true 为不加载 false 为加载
        webView.loadUrl(data);
        webView.setWebViewClient(new WebViewClient() {

                                     @Override
                                     public boolean shouldOverrideUrlLoading(WebView view, String url) {
                                         view.loadUrl(url);
                                         return super.shouldOverrideUrlLoading(view, url);

                                     }

                                     @Override
                                     public void onPageFinished(WebView view, String url) {
                                         super.onPageFinished(view, url);
                                         webSettings.setBlockNetworkImage(false);
                                         mIvLoading.setVisibility(View.GONE);
                                         webView.setVisibility(View.VISIBLE);

                                     }
                                 }

        );


    }

    @Override
    public int getLayoutId() {
        return R.layout.activity_task_show;
    }

}
