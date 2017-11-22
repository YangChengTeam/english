package com.yc.english.news.model.domain;

import java.util.List;

/**
 * Created by admin on 2017/11/7.
 */

public class OrderParams {

    private String userId;

    private String userName;

    private String appId;

    private String title;

    private String priceTotal;

    private String money;

    private String imeil;

    private String payWayName;

    private List<OrderGood> goodsList;

    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }

    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }

    public String getAppId() {
        return appId;
    }

    public void setAppId(String appId) {
        this.appId = appId;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getPriceTotal() {
        return priceTotal;
    }

    public void setPriceTotal(String priceTotal) {
        this.priceTotal = priceTotal;
    }

    public String getMoney() {
        return money;
    }

    public void setMoney(String money) {
        this.money = money;
    }

    public String getImeil() {
        return imeil;
    }

    public void setImeil(String imeil) {
        this.imeil = imeil;
    }

    public String getPayWayName() {
        return payWayName;
    }

    public void setPayWayName(String payWayName) {
        this.payWayName = payWayName;
    }

    public List<OrderGood> getGoodsList() {
        return goodsList;
    }

    public void setGoodsList(List<OrderGood> goodsList) {
        this.goodsList = goodsList;
    }
}
