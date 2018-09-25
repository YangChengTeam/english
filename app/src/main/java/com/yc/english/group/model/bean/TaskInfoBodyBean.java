package com.yc.english.group.model.bean;

import android.os.Parcel;
import android.os.Parcelable;

import java.util.List;

/**
 * Created by wanglin  on 2017/8/9 09:47.
 */

public class TaskInfoBodyBean implements Parcelable {

    private List<String> docs;
    private List<String> imgs;
    private List<String> voices;

    public TaskInfoBodyBean() {
    }

    public TaskInfoBodyBean(Parcel source) {
        docs = source.createStringArrayList();
        imgs = source.createStringArrayList();
        voices = source.createStringArrayList();
    }

    public List<String> getDocs() {
        return docs;
    }

    public void setDocs(List<String> docs) {
        this.docs = docs;
    }

    public List<String> getImgs() {
        return imgs;
    }

    public void setImgs(List<String> imgs) {
        this.imgs = imgs;
    }

    public List<String> getVoices() {
        return voices;
    }

    public void setVoices(List<String> voices) {
        this.voices = voices;
    }

    public static final Creator<TaskInfoBodyBean> CREATOR = new Creator<TaskInfoBodyBean>() {

        @Override
        public TaskInfoBodyBean createFromParcel(Parcel source) {
            return new TaskInfoBodyBean(source);
        }

        @Override
        public TaskInfoBodyBean[] newArray(int size) {
            return new TaskInfoBodyBean[size];
        }
    };

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeStringList(docs);
        dest.writeStringList(imgs);
        dest.writeStringList(voices);
    }
}
