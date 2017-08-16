package com.yc.english.group.model.bean;

import com.alibaba.fastjson.annotation.JSONField;

import org.greenrobot.greendao.annotation.Entity;
import org.greenrobot.greendao.annotation.Generated;
import org.greenrobot.greendao.annotation.Id;

import me.yokeyword.indexablerv.IndexableEntity;

/**
 * Created by wanglin  on 2017/8/2 20:39.
 * 学生信息
 */

@Entity
public class StudentInfo implements IndexableEntity{
    @Id(autoincrement = true)
    private long sId;
    private String add_date;
    private String add_time;
    private String class_id;
    private String id;
    private String nick_name;
    private String user_id;
    private String user_name;
    private String class_name;
    private String sn;//群号
    private String master_id;//
    private boolean isAudit;//是否审核，true表示已审核 false 表示未审核
    @JSONField(name = "user_face")
    private String face;

    @Generated(hash = 939309314)
    public StudentInfo(long sId, String add_date, String add_time, String class_id,
                       String id, String nick_name, String user_id, String user_name,
                       String class_name, String sn, String master_id, boolean isAudit,
                       String face) {
        this.sId = sId;
        this.add_date = add_date;
        this.add_time = add_time;
        this.class_id = class_id;
        this.id = id;
        this.nick_name = nick_name;
        this.user_id = user_id;
        this.user_name = user_name;
        this.class_name = class_name;
        this.sn = sn;
        this.master_id = master_id;
        this.isAudit = isAudit;
        this.face = face;
    }

    @Generated(hash = 2016856731)
    public StudentInfo() {
    }

    public long getSId() {
        return this.sId;
    }

    public void setSId(long sId) {
        this.sId = sId;
    }

    public String getAdd_date() {
        return this.add_date;
    }

    public void setAdd_date(String add_date) {
        this.add_date = add_date;
    }

    public String getAdd_time() {
        return this.add_time;
    }

    public void setAdd_time(String add_time) {
        this.add_time = add_time;
    }

    public String getClass_id() {
        return this.class_id;
    }

    public void setClass_id(String class_id) {
        this.class_id = class_id;
    }

    public String getId() {
        return this.id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getNick_name() {
        return this.nick_name;
    }

    public void setNick_name(String nick_name) {
        this.nick_name = nick_name;
    }

    public String getUser_id() {
        return this.user_id;
    }

    public void setUser_id(String user_id) {
        this.user_id = user_id;
    }

    public String getUser_name() {
        return this.user_name;
    }

    public void setUser_name(String user_name) {
        this.user_name = user_name;
    }

    public String getClass_name() {
        return this.class_name;
    }

    public void setClass_name(String class_name) {
        this.class_name = class_name;
    }

    public String getSn() {
        return this.sn;
    }

    public void setSn(String sn) {
        this.sn = sn;
    }

    public String getMaster_id() {
        return this.master_id;
    }

    public void setMaster_id(String master_id) {
        this.master_id = master_id;
    }

    public boolean getIsAudit() {
        return this.isAudit;
    }

    public void setIsAudit(boolean isAudit) {
        this.isAudit = isAudit;
    }

    public String getFace() {
        return this.face;
    }

    public void setFace(String face) {
        this.face = face;
    }


    @Override
    public String getFieldIndexBy() {
        return nick_name;
    }

    @Override
    public void setFieldIndexBy(String indexField) {
            this.nick_name=indexField;
    }

    @Override
    public void setFieldPinyinIndexBy(String pinyin) {

    }
}
