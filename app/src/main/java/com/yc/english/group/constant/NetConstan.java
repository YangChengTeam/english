package com.yc.english.group.constant;

/**
 * Created by wanglin  on 2017/7/31 15:10.
 * 网络请求
 */

public interface NetConstan {

    String baser_url = "http://en.qqtn.com/api/";
    /**
     * 获取token
     */


    String get_token = baser_url + "rc/get_token";


    /**
     * 创建班群
     */
    String create_group = baser_url + "class/create";
    /**
     * 群成员
     */

    String memeber_list = baser_url + "class/member_list";


}
