package com.yc.english.news.model.domain;

import com.yc.english.base.model.Config;

/**
 * Created by zhangkai on 2017/8/1.
 */

public class URLConfig {
    public static final boolean DEBUG = Config.DEBUG;

    private static String baseUrl = "http://en.wk2.com/api/";

    private static String debugBaseUrl = "http://en.qqtn.com/api/";

    public static final String ORDER_URL = getBaseUrl() + "order/init";

    public static String getBaseUrl() {
        return (DEBUG ? debugBaseUrl : baseUrl);
    }
}
