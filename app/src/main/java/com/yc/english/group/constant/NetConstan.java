package com.yc.english.group.constant;

import com.yc.english.base.model.Config;

/**
 * Created by wanglin  on 2017/7/31 15:10.
 * 网络请求
 */

public interface NetConstan {


    boolean isDebug = Config.DEBUG;

    String debug_base_url = "http://en.qqtn.com/api/";

    String baser_url = "http://en.wk2.com/api/";

    /**
     * 获取token
     */

    String get_token = (isDebug ? debug_base_url : baser_url) + "rc/get_token";


    /**
     * 创建班群
     */
    String create_group = (isDebug ? debug_base_url : baser_url) + "class/create";
    /**
     * 群成员
     */

    String memeber_list = (isDebug ? debug_base_url : baser_url) + "class/member_list";
    /**
     * 申请加入班群
     */
    String apply_join_group = (isDebug ? debug_base_url : baser_url) + "class/join";
    /**
     * 我的班群列表
     */

    String my_group_list = (isDebug ? debug_base_url : baser_url) + "class/myclass_list";

    /**
     * 同意加入班群
     */
    String agree_join_group = (isDebug ? debug_base_url : baser_url) + "class/agree";
    /**
     * 拒绝加入班群
     */
    String refuse_join_group = (isDebug ? debug_base_url : baser_url) + "class/refuse";
    /**
     * 移除班级成员
     */

    String del_group_member = (isDebug ? debug_base_url : baser_url) + "class/del_member";
    /**
     * 转让班群
     */
    String transfer_group = (isDebug ? debug_base_url : baser_url) + "class/change_master";
    /**
     * 移除班群
     */

    String remove_group = (isDebug ? debug_base_url : baser_url) + "class/remove";
    /**
     * 根据群号查找群
     */

    String query_group_by_groupId = (isDebug ? debug_base_url : baser_url)+ "class/info";

    /**
     * 修改班群信息
     */
    String change_group_info = (isDebug ? debug_base_url : baser_url) + "class/upd";
    /**
     * 发布作业
     */

    String publish_task = (isDebug ? debug_base_url : baser_url) + "task/publish";
    /**
     * 修改作业
     */

    String update_publish_task =(isDebug ? debug_base_url : baser_url) + "task/upd_publish";
    /**
     * 作业详情
     */
    String detail_publish_task = (isDebug ? debug_base_url : baser_url) + "task/detail_publish";

    /**
     * 学生提交作业
     */
    String do_task = (isDebug ? debug_base_url : baser_url) + "task/done";
    /**
     * 学生修改作业
     */
    String update_do_task = (isDebug ? debug_base_url : baser_url) + "task/upd_do";
    /**
     * 学生完成作业详情
     */
    String detail_do_task = (isDebug ? debug_base_url : baser_url)+ "task/detail_do";
    /**
     * 发布作业列表
     */
    String list_publish_task = (isDebug ? debug_base_url : baser_url)+ "task/list_publish";
    /**
     * 完成作业列表
     */
    String list_do_task = (isDebug ? debug_base_url : baser_url) + "task/list_do";
    /**
     * 上传
     */
    String upload_richFile = (isDebug ? debug_base_url : baser_url) + "task/upload";
    /**
     * 是否查阅
     */
    String isRead_member_list = (isDebug ? debug_base_url : baser_url) + "task/isRead_member_list";
    /**
     * 老师评分
     */

    String task_score =(isDebug ? debug_base_url : baser_url)+ "task/score";
    /**
     * 是否完成
     */

    String isDone_member_list = (isDebug ? debug_base_url : baser_url) + "task/isDone_member_list";
    /**
     * 名师辅导
     */

    String index_comm_class_list = (isDebug ? debug_base_url : baser_url) + "index/comm_class_list";

    /**
     * 作业详情2
     */

    String detail_do_task2 = (isDebug ? debug_base_url : baser_url) + "task/detail_do2";

    /**
     * 是否是班群成员
     */
    String is_member =(isDebug ? debug_base_url : baser_url) + "class/is_member";
}
