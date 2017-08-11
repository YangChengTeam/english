package com.yc.english.read.model.domain;

import com.chad.library.adapter.base.entity.MultiItemEntity;

import java.util.List;

/**
 * Created by admin on 2017/7/26.
 */

public class BookUnitInfo implements MultiItemEntity {
    public static final int CLICK_ITEM_VIEW = 1;

    private String bookTitle;

    private String bookUnitTotal;

    private String bookPress;

    private String bookImageUrl;

    private List<UnitInfo> data;

    public int Type;

    public String getBookTitle() {
        return bookTitle;
    }

    public void setBookTitle(String bookTitle) {
        this.bookTitle = bookTitle;
    }

    public String getBookUnitTotal() {
        return bookUnitTotal;
    }

    public void setBookUnitTotal(String bookUnitTotal) {
        this.bookUnitTotal = bookUnitTotal;
    }

    public String getBookPress() {
        return bookPress;
    }

    public void setBookPress(String bookPress) {
        this.bookPress = bookPress;
    }

    public String getBookImageUrl() {
        return bookImageUrl;
    }

    public void setBookImageUrl(String bookImageUrl) {
        this.bookImageUrl = bookImageUrl;
    }

    public List<UnitInfo> getData() {
        return data;
    }

    public void setData(List<UnitInfo> data) {
        this.data = data;
    }

    public BookUnitInfo() {
        super();
    }

    public BookUnitInfo(final int type) {
        Type = type;
    }

    @Override
    public int getItemType() {
        return Type;
    }
}
