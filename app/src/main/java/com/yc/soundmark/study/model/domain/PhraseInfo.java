package com.yc.soundmark.study.model.domain;

import android.os.Parcel;
import android.os.Parcelable;

/**
 * Created by wanglin  on 2018/10/30 17:02.
 * 短语
 */
public class PhraseInfo implements Parcelable {
    /**
     * id : 1
     * vid : 1
     * phrase : gr#ee#n t#ea#
     * en : 绿茶
     * sort : 1
     * mp3 : https://yb.bshu.com/uploads/20180813/5eedfd72b73236559c9703e3f7bb8925.mp3
     */

    private String id;
    private String vid;
    private String phrase;
    private String en;
    private String sort;
    private String mp3;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getVid() {
        return vid;
    }

    public void setVid(String vid) {
        this.vid = vid;
    }

    public String getPhrase() {
        return phrase;
    }

    public void setPhrase(String phrase) {
        this.phrase = phrase;
    }

    public String getEn() {
        return en;
    }

    public void setEn(String en) {
        this.en = en;
    }

    public String getSort() {
        return sort;
    }

    public void setSort(String sort) {
        this.sort = sort;
    }

    public String getMp3() {
        return mp3;
    }

    public void setMp3(String mp3) {
        this.mp3 = mp3;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(this.id);
        dest.writeString(this.vid);
        dest.writeString(this.phrase);
        dest.writeString(this.en);
        dest.writeString(this.sort);
        dest.writeString(this.mp3);
    }

    public PhraseInfo() {
    }

    protected PhraseInfo(Parcel in) {
        this.id = in.readString();
        this.vid = in.readString();
        this.phrase = in.readString();
        this.en = in.readString();
        this.sort = in.readString();
        this.mp3 = in.readString();
    }

    public static final Creator<PhraseInfo> CREATOR = new Creator<PhraseInfo>() {
        @Override
        public PhraseInfo createFromParcel(Parcel source) {
            return new PhraseInfo(source);
        }

        @Override
        public PhraseInfo[] newArray(int size) {
            return new PhraseInfo[size];
        }
    };
}
