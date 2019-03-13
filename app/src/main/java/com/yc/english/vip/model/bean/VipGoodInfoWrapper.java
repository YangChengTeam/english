package com.yc.english.vip.model.bean;

import java.util.List;

/**
 * Created by wanglin  on 2019/3/13 09:51.
 */
public class VipGoodInfoWrapper {
    private List<VipGoodInfo> list;
    private List<VipGoodInfo> goods_list;

    public List<VipGoodInfo> getList() {
        return list;
    }

    public void setList(List<VipGoodInfo> list) {
        this.list = list;
    }

    public List<VipGoodInfo> getGoods_list() {
        return goods_list;
    }

    public void setGoods_list(List<VipGoodInfo> goods_list) {
        this.goods_list = goods_list;
    }
}
