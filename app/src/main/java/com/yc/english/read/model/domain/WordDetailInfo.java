package com.yc.english.read.model.domain;

import com.chad.library.adapter.base.entity.MultiItemEntity;
import com.yc.english.read.view.adapter.ReadWordItemClickAdapter;

/**
 * Created by admin on 2017/7/26.
 */

public class WordDetailInfo implements MultiItemEntity {
    public static final int CLICK_ITEM_VIEW = 1;

    private String wordExample;

    private String wordCnExample;

    private boolean isPlay;

    public boolean isPlay() {
        return isPlay;
    }

    public void setPlay(boolean play) {
        isPlay = play;
    }

    public String getWordExample() {
        return wordExample;
    }

    public void setWordExample(String wordExample) {
        this.wordExample = wordExample;
    }

    public String getWordCnExample() {
        return wordCnExample;
    }

    public void setWordCnExample(String wordCnExample) {
        this.wordCnExample = wordCnExample;
    }

    public int Type;


    public WordDetailInfo() {
        super();
    }

    public WordDetailInfo(final int type) {
        Type = type;
    }

    public WordDetailInfo(String wordExample, String wordCnExample) {
        this.wordExample = wordExample;
        this.wordCnExample = wordCnExample;
    }

    @Override
    public int getItemType() {
        return ReadWordItemClickAdapter.TYPE_LEVEL_1;
    }
}
