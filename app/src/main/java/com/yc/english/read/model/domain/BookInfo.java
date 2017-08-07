package com.yc.english.read.model.domain;

import com.chad.library.adapter.base.entity.MultiItemEntity;

import org.greenrobot.greendao.annotation.Entity;
import org.greenrobot.greendao.annotation.Id;
import org.greenrobot.greendao.annotation.Generated;

/**
 * Created by admin on 2017/7/26.
 * 教材
 */

@Entity
public class BookInfo implements MultiItemEntity {

    public static final int CLICK_ITEM_VIEW = 1;

    @Id
    private Long id;

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

    @Generated(hash = 1135649993)
    public BookInfo(Long id, String bookId, String bookName, String bookImageUrl,
            int Type) {
        this.id = id;
        this.bookId = bookId;
        this.bookName = bookName;
        this.bookImageUrl = bookImageUrl;
        this.Type = Type;
    }

    @Override
    public int getItemType() {
        return Type;
    }

    public Long getId() {
        return this.id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public int getType() {
        return this.Type;
    }

    public void setType(int Type) {
        this.Type = Type;
    }

}
