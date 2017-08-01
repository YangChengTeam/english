package com.yc.english.main.model.domain;

/**
 * Created by zhangkai on 2017/8/1.
 */

public class URLConfig {
    public static final boolean DEBUG = false;

    private static String baseUrl = "http://en.qqtn.com/api/";
    private static String debugBaseUrl = "http://en.qqtn.com/api/";

    public static final String REGISTER_URL = getBaseUrl() + "user/mobile_reg";
    public static final String SEND_CODE_URL = getBaseUrl() +  "user/reg_sendCode";
    public static final String LOGIN_URL = getBaseUrl() +  "user/login";

    public static String getBaseUrl() {
        return (DEBUG ? debugBaseUrl : baseUrl);
    }

}
