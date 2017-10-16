package com.yc.english.speak.model.bean;

import android.os.Parcel;
import android.os.Parcelable;

/**
 * Created by wanglin  on 2017/10/13 10:33.
 */

public class EnglishItemInfo implements Parcelable {
    /**
     * url : http://en.wk2.com/ueditor/php/upload/image/20171012/1507769710256192.png
     * play_count : 13562
     * update_date : 2017年09月28日
     * sub_title : 功夫熊猫3 英文版
     */

    private String url;
    private int play_count;
    private String update_date;
    private String sub_title;

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    public int getPlay_count() {
        return play_count;
    }

    public void setPlay_count(int play_count) {
        this.play_count = play_count;
    }

    public String getUpdate_date() {
        return update_date;
    }

    public void setUpdate_date(String update_date) {
        this.update_date = update_date;
    }

    public String getSub_title() {
        return sub_title;
    }

    public void setSub_title(String sub_title) {
        this.sub_title = sub_title;
    }


    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(this.url);
        dest.writeInt(this.play_count);
        dest.writeString(this.update_date);
        dest.writeString(this.sub_title);
    }

    public EnglishItemInfo() {
    }

    protected EnglishItemInfo(Parcel in) {
        this.url = in.readString();
        this.play_count = in.readInt();
        this.update_date = in.readString();
        this.sub_title = in.readString();
    }

    public static final Parcelable.Creator<EnglishItemInfo> CREATOR = new Parcelable.Creator<EnglishItemInfo>() {
        @Override
        public EnglishItemInfo createFromParcel(Parcel source) {
            return new EnglishItemInfo(source);
        }

        @Override
        public EnglishItemInfo[] newArray(int size) {
            return new EnglishItemInfo[size];
        }
    };
}
