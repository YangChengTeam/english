package com.yc.junior.english.main.model.engin;

import android.content.Context;

import com.alibaba.fastjson.TypeReference;
import com.kk.securityhttp.domain.ResultInfo;
import com.kk.securityhttp.engin.HttpCoreEngin;
import com.yc.junior.english.base.model.BaseEngin;
import com.yc.junior.english.main.hepler.UserInfoHelper;
import com.yc.junior.english.main.model.domain.IndexInfo;
import com.yc.junior.english.main.model.domain.URLConfig;

import java.util.HashMap;
import java.util.Map;

import rx.Observable;

/**
 * Created by zhangkai on 2017/7/26.
 */

public class IndexEngin extends BaseEngin {
    public IndexEngin(Context context) {
        super(context);
    }


    public Observable<ResultInfo<IndexInfo>> getIndexInfo() {
        Map<String, String> params = null;
        if (UserInfoHelper.isLogin()) {
            params = new HashMap<>();
            params.put("user_id", UserInfoHelper.getUserInfo().getUid());
        }
        return HttpCoreEngin.get(mContext).rxpost(URLConfig.INDEX_URL, new TypeReference<ResultInfo<IndexInfo>>() {
        }.getType(), params, true, true, true);
    }




}
