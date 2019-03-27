package com.yc.english.composition.model.bean;

import com.alibaba.fastjson.annotation.JSONField;

import java.util.List;

/**
 * Created by wanglin  on 2019/3/26 11:58.
 */
public class FodderInfoWrapper {
    @JSONField(name = "nav")
    private List<FodderInfo> fodderInfos;

    public List<FodderInfo> getFodderInfos() {
        return fodderInfos;
    }

    public void setFodderInfos(List<FodderInfo> fodderInfos) {
        this.fodderInfos = fodderInfos;
    }
}
