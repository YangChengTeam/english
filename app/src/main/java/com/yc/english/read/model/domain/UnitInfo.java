package com.yc.english.read.model.domain;

import android.os.Parcel;
import android.os.Parcelable;

import com.alibaba.fastjson.annotation.JSONField;
import com.chad.library.adapter.base.entity.MultiItemEntity;

/**
 * Created by admin on 2017/7/26.
 */

public class UnitInfo implements MultiItemEntity, Parcelable {

    public static final int CLICK_ITEM_VIEW = 1;

    public int Type = CLICK_ITEM_VIEW;

    private String id;

    private String name;

    @JSONField(name = "simple_name")
    private String simpleName;

    private String pid;

    @JSONField(name = "book_id")
    private String bookId;

    private String number;

    @JSONField(name = "word_count")
    private String wordCount;

    @JSONField(name = "sentence_count")
    private String sentenceCount;

    @JSONField(name = "is_del")
    private String isDel;

    private String sort;

    private int free;//1是免费，2是收费

    public UnitInfo() {
        super();
    }

    public UnitInfo(final int type) {
        Type = type;
    }

    @Override
    public int getItemType() {
        return Type;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getSimpleName() {
        return simpleName;
    }

    public void setSimpleName(String simpleName) {
        this.simpleName = simpleName;
    }

    public String getPid() {
        return pid;
    }

    public void setPid(String pid) {
        this.pid = pid;
    }

    public String getBookId() {
        return bookId;
    }

    public void setBookId(String bookId) {
        this.bookId = bookId;
    }

    public String getNumber() {
        return number;
    }

    public void setNumber(String number) {
        this.number = number;
    }

    public String getWordCount() {
        return wordCount;
    }

    public void setWordCount(String wordCount) {
        this.wordCount = wordCount;
    }

    public String getSentenceCount() {
        return sentenceCount;
    }

    public void setSentenceCount(String sentenceCount) {
        this.sentenceCount = sentenceCount;
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

    public int getFree() {
        return free;
    }

    public void setFree(int free) {
        this.free = free;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeInt(this.Type);
        dest.writeString(this.id);
        dest.writeString(this.name);
        dest.writeString(this.simpleName);
        dest.writeString(this.pid);
        dest.writeString(this.bookId);
        dest.writeString(this.number);
        dest.writeString(this.wordCount);
        dest.writeString(this.sentenceCount);
        dest.writeString(this.isDel);
        dest.writeString(this.sort);
        dest.writeInt(this.free);
    }

    protected UnitInfo(Parcel in) {
        this.Type = in.readInt();
        this.id = in.readString();
        this.name = in.readString();
        this.simpleName = in.readString();
        this.pid = in.readString();
        this.bookId = in.readString();
        this.number = in.readString();
        this.wordCount = in.readString();
        this.sentenceCount = in.readString();
        this.isDel = in.readString();
        this.sort = in.readString();
        this.free = in.readInt();
    }

    public static final Parcelable.Creator<UnitInfo> CREATOR = new Parcelable.Creator<UnitInfo>() {
        @Override
        public UnitInfo createFromParcel(Parcel source) {
            return new UnitInfo(source);
        }

        @Override
        public UnitInfo[] newArray(int size) {
            return new UnitInfo[size];
        }
    };
}
