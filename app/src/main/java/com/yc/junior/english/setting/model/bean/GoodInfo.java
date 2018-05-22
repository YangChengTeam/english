package com.yc.junior.english.setting.model.bean;

/**
 * Created by wanglin  on 2017/11/6 16:23.
 */

public class GoodInfo {
    /**
     * id : 55
     * name : 6个月会员
     * type_id : 1
     * type_relate_val : 0
     * app_id : 0
     * img : http://wk2-voice.oss-cn-shenzhen.aliyuncs.com/mp3/2017-11-06/5a0019404c67a.png
     * desp : 6个月会员
     * price : 368.00
     * m_price : 368.00
     * vip_price : 368.00
     * unit : 月
     * use_time_limit : 6
     * sort : 50
     * status : 1
     * status_name : 上架
     */

    private String app_id;
    private String desp;
    private String id;
    private String img;
    private String m_price;
    private String name;
    private String pay_price;//支付价格
    private String price;
    private String sort;
    private String status;
    private String status_name;
    private String type_id;//1.普通会员 2.单独微课 3. 点读会员 4.svip 5.微课会员
    private String type_relate_val;

    private String vip_price;
    private String unit;
    private String use_time_limit;


    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getType_id() {
        return type_id;
    }

    public void setType_id(String type_id) {
        this.type_id = type_id;
    }

    public String getType_relate_val() {
        return type_relate_val;
    }

    public void setType_relate_val(String type_relate_val) {
        this.type_relate_val = type_relate_val;
    }

    public String getApp_id() {
        return app_id;
    }

    public void setApp_id(String app_id) {
        this.app_id = app_id;
    }

    public String getImg() {
        return img;
    }

    public void setImg(String img) {
        this.img = img;
    }

    public String getDesp() {
        return desp;
    }

    public void setDesp(String desp) {
        this.desp = desp;
    }

    public String getPrice() {
        return price;
    }

    public void setPrice(String price) {
        this.price = price;
    }

    public String getM_price() {
        return m_price;
    }

    public void setM_price(String m_price) {
        this.m_price = m_price;
    }

    public String getVip_price() {
        return vip_price;
    }

    public void setVip_price(String vip_price) {
        this.vip_price = vip_price;
    }

    public String getUnit() {
        return unit;
    }

    public void setUnit(String unit) {
        this.unit = unit;
    }

    public String getUse_time_limit() {
        return use_time_limit;
    }

    public void setUse_time_limit(String use_time_limit) {
        this.use_time_limit = use_time_limit;
    }

    public String getSort() {
        return sort;
    }

    public void setSort(String sort) {
        this.sort = sort;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getStatus_name() {
        return status_name;
    }

    public void setStatus_name(String status_name) {
        this.status_name = status_name;
    }

    public String getPay_price() {
        return pay_price;
    }

    public void setPay_price(String pay_price) {
        this.pay_price = pay_price;
    }



}
