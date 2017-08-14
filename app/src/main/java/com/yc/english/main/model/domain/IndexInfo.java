package com.yc.english.main.model.domain;

import com.alibaba.fastjson.annotation.JSONField;

import java.util.List;

/**
 * Created by zhangkai on 2017/8/11.
 */

public class IndexInfo {
    @JSONField(name = "slide_list")
    private List<SlideInfo> slideInfo;

    public List<SlideInfo> getSlideInfo() {
        return slideInfo;
    }

    public void setSlideInfo(List<SlideInfo> slideInfo) {
        this.slideInfo = slideInfo;
    }

    @JSONField(name = "count")
    private CountInfo countInfo;



    public CountInfo getCountInfo() {
        return countInfo;
    }

    public void setCountInfo(CountInfo countInfo) {
        this.countInfo = countInfo;
    }
}
