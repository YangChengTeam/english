package com.yc.english.group.model.bean;

import android.os.Parcel;
import android.os.Parcelable;

import java.util.List;

/**
 * Created by wanglin  on 2017/8/7 17:10.
 */

public class TaskInfo implements Parcelable {


    /**
     * body : {"docs":[""],"imgs":[""],"voices":[""]}
     * class_ids : ["97"]
     * desp : 好咯
     * id : 22
     * publisher : 16
     */

    private TaskInfoBodyBean body;
    private String desp;
    private String id;
    private List<String> class_ids;
    private String publisher;



    private String add_date;
    private String add_time;
    private String class_id;
    private String is_del;
    private String task_id;
    private String title;
    private String type;
    private String add_week;
    private String score;

    public TaskInfo() {
    }

    public TaskInfo(Parcel source) {
        desp = source.readString();
        id = source.readString();
        publisher = source.readString();
        class_ids=  source.createStringArrayList();
        body= source.readParcelable(TaskInfo.class.getClassLoader());

    }

    public TaskInfoBodyBean getBody() {
        return body;
    }

    public void setBody(TaskInfoBodyBean body) {
        this.body = body;
    }

    public String getDesp() {
        return desp;
    }

    public void setDesp(String desp) {
        this.desp = desp;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getPublisher() {
        return publisher;
    }

    public void setPublisher(String publisher) {
        this.publisher = publisher;
    }

    public List<String> getClass_ids() {
        return class_ids;
    }

    public void setClass_ids(List<String> class_ids) {
        this.class_ids = class_ids;
    }

    public String getAdd_date() {
        return add_date;
    }

    public void setAdd_date(String add_date) {
        this.add_date = add_date;
    }

    public String getAdd_time() {
        return add_time;
    }

    public void setAdd_time(String add_time) {
        this.add_time = add_time;
    }

    public String getClass_id() {
        return class_id;
    }

    public void setClass_id(String class_id) {
        this.class_id = class_id;
    }

    public String getIs_del() {
        return is_del;
    }

    public void setIs_del(String is_del) {
        this.is_del = is_del;
    }

    public String getTask_id() {
        return task_id;
    }

    public void setTask_id(String task_id) {
        this.task_id = task_id;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getAdd_week() {
        return add_week;
    }

    public void setAdd_week(String add_week) {
        this.add_week = add_week;
    }

    public String getScore() {
        return score;
    }

    public void setScore(String score) {
        this.score = score;
    }

    public static final Creator<TaskInfo> CREATOR = new Creator<TaskInfo>() {

        @Override
        public TaskInfo createFromParcel(Parcel source) {
            return new TaskInfo(source);
        }

        @Override
        public TaskInfo[] newArray(int size) {
            return new TaskInfo[size];
        }
    };

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(desp);
        dest.writeString(id);
        dest.writeString(publisher);
        dest.writeStringList(class_ids);
        dest.writeParcelable(body,flags);

    }

    @Override
    public String toString() {
        return "TaskInfo{" +
                "body=" + body +
                ", desp='" + desp + '\'' +
                ", id='" + id + '\'' +
                ", publisher='" + publisher + '\'' +
                ", class_ids=" + class_ids +
                '}';
    }
}
