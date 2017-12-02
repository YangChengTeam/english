package com.yc.english.vip.utils;

import com.alibaba.fastjson.JSON;
import com.blankj.utilcode.util.LogUtils;
import com.blankj.utilcode.util.SPUtils;
import com.yc.english.main.model.domain.Constant;
import com.yc.english.pay.PayWayInfo;
import com.yc.english.setting.model.bean.GoodInfo;

import java.util.Collections;
import java.util.Comparator;
import java.util.List;

/**
 * Created by wanglin  on 2017/12/1 15:18.
 */

public class VipInfoHelper {
    private static List<GoodInfo> mGoodInfoList;

    public static List<GoodInfo> getGoodInfoList() {
        if (mGoodInfoList != null) {
            return mGoodInfoList;
        }
        String str = SPUtils.getInstance().getString(Constant.VIP_INFO_LIST_INFO);
        try {
            mGoodInfoList = JSON.parseArray(str, GoodInfo.class);
        } catch (Exception e) {
            e.printStackTrace();
            LogUtils.e("error:->>" + e.getMessage());
        }
        return mGoodInfoList;
    }

    public static void setGoodInfoList(List<GoodInfo> goodInfoList) {
        try {
            Collections.sort(goodInfoList, new Comparator<GoodInfo>() {
                @Override
                public int compare(GoodInfo o1, GoodInfo o2) {
                    return Integer.parseInt(o1.getUse_time_limit()) - Integer.parseInt(o2.getUse_time_limit());
                }
            });
            String json = JSON.toJSONString(goodInfoList);
            SPUtils.getInstance().put(Constant.VIP_INFO_LIST_INFO, json);
        } catch (Exception e) {
            e.printStackTrace();
            LogUtils.e("error:->>" + e.getMessage());
        }
        mGoodInfoList = goodInfoList;
    }
}
