package com.yc.junior.english.group.model.bean;

import android.os.Parcel;
import android.os.Parcelable;

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
public class StudentInfo implements IndexableEntity, Parcelable {
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
    private String class_sn;//群号
    private String master_id;//
    private boolean isAudit;//是否审核，true表示已审核 false 表示未审核
    @JSONField(name = "user_face")
    private String face;

    private boolean isForbid;//是否被禁言

    private String forbidTime;//禁言的时间

    private boolean isSelected;

    private boolean isTempForbid;

    @Generated(hash = 2016856731)
    public StudentInfo() {
    }

    public StudentInfo(Parcel source) {
        add_date = source.readString();
        add_time = source.readString();
        class_id = source.readString();
        id = source.readString();
        nick_name = source.readString();
        user_id = source.readString();
        user_name = source.readString();
        class_name = source.readString();
        class_sn = source.readString();
        master_id = source.readString();
        face = source.readString();
        isForbid = source.readByte() != 0;
        forbidTime = source.readString();
    }

    @Generated(hash = 21243913)
    public StudentInfo(long sId, String add_date, String add_time, String class_id, String id,
            String nick_name, String user_id, String user_name, String class_name,
            String class_sn, String master_id, boolean isAudit, String face, boolean isForbid,
            String forbidTime, boolean isSelected, boolean isTempForbid) {
        this.sId = sId;
        this.add_date = add_date;
        this.add_time = add_time;
        this.class_id = class_id;
        this.id = id;
        this.nick_name = nick_name;
        this.user_id = user_id;
        this.user_name = user_name;
        this.class_name = class_name;
        this.class_sn = class_sn;
        this.master_id = master_id;
        this.isAudit = isAudit;
        this.face = face;
        this.isForbid = isForbid;
        this.forbidTime = forbidTime;
        this.isSelected = isSelected;
        this.isTempForbid = isTempForbid;
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

    public String getClass_sn() {
        return this.class_sn;
    }

    public void setClass_sn(String class_sn) {
        this.class_sn = class_sn;
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
        this.nick_name = indexField;
    }

    @Override
    public void setFieldPinyinIndexBy(String pinyin) {

    }

    public static final Creator<StudentInfo> CREATOR = new Creator<StudentInfo>() {
        @Override
        public StudentInfo createFromParcel(Parcel source) {
            return new StudentInfo(source);
        }

        @Override
        public StudentInfo[] newArray(int size) {
            return new StudentInfo[size];
        }
    };

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(add_date);
        dest.writeString(add_time);
        dest.writeString(class_id);
        dest.writeString(id);
        dest.writeString(nick_name);
        dest.writeString(user_id);
        dest.writeString(user_name);
        dest.writeString(class_name);
        dest.writeString(class_sn);
        dest.writeString(master_id);
        dest.writeString(face);
        dest.writeByte((byte) (isForbid ? 1 : 0));
        dest.writeString(forbidTime);
    }

    public boolean getIsForbid() {
        return this.isForbid;
    }

    public void setIsForbid(boolean isForbid) {
        this.isForbid = isForbid;
    }

    public String getForbidTime() {
        return this.forbidTime;
    }

    public void setForbidTime(String forbidTime) {
        this.forbidTime = forbidTime;
    }

    public boolean getIsSelected() {
        return this.isSelected;
    }

    public void setIsSelected(boolean isSelected) {
        this.isSelected = isSelected;
    }

    public boolean getIsTempForbid() {
        return this.isTempForbid;
    }

    public void setIsTempForbid(boolean isTempForbid) {
        this.isTempForbid = isTempForbid;
    }

}
