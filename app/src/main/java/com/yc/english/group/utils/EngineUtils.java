package com.yc.english.group.utils;

import android.content.Context;
import android.text.TextUtils;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.TypeReference;
import com.kk.securityhttp.domain.ResultInfo;
import com.kk.securityhttp.engin.HttpCoreEngin;
import com.kk.securityhttp.net.entry.UpFileInfo;
import com.yc.english.group.constant.NetConstant;
import com.yc.english.group.model.bean.ClassInfoList;
import com.yc.english.group.model.bean.ClassInfoWarpper;
import com.yc.english.group.model.bean.GroupApplyInfo;
import com.yc.english.group.model.bean.MemberInfo;
import com.yc.english.group.model.bean.RemoveGroupInfo;
import com.yc.english.group.model.bean.StudentInfoWrapper;
import com.yc.english.group.model.bean.StudentRemoveInfo;
import com.yc.english.group.model.bean.TaskAllInfoWrapper;
import com.yc.english.group.model.bean.TaskInfoWrapper;
import com.yc.english.group.model.bean.TaskUploadInfo;
import com.yc.english.group.rong.models.CodeSuccessResult;
import com.yc.english.group.rong.models.ListGagGroupUserResult;
import com.yc.english.group.rong.util.RongIMUtil;
import com.yc.english.main.hepler.UserInfoHelper;
import com.yc.english.main.model.domain.UserInfo;
import com.yc.english.news.model.domain.OrderGood;
import com.yc.english.pay.alipay.OrderInfo;
import com.yc.english.setting.model.bean.GoodInfoWrapper;
import com.yc.english.pay.PayWayInfo;
import com.yc.english.setting.model.bean.ShareStateInfo;

import java.io.File;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import rx.Observable;

/**
 * Created by wanglin  on 2017/8/2 19:30.
 */

public class EngineUtils {


    /**
     * 获取班群成员列表
     * status ：1 已审核
     * status ：0 未审核
     * 查询好友请求列表时不用传class_id
     * 查询具体某个班群时不用传master_id
     * type 0为作业班群，1为英语公会，2为名师辅导
     * page 分页的页码
     *
     * @param page_size 每页请求的条数
     * @param
     * @param status
     * @return sn群号
     */
    public static Observable<ResultInfo<StudentInfoWrapper>> getMemberList(Context context, String class_id, int page, int page_size, String status, String master_id, String type) {

        Map<String, String> params = new HashMap<>();
        params.put("status", status);
        if (!TextUtils.isEmpty(master_id))
            params.put("master_id", master_id);
        if (!TextUtils.isEmpty(class_id))
            params.put("class_id", class_id);
        params.put("type", type);
        params.put("page", page + "");
        params.put("page_size", page_size + "");


        return HttpCoreEngin.get(context).rxpost(NetConstant.member_list, new TypeReference<ResultInfo<StudentInfoWrapper>>() {
        }.getType(), params, true, true, true);

    }

    /**
     * 获取我的班群列表
     *
     * @param context
     * @param user_id
     * @param role    2为管理员，0为普通成员 -1所有
     * @param type    0 作业 1.公会 2 名师辅导
     * @return
     */
    public static Observable<ResultInfo<ClassInfoList>> getMyGroupList(Context context, String user_id, String role, String type) {

        Map<String, String> params = new HashMap<>();
        params.put("user_id", user_id);
        params.put("role", role);
        params.put("type", type);

        return HttpCoreEngin.get(context).rxpost(NetConstant.my_group_list, new TypeReference<ResultInfo<ClassInfoList>>() {
        }.getType(), params, true, true, true);
    }

    /**
     * 根据群号查找群
     *
     * @param context 上下文
     * @param id      班级号
     * @param sn      群号
     *                两者传其中一个即可，必须传
     * @return
     */
    public static Observable<ResultInfo<ClassInfoWarpper>> queryGroupById(Context context, String id, String sn) {
        Map<String, String> params = new HashMap<>();
        if (!TextUtils.isEmpty(id))
            params.put("id", id);
        if (!TextUtils.isEmpty(sn))
            params.put("sn", sn);

        return HttpCoreEngin.get(context).rxpost(NetConstant.query_group_by_groupId, new TypeReference<ResultInfo<ClassInfoWarpper>>() {
        }.getType(), params, true, true, true);

    }


    /**
     * @param class_id      班级ID 必传
     * @param name          班级名称 修改班群名称时传该字段
     * @param face          班群图像 修改班群图像时传该字段
     * @param vali_type     验证类型，0：不验证加入，1：验证加入，2：拒绝加入
     * @param is_allow_talk 禁言状态 0：开启全员禁言 1：关闭全员禁言
     * @return
     */
    public static Observable<ResultInfo<RemoveGroupInfo>> changeGroupInfo(Context context, String class_id, String name, String face, String vali_type, String is_allow_talk) {
        Map<String, String> params = new HashMap<>();
        params.put("id", class_id);
        if (!TextUtils.isEmpty(name))
            params.put("name", name);
        if (!TextUtils.isEmpty(face))
            params.put("face", face);
        if (!TextUtils.isEmpty(vali_type))
            params.put("vali_type", vali_type);
        if (!TextUtils.isEmpty(is_allow_talk)) {
            params.put("is_allow_talk", is_allow_talk);
        }

        return HttpCoreEngin.get(context).rxpost(NetConstant.change_group_info, new TypeReference<ResultInfo<RemoveGroupInfo>>() {
        }.getType(), params, false, true, true);

    }

    /**
     * 布置作业详情
     *
     * @param task_id
     * @param class_id
     * @param user_id
     * @return
     */

    public static Observable<ResultInfo<TaskInfoWrapper>> getPublishTaskDetail(Context context, String task_id, String class_id, String user_id) {
        Map<String, String> params = new HashMap<>();
        params.put("task_id", task_id);
        if (!TextUtils.isEmpty(class_id))
            params.put("class_id", class_id);
        if (!TextUtils.isEmpty(user_id))
            params.put("user_id", user_id);
        return HttpCoreEngin.get(context).rxpost(NetConstant.detail_publish_task, new TypeReference<ResultInfo<TaskInfoWrapper>>() {
        }.getType(), params, true, true, true);


    }


    /**
     * 上传图片，音频，文档
     *
     * @param context
     * @param file
     * @param fileName
     * @param name
     * @return
     */
    public static Observable<ResultInfo<TaskUploadInfo>> uploadFile(Context context, File file, String fileName, String name) {
        UpFileInfo upFileInfo = new UpFileInfo();
        upFileInfo.filename = fileName;
        upFileInfo.file = file;
        upFileInfo.name = "file";

        return HttpCoreEngin.get(context).rxuploadFile(NetConstant.upload_richFile, new TypeReference<ResultInfo<TaskUploadInfo>>() {
        }.getType(), upFileInfo, null, true);

    }


    /**
     * 获取完成作业详情
     *
     * @param context
     * @param id
     * @param user_id
     * @return
     */
    public static Observable<ResultInfo<TaskInfoWrapper>> getDoneTaskDetail(Context context, String id, String user_id) {

        Map<String, String> params = new HashMap<>();
        params.put("id", id);
        params.put("user_id", user_id);

        return HttpCoreEngin.get(context).rxpost(NetConstant.detail_do_task, new TypeReference<ResultInfo<TaskInfoWrapper>>() {
        }.getType(), params, true, true, true);

    }


    /**
     * 获取完成作业详情2
     *
     * @param context
     * @param task_id
     * @param user_id
     * @return
     */
    public static Observable<ResultInfo<TaskInfoWrapper>> getDoneTaskDetail2(Context context, String task_id, String user_id) {

        Map<String, String> params = new HashMap<>();
        params.put("task_id", task_id);
        params.put("user_id", user_id);

        return HttpCoreEngin.get(context).rxpost(NetConstant.detail_do_task2, new TypeReference<ResultInfo<TaskInfoWrapper>>() {
        }.getType(), params, true, true, true);

    }

    /**
     * 申请加入班群
     *
     * @param context
     * @param user_id
     * @param sn
     * @return
     */
    public static Observable<ResultInfo<GroupApplyInfo>> applyJoinGroup(Context context, String user_id, String sn) {

        Map<String, String> params = new HashMap<>();
        params.put("user_id", user_id);
        params.put("sn", sn);
        return HttpCoreEngin.get(context).rxpost(NetConstant.apply_join_group, new TypeReference<ResultInfo<GroupApplyInfo>>() {
        }.getType(), params, true, true, true);

    }

    /**
     * 获取发布作业列表
     *
     * @param context
     * @param publisher
     * @param class_id
     * @return
     */
    public static Observable<ResultInfo<TaskAllInfoWrapper>> getPublishTaskList(Context context, String publisher, String class_id) {

        Map<String, String> params = new HashMap<>();
        if (!TextUtils.isEmpty(publisher))
            params.put("publisher", publisher);
        if (!TextUtils.isEmpty(class_id))
            params.put("class_id", class_id);

        return HttpCoreEngin.get(context).rxpost(NetConstant.list_publish_task, new TypeReference<ResultInfo<TaskAllInfoWrapper>>() {
        }.getType(), params, true, true, true);

    }

    /**
     * 移除班群 和退出班群
     *
     * @param context
     * @param class_id
     * @param master_id
     * @param members
     * @return
     */
    public static Observable<ResultInfo<StudentRemoveInfo>> deleteMember(Context context, String class_id, String master_id, String members) {
        Map<String, Object> params = new HashMap<>();
        params.put("class_id", class_id);//群号
        params.put("master_id", master_id);
        params.put("members", members);
        return HttpCoreEngin.get(context).rxpost(NetConstant.del_group_member, new TypeReference<ResultInfo<StudentRemoveInfo>>() {
        }.getType(), params, true, true, true);
    }

    /**
     * 获取禁言成员
     *
     * @param groupId
     * @return
     */
    public static Observable<ListGagGroupUserResult> lisGagUser(String groupId) {
        return RongIMUtil.lisGagUser(groupId);
    }

    /**
     * 添加禁言成员
     *
     * @param userId
     * @param groupId
     * @param minute
     * @return
     */
    public static Observable<CodeSuccessResult> addForbidMember(String userId, String groupId, String minute) {
        return RongIMUtil.addGagUser(userId, groupId, minute);
    }


    public static Observable<ResultInfo<ClassInfoList>> getUnionList(Context context, String type, int page, int page_size, String user_id) {

        Map<String, Object> params = new HashMap<>();
        params.put("type", type);
        params.put("page", page);
        params.put("page_size", page_size);
        params.put("user_id", user_id);
        return HttpCoreEngin.get(context).rxpost(NetConstant.union_class_list, new TypeReference<ResultInfo<ClassInfoList>>() {
        }.getType(), params, true, true, true);

    }


    /**
     * 是否是班群成员
     *
     * @param context
     * @param class_id
     * @param user_id
     * @return
     */
    public static Observable<ResultInfo<MemberInfo>> isGroupMember(Context context, String class_id, String user_id) {
        Map<String, String> params = new HashMap<>();
        params.put("class_id", class_id);
        params.put("user_id", user_id);
        return HttpCoreEngin.get(context).rxpost(NetConstant.is_member, new TypeReference<ResultInfo<MemberInfo>>() {
        }.getType(), params, true, true, true);
    }

    /**
     * 获取名师辅导班群列表
     *
     * @return
     */
    public static Observable<ResultInfo<ClassInfoList>> getCommonClassList(Context context) {

        return HttpCoreEngin.get(context).rxpost(NetConstant.index_comm_class_list, new TypeReference<ResultInfo<ClassInfoList>>() {
        }.getType(), null, true, true, true);
    }

    /**
     * 获取商品列表
     *
     * @param context
     * @param goods_type_id
     * @param page
     * @return
     */
    public static Observable<ResultInfo<GoodInfoWrapper>> getGoodsList(Context context, int goods_type_id, int page) {
        Map<String, String> params = new HashMap<>();
        params.put("goods_type_id", String.valueOf(goods_type_id));
        params.put("page", String.valueOf(page));
        return HttpCoreEngin.get(context).rxpost(NetConstant.goods_getGoodsList, new TypeReference<ResultInfo<GoodInfoWrapper>>() {
        }.getType(), params, true, true, true);
    }

    /**
     * 获取支付列表
     *
     * @param context
     * @return
     */
    public static Observable<ResultInfo<List<PayWayInfo>>> getPayWayList(Context context) {
        return HttpCoreEngin.get(context).rxpost(NetConstant.order_payWayList, new TypeReference<ResultInfo<List<PayWayInfo>>>() {
                }.getType(), null,
                true, true, true);

    }

    /**
     * 创建订单
     *
     * @param context
     * @param title
     * @param price_total
     * @param money
     * @param pay_way_name
     * @param goods_list
     * @return
     */
    public static Observable<ResultInfo<OrderInfo>> createOrder(Context context, String title, String price_total, String money, String pay_way_name, List<OrderGood> goods_list) {

        Map<String, String> params = new HashMap<>();
        UserInfo userInfo = UserInfoHelper.getUserInfo();
        params.put("user_id", UserInfoHelper.getUserInfo().getUid());
        params.put("user_name", userInfo.getName());
        params.put("app_id", String.valueOf(1));
        params.put("title", title);
        params.put("price_total", price_total);
        params.put("money", money);
        params.put("pay_way_name", pay_way_name);
        params.put("goods_list", JSON.toJSONString(goods_list));
        return HttpCoreEngin.get(context).rxpost(NetConstant.order_init, new TypeReference<ResultInfo<OrderInfo>>() {
        }.getType(), params, true, true, true);

    }

    /**
     * 统计音频学习人数
     *
     * @param context
     * @param user_id
     * @param news_id
     * @return
     */
    public static Observable<ResultInfo<String>> statisticsStudyTotal(Context context, String user_id, String news_id) {

        Map<String, String> params = new HashMap<>();

        params.put("user_id", user_id);

        params.put("news_id", news_id);

        return HttpCoreEngin.get(context).rxpost(NetConstant.news_weikeStudying, new TypeReference<ResultInfo<String>>() {
        }.getType(), params, true, true, true);
    }

    /**
     * 获取是否开启分享体验VIP
     * @param context
     * @return
     */
    public static Observable<ResultInfo<ShareStateInfo>> getShareVipState(Context context) {
        return HttpCoreEngin.get(context).rxpost(NetConstant.share_is_vip, new TypeReference<ResultInfo<ShareStateInfo>>() {
        }.getType(), null, true, true, true);
    }

    public static Observable<ResultInfo> getShareVipAllow(Context context, String user_id) {
        Map<String, String> params = new HashMap<>();
        params.put("user_id", user_id);

        return HttpCoreEngin.get(context).rxpost(NetConstant.share_is_allow, new TypeReference<ResultInfo>() {
        }.getType(), params, true, true, true);
    }

}
