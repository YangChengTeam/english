package com.kk.securityhttp.net.contains;

import com.kk.securityhttp.domain.GoagalInfo;
import com.kk.securityhttp.net.utils.OKHttpUtil;

import java.util.Map;

import okhttp3.MediaType;

/**
 * Created by zhangkai on 16/9/9.
 */
public class HttpConfig {
    public static final MediaType MEDIA_TYPE = MediaType.parse("text/html");

    public static final int TIMEOUT = 10 * 1000;

    public static final int STATUS_OK = 1;
    public static final int SERVICE_ERROR_CODE = -110;

    public static final int PUBLICKEY_ERROR = -100;

    public static final String NET_ERROR = "网络不给力";
    public static final String SERVICE_ERROR = "服务器出现异常,请重试";
    public static final String JSON_ERROR = "数据解析异常";


    public static void setDefaultParams(Map<String, String> params) {
        OKHttpUtil.setDefaultParams(params);
    }

    public static void setPublickey(String publickey){
        GoagalInfo.get().publicKey = publickey;
    }
}
