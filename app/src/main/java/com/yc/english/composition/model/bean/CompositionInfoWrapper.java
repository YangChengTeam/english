package com.yc.english.composition.model.bean;

import com.alibaba.fastjson.annotation.JSONField;
import com.yc.english.main.model.domain.SlideInfo;

import java.util.List;

/**
 * Created by wanglin  on 2019/3/26 10:58.
 */
public class CompositionInfoWrapper {

    private VersionInfo option;
    private List<CompositionInfo> kaoshi;
    private List<CompositionInfo> richang;

    //作文素材列表
    private List<CompositionInfo> list;
    @JSONField(name = "slide_list")
    private List<SlideInfo> slideList;

    public VersionInfo getOption() {
        return option;
    }

    public void setOption(VersionInfo option) {
        this.option = option;
    }

    public List<CompositionInfo> getKaoshi() {
        return kaoshi;
    }

    public void setKaoshi(List<CompositionInfo> kaoshi) {
        this.kaoshi = kaoshi;
    }

    public List<CompositionInfo> getRichang() {
        return richang;
    }

    public void setRichang(List<CompositionInfo> richang) {
        this.richang = richang;
    }

    public List<CompositionInfo> getList() {
        return list;
    }

    public void setList(List<CompositionInfo> list) {
        this.list = list;
    }

    public List<SlideInfo> getSlideList() {
        return slideList;
    }

    public void setSlideList(List<SlideInfo> slideList) {
        this.slideList = slideList;
    }
}
