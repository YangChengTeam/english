package com.yc.english.pay;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.blankj.utilcode.util.LogUtils;
import com.blankj.utilcode.util.SPUtils;
import com.yc.english.main.model.domain.Constant;

import java.util.List;

/**
 * Created by wanglin  on 2017/11/8 14:12.
 */

public class PayWayInfoHelper {

    public static List<PayWayInfo> mPayWayInfoList;

    public static List<PayWayInfo> getPayWayInfoList() {
        if (mPayWayInfoList != null) {
            return mPayWayInfoList;
        }
        String result = SPUtils.getInstance().getString(Constant.PAY_WAY_LIST_INFO);

        try {
            mPayWayInfoList = JSON.parseArray(result, PayWayInfo.class);
        } catch (Exception e) {
            e.printStackTrace();
            LogUtils.e("error:->>" + e.getMessage());
        }
        return mPayWayInfoList;
    }

    public static void setPayWayInfoList(List<PayWayInfo> payWayInfoList) {

        try {
            String json = JSON.toJSONString(payWayInfoList);

            SPUtils.getInstance().put(Constant.PAY_WAY_LIST_INFO, json);

        } catch (Exception e) {
            e.printStackTrace();
            LogUtils.e("error:->>" + e.getMessage());
        }
        mPayWayInfoList = payWayInfoList;

    }


}
