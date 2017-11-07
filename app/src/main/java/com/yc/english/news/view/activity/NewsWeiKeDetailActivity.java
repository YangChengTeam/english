package com.yc.english.news.view.activity;

import android.content.Intent;
import android.hardware.Sensor;
import android.hardware.SensorManager;
import android.util.Log;
import android.view.View;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.blankj.utilcode.util.LogUtils;
import com.blankj.utilcode.util.ScreenUtils;
import com.blankj.utilcode.util.ToastUtils;
import com.bumptech.glide.Glide;
import com.jakewharton.rxbinding.view.RxView;
import com.kk.securityhttp.net.contains.HttpConfig;
import com.yc.english.R;
import com.yc.english.base.view.BaseToolBar;
import com.yc.english.base.view.FullScreenActivity;
import com.yc.english.base.view.SharePopupWindow;
import com.yc.english.base.view.StateView;
import com.yc.english.group.view.activitys.GroupPictureDetailActivity;
import com.yc.english.news.adapter.NewsDetailAdapter;
import com.yc.english.news.bean.CourseInfoWrapper;
import com.yc.english.news.contract.NewsDetailContract;
import com.yc.english.news.presenter.NewsDetailPresenter;
import com.yc.english.weixin.model.domain.CourseInfo;

import java.util.ArrayList;
import java.util.concurrent.TimeUnit;

import butterknife.BindView;
import fm.jiecao.jcvideoplayer_lib.JCVideoPlayer;
import fm.jiecao.jcvideoplayer_lib.JCVideoPlayerStandard;
import rx.functions.Action1;

/**
 * Created by wanglin  on 2017/9/6 08:32.
 */

public class NewsWeiKeDetailActivity extends FullScreenActivity<NewsDetailPresenter> implements NewsDetailContract.View {

    private static final String TAG = "NewsDetailActivity";

    @BindView(R.id.stateView)
    StateView stateView;

    @BindView(R.id.mJCVideoPlayer)
    JCVideoPlayerStandard mJCVideoPlayer;

    @BindView(R.id.webView)
    WebView webView;

    @BindView(R.id.ll_rootView)
    RelativeLayout llRootView;

    @BindView(R.id.mTextViewTitle)
    TextView mTextViewTitle;

    @BindView(R.id.layout_add_to_cart)
    LinearLayout mAddToCartLayout;

    @BindView(R.id.layout_buy_now)
    LinearLayout mBuyNowLayout;

    private NewsDetailAdapter newsDetailAdapter;

    private String title;

    private int screenHeight;

    private String id;

    private long startTime;

    @Override
    public int getLayoutId() {
        return R.layout.common_activity_weike_detail;
    }

    @Override
    public void init() {
        mPresenter = new NewsDetailPresenter(this, this);
        mToolbar.setTitle("");
        mToolbar.setMenuTitle(getString(R.string.share));
        mToolbar.showNavigationIcon();

        mToolbar.setMenuTitleColor(R.color.black_333333);

        startTime = System.currentTimeMillis();

        if (getIntent() != null) {
            CourseInfo courseInfo = getIntent().getParcelableExtra("info");
            id = courseInfo.getId();
            //mPresenter.getWeixinInfo(id);
        }

        screenHeight = ScreenUtils.getScreenHeight();

        initListener();
        mSensorManager = (SensorManager) getSystemService(SENSOR_SERVICE);
        mSensorEventListener = new JCVideoPlayer.JCAutoFullscreenListener();
        //initData(null);
        stateView.hide();
        playVideo("http://v.cctv.com/flash/mp4video6/TMS/2011/01/05/cf752b1c12ce452b3040cab2f90bc265_h264818000nero_aac32-1.mp4", "http://pic9.qiyipic.com/image/20171025/6a/5f/v_113765412_m_601_220_124.jpg");

        RxView.clicks(mAddToCartLayout).throttleFirst(200, TimeUnit.MILLISECONDS).subscribe(new Action1<Void>() {
            @Override
            public void call(Void aVoid) {
                ToastUtils.showLong("加入购物车成功");
            }
        });

        RxView.clicks(mBuyNowLayout).throttleFirst(200, TimeUnit.MILLISECONDS).subscribe(new Action1<Void>() {
            @Override
            public void call(Void aVoid) {
                Intent intent = new Intent(NewsWeiKeDetailActivity.this,ConfirmOrderActivity.class);
                startActivity(intent);
            }
        });
    }

    private void initData(CourseInfo courseInfo) {
        title = courseInfo.getTitle();
        mTextViewTitle.setText(title);

        String url = courseInfo.getUrl();
        if (courseInfo.getUrl_type() == 1) {
            //playAudio(url);
        } else if (courseInfo.getUrl_type() == 2) {
            playVideo(url, courseInfo.getImg());
        }
    }

    private void initListener() {
        mToolbar.setOnItemClickLisener(new BaseToolBar.OnItemClickLisener() {
            @Override
            public void onClick() {
                SharePopupWindow sharePopupWindow = new SharePopupWindow(NewsWeiKeDetailActivity.this);
                sharePopupWindow.show(llRootView);
            }
        });
    }


    private String makeBody(String data) {

        StringBuilder sb = new StringBuilder();
        sb.append("<html><head><meta charset=\"utf-8\" /><meta content=\"yes\" name=\"apple-mobile-web-app-capable\" />\n" +
                "    <meta content=\"yes\" name=\"apple-touch-fullscreen\" />\n" +
                "    <meta content=\"telephone=no,email=no\" name=\"format-detection\" />\n" +
                "    <meta name=\"App-Config\" content=\"fullscreen=yes,useHistoryState=yes,transition=yes\" /><meta name=\"viewport\" content=\"width=device-width,initial-scale=1,minimum-scale=1,maximum-scale=1,user-scalable=no\" />");
        sb.append("<style> html,body{overflow:hidden;} img { width:100%; height:auto; overflow:hidden;}</style></head><body>");
        sb.append(data);
        sb.append("</body></html>");
        return sb.toString();
    }

    private void initWebView(final CourseInfoWrapper data) {

        final WebSettings webSettings = webView.getSettings();
        webSettings.setJavaScriptEnabled(true);

        //设置自适应屏幕，两者合用
        webSettings.setUseWideViewPort(true); //将图片调整到适合webview的大小
        webSettings.setLoadWithOverviewMode(true); // 缩放至屏幕的大小

        webView.addJavascriptInterface(new JavascriptInterface(), "HTML");

        //其他细节操作
        webSettings.setCacheMode(WebSettings.LOAD_CACHE_ELSE_NETWORK); //关闭webview中缓存 //优先使用缓存:
        webSettings.setAllowFileAccess(true); //设置可以访问文件
        webSettings.setJavaScriptCanOpenWindowsAutomatically(true); //支持通过JS打开新窗口
        webSettings.setLoadsImagesAutomatically(true); //支持自动加载图片
        webSettings.setDefaultTextEncodingName("utf-8");//设置编码格式
        webSettings.setBlockNetworkImage(true);//设置是否加载网络图片 true 为不加载 false 为加载

        String body = makeBody(data.getInfo().getBody());
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
                        + "    window.HTML.openImg(this.src); "
                        + "   }  " + "}" + "}())");

            }


        });


    }

    /**
     * 播放视频
     *
     * @param url
     */
    private void playVideo(String url, String imgUrl) {
        mJCVideoPlayer.setVisibility(View.VISIBLE);
        mJCVideoPlayer.setUp(url, JCVideoPlayerStandard.SCREEN_LAYOUT_NORMAL);
        Glide.with(this).load(imgUrl).into(mJCVideoPlayer.thumbImageView);
        mJCVideoPlayer.battery_level.setVisibility(View.GONE);
        mJCVideoPlayer.backButton.setVisibility(View.GONE);

    }

    @Override
    public void showCourseResult(CourseInfoWrapper data) {
        initWebView(data);
        initData(data.getInfo());
        if (data.getRecommend() != null && data.getRecommend().size() > 0) {
            newsDetailAdapter.setData(data.getRecommend());
        } else {
        }
    }

    @Override
    public void hideStateView() {
        stateView.hide();
    }

    @Override
    public void showNoNet() {
        stateView.showNoNet(llRootView, HttpConfig.NET_ERROR, new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mPresenter.getWeixinInfo(id);
            }
        });
    }

    @Override
    public void showNoData() {
        stateView.showNoData(llRootView);
    }

    @Override
    public void showLoading() {
        stateView.showLoading(llRootView);
    }

    private ArrayList<String> imageList = new ArrayList<>();

    private class JavascriptInterface {

        @android.webkit.JavascriptInterface
        public void openImg(final String imgPath) {
            if (imageList.indexOf(imgPath) == -1) {
                imageList.add(imgPath);
            }
            Log.e(TAG, "openImg: " + imgPath);
            Intent intent = new Intent(NewsWeiKeDetailActivity.this, GroupPictureDetailActivity.class);
            intent.putExtra("mList", imageList);
            intent.putExtra("position", imageList.indexOf(imgPath));
            startActivity(intent);

        }

    }

    private JCVideoPlayer.JCAutoFullscreenListener mSensorEventListener;

    private SensorManager mSensorManager;


    @Override
    protected void onResume() {
        super.onResume();
        Sensor accelerometerSensor = mSensorManager.getDefaultSensor(Sensor.TYPE_ACCELEROMETER);
        mSensorManager.registerListener(mSensorEventListener, accelerometerSensor, SensorManager.SENSOR_DELAY_NORMAL);
    }


    @Override
    protected void onPause() {
        super.onPause();
        JCVideoPlayer.releaseAllVideos();
        mSensorManager.unregisterListener(mSensorEventListener);
        JCVideoPlayer.clearSavedProgress(this, null);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        if (webView != null && llRootView != null) {
            webView.loadDataWithBaseURL(null, "", "text/html", "utf-8", null);
            webView.clearHistory();

            llRootView.removeView(webView);
            webView.destroy();
        }
    }

    @Override
    public void onBackPressed() {
        if (JCVideoPlayer.backPress()) {
            return;
        }
        super.onBackPressed();
    }
}
