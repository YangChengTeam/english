package com.yc.english.community.model.domain;

import com.alibaba.fastjson.annotation.JSONField;
import com.chad.library.adapter.base.entity.MultiItemEntity;

import java.util.List;

/**
 * 互动社区-帖子信息
 */
public class CommunityInfo implements MultiItemEntity {

    public static final int CLICK_ITEM_VIEW = 1;

    private String id;
    @JSONField(name = "user_id")
    private String userId;
    private String content;
    private List<String> notice;

    @JSONField(name = "type")
    private String cType;

    private String flag;
    private String sort;
    private String follow_count;
    private String agree_count;
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

    public String getSort() {
        return sort;
    }

    public void setSort(String sort) {
        this.sort = sort;
    }

    public String getFollow_count() {
        return follow_count;
    }

    public void setFollow_count(String follow_count) {
        this.follow_count = follow_count;
    }

    public String getAgree_count() {
        return agree_count;
    }

    public void setAgree_count(String agree_count) {
        this.agree_count = agree_count;
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

    @Override
    public int getItemType() {
        return Type;
    }
}
