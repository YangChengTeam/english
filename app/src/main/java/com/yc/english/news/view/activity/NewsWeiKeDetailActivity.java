package com.yc.english.news.view.activity;

import android.content.Intent;
import android.graphics.Paint;
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
import com.yc.english.base.helper.ShoppingHelper;
import com.yc.english.base.view.AlertDialog;
import com.yc.english.base.view.BaseToolBar;
import com.yc.english.base.view.FullScreenActivity;
import com.yc.english.base.view.SharePopupWindow;
import com.yc.english.base.view.StateView;
import com.yc.english.group.view.activitys.GroupPictureDetailActivity;
import com.yc.english.main.hepler.UserInfoHelper;
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

    @BindView(R.id.tv_title)
    TextView mTextViewTitle;

    @BindView(R.id.layout_add_to_cart)
    LinearLayout mAddToCartLayout;

    @BindView(R.id.layout_buy_now)
    LinearLayout mBuyNowLayout;

    @BindView(R.id.tv_learn_count)
    TextView mLearnCountTextView;

    @BindView(R.id.tv_now_price)
    TextView mNowPriceTextView;

    @BindView(R.id.tv_old_price)
    TextView mOldPriceTextView;

    @BindView(R.id.layout_is_buy_or_vip)
    LinearLayout mIsBuyOrVipLayout;

    private String title;

    private int screenHeight;

    private String id;

    private long startTime;

    private CourseInfo currentCourseInfo;

    private boolean isPlay = true;

    @Override
    public int getLayoutId() {
        return R.layout.common_activity_weike_detail;
    }

    @Override
    public void init() {
        mPresenter = new NewsDetailPresenter(this, this);
        mToolbar.setTitle("视频微课学习");
        mToolbar.setMenuTitle(getString(R.string.share));
        mToolbar.showNavigationIcon();
        mToolbar.setMenuTitleColor(R.color.black_333333);
        mOldPriceTextView.getPaint().setFlags(Paint.STRIKE_THRU_TEXT_FLAG);

        startTime = System.currentTimeMillis();

        if (getIntent() != null) {
            id = getIntent().getStringExtra("id");
            mPresenter.getWeiKeDetail(id, UserInfoHelper.getUserInfo() != null ? UserInfoHelper.getUserInfo().getUid() : "");
        }

        screenHeight = ScreenUtils.getScreenHeight();
        initListener();
        mSensorManager = (SensorManager) getSystemService(SENSOR_SERVICE);
        mSensorEventListener = new JCVideoPlayer.JCAutoFullscreenListener();

        RxView.clicks(mAddToCartLayout).throttleFirst(200, TimeUnit.MILLISECONDS).subscribe(new Action1<Void>() {
            @Override
            public void call(Void aVoid) {
                if (currentCourseInfo != null) {
                    if (UserInfoHelper.getUserInfo() != null) {
                        currentCourseInfo.setUserId(UserInfoHelper.getUserInfo().getUid());
                        ShoppingHelper.saveCourseInfoToDB(currentCourseInfo);
                        ToastUtils.showLong("加入购物车成功");
                    } else {
                        UserInfoHelper.isGotoLogin(NewsWeiKeDetailActivity.this);
                    }
                }
            }
        });

        RxView.clicks(mBuyNowLayout).throttleFirst(200, TimeUnit.MILLISECONDS).subscribe(new Action1<Void>() {
            @Override
            public void call(Void aVoid) {
                if (currentCourseInfo != null) {
                    if (UserInfoHelper.getUserInfo() != null) {
                        currentCourseInfo.setUserId(UserInfoHelper.getUserInfo().getUid());

                        Intent intent = new Intent(NewsWeiKeDetailActivity.this, ConfirmOrderActivity.class);
                        ArrayList<CourseInfo> goodsList = new ArrayList<>();
                        goodsList.add(currentCourseInfo);
                        intent.putExtra("total_price", currentCourseInfo.getMPrice());
                        intent.putParcelableArrayListExtra("goods_list", goodsList);
                        startActivity(intent);
                    } else {
                        UserInfoHelper.isGotoLogin(NewsWeiKeDetailActivity.this);
                    }
                }

            }
        });
    }

    private void initData(CourseInfo courseInfo) {

        if (courseInfo != null) {
            currentCourseInfo = courseInfo;

            title = courseInfo.getTitle();
            mTextViewTitle.setText(title);

            String url = courseInfo.getUrl();
            if (courseInfo.getUrl_type() == 1) {
                //playAudio(url);
            } else if (courseInfo.getUrl_type() == 2) {
                playVideo(url, courseInfo.getImg());
            }

            mLearnCountTextView.setText(courseInfo.getUserNum());
            mNowPriceTextView.setText("¥" + courseInfo.getMPrice());
            mOldPriceTextView.setText("原价:¥" + courseInfo.getPrice());

            if(currentCourseInfo.getIsPay() == 1){
                mIsBuyOrVipLayout.setVisibility(View.GONE);
            }else {
                if (UserInfoHelper.getUserInfo() != null) {
                    if (UserInfoHelper.getUserInfo().getIsVip() == 0) {
                        mIsBuyOrVipLayout.setVisibility(View.VISIBLE);
                    } else {
                        mIsBuyOrVipLayout.setVisibility(View.GONE);
                    }
                }
            }
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

        if (currentCourseInfo != null) {

            //收费
            if (currentCourseInfo.getIsPay() == 0) {
                //未购买
                if (currentCourseInfo.getUserHas() == 0) {
                    if (UserInfoHelper.getUserInfo() != null) {
                        if (UserInfoHelper.getUserInfo().getIsVip() == 0) {
                            isPlay = false;
                        } else {
                            if (currentCourseInfo.getIs_vip() == 1) {
                                isPlay = false;
                            }
                        }
                    } else {
                        isPlay = false;
                    }
                }
            }
        }

        mJCVideoPlayer.startButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (UserInfoHelper.getUserInfo() != null) {
                    if (isPlay) {
                        mJCVideoPlayer.startVideo();
                    } else {
                        final AlertDialog alertDialog = new AlertDialog(NewsWeiKeDetailActivity.this);
                        alertDialog.setDesc("未购买此课程，是否马上购买？");
                        alertDialog.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View v) {
                                alertDialog.dismiss();

                                currentCourseInfo.setUserId(UserInfoHelper.getUserInfo().getUid());
                                Intent intent = new Intent(NewsWeiKeDetailActivity.this, ConfirmOrderActivity.class);
                                ArrayList<CourseInfo> goodsList = new ArrayList<>();
                                goodsList.add(currentCourseInfo);
                                intent.putExtra("total_price", currentCourseInfo.getMPrice());
                                intent.putParcelableArrayListExtra("goods_list", goodsList);
                                startActivity(intent);

                            }
                        });
                        alertDialog.show();
                    }
                } else {
                    UserInfoHelper.isGotoLogin(NewsWeiKeDetailActivity.this);
                }
            }
        });

    }

    @Override
    public void showCourseResult(CourseInfoWrapper data) {
        initWebView(data);
        initData(data.getInfo());
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
                mPresenter.getWeiKeDetail(id, UserInfoHelper.getUserInfo() != null ? UserInfoHelper.getUserInfo().getUid() : "");
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
