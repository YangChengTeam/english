package com.yc.soundmark.study.model.domain;

import java.util.List;



/**
 * Created by wanglin  on 2018/10/30 16:58.
 */
public class StudyInfoWrapper {


    /**
     * words : [{"id":"1","vid":"1","word":"w#e#","en":"我们","sort":"1","mp3":"https://yb.bshu.com/uploads/20180813/d812c3b94b9d3726b97cf0b3e5dac90b.mp3"},{"id":"2","vid":"1","word":"m#ee#t ","en":"遇见","sort":"2","mp3":"https://yb.bshu.com/uploads/20180813/6ab830e13e98f6ca74bd97cb21022014.mp3"},{"id":"4","vid":"1","word":"t#ea#ch","en":"教","sort":"3","mp3":"https://yb.bshu.com/uploads/20180813/816c5b7b0e2b494aed947d6b06b0ae17.mp3"},{"id":"3","vid":"1","word":"#e#vening","en":"晚上","sort":"4","mp3":"https://yb.bshu.com/uploads/20180813/8879ecc4297fd0ac1bc349c9483f6032.mp3"}]
     * info : {"id":"1","name":"/i:/","is_vip":"0","vs":"0","type":"1","sort":"1","word":"sh#ee#p","voice":"/ʃ#i:#p/","image":"https://yb.bshu.com/uploads/20180821/a2751f6a0b6a260f2c7114aaf425cf03.jpg","cn":"绵羊","voice_video":"http://tic.upkao.com/Upload/video/1.mp4","desp":"舌尖抵下齿，舌前部尽量向硬颚抬起。嘴唇向两旁伸开成扁平型。注意一定要把音发足。","vowel":"0","mp3":"https://yb.bshu.com/uploads/20180820/f71cd264e5cfbf54df79b401f70e66c4.mp3","mouth_desp":"舌尖抵下齿，舌前部尽量向硬颚抬起。嘴唇向两旁伸开成扁平型。注意一定要把音发足。","mouth_cover":"https://yb.bshu.com/uploads/20180821/f4564c9f63b3e293189c407468371e23.jpg","video_cover":"http://tic.upkao.com/Upload/cover/1.jpg","desp_mp3":"http://phonetic.upkao.com/video/ttS8dPyzKh.mp3","mouth_desp_mp3":null,"vowel_type":"1","voice_info":"","vowel_img":"http://tic.upkao.com/Upload/Picture/1.png","vowel_mp3":"http://tic.upkao.com/Upload/mp3/1.mp3","voice_desp":null,"voice_desp_1":null}
     * phrase : [{"id":"1","vid":"1","phrase":"gr#ee#n t#ea#","en":"绿茶","sort":"1","mp3":"https://yb.bshu.com/uploads/20180813/5eedfd72b73236559c9703e3f7bb8925.mp3"},{"id":"3","vid":"1","phrase":"d#ee#p s#ea#","en":"深海","sort":"2","mp3":"https://yb.bshu.com/uploads/20180813/f60f3bd6d66908bdf757ca0adc7333c4.mp3"},{"id":"4","vid":"1","phrase":"#ea#t the m#ea#t","en":"吃肉","sort":"3","mp3":"https://yb.bshu.com/uploads/20180813/3ed26c076e665fd5b021a93602dc398f.mp3"}]
     * sentence : [{"id":"1","vid":"1","sentence":"Would you like coff#ee# or t#ea#?","en":"您想喝咖啡还是茶？","sort":"1","mp3":"https://yb.bshu.com/uploads/20180813/31fe2d19cb70620c5332324db52986ee.mp3"},{"id":"3","vid":"1","sentence":"A friend in n#ee#d is a friend ind#ee#d.","en":"患难见真情。","sort":"2","mp3":"https://yb.bshu.com/uploads/20180813/f907ff6857e46ed3cdfddd1c3e061b77.mp3"}]
     */

    private StudyInfo info;
    private List<WordInfo> words;
    private List<PhraseInfo> phrase;
    private List<SentenceInfo> sentence;

    public StudyInfo getInfo() {
        return info;
    }

    public void setInfo(StudyInfo info) {
        this.info = info;
    }

    public List<WordInfo> getWords() {
        return words;
    }

    public void setWords(List<WordInfo> words) {
        this.words = words;
    }

    public List<PhraseInfo> getPhrase() {
        return phrase;
    }

    public void setPhrase(List<PhraseInfo> phrase) {
        this.phrase = phrase;
    }

    public List<SentenceInfo> getSentence() {
        return sentence;
    }

    public void setSentence(List<SentenceInfo> sentence) {
        this.sentence = sentence;
    }

}
