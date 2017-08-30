package com.yc.english.community.model.domain;

import com.chad.library.adapter.base.entity.MultiItemEntity;

import java.util.List;

/**
 * 互动社区-帖子信息
 */
public class CommunityInfo implements MultiItemEntity {

    public static final int CLICK_ITEM_VIEW = 1;

    private String communityNoteTitle;//标题

    private String communityNoteContent;//内容

    private String communityNoteDate;//发帖时间

    private String commentCount;//评论数

    private String praiseCount;//点赞数

    private List<String> imgUrls;

    public int Type;

    public String getCommunityNoteTitle() {
        return communityNoteTitle;
    }

    public void setCommunityNoteTitle(String communityNoteTitle) {
        this.communityNoteTitle = communityNoteTitle;
    }

    public String getCommunityNoteContent() {
        return communityNoteContent;
    }

    public void setCommunityNoteContent(String communityNoteContent) {
        this.communityNoteContent = communityNoteContent;
    }

    public String getCommunityNoteDate() {
        return communityNoteDate;
    }

    public void setCommunityNoteDate(String communityNoteDate) {
        this.communityNoteDate = communityNoteDate;
    }

    public String getCommentCount() {
        return commentCount;
    }

    public void setCommentCount(String commentCount) {
        this.commentCount = commentCount;
    }

    public String getPraiseCount() {
        return praiseCount;
    }

    public void setPraiseCount(String praiseCount) {
        this.praiseCount = praiseCount;
    }

    public List<String> getImgUrls() {
        return imgUrls;
    }

    public void setImgUrls(List<String> imgUrls) {
        this.imgUrls = imgUrls;
    }

    public CommunityInfo() {
        super();
    }

    public CommunityInfo(final int type) {
        Type = type;
    }

    @Override
    public int getItemType() {
        return Type;
    }
}
