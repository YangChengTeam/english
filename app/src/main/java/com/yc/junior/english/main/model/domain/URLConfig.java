package com.yc.junior.english.main.model.domain;

import com.yc.junior.english.base.model.Config;

/**
 * Created by zhangkai on 2017/8/1.
 */

public class URLConfig {
    public static final boolean DEBUG = Config.DEBUG;

    private static String baseUrl = "http://en.wk2.com/api/";
    private static String debugBaseUrl = "http://en.qqtn.com/api/";

    public static final String REGISTER_URL = getBaseUrl() + "user/mobile_reg";
    public static final String REGISTER_SEND_CODE_URL = getBaseUrl() + "user/reg_sendCode";
    public static final String LOGIN_URL = getBaseUrl() + "user/login";
    public static final String FORGOT_SEND_CODE_URL = getBaseUrl() + "user/forgetPwd_sendCode";
    public static final String FORGOT_URL = getBaseUrl() + "user/reset_pwd";

    public static final String POST_MESSAGE_URL = getBaseUrl() + "user/post_msg";
    public static final String UPD_MESSAGE_URL = getBaseUrl() + "user/upd";
    public static final String UPD_PWD_URL = getBaseUrl() + "user/upd_pwd";


    public static final String INDEX_URL = getBaseUrl() + "index/index";

    public static final String GET_USER_INFO_URL = getBaseUrl() + "user/info";

    public static final String SHARE_INFO_URL = getBaseUrl() + "index/share_info";

    public static final String APPIDS_URL = getBaseUrl() + "index/xfapp_list";

    public static final String NEWS_URL = getBaseUrl() + "news/search";

    public static final String NEWS_INFO_URL = getBaseUrl() + "news/info";

    public static final String WEIKE_CATEGORY_URL = getBaseUrl() + "news/getParentList";

    public static final String WEIKE_UNIT_URL = getBaseUrl() + "news/getChildList";

    public static final String MY_ORDER_URL = getBaseUrl() + "order/lists";

    public static final String Ability_score_url = getBaseUrl() + "UserTest/unitTest";

    public static String getBaseUrl() {
        return (DEBUG ? debugBaseUrl : baseUrl);
    }

}
