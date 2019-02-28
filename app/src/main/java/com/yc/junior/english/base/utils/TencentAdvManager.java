package com.yc.junior.english.base.utils;

import android.app.Activity;
import android.util.Log;
import android.view.ViewGroup;

import com.qq.e.ads.banner.AbstractBannerADListener;
import com.qq.e.ads.banner.BannerView;
import com.qq.e.comm.util.AdError;
import com.yc.junior.english.main.model.domain.Constant;

/**
 * Created by wanglin  on 2018/10/11 16:41.
 * 腾讯广告管理类
 */
public class TencentAdvManager {

    private static BannerView banner;

    public static void showBannerAdv(Activity activity, ViewGroup viewGroup, String bannerId) {

        // 创建 Banner 广告 AdView 对象
        // appId : 在 http://e.qq.com/dev/ 能看到的 app 唯一字符串
        // posId : 在 http://e.qq.com/dev/ 生成的数字串，并非 appid 或者 appkey

//        if (banner != null) {
//            banner.destroy();
//        }
        BannerView banner = new BannerView(activity, com.qq.e.ads.banner.ADSize.BANNER, Constant.TENCENT_ADV_ID, bannerId);
        //设置广告轮播时间，为0或30~120之间的数字，单位为s,0标识不自动轮播
        banner.setRefresh(30);
        banner.setADListener(new AbstractBannerADListener() {
            @Override
            public void onNoAD(AdError adError) {
                Log.i("AD_DEMO", "BannerNoAD，eCode=" + adError.getErrorCode());
//                banner.loadAD();
            }

            @Override
            public void onADReceiv() {
                Log.i("AD_DEMO", "ONBannerReceive");
            }

            @Override
            public void onADClicked() {
                super.onADClicked();
                Log.e("AD_DEMO", "onADClicked: ");
            }
        });
        viewGroup.addView(banner);
        /* 发起广告请求，收到广告数据后会展示数据   */
        banner.loadAD();
    }
}
