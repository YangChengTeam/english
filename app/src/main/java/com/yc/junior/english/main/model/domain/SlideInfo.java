package com.yc.junior.english.main.model.domain;

import android.os.Parcel;
import android.os.Parcelable;

import com.alibaba.fastjson.annotation.JSONField;

/**
 * Created by zhangkai on 2017/8/11.
 */

public class SlideInfo implements Parcelable {
    private String id;
    private String title;
    private String type;
    private String img;
    @JSONField(name = "type_value")
    private String typeValue;
    @JSONField(name = "is_del")
    private String isDel;
    private String sort;

    private String statistics;//友盟统计字段

    private String url;//跳转链接


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

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getImg() {
        return img;
    }

    public void setImg(String img) {
        this.img = img;
    }

    public String getTypeValue() {
        return typeValue;
    }

    public void setTypeValue(String typeValue) {
        this.typeValue = typeValue;
    }

    public String getIsDel() {
        return isDel;
    }

    public void setIsDel(String isDel) {
        this.isDel = isDel;
    }

    public String getSort() {
        return sort;
    }

    public void setSort(String sort) {
        this.sort = sort;
    }

    public String getStatistics() {
        return statistics;
    }

    public void setStatistics(String statistics) {
        this.statistics = statistics;
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(this.id);
        dest.writeString(this.title);
        dest.writeString(this.type);
        dest.writeString(this.img);
        dest.writeString(this.typeValue);
        dest.writeString(this.isDel);
        dest.writeString(this.sort);
        dest.writeString(this.statistics);
        dest.writeString(this.url);
    }

    public SlideInfo() {
    }

    protected SlideInfo(Parcel in) {
        this.id = in.readString();
        this.title = in.readString();
        this.type = in.readString();
        this.img = in.readString();
        this.typeValue = in.readString();
        this.isDel = in.readString();
        this.sort = in.readString();
        this.statistics = in.readString();
        this.url = in.readString();
    }

    public static final Parcelable.Creator<SlideInfo> CREATOR = new Parcelable.Creator<SlideInfo>() {
        @Override
        public SlideInfo createFromParcel(Parcel source) {
            return new SlideInfo(source);
        }

        @Override
        public SlideInfo[] newArray(int size) {
            return new SlideInfo[size];
        }
    };
}
