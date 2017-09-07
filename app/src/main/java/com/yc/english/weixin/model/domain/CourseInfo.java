package com.yc.english.weixin.model.domain;

import android.os.Parcel;
import android.os.Parcelable;

import org.greenrobot.greendao.annotation.Entity;

import java.io.Serializable;
import org.greenrobot.greendao.annotation.Generated;

/**
 * Created by zhangkai on 2017/8/30.
 */

@Entity
public class CourseInfo implements Parcelable {
    public CourseInfo() {
    }


    private String id;
    private String title;
    private String url;
    private String keywords;
    private String type_id;
    private String period;
    private String flag;
    private String author;
    private String add_time;
    private String add_date;
    private String img;
    private String html;

    public String getHtml() {
        return html;
    }

    public void setHtml(String html) {
        this.html = html;
    }

    public String getImg() {
        return img;
    }

    public void setImg(String img) {
        this.img = img;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getKeywords() {
        return keywords;
    }

    public void setKeywords(String keywords) {
        this.keywords = keywords;
    }

    public String getType_id() {
        return type_id;
    }

    public void setType_id(String type_id) {
        this.type_id = type_id;
    }

    public String getPeriod() {
        return period;
    }

    public void setPeriod(String period) {
        this.period = period;
    }

    public String getFlag() {
        return flag;
    }

    public void setFlag(String flag) {
        this.flag = flag;
    }

    public String getAuthor() {
        return author;
    }

    public void setAuthor(String author) {
        this.author = author;
    }

    public String getAdd_time() {
        return add_time;
    }

    public void setAdd_time(String add_time) {
        this.add_time = add_time;
    }

    public String getAdd_date() {
        return add_date;
    }

    public void setAdd_date(String add_date) {
        this.add_date = add_date;
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(id);
        dest.writeString(title);
        dest.writeString(url);
        dest.writeString(keywords);
        dest.writeString(type_id);
        dest.writeString(period);
        dest.writeString(author);
        dest.writeString(add_time);
        dest.writeString(img);
        dest.writeString(html);
    }

    public CourseInfo(Parcel in) {
        id = in.readString();
        title = in.readString();
        url = in.readString();
        keywords = in.readString();
        type_id = in.readString();
        period = in.readString();
        author = in.readString();
        add_time = in.readString();
        img = in.readString();
        html = in.readString();
    }

    @Generated(hash = 902842800)
    public CourseInfo(String id, String title, String url, String keywords, String type_id,
            String period, String flag, String author, String add_time, String add_date, String img,
            String html) {
        this.id = id;
        this.title = title;
        this.url = url;
        this.keywords = keywords;
        this.type_id = type_id;
        this.period = period;
        this.flag = flag;
        this.author = author;
        this.add_time = add_time;
        this.add_date = add_date;
        this.img = img;
        this.html = html;
    }

    public static final Parcelable.Creator<CourseInfo> CREATOR = new Parcelable.Creator<CourseInfo>() {
        public CourseInfo createFromParcel(Parcel in) {
            return new CourseInfo(in);
        }

        public CourseInfo[] newArray(int size) {
            return new CourseInfo[size];
        }
    };
}
