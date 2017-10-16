package com.yc.english.speak.model.bean;

import android.os.Parcel;
import android.os.Parcelable;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by wanglin  on 2017/10/12 15:26.
 */

public class EnglishInfo implements Parcelable {

    /**
     * title : 经典影视
     * type : 1
     * item_list : [{"url":"http://en.wk2.com/ueditor/php/upload/image/20171012/1507769710256192.png","play_count":13562,"update_date":"2017年09月28日","sub_title":"功夫熊猫3 英文版"},{"url":"http://en.wk2.com/ueditor/php/upload/image/20171012/1507769710256192.png","play_count":10871,"update_date":"2017年08月28日","sub_title":"冰川时代4 英文版"},{"url":"http://en.wk2.com/ueditor/php/upload/image/20171012/1507769710256192.png","play_count":12362,"update_date":"2017年09月20日","sub_title":"机器总动员 英文版"},{"url":"http://en.wk2.com/ueditor/php/upload/image/20171012/1507769710256192.png","play_count":11762,"update_date":"2017年09月02日","sub_title":"怪物史瑞克4 英文版"},{"url":"http://en.wk2.com/ueditor/php/upload/image/20171012/1507769710256192.png","play_count":14456,"update_date":"2017年08月28日","sub_title":"赛车总动员 英文版"}]
     */

    private String title;
    private int type;
    private List<EnglishItemInfo> item_list;

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public int getType() {
        return type;
    }

    public void setType(int type) {
        this.type = type;
    }

    public List<EnglishItemInfo> getItem_list() {
        return item_list;
    }

    public void setItem_list(List<EnglishItemInfo> item_list) {
        this.item_list = item_list;
    }


    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(this.title);
        dest.writeInt(this.type);
        dest.writeList(this.item_list);
    }

    public EnglishInfo() {
    }

    protected EnglishInfo(Parcel in) {
        this.title = in.readString();
        this.type = in.readInt();
        this.item_list = new ArrayList<EnglishItemInfo>();
        in.readList(this.item_list, EnglishItemInfo.class.getClassLoader());
    }

    public static final Parcelable.Creator<EnglishInfo> CREATOR = new Parcelable.Creator<EnglishInfo>() {
        @Override
        public EnglishInfo createFromParcel(Parcel source) {
            return new EnglishInfo(source);
        }

        @Override
        public EnglishInfo[] newArray(int size) {
            return new EnglishInfo[size];
        }
    };
}
