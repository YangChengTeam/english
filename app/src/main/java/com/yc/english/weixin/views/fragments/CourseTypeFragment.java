package com.yc.english.weixin.views.fragments;

import android.content.Intent;
import android.net.Uri;
import android.webkit.JavascriptInterface;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.alibaba.fastjson.JSON;
import com.blankj.utilcode.util.SPUtils;
import com.blankj.utilcode.util.ToastUtils;
import com.yc.english.R;
import com.yc.english.base.utils.StatusBarCompat;
import com.yc.english.base.view.BaseActivity;
import com.yc.english.base.view.BaseFragment;
import com.yc.english.base.view.CommonWebView;
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
    CommonWebView wvMain;
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

        wvMain.addJavascriptInterface(new CourseTypeFragment.JavascriptInterface(), "QQ");
        wvMain.loadUrl(url);
        wvMain.setWebViewClient(new WebViewClient() {
            @Override
            public void onPageFinished(WebView view, String url) {
                stateView.hide();
            }

        });


    }


    @Override
    public int getLayoutId() {

        return R.layout.weixin_fragment_course_type;
    }


    public class JavascriptInterface {

        @android.webkit.JavascriptInterface
        public void startQQChat() {
            try {
                String url3521 = "mqqwpa://im/chat?chat_type=wpa&uin=2037097758";
                startActivity(new Intent(Intent.ACTION_VIEW, Uri.parse(url3521)));
            } catch (Exception e) {

                ToastUtils.showShort("你的手机还未安装qq,请先安装");
            }

        }
    }


}
