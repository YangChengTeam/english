package com.yc.english.setting.model.bean;

import java.util.List;

/**
 * Created by wanglin  on 2017/11/6 16:22.
 */

public class GoodInfoWrapper {

    /**
     * code : 1
     * data : {"list":[{"id":"55","name":"6个月会员","type_id":"1","type_relate_val":"0","app_id":"0","img":"http://wk2-voice.oss-cn-shenzhen.aliyuncs.com/mp3/2017-11-06/5a0019404c67a.png","desp":"6个月会员","price":"368.00","m_price":"368.00","vip_price":"368.00","unit":"月","use_time_limit":"6","sort":"50","status":"1","status_name":"上架"},{"id":"54","name":"3个月会员","type_id":"1","type_relate_val":"0","app_id":"0","img":"http://wk2-voice.oss-cn-shenzhen.aliyuncs.com/mp3/2017-11-06/5a0018fd7787a.png","desp":"3个月会员","price":"198.00","m_price":"198.00","vip_price":"198.00","unit":"月","use_time_limit":"3","sort":"50","status":"1","status_name":"上架"}]}
     * msg :
     */


    private List<GoodInfo> svip;
    private List<GoodInfo> vip;

    private List<GoodInfo> weike;
    private List<GoodInfo> diandu;

    public List<GoodInfo> getSvip() {
        return svip;
    }

    public void setSvip(List<GoodInfo> svip) {
        this.svip = svip;
    }

    public List<GoodInfo> getVip() {
        return vip;
    }

    public void setVip(List<GoodInfo> vip) {
        this.vip = vip;
    }

    public List<GoodInfo> getWeike() {
        return weike;
    }

    public void setWeike(List<GoodInfo> weike) {
        this.weike = weike;
    }

    public List<GoodInfo> getDiandu() {
        return diandu;
    }

    public void setDiandu(List<GoodInfo> diandu) {
        this.diandu = diandu;
    }
}
