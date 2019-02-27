package com.yc.soundmark.index.model.domain;

import com.yc.english.main.model.domain.UserInfo;
import com.yc.english.pay.PayWayInfo;
import com.yc.soundmark.base.model.domain.VipInfo;

import java.util.List;

/**
 * Created by wanglin  on 2018/10/29 09:28.
 */
public class IndexInfoWrapper {

    /**
     * share_info : {"title":null,"content":null,"url":null,"share_add_hour":1}
     * user_info : {"is_reg":true,"user_id":"208000","vip_test_hour":1,"vip_test_num":"0","status":"0"}
     * user_vip_list : null
     * contact_info : {"tel":null,"qq":null}
     * payway_list : []
     */

    private ShareInfo share_info;
    private UserInfo user_info;

    private ContactInfo contact_info;
    private List<PayWayInfo> payway_list;

    private List<VipInfo> user_vip_list;

    public ShareInfo getShare_info() {
        return share_info;
    }

    public void setShare_info(ShareInfo share_info) {
        this.share_info = share_info;
    }

    public UserInfo getUserInfo() {
        return user_info;
    }

    public void setUser_info(UserInfo user_info) {
        this.user_info = user_info;
    }

    public ContactInfo getContact_info() {
        return contact_info;
    }

    public void setContact_info(ContactInfo contact_info) {
        this.contact_info = contact_info;
    }

    public List<PayWayInfo> getPayway_list() {
        return payway_list;
    }

    public void setPayway_list(List<PayWayInfo> payway_list) {
        this.payway_list = payway_list;
    }

    public List<VipInfo> getUser_vip_list() {
        return user_vip_list;
    }

    public void setUser_vip_list(List<VipInfo> user_vip_list) {
        this.user_vip_list = user_vip_list;
    }
}
