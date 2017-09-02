package com.yc.english.community.model.domain;

import com.alibaba.fastjson.annotation.JSONField;

import java.io.Serializable;
import java.util.List;

/**
 * 互动社区-帖子信息
 */
public class CommunityInfo implements Serializable {

    private static final long serialVersionUID = -7060210533600464481L;

    private String id;
    @JSONField(name = "user_id")
    private String userId;

    @JSONField(name = "user_name")
    private String userName;

    private String face;

    private String content;
    private List<String> notice;

    @JSONField(name = "type")
    private String cType;

    private String flag;
    private String sort;
    @JSONField(name = "follow_count")
    private String followCount;
    @JSONField(name = "agree_count")
    private String agreeCount;
    private String status;
    @JSONField(name = "add_time")
    private String addTime;

    private List<String> images;

    public int Type;

    public CommunityInfo() {
        super();
    }

    public CommunityInfo(final int type) {
        Type = type;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }

    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public List<String> getNotice() {
        return notice;
    }

    public void setNotice(List<String> notice) {
        this.notice = notice;
    }

    public String getcType() {
        return cType;
    }

    public void setcType(String cType) {
        this.cType = cType;
    }

    public String getFlag() {
        return flag;
    }

    public void setFlag(String flag) {
        this.flag = flag;
    }

    public String getFace() {
        return face;
    }

    public void setFace(String face) {
        this.face = face;
    }

    public String getSort() {
        return sort;
    }

    public void setSort(String sort) {
        this.sort = sort;
    }

    public String getFollowCount() {
        return followCount;
    }

    public void setFollowCount(String followCount) {
        this.followCount = followCount;
    }

    public String getAgreeCount() {
        return agreeCount;
    }

    public void setAgreeCount(String agreeCount) {
        this.agreeCount = agreeCount;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getAddTime() {
        return addTime;
    }

    public void setAddTime(String addTime) {
        this.addTime = addTime;
    }

    public List<String> getImages() {
        return images;
    }

    public void setImages(List<String> images) {
        this.images = images;
    }


}
