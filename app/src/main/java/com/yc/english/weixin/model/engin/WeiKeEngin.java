package com.yc.english.weixin.model.engin;

import android.content.Context;
import android.text.TextUtils;

import com.alibaba.fastjson.TypeReference;
import com.blankj.utilcode.util.LogUtils;
import com.kk.securityhttp.domain.ResultInfo;
import com.kk.securityhttp.engin.HttpCoreEngin;
import com.yc.english.base.model.BaseEngin;
import com.yc.english.main.model.domain.URLConfig;
import com.yc.english.weixin.model.domain.WeiKeCategoryWrapper;
import com.yc.english.weixin.model.domain.WeiKeInfoWrapper;

import java.util.HashMap;
import java.util.Map;

import rx.Observable;

/**
 * Created by zhangkai on 2017/9/6.
 */

public class WeiKeEngin extends BaseEngin {

    private Context mContext;

    public WeiKeEngin(Context context) {
        super(context);
        mContext = context;
    }

    /**
     *
     * @param type 指定微课类型 7.音频 8.视频
     * @param page
     * @return
     */
    public Observable<ResultInfo<WeiKeCategoryWrapper>> getWeikeCategoryList(String type, String page) {

        Map<String, String> params = new HashMap<>();
        params.put("page", page);

        params.put("top_list_type", type);

        LogUtils.e("请求地址--->" + URLConfig.WEIKE_CATEGORY_URL);

        return HttpCoreEngin.get(mContext).rxpost(URLConfig.WEIKE_CATEGORY_URL, new TypeReference<ResultInfo<WeiKeCategoryWrapper>>() {
                }.getType(),
                params,
                true,
                true, true);
    }

    public Observable<ResultInfo<WeiKeInfoWrapper>> getWeiKeInfoList(String pid, String page) {

        Map<String, String> params = new HashMap<>();
        params.put("page", page);
        params.put("pid", pid);

        LogUtils.e("请求地址--->" + URLConfig.WEIKE_UNIT_URL);

        return HttpCoreEngin.get(mContext).rxpost(URLConfig.WEIKE_UNIT_URL, new TypeReference<ResultInfo<WeiKeInfoWrapper>>() {
                }.getType(),
                params,
                true,
                true, true);
    }
}
