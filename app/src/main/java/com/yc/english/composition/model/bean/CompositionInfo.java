package com.yc.english.composition.model.bean;

import com.alibaba.fastjson.annotation.JSONField;

import java.util.List;

/**
 * Created by wanglin  on 2019/3/22 18:18.
 */
public class CompositionInfo {
    //精选范文
    @JSONField(name = "iconzw")
    private String img;
    @JSONField(name = "zwid")
    private String id;

    @JSONField(name = "titlezw")
    private String title;
    @JSONField(name = "tags")
    private List<VersionDetailInfo> flags;
    private String date;



    @JSONField(name = "introzw")
    private String description;

    private String attrtype;
    private long addtime;
    private int readnum;
    private String zwattrid;
    private String attrid;
    private String attrpid;
    //写作素材
    @JSONField(name = "attrname")
    private String sub_title;

    @JSONField(name = "attrtext")
    private String fodderText;

    private String status;
    @JSONField(name = "despattr")
    private String fodderDesp;
    @JSONField(name = "iconattr")
    private String fodderIcon;



    public String getImg() {
        return img;
    }

    public void setImg(String img) {
        this.img = img;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }



    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }


    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public String getSub_title() {
        return sub_title;
    }

    public void setSub_title(String sub_title) {
        this.sub_title = sub_title;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getAttrtype() {
        return attrtype;
    }

    public void setAttrtype(String attrtype) {
        this.attrtype = attrtype;
    }

    public long getAddtime() {
        return addtime;
    }

    public void setAddtime(long addtime) {
        this.addtime = addtime;
    }

    public int getReadnum() {
        return readnum;
    }

    public void setReadnum(int readnum) {
        this.readnum = readnum;
    }

    public String getZwattrid() {
        return zwattrid;
    }

    public void setZwattrid(String zwattrid) {
        this.zwattrid = zwattrid;
    }

    public String getAttrid() {
        return attrid;
    }

    public void setAttrid(String attrid) {
        this.attrid = attrid;
    }

    public String getAttrpid() {
        return attrpid;
    }

    public void setAttrpid(String attrpid) {
        this.attrpid = attrpid;
    }

    public List<VersionDetailInfo> getFlags() {
        return flags;
    }

    public void setFlags(List<VersionDetailInfo> flags) {

        this.flags = flags;
    }


    public String getFodderText() {
        return fodderText;
    }

    public void setFodderText(String fodderText) {
        this.fodderText = fodderText;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getFodderDesp() {
        return fodderDesp;
    }

    public void setFodderDesp(String fodderDesp) {
        this.fodderDesp = fodderDesp;
    }

    public String getFodderIcon() {
        return fodderIcon;
    }

    public void setFodderIcon(String fodderIcon) {
        this.fodderIcon = fodderIcon;
    }
}
