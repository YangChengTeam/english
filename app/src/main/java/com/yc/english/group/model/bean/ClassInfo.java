package com.yc.english.group.model.bean;

import android.os.Parcel;
import android.os.Parcelable;

import com.alibaba.fastjson.annotation.JSONField;

import org.greenrobot.greendao.annotation.Entity;
import org.greenrobot.greendao.annotation.Id;
import org.greenrobot.greendao.annotation.Transient;
import org.greenrobot.greendao.annotation.Generated;

/**
 * Created by wanglin  on 2017/7/24 18:03.
 * 班级信息
 */

@Entity
public class ClassInfo implements Parcelable {
    @Id(autoincrement = true)
    private Long id;
    @JSONField(name = "face")
    private String imageUrl;
    @JSONField(name = "name")
    private String className;
    @JSONField(name = "member_count")
    private String count;
    @JSONField(name = "sn")
    private int groupId;
    private String founder_id;//创始人
    private String master_id;//群主
    private String vali_type;
    private String is_del;
    private String add_time;
    private String add_date;
    private String del_time;
    private String sort;


    public static final Creator<ClassInfo> CREATOR = new Creator<ClassInfo>() {

        @Override
        public ClassInfo createFromParcel(Parcel source) {
            return new ClassInfo(source);
        }

        @Override
        public ClassInfo[] newArray(int size) {
            return new ClassInfo[size];
        }
    };

    public ClassInfo() {
    }


    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public ClassInfo(String imageUrl, String className, int groupId) {
        this.imageUrl = imageUrl;
        this.className = className;
        this.groupId = groupId;
    }

    public ClassInfo(String imageUrl, String className, String count, int groupId) {
        this.imageUrl = imageUrl;
        this.className = className;
        this.count = count;
        this.groupId = groupId;
    }

    public ClassInfo(Parcel source) {
        imageUrl = source.readString();
        className = source.readString();
        groupId = source.readInt();
    }



    @Generated(hash = 80881934)
    public ClassInfo(Long id, String imageUrl, String className, String count, int groupId,
                     String founder_id, String master_id, String vali_type, String is_del,
                     String add_time, String add_date, String del_time, String sort) {
        this.id = id;
        this.imageUrl = imageUrl;
        this.className = className;
        this.count = count;
        this.groupId = groupId;
        this.founder_id = founder_id;
        this.master_id = master_id;
        this.vali_type = vali_type;
        this.is_del = is_del;
        this.add_time = add_time;
        this.add_date = add_date;
        this.del_time = del_time;
        this.sort = sort;
    }

    public String getImageUrl() {
        return imageUrl;
    }

    public void setImageUrl(String imageUrl) {
        this.imageUrl = imageUrl;
    }

    public String getClassName() {
        return className;
    }

    public void setClassName(String className) {
        this.className = className;
    }

    public String getCount() {
        return count;
    }

    public void setCount(String count) {
        this.count = count;
    }

    public int getGroupId() {
        return groupId;
    }

    public void setGroupId(int groupId) {
        this.groupId = groupId;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(imageUrl);
        dest.writeString(className);
        dest.writeInt(groupId);
    }


    public String getFounder_id() {
        return this.founder_id;
    }


    public void setFounder_id(String founder_id) {
        this.founder_id = founder_id;
    }


    public String getMaster_id() {
        return this.master_id;
    }


    public void setMaster_id(String master_id) {
        this.master_id = master_id;
    }


    public String getVali_type() {
        return this.vali_type;
    }


    public void setVali_type(String vali_type) {
        this.vali_type = vali_type;
    }


    public String getIs_del() {
        return this.is_del;
    }


    public void setIs_del(String is_del) {
        this.is_del = is_del;
    }


    public String getAdd_time() {
        return this.add_time;
    }


    public void setAdd_time(String add_time) {
        this.add_time = add_time;
    }


    public String getAdd_date() {
        return this.add_date;
    }


    public void setAdd_date(String add_date) {
        this.add_date = add_date;
    }


    public String getDel_time() {
        return this.del_time;
    }


    public void setDel_time(String del_time) {
        this.del_time = del_time;
    }


    public String getSort() {
        return this.sort;
    }


    public void setSort(String sort) {
        this.sort = sort;
    }
}
