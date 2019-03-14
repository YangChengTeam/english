package com.yc.junior.english.community.model.engin;

import android.content.Context;

import com.alibaba.fastjson.TypeReference;
import com.kk.securityhttp.domain.ResultInfo;
import com.kk.securityhttp.engin.HttpCoreEngin;
import com.kk.securityhttp.net.entry.UpFileInfo;
import com.yc.junior.english.community.model.domain.CommentInfo;
import com.yc.junior.english.community.model.domain.CommentInfoList;
import com.yc.junior.english.community.model.domain.CommunityInfo;
import com.yc.junior.english.community.model.domain.CommunityInfoList;
import com.yc.junior.english.main.hepler.UserInfoHelper;
import com.yc.junior.english.read.model.domain.URLConfig;

import java.util.HashMap;
import java.util.Map;

import rx.Observable;
import yc.com.base.BaseEngine;
import yc.com.blankj.utilcode.util.EncryptUtils;

import static com.umeng.socialize.utils.DeviceConfig.context;


/**
 * Created by admin on 2017/8/30.
 */

public class CommunityInfoEngin extends BaseEngine {

    public CommunityInfoEngin(Context context) {
        super(context);
    }

    public Observable<ResultInfo<CommunityInfoList>> communityInfoList(String userId, int type, int currentPage, int pageCount) {
        Map<String, String> params = new HashMap<>();
        params.put("user_id", userId);
        params.put("type", type + "");
        params.put("page", currentPage + "");
        params.put("limit", pageCount + "");

        return HttpCoreEngin.get(context).rxpost(URLConfig.NOTE_LIST_URL, new TypeReference<ResultInfo<CommunityInfoList>>() {
                }.getType(), params,
                true, true,
                true);
    }

    public Observable<ResultInfo<CommunityInfo>> addCommunityInfo(CommunityInfo communityInfo, UpFileInfo upFileInfo) {
        Map<String, String> params = new HashMap<>();
        params.put("user_id", communityInfo.getUserId());
        params.put("content", communityInfo.getContent());
        params.put("type", communityInfo.getcType());

        Map<String, String> headParams = new HashMap<>();

        String sign = EncryptUtils.encryptMD5ToString("english" + (UserInfoHelper.isLogin() ? UserInfoHelper.getUserInfo().getUid() : ""));
        headParams.put("sign",sign);

        return HttpCoreEngin.get(context).rxuploadFile(URLConfig.ADD_NOTE_URL, new TypeReference<ResultInfo<CommunityInfo>>() {
                }.getType(), upFileInfo, params,headParams,
                true);
    }

    public Observable<ResultInfo<CommentInfoList>> commentInfoList(int nid, int currentPage, int pageCount) {
        Map<String, String> params = new HashMap<>();
        params.put("note_id", nid + "");
        params.put("page", currentPage + "");
        params.put("limit", pageCount + "");

        return HttpCoreEngin.get(context).rxpost(URLConfig.FOLLOW_LIST_URL, new TypeReference<ResultInfo<CommentInfoList>>() {
                }.getType(), params,
                true, true,
                true);
    }

    public Observable<ResultInfo<CommentInfo>> addCommentInfo(CommentInfo commentInfo) {
        Map<String, String> params = new HashMap<>();
        params.put("user_id", commentInfo.getUserId());
        params.put("note_id", commentInfo.getNoteId());
        params.put("content", commentInfo.getContent());
        params.put("follow_id", "0");
        params.put("notice", "");

        return HttpCoreEngin.get(context).rxpost(URLConfig.ADD_COMMENT_URL, new TypeReference<ResultInfo<CommentInfo>>() {
                }.getType(), params,
                true, true, true);
    }

    public Observable<ResultInfo> addAgreeInfo(String userId, String noteId) {
        Map<String, String> params = new HashMap<>();
        params.put("user_id", userId);
        params.put("note_id", noteId);

        return HttpCoreEngin.get(context).rxpost(URLConfig.ADD_AGREE_URL, new TypeReference<ResultInfo<CommentInfo>>() {
                }.getType(), params,
                true, true, true);
    }

    public Observable<ResultInfo> deleteNote(String userId, String noteId) {
        Map<String, String> params = new HashMap<>();
        params.put("user_id", userId);
        params.put("note_id", noteId);

        return HttpCoreEngin.get(context).rxpost(URLConfig.DELETE_NOTE_URL, new TypeReference<ResultInfo<CommentInfo>>() {
                }.getType(), params,
                true, true, true);
    }

}
