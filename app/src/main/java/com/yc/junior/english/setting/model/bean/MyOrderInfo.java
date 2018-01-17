package com.yc.junior.english.setting.model.bean;

import com.alibaba.fastjson.annotation.JSONField;

/**
 * Created by admin on 2017/11/10.
 * 我的订单信息
 */

public class MyOrderInfo {

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
    private String ipNum;//IP数量
    @JSONField(name = "user_num")
    private String userNum;//学习人数,
    @JSONField(name = "unit_num")
    private String unitNum;//系列单元数量,
    private String sort;//排序位置
    @JSONField(name = "add_time")
    private String addTime;//添加时间
    @JSONField(name = "add_date")
    private String addDate;////添加日期

    @JSONField(name = "order_sn")
    private String orderSn;//订单号
    @JSONField(name = "order_money")
    private String orderMoney;//订单金额
    @JSONField(name = "order_status")
    private int orderStatus;//状态：0(待支付)，1(支付失败)，2(支付成功)，3(发货失败)，4(交易完成)，5（申请退货），6（退货失败），7（退货成功）
    @JSONField(name = "order_add_time")
    private Long orderAddTime;//订单添加时间

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

    public String getOrderSn() {
        return orderSn;
    }

    public void setOrderSn(String orderSn) {
        this.orderSn = orderSn;
    }

    public String getOrderMoney() {
        return orderMoney;
    }

    public void setOrderMoney(String orderMoney) {
        this.orderMoney = orderMoney;
    }

    public int getOrderStatus() {
        return orderStatus;
    }

    public void setOrderStatus(int orderStatus) {
        this.orderStatus = orderStatus;
    }

    public Long getOrderAddTime() {
        return orderAddTime;
    }

    public void setOrderAddTime(Long orderAddTime) {
        this.orderAddTime = orderAddTime;
    }
}
