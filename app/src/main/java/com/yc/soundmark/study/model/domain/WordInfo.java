package com.yc.soundmark.study.model.domain;

import android.os.Parcel;
import android.os.Parcelable;

import java.util.List;

/**
 * Created by wanglin  on 2018/10/30 17:02.
 * 单词
 */
public class WordInfo implements Parcelable {
    /**
     * id : 1
     * vid : 1
     * word : w#e#
     * en : 我们
     * sort : 1
     * mp3 : https://yb.bshu.com/uploads/20180813/d812c3b94b9d3726b97cf0b3e5dac90b.mp3
     */

    private String id;
    private String vid;
    private String word;
    private String en;
    private String sort;
    private String mp3;
    private String pronunciation;

    private String name;

    private int is_vip;//是否收费0免费1收费

    private int page;//页码

    private String type_text;

    private int type;



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

    public String getWord() {
        return word;
    }

    public void setWord(String word) {
        this.word = word;
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

    public String getPronunciation() {
        return pronunciation;
    }

    public void setPronunciation(String pronunciation) {
        this.pronunciation = pronunciation;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public int getIs_vip() {
        return is_vip;
    }

    public int getPage() {
        return page;
    }

    public void setPage(int page) {
        this.page = page;
    }

    public void setIs_vip(int is_vip) {
        this.is_vip = is_vip;
    }

    public String getType_text() {
        return type_text;
    }

    public void setType_text(String type_text) {
        this.type_text = type_text;
    }

    public int getType() {
        return type;
    }

    public void setType(int type) {
        this.type = type;
    }



    public WordInfo() {
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(this.id);
        dest.writeString(this.vid);
        dest.writeString(this.word);
        dest.writeString(this.en);
        dest.writeString(this.sort);
        dest.writeString(this.mp3);
        dest.writeString(this.pronunciation);
        dest.writeString(this.name);
        dest.writeInt(this.is_vip);
        dest.writeInt(this.page);
        dest.writeString(this.type_text);
        dest.writeInt(this.type);
    }

    protected WordInfo(Parcel in) {
        this.id = in.readString();
        this.vid = in.readString();
        this.word = in.readString();
        this.en = in.readString();
        this.sort = in.readString();
        this.mp3 = in.readString();
        this.pronunciation = in.readString();
        this.name = in.readString();
        this.is_vip = in.readInt();
        this.page = in.readInt();
        this.type_text = in.readString();
        this.type = in.readInt();
    }

    public static final Creator<WordInfo> CREATOR = new Creator<WordInfo>() {
        @Override
        public WordInfo createFromParcel(Parcel source) {
            return new WordInfo(source);
        }

        @Override
        public WordInfo[] newArray(int size) {
            return new WordInfo[size];
        }
    };
}
