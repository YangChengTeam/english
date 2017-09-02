package com.yc.english.community.model.domain;

import com.alibaba.fastjson.annotation.JSONField;

/**
 * Created by admin on 2017/9/1.
 */

public class CommentInfo {

    private String id;

    @JSONField(name = "user_id")
    private String userId;

    @JSONField(name = "user_name")
    private String userName;

    @JSONField(name = "note_id")
    private String noteId;

    @JSONField(name = "follow_id")
    private String followId;

    private String face;

    private String content;

    private String notice;

    private String status;

    @JSONField(name = "add_time")
    private String addTime;

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

    public String getNoteId() {
        return noteId;
    }

    public void setNoteId(String noteId) {
        this.noteId = noteId;
    }

    public String getFollowId() {
        return followId;
    }

    public void setFollowId(String followId) {
        this.followId = followId;
    }

    public String getFace() {
        return face;
    }

    public void setFace(String face) {
        this.face = face;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public String getNotice() {
        return notice;
    }

    public void setNotice(String notice) {
        this.notice = notice;
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
}
