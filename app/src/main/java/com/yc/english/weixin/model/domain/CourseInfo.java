package com.yc.english.weixin.model.domain;

import android.os.Parcel;
import android.os.Parcelable;

import org.greenrobot.greendao.annotation.Entity;
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
    private int url_type;

    /**
     * add_date : 20170906
     * add_time : 1504669373
     * author :
     * body : 哈希表（Hash table，也叫散列表），是根据关键码值(Key value)而直接进行访问的数据结构。也就是说，它通过把关键码值映射到表中一个位置来访问记录，以加快查找的速度。这个映射函数叫做散列函数，存放记录的数组叫做散列表。哈希表hashtable(key，value) 的做法其实很简单，就是把Key通过一个固定的算法函数既所谓的哈希函数转换成一个整型数字，然后就将该数字对数组长度进行取余，取余结果就当作数组的下标，将value存储在以该数字为下标的数组空间里。
     * flag : 1
     * id : 81
     * img : http://en.qqtn.com/Upload/task/59af6ebd9fbd2.png
     * ip_num : 0
     * keywords : 1111111111111
     * period : 0
     * pv_num : 0
     * sort : 30
     * title : 什么是哈希表？1225
     * type_id : 7
     * url : http://en.qqtn.com/Upload/Picture/2017-08-16/59943ac3ed7af.voice
     */


    private String body;
    private String ip_num;
    private String pv_num;
    private String sort;


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
        dest.writeString(add_date);
        dest.writeString(img);
        dest.writeString(html);
        dest.writeString(body);
        dest.writeString(ip_num);
        dest.writeString(pv_num);
        dest.writeString(sort);
        dest.writeInt(url_type);
    }

    public String getBody() {
        return this.body;
    }

    public void setBody(String body) {
        this.body = body;
    }

    public String getIp_num() {
        return this.ip_num;
    }

    public void setIp_num(String ip_num) {
        this.ip_num = ip_num;
    }

    public String getPv_num() {
        return this.pv_num;
    }

    public void setPv_num(String pv_num) {
        this.pv_num = pv_num;
    }

    public String getSort() {
        return this.sort;
    }

    public void setSort(String sort) {
        this.sort = sort;
    }

    public int getUrl_type() {
        return this.url_type;
    }

    public void setUrl_type(int url_type) {
        this.url_type = url_type;
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
        add_date = in.readString();
        img = in.readString();
        html = in.readString();
        body = in.readString();
        ip_num = in.readString();
        pv_num = in.readString();
        sort = in.readString();
        url_type = in.readInt();
    }

    @Generated(hash = 1503061438)
    public CourseInfo(String id, String title, String url, String keywords, String type_id, String period, String flag, String author, String add_time, String add_date, String img, String html, int url_type, String body, String ip_num, String pv_num,
            String sort) {
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
        this.url_type = url_type;
        this.body = body;
        this.ip_num = ip_num;
        this.pv_num = pv_num;
        this.sort = sort;
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
