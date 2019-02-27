package com.yc.junior.english.base.helper;

import com.kk.securityhttp.domain.ResultInfo;
import com.kk.securityhttp.net.contains.HttpConfig;

import yc.com.base.EmptyUtils;

/**
 * Created by zhangkai on 2017/8/3.
 */

public class OrderResultInfoHelper {
    public static <T> void handleResultInfo(ResultInfo<T> resultInfo, Callback callback) {
        if (EmptyUtils.isEmpty(resultInfo)) {
            callback.resultInfoEmpty(HttpConfig.NET_ERROR);
            return;
        }

        if (resultInfo.code == HttpConfig.STATUS_OK) {
            callback.reulstInfoOk();
        } else if (resultInfo.code == 2) {
            callback.reulstInfoIsBuy();
        } else {
            callback.resultInfoNotOk(getMessage(resultInfo.message, HttpConfig.SERVICE_ERROR));
        }
    }

    public static String getMessage(String message, String desc) {
        return EmptyUtils.isEmpty(message) ? desc : message;
    }

    public interface Callback {
        void resultInfoEmpty(String message);

        void resultInfoNotOk(String message);

        void reulstInfoOk();

        void reulstInfoIsBuy();
    }

}
