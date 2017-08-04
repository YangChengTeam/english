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
    /**
     * 申请加入班群
     */
    String apply_join_group = baser_url + "class/join";
    /**
     * 我的班群列表
     */

    String my_group_list = baser_url + "class/myclass_list";

    /**
     * 同意加入班群
     */
    String agree_join_group = baser_url + "class/agree";
    /**
     * 拒绝加入班群
     */
    String refuse_join_group = baser_url + "class/refuse";
    /**
     * 移除班级成员
     */

    String del_group_member = baser_url + "class/del_member";
    /**
     * 转让班群
     */
    String transfer_group = baser_url + "class/change_master";
    /**
     * 移除班群
     */

    String remove_group = baser_url + "class/remove";
    /**
     * 根据群号查找群
     */

    String query_group_by_groupId = baser_url + "class/info";

    /**
     * 修改班群信息
     */
    String change_group_info = baser_url + "class/upd";
}
