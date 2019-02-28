package com.yc.junior.english.community.model.domain;

import com.yc.junior.english.base.model.Config;

/**
 * Created by zhangkai on 2017/8/1.
 */

public class URLConfig {
    public static final boolean DEBUG = Config.DEBUG;

    private static String baseUrl = "http://en.wk2.com/api/";
    private static String debugBaseUrl = "http://en.qqtn.com/api/";

    public static final String COMMUNITY_INFO_URL = getBaseUrl() + "community/community_info";

    public static String getBaseUrl() {
        return (DEBUG ? debugBaseUrl : baseUrl);
    }

}
