package com.yc.english.read.model.domain;

import com.chad.library.adapter.base.entity.AbstractExpandableItem;
import com.chad.library.adapter.base.entity.MultiItemEntity;
import com.yc.english.read.view.adapter.ReadWordItemClickAdapter;

/**
 * Created by admin on 2017/7/26.
 */

public class WordInfo extends AbstractExpandableItem<WordDetailInfo> implements MultiItemEntity {
    public static final int CLICK_ITEM_VIEW = 1;

    private String wordName;

    private String wordCnName;

    public String getWordName() {
        return wordName;
    }

    public void setWordName(String wordName) {
        this.wordName = wordName;
    }

    public String getWordCnName() {
        return wordCnName;
    }

    public void setWordCnName(String wordCnName) {
        this.wordCnName = wordCnName;
    }

    public WordInfo() {
        super();
    }

    public WordInfo(String wordName, String wordCnName) {
        this.wordName = wordName;
        this.wordCnName = wordCnName;
    }

    @Override
    public int getItemType() {
        return ReadWordItemClickAdapter.TYPE_LEVEL_0;
    }

    @Override
    public int getLevel() {
        return 0;
    }
}
