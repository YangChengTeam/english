package com.yc.english.main.model.domain;

import com.alibaba.fastjson.annotation.JSONField;
import com.yc.english.community.model.domain.CommunityInfo;
import com.yc.english.weixin.model.domain.CourseInfo;
import com.yc.english.weixin.model.domain.WeiKeCategory;

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

    private List<CourseInfo> redian;

    private List<CourseInfo> cihui;
    private List<CourseInfo> yufa;
    private List<CourseInfo> juxing;
    private List<CourseInfo> zuowen;
    private List<CourseInfo> tingli;

    private List<CourseInfo> tuijian;

    private List<WeiKeCategory> weike;

    private List<CommunityInfo> shequ;

    public List<CourseInfo> getRedian() {
        return redian;
    }

    public void setRedian(List<CourseInfo> redian) {
        this.redian = redian;
    }

    public List<CourseInfo> getCihui() {
        return cihui;
    }

    public void setCihui(List<CourseInfo> cihui) {
        this.cihui = cihui;
    }

    public List<CourseInfo> getYufa() {
        return yufa;
    }

    public void setYufa(List<CourseInfo> yufa) {
        this.yufa = yufa;
    }

    public List<CourseInfo> getJuxing() {
        return juxing;
    }

    public void setJuxing(List<CourseInfo> juxing) {
        this.juxing = juxing;
    }

    public List<CourseInfo> getZuowen() {
        return zuowen;
    }

    public void setZuowen(List<CourseInfo> zuowen) {
        this.zuowen = zuowen;
    }

    public List<CourseInfo> getTingli() {
        return tingli;
    }

    public void setTingli(List<CourseInfo> tingli) {
        this.tingli = tingli;
    }

    public List<WeiKeCategory> getWeike() {
        return weike;
    }

    public void setWeike(List<WeiKeCategory> weike) {
        this.weike = weike;
    }

    public List<CommunityInfo> getShequ() {
        return shequ;
    }

    public void setShequ(List<CommunityInfo> shequ) {
        this.shequ = shequ;
    }

    public List<CourseInfo> getTuijian() {
        return tuijian;
    }

    public void setTuijian(List<CourseInfo> tuijian) {
        this.tuijian = tuijian;
    }
}
