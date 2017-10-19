package com.yc.english.speak.model.bean;

import android.os.Parcel;
import android.os.Parcelable;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by wanglin  on 2017/10/18 18:20.
 */

public class SpeakAndReadInfo implements Parcelable {

    /**
     * id : 37
     * type_id : 3  分类id
     * type_name 分类标题
     */

    private String type_id;

    private String type_name;


    private List<SpeakAndReadItemInfo> itemInfoList;//整理后数据集合

    public String getType_id() {
        return type_id;
    }

    public void setType_id(String type_id) {
        this.type_id = type_id;
    }

    public String getType_name() {
        return type_name;
    }

    public void setType_name(String type_name) {
        this.type_name = type_name;
    }

    public List<SpeakAndReadItemInfo> getItemInfoList() {
        return itemInfoList;
    }

    public void setItemInfoList(List<SpeakAndReadItemInfo> itemInfoList) {
        this.itemInfoList = itemInfoList;
    }


    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(this.type_id);
        dest.writeString(this.type_name);
        dest.writeList(this.itemInfoList);
    }

    public SpeakAndReadInfo() {
    }

    public SpeakAndReadInfo(String type_id, String type_name) {
        this.type_id = type_id;
        this.type_name = type_name;
    }

    protected SpeakAndReadInfo(Parcel in) {
        this.type_id = in.readString();
        this.type_name = in.readString();
        this.itemInfoList = new ArrayList<SpeakAndReadItemInfo>();
        in.readList(this.itemInfoList, SpeakAndReadItemInfo.class.getClassLoader());
    }

    public static final Parcelable.Creator<SpeakAndReadInfo> CREATOR = new Parcelable.Creator<SpeakAndReadInfo>() {
        @Override
        public SpeakAndReadInfo createFromParcel(Parcel source) {
            return new SpeakAndReadInfo(source);
        }

        @Override
        public SpeakAndReadInfo[] newArray(int size) {
            return new SpeakAndReadInfo[size];
        }
    };

    @Override
    public String toString() {
        return "SpeakAndReadInfo{" +
                "type_id='" + type_id + '\'' +
                ", type_name='" + type_name + '\'' +
                ", itemInfoList=" + itemInfoList +
                '}';
    }
}
