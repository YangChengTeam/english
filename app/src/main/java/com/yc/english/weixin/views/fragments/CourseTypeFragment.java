package com.yc.english.weixin.views.fragments;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.blankj.utilcode.util.LogUtils;
import com.yc.english.R;
import com.yc.english.base.helper.ShoppingHelper;
import com.yc.english.base.utils.StatusBarCompat;
import com.yc.english.base.view.BaseActivity;
import com.yc.english.base.view.BaseFragment;
import com.yc.english.main.hepler.UserInfoHelper;
import com.yc.english.news.view.activity.NewsDetailActivity;
import com.yc.english.weixin.model.domain.CourseInfo;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.Unbinder;

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

    private String url = "https://m.zhangmen.com/lp/feed?channel_code=12800&channel_keyword=1623d2f0f5b633a6";

    @Override
    public void init() {

        StatusBarCompat.compat((BaseActivity) getActivity(), mToolbarWarpper, mToolbar, R.mipmap.base_actionbar);

        initWebview();
    }

    private void initWebview() {
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
        webSettings.setBlockNetworkImage(true);//设置是否加载网络图片 true 为不加载 false 为加载

//        String body = data.getInfo().getBody();
//        wvMain.loadDataWithBaseURL(null, body, "text/html", "utf-8", null);

        wvMain.loadUrl(url);
        wvMain.setWebViewClient(new WebViewClient() {

            @Override
            public void onPageFinished(WebView view, String url) {
//                view.loadUrl("javascript:window.HTML.getContentHeight(document.getElementsByTagName('html')[0].scrollHeight);");

                webSettings.setBlockNetworkImage(false);


            }


        });
    }

    @Override
    public int getLayoutId() {
        return R.layout.weixin_fragment_course_type;
    }

    @Override
    public void onResume() {
        super.onResume();
        if (UserInfoHelper.getUserInfo() != null) {
            List<CourseInfo> list = ShoppingHelper.getCourseInfoListFromDB(UserInfoHelper.getUserInfo().getUid());
            if (list != null) {
                if (list.size() > 10) {
                    mNumLayout.setBackgroundResource(R.mipmap.more_num_icon);
                } else {
                    mNumLayout.setBackgroundResource(R.mipmap.single_num_icon);
                    if (list.size() == 0) {
                        mNumLayout.setVisibility(View.GONE);
                    } else {
                        mNumLayout.setVisibility(View.GONE);
                    }
                }
                mCartNumTextView.setText(list.size() + "");
            }
        } else {
            mCartNumTextView.setText("0");
            mNumLayout.setBackgroundResource(R.mipmap.single_num_icon);
            mNumLayout.setVisibility(View.GONE);
        }
    }


}
