package com.yc.english.group.utils;

import android.content.Context;
import android.text.TextUtils;

import com.alibaba.fastjson.TypeReference;
import com.blankj.utilcode.util.ToastUtils;
import com.hwangjr.rxbus.RxBus;
import com.kk.securityhttp.domain.ResultInfo;
import com.kk.securityhttp.engin.HttpCoreEngin;
import com.yc.english.group.constant.BusAction;
import com.yc.english.group.constant.NetConstan;
import com.yc.english.group.model.bean.ClassInfoWarpper;
import com.yc.english.group.model.bean.StudentInfoWrapper;
import com.yc.english.group.rong.ImUtils;
import com.yc.english.group.rong.models.CodeSuccessResult;
import com.yc.english.group.rong.models.GroupUser;
import com.yc.english.group.rong.models.GroupUserQueryResult;

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


}
