package com.yc.junior.english.speak.model.bean;

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


    private String type_name;

    private List<SpeakAndReadItemInfo> data;//整理后数据集合




    public String getType_name() {
        return type_name;
    }

    public void setType_name(String type_name) {
        this.type_name = type_name;
    }

    public List<SpeakAndReadItemInfo> getData() {
        return data;
    }

    public void setData(List<SpeakAndReadItemInfo> data) {
        this.data = data;
    }


    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(this.type_name);
        dest.writeTypedList(this.data);
    }

    public SpeakAndReadInfo() {
    }

    protected SpeakAndReadInfo(Parcel in) {
        this.type_name = in.readString();
        this.data = in.createTypedArrayList(SpeakAndReadItemInfo.CREATOR);
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
}
