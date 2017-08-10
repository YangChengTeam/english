package com.yc.english.group.utils;

import android.content.Context;
import android.text.TextUtils;

import com.alibaba.fastjson.TypeReference;
import com.blankj.utilcode.util.ToastUtils;
import com.hwangjr.rxbus.RxBus;
import com.kk.securityhttp.domain.ResultInfo;
import com.kk.securityhttp.engin.HttpCoreEngin;
import com.kk.securityhttp.net.entry.UpFileInfo;
import com.yc.english.group.constant.BusAction;
import com.yc.english.group.constant.NetConstan;
import com.yc.english.group.model.bean.ClassInfoList;
import com.yc.english.group.model.bean.ClassInfoWarpper;
import com.yc.english.group.model.bean.RemoveGroupInfo;
import com.yc.english.group.model.bean.StudentInfoWrapper;
import com.yc.english.group.model.bean.TaskInfoWrapper;
import com.yc.english.group.model.bean.TaskUploadInfo;
import com.yc.english.group.rong.ImUtils;
import com.yc.english.group.rong.models.CodeSuccessResult;
import com.yc.english.group.rong.models.GroupUser;
import com.yc.english.group.rong.models.GroupUserQueryResult;

import java.io.File;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import rx.Observable;
import rx.android.schedulers.AndroidSchedulers;
import rx.functions.Action1;

/**
 * Created by wanglin  on 2017/8/2 19:30.
 */

public class EngineUtils {


    /**
     * 获取班群成员列表
     * status ：1 已审核
     * status ：0 未审核
     * 查询好友请求列表时不用传sn
     * 查询具体某个班群时不用传master_id
     *
     * @param
     * @param status
     * @return sn群号
     */
    public static Observable<ResultInfo<StudentInfoWrapper>> getMemberList(Context context, String sn, String status, String master_id) {

        Map<String, String> params = new HashMap<>();
        params.put("status", status);
        if (!TextUtils.isEmpty(master_id))
            params.put("master_id", master_id);
        if (!TextUtils.isEmpty(sn))
            params.put("class_id", sn);

        return HttpCoreEngin.get(context).rxpost(NetConstan.memeber_list, new TypeReference<ResultInfo<StudentInfoWrapper>>() {
        }.getType(), params, true, true, true);

    }

    /**
     * 获取我的班群列表
     *
     * @param context
     * @param user_id
     * @param is_admin 1为管理员，0为所有
     * @return
     */
    public static Observable<ResultInfo<ClassInfoList>> getMyGroupList(Context context, String user_id, String is_admin) {

        Map<String, String> params = new HashMap<>();
        params.put("user_id", user_id);
        params.put("is_admin", is_admin);

        return HttpCoreEngin.get(context).rxpost(NetConstan.my_group_list, new TypeReference<ResultInfo<ClassInfoList>>() {
        }.getType(), params, true, true, true);
    }

    /**
     * 根据群号查找群
     *
     * @param context
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

        return HttpCoreEngin.get(context).rxpost(NetConstan.query_group_by_groupId, new TypeReference<ResultInfo<ClassInfoWarpper>>() {
        }.getType(), params, true, true, true);

    }

    /**
     * 加群
     *
     * @param
     * @param usre_id
     * @param groupId
     * @param groupName
     */
    public static void joinGroup(String usre_id, final String groupId, final String groupName) {
        final String[] userIds = new String[]{usre_id};
        ImUtils.queryGroup(groupId).observeOn(AndroidSchedulers.mainThread()).subscribe(new Action1<GroupUserQueryResult>() {
            @Override
            public void call(GroupUserQueryResult groupUserQueryResult) {
                if (groupUserQueryResult.getCode() == 200) {
                    final List<GroupUser> users = groupUserQueryResult.getUsers();
                    ImUtils.joinGroup(userIds, groupId, groupName).observeOn(AndroidSchedulers.mainThread()).subscribe(new Action1<CodeSuccessResult>() {
                        @Override
                        public void call(CodeSuccessResult codeSuccessResult) {
                            if (codeSuccessResult.getCode() == 200) {//加入成功
                                ToastUtils.showShort("加入成功");

//                                mView.startGroupChat(groupId, groupName);
//                                ClassInfo info = new ClassInfo("", groupName, users.size() + "", Integer.parseInt(groupId));
//                                classInfoDao.insert(info);
                                RxBus.get().post(BusAction.GROUPLIST, "from groupjoin");
                            }
                        }
                    });
                } else {
                    ToastUtils.showShort("没有该群组，请重新输入");
                }
            }
        });

    }

    /**
     * @param class_id  班级ID 必传
     * @param name      班级名称 修改班群名称时传该字段
     * @param face      班群图像 修改班群图像时传该字段
     * @param vali_type 验证类型，0：不验证加入，1：验证加入，2：拒绝加入
     * @return
     */
    public static Observable<ResultInfo<RemoveGroupInfo>> changeGroupInfo(Context context, String class_id, String name, String face, String vali_type) {
        Map<String, String> params = new HashMap<>();
        params.put("id", class_id);
        if (!TextUtils.isEmpty(name))
            params.put("name", name);
        if (!TextUtils.isEmpty(face))
            params.put("face", face);
        if (!TextUtils.isEmpty(vali_type))
            params.put("vali_type", vali_type);

        return HttpCoreEngin.get(context).rxpost(NetConstan.change_group_info, new TypeReference<ResultInfo<RemoveGroupInfo>>() {
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
        return HttpCoreEngin.get(context).rxpost(NetConstan.detail_publish_task, new TypeReference<ResultInfo<TaskInfoWrapper>>() {
        }.getType(), params, true, true, true);


    }


    /**
     * 上传图片，音频，文档
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

        return HttpCoreEngin.get(context).rxuploadFile(NetConstan.upload_richFile, new TypeReference<ResultInfo<TaskUploadInfo>>() {
        }.getType(), upFileInfo, null, true);

    }

}
