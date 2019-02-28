package com.yc.junior.english.weixin.views.fragments;

import android.content.Intent;
import android.net.Uri;
import android.os.Handler;
import android.text.TextUtils;
import android.view.View;
import android.webkit.WebChromeClient;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.FrameLayout;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;

import com.alibaba.fastjson.JSON;
import com.hwangjr.rxbus.RxBus;
import com.kk.securityhttp.domain.ResultInfo;
import com.kk.securityhttp.net.contains.HttpConfig;
import com.kk.utils.LogUtil;
import com.kk.utils.ToastUtil;
import com.umeng.analytics.MobclickAgent;
import com.yc.junior.english.R;
import com.yc.junior.english.base.utils.StatusBarCompat;
import com.yc.junior.english.base.view.BaseActivity;
import com.yc.junior.english.base.view.BaseFragment;
import com.yc.junior.english.base.view.CommonWebView;
import com.yc.junior.english.base.view.LoadingDialog;
import com.yc.junior.english.base.view.WebActivity;
import com.yc.junior.english.group.utils.EngineUtils;
import com.yc.junior.english.main.hepler.BannerImageLoader;
import com.yc.junior.english.main.hepler.UserInfoHelper;
import com.yc.junior.english.main.model.domain.Constant;
import com.yc.junior.english.main.model.domain.SlideInfo;
import com.yc.junior.english.main.model.domain.UserInfo;
import com.yc.junior.english.news.model.domain.OrderGood;
import com.yc.junior.english.news.utils.SmallProcedureUtils;
import com.yc.junior.english.pay.alipay.IAliPay1Impl;
import com.yc.junior.english.pay.alipay.IPayCallback;
import com.yc.junior.english.pay.alipay.IWXPay1Impl;
import com.yc.junior.english.pay.alipay.OrderInfo;
import com.yc.junior.english.setting.model.bean.Config;
import com.yc.junior.english.weixin.contract.CourseTypeContract;
import com.yc.junior.english.weixin.model.domain.CourseInfo;
import com.yc.junior.english.weixin.presenter.CourseTypePresenter;
import com.youth.banner.Banner;
import com.youth.banner.listener.OnBannerListener;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import butterknife.BindView;
import rx.functions.Action1;
import yc.com.base.EmptyUtils;
import yc.com.blankj.utilcode.util.SPUtils;
import yc.com.blankj.utilcode.util.ToastUtils;
import yc.com.blankj.utilcode.util.UIUitls;

/**
 * Created by zhangkai on 2017/8/30.
 */

public class CourseTypeFragment extends BaseFragment<CourseTypePresenter> implements CourseTypeContract.View {


    @BindView(R.id.toolbar)
    RelativeLayout mToolbar;

    @BindView(R.id.toolbarWarpper)
    FrameLayout mToolbarWarpper;
    @BindView(R.id.wv_main)
    CommonWebView wvMain;

    @BindView(R.id.content)
    LinearLayout content;
    @BindView(R.id.banner)
    Banner mBanner;
    @BindView(R.id.progress_bar)
    ProgressBar progressBar;

    private String url = "http://m.upkao.com/ssyy.html";

    private IAliPay1Impl iAliPay;
    private IWXPay1Impl iwxPay;
    private LoadingDialog loadingDialog;

    private Handler mHandler;

    @Override
    public void init() {
        mPresenter = new CourseTypePresenter(getActivity(), this);
        StatusBarCompat.compat((BaseActivity) getActivity(), mToolbarWarpper, mToolbar, R.mipmap.base_actionbar);
        mHandler = new Handler();

        initWebview();
        iAliPay = new IAliPay1Impl(getActivity());
        iwxPay = new IWXPay1Impl(getActivity());
        loadingDialog = new LoadingDialog(getActivity());
    }

    private void initWebview() {


        progressBar.setMax(100);

        SlideInfo slideInfo = JSON.parseObject(SPUtils.getInstance().getString(Constant.INDEX_MENU_STATICS), SlideInfo.class);
        if (null != slideInfo) {
            url = slideInfo.getUrl();
        }
//        url += "?time=" + System.currentTimeMillis();


        wvMain.addJavascriptInterface(new JavascriptInterface(), "study");


//        wvMain.loadUrl("file:///android_asset/m/ssyy.html");
        wvMain.loadUrl(url);
        wvMain.setWebViewClient(new WebViewClient() {
            @Override
            public void onPageFinished(WebView view, String url) {

            }

        });
        wvMain.setWebChromeClient(new WebChromeClient() {
            @Override
            public void onProgressChanged(WebView view, int newProgress) {
                super.onProgressChanged(view, newProgress);

                final int progress = newProgress;
                mHandler.post(new Runnable() {
                    @Override
                    public void run() {
                        // TODO Auto-generated method stub
                        if (progressBar.getVisibility() == ProgressBar.GONE) {
                            progressBar.setVisibility(ProgressBar.VISIBLE);
                        }
                        progressBar.setProgress(progress);
                        progressBar.postInvalidate();
                        if (progress == 100) {
                            progressBar.setVisibility(View.GONE);
                        }

                    }
                });


            }
        });

        initBanner();

    }


    @Override
    public int getLayoutId() {

        return R.layout.weixin_fragment_course_type;
    }

    @Override
    public void showBanner(List<String> images) {
        mBanner.isAutoPlay(true)
                .setDelayTime(3000)
                .setImageLoader(new BannerImageLoader())
                .setImages(images)
                .start();
    }


    private void initBanner() {
        mBanner.setFocusable(false);
        mBanner.setOnBannerListener(new OnBannerListener() {

            @Override
            public void OnBannerClick(int position) {
                SlideInfo slideInfo = mPresenter.getSlideInfo(position);
                //友盟统计各个幻灯点击数
                MobclickAgent.onEvent(getActivity(), slideInfo.getStatistics());
                if (slideInfo.getType().equals("0")) {
                    if (EmptyUtils.isEmpty(slideInfo.getTypeValue())) {
                        return;
                    }
                    Intent intent = new Intent(getActivity(), WebActivity.class);
                    intent.putExtra("title", slideInfo.getTitle());
                    intent.putExtra("url", slideInfo.getTypeValue());
                    startActivity(intent);
                } else if (slideInfo.getType().equals("1")) {
                    try {
                        String typeValue = slideInfo.getTypeValue();
                        if (TextUtils.isEmpty(typeValue)) return;
                        String[] split = typeValue.split("\\|");
                        Class clazz = Class.forName(split[0]);
                        Intent intent = new Intent(getActivity(), clazz);
                        if (split.length == 2) {
                            CourseInfo courseInfo = new CourseInfo();
                            courseInfo.setId(split[1]);
                            intent.putExtra("info", courseInfo);
                        }
                        startActivity(intent);
                    } catch (Exception e) {

                    }
                } else if (slideInfo.getType().equals("2")) {
                    try {
                        String typeValue = slideInfo.getTypeValue();
                        if (TextUtils.isEmpty(typeValue)) return;
                        String[] strs = typeValue.split("\\|");
                        LogUtil.msg("tag: " + strs[0] + "---" + strs[1]);
                        if (strs.length > 1) {
                            // 填应用AppId
                            String appId = strs[1];
                            String originId = strs[0];
                            SmallProcedureUtils.switchSmallProcedure(getActivity(), originId, appId);
                        }
                    } catch (Exception e) {
                        LogUtil.msg("e :" + e.getMessage());
                        ToastUtil.toast(getActivity(), "");
                    }
                }
            }
        });
    }


    public class JavascriptInterface {

        @android.webkit.JavascriptInterface
        public void startQQChat(String qq) {
            try {
                String url3521 = "mqqwpa://im/chat?chat_type=wpa&uin=" + qq;
                startActivity(new Intent(Intent.ACTION_VIEW, Uri.parse(url3521)));
            } catch (Exception e) {
                ToastUtils.showShort("你的手机还未安装qq,请先安装");
            }

        }

        @android.webkit.JavascriptInterface
        public void pay(final String title, final String money, String paywayname, String id) {
            if (UserInfoHelper.getUserInfo() == null) {
                UserInfoHelper.isGotoLogin(getActivity());
                return;
            }

            if (TextUtils.isEmpty(paywayname)) {
                paywayname = "alipay";
            }
            List<OrderGood> list = new ArrayList<>();
            OrderGood orderGood = new OrderGood();
            orderGood.setGood_id(id);
            orderGood.setNum(1);
            list.add(orderGood);

            showLoading();
            final String finalPaywayname = paywayname;
            EngineUtils.createOrder(getActivity(), title, money, money, paywayname, list)
                    .subscribe(new Action1<ResultInfo<OrderInfo>>() {
                                   @Override
                                   public void call(ResultInfo<OrderInfo> orderInfoResultInfo) {
                                       dismissLoading();
                                       if (orderInfoResultInfo != null) {
                                           if (orderInfoResultInfo.code == HttpConfig.STATUS_OK && orderInfoResultInfo.data != null) {
                                               OrderInfo orderInfo = orderInfoResultInfo.data;
                                               orderInfo.setMoney(Float.parseFloat(money));
                                               orderInfo.setName(title);
                                               if (finalPaywayname.equals("alipay")) {
                                                   iAliPay.pay(orderInfo, payCallBack);
                                               } else {
                                                   iwxPay.pay(orderInfo, payCallBack);
                                               }
                                           } else {
                                               ToastUtil.toast2(getActivity(), orderInfoResultInfo.message);
                                           }
                                       }

                                   }
                               }
                    );


        }

        private IPayCallback payCallBack = new IPayCallback() {

            @Override
            public void onSuccess(OrderInfo orderInfo) {
                UserInfo userInfo = UserInfoHelper.getUserInfo();
                userInfo.setIsVip(1);
                Date date = new Date();
                userInfo.setVip_start_time(date.getTime() / 1000);


                long vip_end_time = date.getTime() + 3 * 12 * 30 * (Config.MS_IN_A_DAY);
                userInfo.setVip_end_time(vip_end_time / 1000);

                UserInfoHelper.saveUserInfo(userInfo);
                RxBus.get().post(Constant.COMMUNITY_ACTIVITY_REFRESH, "form pay");
                RxBus.get().post(Constant.USER_INFO, userInfo);
                dismissPayDialog();

            }

            @Override
            public void onFailure(OrderInfo orderInfo) {
                dismissPayDialog();
            }
        };

        private void showLoading() {
            if (loadingDialog != null) {
                UIUitls.post(new Runnable() {
                    @Override
                    public void run() {
                        loadingDialog.setMessage("创建订单中，请稍候...");
                        loadingDialog.show();
                    }
                });
            }

        }

        private void dismissLoading() {
            UIUitls.post(new Runnable() {
                @Override
                public void run() {
                    loadingDialog.dismiss();
                }
            });
        }

        private void dismissPayDialog() {

            UIUitls.post(new Runnable() {
                @Override
                public void run() {
                    wvMain.loadUrl("javascript:hidePay()");
                }
            });
        }


    }


}
