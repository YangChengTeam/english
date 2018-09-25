package com.yc.english.weixin.views.fragments;

import android.view.KeyEvent;
import android.view.View;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.alibaba.fastjson.JSON;
import com.blankj.utilcode.util.SPUtils;
import com.yc.english.R;
import com.yc.english.base.utils.StatusBarCompat;
import com.yc.english.base.view.BaseActivity;
import com.yc.english.base.view.BaseFragment;
import com.yc.english.base.view.StateView;
import com.yc.english.main.model.domain.Constant;
import com.yc.english.main.model.domain.SlideInfo;

import butterknife.BindView;

/**
 * Created by zhangkai on 2017/8/30.
 */

public class CourseTypeFragment extends BaseFragment {


    @BindView(R.id.iv_shopping_cart)
    ImageView mShoppingImageView;

    @BindView(R.id.layout_num)
    LinearLayout mNumLayout;

    @BindView(R.id.tv_cart_num)
    TextView mCartNumTextView;

    @BindView(R.id.toolbar)
    RelativeLayout mToolbar;

    @BindView(R.id.toolbarWarpper)
    FrameLayout mToolbarWarpper;
    @BindView(R.id.wv_main)
    WebView wvMain;
    @BindView(R.id.tv_tb_title)
    TextView tvTbTitle;
    @BindView(R.id.stateView)
    StateView stateView;


    private String url = "https://m.zhangmen.com/lp/feed?channel_code=12800&channel_keyword=1623d2f0f5b633a6";


    @Override
    public void init() {
        StatusBarCompat.compat((BaseActivity) getActivity(), mToolbarWarpper, mToolbar, R.mipmap.base_actionbar);
        initWebview();
    }

    private void initWebview() {
        stateView.showLoading(wvMain);
        SlideInfo slideInfo = JSON.parseObject(SPUtils.getInstance().getString(Constant.INDEX_MENU_STATICS), SlideInfo.class);
        if (null != slideInfo) {
            url = slideInfo.getUrl();
        }


        final WebSettings webSettings = wvMain.getSettings();
        webSettings.setJavaScriptEnabled(true);

        //设置自适应屏幕，两者合用
        webSettings.setUseWideViewPort(true); //将图片调整到适合webview的大小
        webSettings.setLoadWithOverviewMode(true); // 缩放至屏幕的大小

//        wvMain.addJavascriptInterface(new NewsDetailActivity.JavascriptInterface(), "HTML");

        //其他细节操作
        webSettings.setCacheMode(WebSettings.LOAD_CACHE_ELSE_NETWORK); //关闭webview中缓存 //优先使用缓存:
        webSettings.setAllowFileAccess(true); //设置可以访问文件
        webSettings.setJavaScriptCanOpenWindowsAutomatically(true); //支持通过JS打开新窗口
        webSettings.setLoadsImagesAutomatically(true); //支持自动加载图片
        webSettings.setDefaultTextEncodingName("utf-8");//设置编码格式
        webSettings.setBlockNetworkImage(false);//设置是否加载网络图片 true 为不加载 false 为加载

        wvMain.loadUrl(url);
        wvMain.setWebViewClient(new WebViewClient() {

            @Override
            public void onPageFinished(WebView view, String url) {
                stateView.hide();
//
            }



        });
        wvMain.removeJavascriptInterface("searchBoxJavaBridge_");

        wvMain.setOnKeyListener(new View.OnKeyListener() {
            @Override
            public boolean onKey(View v, int keyCode, KeyEvent event) {
                if (event.getAction() == KeyEvent.ACTION_DOWN) {
                    if (keyCode == KeyEvent.KEYCODE_BACK && wvMain.canGoBack()) {
                        wvMain.goBack();
                        return true;
                    }
                }
                return false;
            }
        });


    }


    @Override
    public int getLayoutId() {

        return R.layout.weixin_fragment_course_type;
    }


}
