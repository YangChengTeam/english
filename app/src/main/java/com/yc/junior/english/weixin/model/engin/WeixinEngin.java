package com.yc.junior.english.weixin.model.engin;

import android.content.Context;

import com.kk.securityhttp.domain.ResultInfo;

import com.yc.junior.english.base.model.BaseEngin;
import com.yc.junior.english.weixin.model.domain.CourseInfoWrapper;

import rx.Observable;

/**
 * Created by zhangkai on 2017/9/6.
 */

public class WeixinEngin extends BaseEngin {
    public WeixinEngin(Context context) {
        super(context);
    }

    public Observable<ResultInfo<CourseInfoWrapper>> getWeixinList(String type_id, String page,
                                                                   String
                                                                           page_size){

        return EnginHelper.getWeixinList(mContext, type_id, page, page_size);
    }
}
