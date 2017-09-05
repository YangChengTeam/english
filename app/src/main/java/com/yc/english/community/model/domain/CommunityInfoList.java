package com.yc.english.community.model.domain;

import com.alibaba.fastjson.annotation.JSONField;

import java.util.List;

/**
 * Created by admin on 2017/7/26.
 */

public class CommunityInfoList {

    @JSONField(name = "page_count")
    public int pageCount;

    public List<CommunityInfo> list;

    public List<CommunityInfo> getList() {
        return list;
    }

    public void setList(List<CommunityInfo> list) {
        this.list = list;
    }
}
