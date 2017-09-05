package com.yc.english.community.model.domain;

import com.alibaba.fastjson.annotation.JSONField;

import java.util.List;

/**
 * Created by admin on 2017/7/26.
 */

public class CommentInfoList {

    @JSONField(name = "page_count")
    public int pageCount;

    public List<CommentInfo> list;

    public List<CommentInfo> getList() {
        return list;
    }

    public void setList(List<CommentInfo> list) {
        this.list = list;
    }
}
