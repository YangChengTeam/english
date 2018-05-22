package com.yc.english.topic.model.bean;

import android.os.Parcel;
import android.os.Parcelable;

import java.util.List;

/**
 * Created by wanglin  on 2017/9/29 16:57.
 */

public class TopicInfo implements Parcelable {
    private String subTitle;
    private List<String> anwserList;
    private String answer;
    private String analysis;

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(this.subTitle);
        dest.writeStringList(this.anwserList);
        dest.writeString(this.answer);
        dest.writeString(this.analysis);
    }

    public TopicInfo() {
    }

    protected TopicInfo(Parcel in) {
        this.subTitle = in.readString();
        this.anwserList = in.createStringArrayList();
        this.answer = in.readString();
        this.analysis = in.readString();
    }

    public static final Parcelable.Creator<TopicInfo> CREATOR = new Parcelable.Creator<TopicInfo>() {
        @Override
        public TopicInfo createFromParcel(Parcel source) {
            return new TopicInfo(source);
        }

        @Override
        public TopicInfo[] newArray(int size) {
            return new TopicInfo[size];
        }
    };

    public String getSubTitle() {
        return subTitle;
    }

    public void setSubTitle(String subTitle) {
        this.subTitle = subTitle;
    }

    public List<String> getAnwserList() {
        return anwserList;
    }

    public void setAnwserList(List<String> anwserList) {
        this.anwserList = anwserList;
    }

    public String getAnswer() {
        return answer;
    }

    public void setAnswer(String answer) {
        this.answer = answer;
    }

    public String getAnalysis() {
        return analysis;
    }

    public void setAnalysis(String analysis) {
        this.analysis = analysis;
    }
}
