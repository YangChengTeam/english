package com.yc.english.composition.activity;

import android.content.Context;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.support.v4.content.ContextCompat;
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

import com.kk.securityhttp.net.contains.HttpConfig;
import com.kk.utils.LogUtil;
import com.yc.english.R;
import com.yc.english.base.view.BaseToolBar;
import com.yc.english.base.view.FullScreenActivity;
import com.yc.english.base.view.SharePopupWindow;
import com.yc.english.base.view.StateView;
import com.yc.english.composition.contract.CompositionDetailContract;
import com.yc.english.composition.model.bean.CompositionDetailInfo;
import com.yc.english.composition.presenter.CompositionDetailPresenter;
import com.yc.english.group.activitys.GroupPictureDetailActivity;
import com.yc.english.news.bean.CourseInfoWrapper;
import com.yc.english.news.view.activity.NewsDetailActivity;
import com.yc.english.news.view.widget.MediaPlayerView;
import com.yc.english.news.view.widget.NewsScrollView;
import com.yc.english.vip.views.fragments.BasePayItemView;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Locale;

import butterknife.BindView;
import butterknife.ButterKnife;
import cn.jzvd.JZVideoPlayerStandard;
import yc.com.base.StatusBarCompat;
import yc.com.blankj.utilcode.util.LogUtils;
import yc.com.blankj.utilcode.util.TimeUtils;

/**
 * Created by wanglin  on 2019/3/26 14:23.
 */
public class CompositionDetailActivity extends FullScreenActivity<CompositionDetailPresenter> implements CompositionDetailContract.View {


    @BindView(R.id.stateView)
    StateView stateView;
    @BindView(R.id.mTextViewTitle)
    TextView mTextViewTitle;
    @BindView(R.id.mTextViewFrom)
    TextView mTextViewFrom;
    @BindView(R.id.mTextViewTime)
    TextView mTextViewTime;
    @BindView(R.id.mJCVideoPlayer)
    JZVideoPlayerStandard mJCVideoPlayer;
    @BindView(R.id.mMediaPlayerView)
    MediaPlayerView mMediaPlayerView;
    @BindView(R.id.fl_player)
    FrameLayout flPlayer;
    @BindView(R.id.webView)
    WebView webView;
    @BindView(R.id.baseItemView_textbook_read)
    BasePayItemView baseItemViewTextbookRead;
    @BindView(R.id.baseItemView_word_valuable)
    BasePayItemView baseItemViewWordValuable;
    @BindView(R.id.baseItemView_brainpower_appraisal)
    BasePayItemView baseItemViewBrainpowerAppraisal;
    @BindView(R.id.baseItemView_score_tutorship)
    BasePayItemView baseItemViewScoreTutorship;
    @BindView(R.id.view_synchronization_teach)
    LinearLayout viewSynchronizationTeach;
    @BindView(R.id.mLinearLayoutMore)
    LinearLayout mLinearLayoutMore;
    @BindView(R.id.mRecyclerView)
    RecyclerView mRecyclerView;
    @BindView(R.id.m_ll_recommend)
    LinearLayout mLlRecommend;
    @BindView(R.id.ll_rootView)
    LinearLayout llRootView;
    @BindView(R.id.nestedScrollView)
    NewsScrollView nestedScrollView;
    private String zwid;

    private static final String TAG = "CompositionDetailActiv";
    private String title;

    public static void startActivity(Context context, String zwid) {
        Intent intent = new Intent(context, CompositionDetailActivity.class);
        intent.putExtra("zwid", zwid);
        context.startActivity(intent);
    }


    @Override
    public int getLayoutId() {
        return R.layout.common_activity_news_detail;
    }


    @Override

    public void init() {
        mToolbar.setTitle("");
        mToolbar.setMenuTitle(getString(R.string.share));
        mToolbar.showNavigationIcon();
        mToolbar.setMenuTitleColor(R.color.black_333333);
        StatusBarCompat.light(this);
        nestedScrollView.setBackgroundColor(ContextCompat.getColor(this, R.color.white));
        if (getIntent() != null) {
            zwid = getIntent().getStringExtra("zwid");
        }
        mPresenter = new CompositionDetailPresenter(this, this);
        mPresenter.getCompositionDetail(zwid);
        viewSynchronizationTeach.setVisibility(View.GONE);
        initListener();
    }

    private void initListener() {
        mToolbar.setOnItemClickLisener(new BaseToolBar.OnItemClickLisener() {
            @Override
            public void onClick() {
                SharePopupWindow sharePopupWindow = new SharePopupWindow(CompositionDetailActivity.this);
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

    @Override
    public void showCompositionDetailInfo(CompositionDetailInfo info) {
        title = info.getTitle();
        mTextViewTitle.setText(title);
        String str = getString(R.string.from_author);
        mTextViewFrom.setText(String.format(str, "网络整理"));
        String time = null;
        if (!TextUtils.isEmpty(info.getAddtime())) {
            time = TimeUtils.millis2String(Long.parseLong(info.getAddtime()) * 1000, new SimpleDateFormat("yyyy-MM-dd HH:mm:ss", Locale.getDefault()));
        }

        mTextViewTime.setText(time);
        initWebView(info);

    }

    private void initWebView(final CompositionDetailInfo data) {

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

        String body = data.getContent();
        webView.loadDataWithBaseURL(null, body, "text/html", "utf-8", null);


        webView.setWebViewClient(new WebViewClient() {

            @Override
            public void onPageFinished(WebView view, String url) {
//                view.loadUrl("javascript:window.HTML.getContentHeight(document.getElementsByTagName('html')[0].scrollHeight);");

                llRootView.setVisibility(View.VISIBLE);

                webSettings.setBlockNetworkImage(false);

//                LogUtils.e("startTime-->" + (System.currentTimeMillis() - startTime));

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


        webView.setOnLongClickListener(new View.OnLongClickListener() {
            @Override
            public boolean onLongClick(View v) {
                // 长按事件监听（注意：需要实现LongClickCallBack接口并传入对象）

                final WebView.HitTestResult htr = webView.getHitTestResult();//获取所点击的内容
                if (htr.getType() == WebView.HitTestResult.IMAGE_TYPE
                        || htr.getType() == WebView.HitTestResult.IMAGE_ANCHOR_TYPE
                        || htr.getType() == WebView.HitTestResult.SRC_IMAGE_ANCHOR_TYPE) {
                    //判断被点击的类型为图片

//                    showQRCodeDialog(htr.getExtra());

                }

                LogUtil.msg("url: " + htr.getExtra());

                return false;
            }
        });
    }


    @Override
    public void hide() {
        stateView.hide();
    }

    @Override
    public void showLoading() {
        stateView.showLoading(nestedScrollView);
    }

    @Override
    public void showNoData() {
        stateView.showNoData(nestedScrollView);
    }

    @Override
    public void showNoNet() {
        stateView.showNoNet(nestedScrollView, HttpConfig.NET_ERROR, new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mPresenter.getCompositionDetail(zwid);
            }
        });

    }

    private ArrayList<String> imageList = new ArrayList<>();

    private class JavascriptInterface {

        @android.webkit.JavascriptInterface
        public void openImg(final String imgPath) {
            if (imageList.indexOf(imgPath) == -1) {
                imageList.add(imgPath);
            }
            Log.e(TAG, "openImg: " + imgPath);
            Intent intent = new Intent(CompositionDetailActivity.this, GroupPictureDetailActivity.class);
            intent.putExtra("mList", imageList);
            intent.putExtra("position", imageList.indexOf(imgPath));
            startActivity(intent);

        }

        @android.webkit.JavascriptInterface
        public void getImgs(String imgPaths) {
            LogUtils.e("getImgs " + imgPaths);
        }

    }
}
