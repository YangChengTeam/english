package com.yc.soundmark.base.model.domain;

/**
 * Created by wanglin  on 2018/10/30 08:55.
 */
public class GoodInfo {


    /**
     * id : 17
     * title : 音标点读
     * sub_title : 每天十分钟，十分钟就会读单词
     * price : 79.00
     * real_price : 49.00
     * alias :
     * icon :
     */

    private String id;
    private String title;
    private String sub_title;
    private String price;
    private String real_price;
    private String alias;
    private String icon;

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

    public String getSub_title() {
        return sub_title;
    }

    public void setSub_title(String sub_title) {
        this.sub_title = sub_title;
    }

    public String getPrice() {
        return price;
    }

    public void setPrice(String price) {
        this.price = price;
    }

    public String getReal_price() {
        return real_price;
    }

    public void setReal_price(String real_price) {
        this.real_price = real_price;
    }

    public String getAlias() {
        return alias;
    }

    public void setAlias(String alias) {
        this.alias = alias;
    }

    public String getIcon() {
        return icon;
    }

    public void setIcon(String icon) {
        this.icon = icon;

    }
}
