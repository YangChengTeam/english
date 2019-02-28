package com.yc.junior.english.vip.utils;

import com.alibaba.fastjson.JSON;
import com.yc.junior.english.main.model.domain.Constant;
import com.yc.junior.english.setting.model.bean.GoodInfo;
import com.yc.junior.english.setting.model.bean.GoodInfoWrapper;

import java.util.List;

import yc.com.blankj.utilcode.util.LogUtils;
import yc.com.blankj.utilcode.util.SPUtils;

/**
 * Created by wanglin  on 2017/12/1 15:18.
 */

public class VipInfoHelper {

    private static GoodInfoWrapper mGoodInfoWrapper;
    private static List<GoodInfo> mGoodInfoList;

//    public static List<GoodInfo> getGoodInfoList() {
//        if (mGoodInfoList != null) {
//            return mGoodInfoList;
//        }
//        String str = SPUtils.getInstance().getString(Constant.VIP_INFO_LIST_INFO);
//        try {
//            mGoodInfoList = JSON.parseArray(str, GoodInfo.class);
//        } catch (Exception e) {
//            e.printStackTrace();
//            LogUtils.e("error:->>" + e.getMessage());
//        }
//        return mGoodInfoList;
//    }
//
//    public static void setGoodInfoList(List<GoodInfo> goodInfoList) {
//        try {
//            Collections.sort(goodInfoList, new Comparator<GoodInfo>() {
//                @Override
//                public int compare(GoodInfo o1, GoodInfo o2) {
//                    return Integer.parseInt(o1.getUse_time_limit()) - Integer.parseInt(o2.getUse_time_limit());
//                }
//            });
//            String json = JSON.toJSONString(goodInfoList);
//            SPUtils.getInstance().put(Constant.VIP_INFO_LIST_INFO, json);
//        } catch (Exception e) {
//            e.printStackTrace();
//            LogUtils.e("error:->>" + e.getMessage());
//        }
//        mGoodInfoList = goodInfoList;
//    }

    public static GoodInfoWrapper getGoodInfoWrapper() {
        if (mGoodInfoWrapper != null) {
            return mGoodInfoWrapper;
        }
        String str = SPUtils.getInstance().getString(Constant.VIP_INFO_LIST_INFO);
        try {
            mGoodInfoWrapper = JSON.parseObject(str, GoodInfoWrapper.class);
        } catch (Exception e) {
            e.printStackTrace();
            LogUtils.e("error:->>" + e.getMessage());
        }

        return mGoodInfoWrapper;
    }

    public static void setGoodInfoWrapper(GoodInfoWrapper goodInfoWrapper) {
        try {

            String json = JSON.toJSONString(goodInfoWrapper);
            SPUtils.getInstance().put(Constant.VIP_INFO_LIST_INFO, json);
        } catch (Exception e) {
            e.printStackTrace();
            LogUtils.e("error:->>" + e.getMessage());
        }
        mGoodInfoWrapper = goodInfoWrapper;
    }
}
