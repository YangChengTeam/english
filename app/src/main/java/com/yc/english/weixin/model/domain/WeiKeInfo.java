package com.yc.english.weixin.model.domain;

import android.os.Parcel;
import android.os.Parcelable;

import com.alibaba.fastjson.annotation.JSONField;

/**
 * Created by admin on 2017/11/6.
 */

public class WeiKeInfo implements Parcelable{

    private String id;
    private String title;
    private String pid;
    private String img;
    private String url;
    private String keywords;
    @JSONField(name = "type_id")
    private String typeId;
    private String period;
    private String flag;
    private String author;
    @JSONField(name = "pv_num")
    private String pvNum;
    @JSONField(name = "ip_num")
    private String ipNum;
    @JSONField(name = "user_num")
    private String userNum;
    @JSONField(name = "unit_num")
    private String unitNum;
    private String sort;
    @JSONField(name = "add_time")
    private String addTime;
    @JSONField(name = "add_date")
    private String addDate;
    @JSONField(name = "type_name")
    private String typeName;
    private String price;
    @JSONField(name = "m_price")
    private String mPrice;
    @JSONField(name = "vip_price")
    private String vip_price;
    @JSONField(name = "good_id")
    private String goodId;
    @JSONField(name = "is_pay")
    private String isPay;
    @JSONField(name = "is_vip")
    private String isVip;
    private String cnt;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getPid() {
        return pid;
    }

    public void setPid(String pid) {
        this.pid = pid;
    }

    public String getImg() {
        return img;
    }

    public void setImg(String img) {
        this.img = img;
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    public String getKeywords() {
        return keywords;
    }

    public void setKeywords(String keywords) {
        this.keywords = keywords;
    }

    public String getTypeId() {
        return typeId;
    }

    public void setTypeId(String typeId) {
        this.typeId = typeId;
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

    public String getPvNum() {
        return pvNum;
    }

    public void setPvNum(String pvNum) {
        this.pvNum = pvNum;
    }

    public String getIpNum() {
        return ipNum;
    }

    public void setIpNum(String ipNum) {
        this.ipNum = ipNum;
    }

    public String getUserNum() {
        return userNum;
    }

    public void setUserNum(String userNum) {
        this.userNum = userNum;
    }

    public String getUnitNum() {
        return unitNum;
    }

    public void setUnitNum(String unitNum) {
        this.unitNum = unitNum;
    }

    public String getSort() {
        return sort;
    }

    public void setSort(String sort) {
        this.sort = sort;
    }

    public String getAddTime() {
        return addTime;
    }

    public void setAddTime(String addTime) {
        this.addTime = addTime;
    }

    public String getAddDate() {
        return addDate;
    }

    public void setAddDate(String addDate) {
        this.addDate = addDate;
    }

    public String getTypeName() {
        return typeName;
    }

    public void setTypeName(String typeName) {
        this.typeName = typeName;
    }

    public String getPrice() {
        return price;
    }

    public void setPrice(String price) {
        this.price = price;
    }

    public String getmPrice() {
        return mPrice;
    }

    public void setmPrice(String mPrice) {
        this.mPrice = mPrice;
    }

    public String getVip_price() {
        return vip_price;
    }

    public void setVip_price(String vip_price) {
        this.vip_price = vip_price;
    }

    public String getGoodId() {
        return goodId;
    }

    public void setGoodId(String goodId) {
        this.goodId = goodId;
    }

    public String getIsPay() {
        return isPay;
    }

    public void setIsPay(String isPay) {
        this.isPay = isPay;
    }

    public String getIsVip() {
        return isVip;
    }

    public void setIsVip(String isVip) {
        this.isVip = isVip;
    }

    public String getCnt() {
        return cnt;
    }

    public void setCnt(String cnt) {
        this.cnt = cnt;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(this.id);
        dest.writeString(this.title);
        dest.writeString(this.pid);
        dest.writeString(this.img);
        dest.writeString(this.url);
        dest.writeString(this.keywords);
        dest.writeString(this.typeId);
        dest.writeString(this.period);
        dest.writeString(this.flag);
        dest.writeString(this.author);
        dest.writeString(this.pvNum);
        dest.writeString(this.ipNum);
        dest.writeString(this.userNum);
        dest.writeString(this.unitNum);
        dest.writeString(this.sort);
        dest.writeString(this.addTime);
        dest.writeString(this.addDate);
        dest.writeString(this.typeName);
        dest.writeString(this.price);
        dest.writeString(this.mPrice);
        dest.writeString(this.vip_price);
        dest.writeString(this.goodId);
        dest.writeString(this.isPay);
        dest.writeString(this.isVip);
        dest.writeString(this.cnt);
    }

    public WeiKeInfo() {
    }

    protected WeiKeInfo(Parcel in) {
        this.id = in.readString();
        this.title = in.readString();
        this.pid = in.readString();
        this.img = in.readString();
        this.url = in.readString();
        this.keywords = in.readString();
        this.typeId = in.readString();
        this.period = in.readString();
        this.flag = in.readString();
        this.author = in.readString();
        this.pvNum = in.readString();
        this.ipNum = in.readString();
        this.userNum = in.readString();
        this.unitNum = in.readString();
        this.sort = in.readString();
        this.addTime = in.readString();
        this.addDate = in.readString();
        this.typeName = in.readString();
        this.price = in.readString();
        this.mPrice = in.readString();
        this.vip_price = in.readString();
        this.goodId = in.readString();
        this.isPay = in.readString();
        this.isVip = in.readString();
        this.cnt = in.readString();
    }

    public static final Parcelable.Creator<WeiKeInfo> CREATOR = new Creator<WeiKeInfo>() {
        @Override
        public WeiKeInfo createFromParcel(Parcel source) {
            return new WeiKeInfo(source);
        }

        @Override
        public WeiKeInfo[] newArray(int size) {
            return new WeiKeInfo[size];
        }
    };
}
