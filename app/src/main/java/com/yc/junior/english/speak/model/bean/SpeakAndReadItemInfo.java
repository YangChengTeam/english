package com.yc.junior.english.speak.model.bean;

import android.os.Parcel;
import android.os.Parcelable;

/**
 * Created by wanglin  on 2017/10/19 10:49.
 */

public class SpeakAndReadItemInfo implements Parcelable {


    /**
     * add_date : 20171019
     * add_time : 1508373922
     * flag : 0
     * flag_name : 普通
     * id : 43
     * img : /Upload/Picture/2017-10-19/59e7f5a215bc9.png
     * sort : 5
     * title : 赛车总动员 英文版
     * type_ico :
     * view_num : 0
     */


    private String add_date;
    private String add_time;
    private String flag;
    private String flag_name;
    private String id;
    private String img;
    private String mp3;
    private String sort;
    private String title;
    private String type_ico;
    private String view_num;
    private String type_id;
    private String type_name;
    private String word_file;

    private String add_date_format;

    private int outPos;//外层的位置

    private int innerPos;//内层的位置

    public SpeakAndReadItemInfo() {
    }

    public String getAdd_date() {
        return add_date;
    }

    public void setAdd_date(String add_date) {
        this.add_date = add_date;
    }

    public String getAdd_time() {
        return add_time;
    }

    public void setAdd_time(String add_time) {
        this.add_time = add_time;
    }

    public String getFlag() {
        return flag;
    }

    public void setFlag(String flag) {
        this.flag = flag;
    }

    public String getFlag_name() {
        return flag_name;
    }

    public void setFlag_name(String flag_name) {
        this.flag_name = flag_name;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getImg() {
        return img;
    }

    public void setImg(String img) {
        this.img = img;
    }

    public String getSort() {
        return sort;
    }

    public void setSort(String sort) {
        this.sort = sort;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getType_ico() {
        return type_ico;
    }

    public void setType_ico(String type_ico) {
        this.type_ico = type_ico;
    }

    public String getView_num() {
        return view_num;
    }

    public void setView_num(String view_num) {
        this.view_num = view_num;
    }

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

    public String getAdd_date_format() {
        return add_date_format;
    }

    public void setAdd_date_format(String add_date_format) {
        this.add_date_format = add_date_format;
    }

    public int getOutPos() {
        return outPos;
    }

    public void setOutPos(int outPos) {
        this.outPos = outPos;
    }

    public int getInnerPos() {
        return innerPos;
    }

    public void setInnerPos(int innerPos) {
        this.innerPos = innerPos;
    }

    public String getMp3() {
        return mp3;
    }

    public void setMp3(String mp3) {
        this.mp3 = mp3;
    }

    public String getWord_file() {
        return word_file;
    }

    public void setWord_file(String word_file) {
        this.word_file = word_file;
    }

    @Override
    public String toString() {
        return "SpeakAndReadItemInfo{" +
                "add_date='" + add_date + '\'' +
                ", add_time='" + add_time + '\'' +
                ", flag='" + flag + '\'' +
                ", flag_name='" + flag_name + '\'' +
                ", id='" + id + '\'' +
                ", img='" + img + '\'' +
                ", sort='" + sort + '\'' +
                ", title='" + title + '\'' +
                ", type_ico='" + type_ico + '\'' +
                ", view_num='" + view_num + '\'' +
                ", type_id='" + type_id + '\'' +
                ", type_name='" + type_name + '\'' +
                '}';
    }


    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(this.add_date);
        dest.writeString(this.add_time);
        dest.writeString(this.flag);
        dest.writeString(this.flag_name);
        dest.writeString(this.id);
        dest.writeString(this.img);
        dest.writeString(this.mp3);
        dest.writeString(this.sort);
        dest.writeString(this.title);
        dest.writeString(this.type_ico);
        dest.writeString(this.view_num);
        dest.writeString(this.type_id);
        dest.writeString(this.type_name);
        dest.writeString(this.word_file);
        dest.writeString(this.add_date_format);
        dest.writeInt(this.outPos);
        dest.writeInt(this.innerPos);
    }

    protected SpeakAndReadItemInfo(Parcel in) {
        this.add_date = in.readString();
        this.add_time = in.readString();
        this.flag = in.readString();
        this.flag_name = in.readString();
        this.id = in.readString();
        this.img = in.readString();
        this.mp3 = in.readString();
        this.sort = in.readString();
        this.title = in.readString();
        this.type_ico = in.readString();
        this.view_num = in.readString();
        this.type_id = in.readString();
        this.type_name = in.readString();
        this.word_file = in.readString();
        this.add_date_format = in.readString();
        this.outPos = in.readInt();
        this.innerPos = in.readInt();
    }

    public static final Parcelable.Creator<SpeakAndReadItemInfo> CREATOR = new Parcelable.Creator<SpeakAndReadItemInfo>() {
        @Override
        public SpeakAndReadItemInfo createFromParcel(Parcel source) {
            return new SpeakAndReadItemInfo(source);
        }

        @Override
        public SpeakAndReadItemInfo[] newArray(int size) {
            return new SpeakAndReadItemInfo[size];
        }
    };

}
