package com.yc.english.read.model.domain;

import com.alibaba.fastjson.annotation.JSONField;
import com.chad.library.adapter.base.entity.AbstractExpandableItem;
import com.chad.library.adapter.base.entity.MultiItemEntity;
import com.yc.english.read.view.adapter.ReadWordItemClickAdapter;

/**
 * Created by admin on 2017/7/26.
 */

public class WordInfo extends AbstractExpandableItem<WordDetailInfo> implements MultiItemEntity {
    public static final int CLICK_ITEM_VIEW = 1;

    public int Type = CLICK_ITEM_VIEW;

    private String id;

    private String name;

    private String means;

    private String syllable;

    private String phonetic;

    @JSONField(name = "type")
    private String wdType;

    @JSONField(name = "book_id")
    private String bookId;

    @JSONField(name = "unit_id")
    private String unitId;

    @JSONField(name = "section_id")
    private String sectionId;

    private String voice;

    @JSONField(name = "is_del")
    private String isDel;

    @JSONField(name = "page_num")
    private String pageNum;

    private String rid;

    @JSONField(name = "ep_sentence")
    private String epSentence;

    @JSONField(name = "ep_sentence_means")
    private String epSentenceMeans;

    private String mp3url;//播放的音频文件

    private String word_voice;

    private boolean isPlay = false;

    public WordInfo() {
        super();
    }

    public WordInfo(String name, String means,int type) {
        this.name = name;
        this.means = means;
        this.Type = type;
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

    public String getPhonetic() {
        return phonetic;
    }

    public void setPhonetic(String phonetic) {
        this.phonetic = phonetic;
    }

    public String getBookId() {
        return bookId;
    }

    public void setBookId(String bookId) {
        this.bookId = bookId;
    }

    public String getUnitId() {
        return unitId;
    }

    public void setUnitId(String unitId) {
        this.unitId = unitId;
    }

    public String getSectionId() {
        return sectionId;
    }

    public void setSectionId(String sectionId) {
        this.sectionId = sectionId;
    }

    public String getVoice() {
        return voice;
    }

    public void setVoice(String voice) {
        this.voice = voice;
    }

    public String getIsDel() {
        return isDel;
    }

    public void setIsDel(String isDel) {
        this.isDel = isDel;
    }

    public String getPageNum() {
        return pageNum;
    }

    public void setPageNum(String pageNum) {
        this.pageNum = pageNum;
    }

    public String getRid() {
        return rid;
    }

    public void setRid(String rid) {
        this.rid = rid;
    }

    public String getWdType() {
        return wdType;
    }

    public void setWdType(String wdType) {
        this.wdType = wdType;
    }

    public String getEpSentence() {
        return epSentence;
    }

    public void setEpSentence(String epSentence) {
        this.epSentence = epSentence;
    }

    public String getEpSentenceMeans() {
        return epSentenceMeans;
    }

    public void setEpSentenceMeans(String epSentenceMeans) {
        this.epSentenceMeans = epSentenceMeans;
    }

    public WordInfo(final int type) {
        Type = type;
    }

    public void setType(int type) {
        this.Type = type;
    }

    public boolean isPlay() {
        return isPlay;
    }

    public void setPlay(boolean play) {
        isPlay = play;
    }

    public String getMp3url() {
        return mp3url;
    }

    public void setMp3url(String mp3url) {
        this.mp3url = mp3url;
    }

    public String getWord_voice() {
        return word_voice;
    }

    public void setWord_voice(String word_voice) {
        this.word_voice = word_voice;
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
