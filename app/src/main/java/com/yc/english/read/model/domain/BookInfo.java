package com.yc.english.read.model.domain;

import com.chad.library.adapter.base.entity.MultiItemEntity;

/**
 * Created by admin on 2017/7/26.
 * 教材
 */

public class BookInfo implements MultiItemEntity {

    public static final int CLICK_ITEM_VIEW = 1;

    private String bookId;

    /**
     * 教材名称
     */

    private String bookName;
    /**
     * 教材图片资源
     */
    private String bookImageUrl;


    public BookInfo() {
        super();
    }

    public String getBookId() {
        return bookId;
    }

    public void setBookId(String bookId) {
        this.bookId = bookId;
    }

    public String getBookName() {
        return bookName;
    }

    public void setBookName(String bookName) {
        this.bookName = bookName;
    }

    public String getBookImageUrl() {
        return bookImageUrl;
    }

    public void setBookImageUrl(String bookImageUrl) {
        this.bookImageUrl = bookImageUrl;
    }

    public int Type;

    public BookInfo(final int type) {
        Type = type;
    }

    @Override
    public int getItemType() {
        return Type;
    }

}
