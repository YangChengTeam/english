package com.yc.english.group.model.bean;

import android.os.Parcel;
import android.os.Parcelable;

/**
 * Created by wanglin  on 2017/7/24 18:03.
 * 班级信息
 */

public class ClassInfo implements Parcelable {

    private String imageUrl;
    private String className;
    private Integer count;
    private int groupId;


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

    public ClassInfo(String imageUrl, String className, int groupId) {
        this.imageUrl = imageUrl;
        this.className = className;
        this.groupId = groupId;
    }

    public ClassInfo(Parcel source) {
        imageUrl = source.readString();
        className = source.readString();
        groupId = source.readInt();
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

    public Integer getCount() {
        return count;
    }

    public void setCount(Integer count) {
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
}
