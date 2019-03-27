package com.yc.english.composition.model.bean;

import com.alibaba.fastjson.annotation.JSONField;

/**
 * Created by wanglin  on 2019/3/26 14:42.
 */
public class CompositionDetailInfo {


    /**
     * zwid : 36
     * titlezw : 自我介绍：以Introduction About Myself为题，写一篇简短的自我介绍。
     * introzw :
     * iconzw : http://en.wk2.com/Upload/Picture/2019-03-22/5c94a704d75c4.png
     * attrtype : 1
     * addtime : 1553478877
     * readnum : 208
     * contentzw : <html><head><meta charset="utf-8" /><meta content="yes" name="apple-mobile-web-app-capable" /><meta content="yes" name="apple-touch-fullscreen" /><meta content="telephone=no,email=no" name="format-detection" /><meta name="App-Config"  content="fullscreen=yes,useHistoryState=yes,transition=yes" /><meta name="viewport" content="width=device-width,initial-scale=1,minimum-scale=1,maximum-scale=1,user-scalable=no" /><style> html,body{overflow:hidden; font-size:16px; line-height: 1.6;} img { width:100%; height:auto; overflow:hidden;}</style></head><body>contentzw<script src="http://en.wk2.com/Public/Home/js/press.js"></script></body></html>
     */

    private String zwid;
    @JSONField(name = "titlezw")
    private String title;
    private String introzw;
    @JSONField(name = "iconzw")
    private String icon;
    private String attrtype;
    private String addtime;
    private String readnum;
    @JSONField(name = "contentzw")
    private String content;

    public String getZwid() {
        return zwid;
    }

    public void setZwid(String zwid) {
        this.zwid = zwid;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getIntrozw() {
        return introzw;
    }

    public void setIntrozw(String introzw) {
        this.introzw = introzw;
    }

    public String getIcon() {
        return icon;
    }

    public void setIcon(String icon) {
        this.icon = icon;
    }

    public String getAttrtype() {
        return attrtype;
    }

    public void setAttrtype(String attrtype) {
        this.attrtype = attrtype;
    }

    public String getAddtime() {
        return addtime;
    }

    public void setAddtime(String addtime) {
        this.addtime = addtime;
    }

    public String getReadnum() {
        return readnum;
    }

    public void setReadnum(String readnum) {
        this.readnum = readnum;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }
}
