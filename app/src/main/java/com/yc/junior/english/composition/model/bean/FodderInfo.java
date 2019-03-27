package com.yc.junior.english.composition.model.bean;

import com.alibaba.fastjson.annotation.JSONField;

import java.util.List;

/**
 * Created by wanglin  on 2019/3/25 08:55.
 */
public class FodderInfo {
    @JSONField(name = "attrtext")
    private String title;
    @JSONField(name = "children")
    private List<CompositionInfo> compositionInfos;

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public List<CompositionInfo> getCompositionInfos() {
        return compositionInfos;
    }

    public void setCompositionInfos(List<CompositionInfo> compositionInfos) {
        this.compositionInfos = compositionInfos;
    }
}
