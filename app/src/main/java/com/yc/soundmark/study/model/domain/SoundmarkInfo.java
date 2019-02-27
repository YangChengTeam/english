package com.yc.soundmark.study.model.domain;

import java.util.List;

/**
 * Created by wanglin  on 2018/11/9 18:20.
 */
public class SoundmarkInfo {
    private List<WordInfo> wordInfos;

    public List<WordInfo> getWordInfos() {
        return wordInfos;
    }

    public void addWordInfos(List<WordInfo> wordInfos) {
        this.wordInfos = wordInfos;
    }
}
