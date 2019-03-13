package com.yc.english.group.utils;

import android.content.Context;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.TypeReference;
import com.kk.securityhttp.domain.ResultInfo;
import com.kk.securityhttp.engin.HttpCoreEngin;
import com.kk.securityhttp.net.entry.UpFileInfo;
import com.yc.english.group.constant.NetConstant;
import com.yc.english.group.model.bean.TaskUploadInfo;
import com.yc.english.main.hepler.UserInfoHelper;
import com.yc.english.main.model.domain.URLConfig;
import com.yc.english.main.model.domain.UserInfo;
import com.yc.english.main.model.domain.UserInfoWrapper;
import com.yc.english.news.model.domain.OrderGood;
import com.yc.english.pay.PayWayInfo;
import com.yc.english.pay.alipay.OrderInfo;
import com.yc.english.setting.model.bean.GoodInfoWrapper;
import com.yc.english.setting.model.bean.ShareStateInfo;
import com.yc.english.vip.model.bean.VipGoodInfo;
import com.yc.english.vip.model.bean.VipGoodInfoWrapper;

import java.io.File;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import rx.Observable;
import rx.android.schedulers.AndroidSchedulers;
import rx.functions.Func1;
import rx.schedulers.Schedulers;

/**
 * Created by wanglin  on 2017/8/2 19:30.
 */

public class EngineUtils {


    /**
     * 上传图片，音频，文档
     *
     * @param context
     * @param file
     * @param fileName
     * @param name
     * @return
     */
    public static Observable<ResultInfo<TaskUploadInfo>> uploadFile(Context context, File file, String fileName, String name) {
        UpFileInfo upFileInfo = new UpFileInfo();
        upFileInfo.filename = fileName;
        upFileInfo.file = file;
        upFileInfo.name = "file";

        return HttpCoreEngin.get(context).rxuploadFile(NetConstant.upload_richFile, new TypeReference<ResultInfo<TaskUploadInfo>>() {
        }.getType(), upFileInfo, null, true);

    }


    /**
     * 获取商品列表
     *
     * @param context
     * @param goods_type_id
     * @param page
     * @return
     */
    public static Observable<ResultInfo<GoodInfoWrapper>> getGoodsList(Context context, int goods_type_id, int page) {
        Map<String, String> params = new HashMap<>();
        params.put("goods_type_id", String.valueOf(goods_type_id));
        params.put("page", String.valueOf(page));
        return HttpCoreEngin.get(context).rxpost(NetConstant.goods_getGoodsList, new TypeReference<ResultInfo<GoodInfoWrapper>>() {
        }.getType(), params, true, true, true);
    }

    /**
     * 获取支付列表
     *
     * @param context
     * @return
     */
    public static Observable<ResultInfo<List<PayWayInfo>>> getPayWayList(Context context) {
        return HttpCoreEngin.get(context).rxpost(NetConstant.order_payWayList, new TypeReference<ResultInfo<List<PayWayInfo>>>() {
                }.getType(), null,
                true, true, true);

    }

    /**
     * 创建订单
     *
     * @param context
     * @param title
     * @param price_total
     * @param money
     * @param pay_way_name
     * @param goods_list
     * @return
     */
    public static Observable<ResultInfo<OrderInfo>> createOrder(Context context, String title, String price_total, String money, String pay_way_name, List<OrderGood> goods_list) {

        Map<String, String> params = new HashMap<>();
        UserInfo userInfo = UserInfoHelper.getUserInfo();
        params.put("user_id", userInfo != null ? userInfo.getUid() : "");
        params.put("user_name", userInfo != null ? userInfo.getName() : "");
        params.put("app_id", String.valueOf(1));
        params.put("title", title);
        params.put("price_total", price_total);
        params.put("money", money);
        params.put("pay_way_name", pay_way_name);
        params.put("goods_list", JSON.toJSONString(goods_list));
        return HttpCoreEngin.get(context).rxpost(NetConstant.order_init, new TypeReference<ResultInfo<OrderInfo>>() {
        }.getType(), params, true, true, true);

    }

    /**
     * 统计音频学习人数
     *
     * @param context
     * @param user_id
     * @param news_id
     * @return
     */
    public static Observable<ResultInfo<String>> statisticsStudyTotal(Context context, String user_id, String news_id) {

        Map<String, String> params = new HashMap<>();

        params.put("user_id", user_id);

        params.put("news_id", news_id);

        return HttpCoreEngin.get(context).rxpost(NetConstant.news_weikeStudying, new TypeReference<ResultInfo<String>>() {
        }.getType(), params, true, true, true);
    }

    /**
     * 获取是否开启分享体验VIP
     *
     * @param context
     * @return
     */
    public static Observable<ResultInfo<ShareStateInfo>> getShareVipState(Context context, String user_id) {
        Map<String, String> params = new HashMap<>();
        params.put("user_id", user_id);
        return HttpCoreEngin.get(context).rxpost(NetConstant.share_is_vip, new TypeReference<ResultInfo<ShareStateInfo>>() {
        }.getType(), null, true, true, true);
    }

    public static Observable<ResultInfo<Integer>> getShareVipAllow(Context context, String user_id) {
        Map<String, String> params = new HashMap<>();
        params.put("user_id", user_id);

        return HttpCoreEngin.get(context).rxpost(NetConstant.share_is_allow, new TypeReference<ResultInfo<Integer>>() {
        }.getType(), params, true, true, true);
    }


    /**
     * 使用imel注册
     *
     * @return
     */
    public static Observable<ResultInfo<UserInfo>> guestReg(Context context) {
        return HttpCoreEngin.get(context).rxpost(URLConfig.GUEST_REG_URL, new TypeReference<ResultInfo<UserInfo>>() {
        }.getType(), null, true, true, true);
    }


    public static Observable<ResultInfo<VipGoodInfoWrapper>> getVipGoodInfos(Context context) {
        return HttpCoreEngin.get(context).rxpost(URLConfig.VIP_GOOD_URL, new TypeReference<ResultInfo<VipGoodInfoWrapper>>() {
        }.getType(), null, true, true, true);


    }
}
