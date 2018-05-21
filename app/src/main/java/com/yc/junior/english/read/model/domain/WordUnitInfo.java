package com.yc.junior.english.read.model.domain;

import com.alibaba.fastjson.annotation.JSONField;
import com.chad.library.adapter.base.entity.MultiItemEntity;

/**
 * Created by admin on 2017/7/26.
 */

public class WordUnitInfo implements MultiItemEntity {
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

    public WordUnitInfo() {
        super();
    }

    public WordUnitInfo(final int type) {
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
}
