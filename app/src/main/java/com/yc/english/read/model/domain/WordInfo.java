package com.yc.english.read.model.domain;

import com.chad.library.adapter.base.entity.AbstractExpandableItem;
import com.chad.library.adapter.base.entity.MultiItemEntity;
import com.yc.english.read.view.adapter.ReadWordItemClickAdapter;

/**
 * Created by admin on 2017/7/26.
 */

public class WordInfo extends AbstractExpandableItem<WordDetailInfo> implements MultiItemEntity {
    public static final int CLICK_ITEM_VIEW = 1;

    private String word;
    private String bookid;
    private String means;
    private String syllable;
    private String wordtype;
    private String course;
    private String lsrw;
    private String unitid;
    private String phonetic;
    private String wordid;
    private String voice;

    public WordInfo() {
        super();
    }

    public String getWord() {
        return word;
    }

    public void setWord(String word) {
        this.word = word;
    }

    public String getBookid() {
        return bookid;
    }

    public void setBookid(String bookid) {
        this.bookid = bookid;
    }

    public String getMeans() {
        return means;
    }

    public void setMeans(String means) {
        this.means = means;
    }

    public String getSyllable() {
        return syllable;
    }

    public void setSyllable(String syllable) {
        this.syllable = syllable;
    }

    public String getWordtype() {
        return wordtype;
    }

    public void setWordtype(String wordtype) {
        this.wordtype = wordtype;
    }

    public String getCourse() {
        return course;
    }

    public void setCourse(String course) {
        this.course = course;
    }

    public String getLsrw() {
        return lsrw;
    }

    public void setLsrw(String lsrw) {
        this.lsrw = lsrw;
    }

    public String getUnitid() {
        return unitid;
    }

    public void setUnitid(String unitid) {
        this.unitid = unitid;
    }

    public String getPhonetic() {
        return phonetic;
    }

    public void setPhonetic(String phonetic) {
        this.phonetic = phonetic;
    }

    public String getWordid() {
        return wordid;
    }

    public void setWordid(String wordid) {
        this.wordid = wordid;
    }

    public String getVoice() {
        return voice;
    }

    public void setVoice(String voice) {
        this.voice = voice;
    }

    public int Type;

    public WordInfo(final int type) {
        Type = type;
    }

    public WordInfo (String word,String means){
        this.word = word;
        this.means = means;
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
