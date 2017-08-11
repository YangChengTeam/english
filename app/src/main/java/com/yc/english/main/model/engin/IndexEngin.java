package com.yc.english.main.model.engin;

import android.content.Context;

import com.alibaba.fastjson.TypeReference;
import com.kk.securityhttp.domain.ResultInfo;
import com.kk.securityhttp.engin.BaseEngin;
import com.yc.english.main.model.domain.IndexInfo;
import com.yc.english.main.model.domain.URLConfig;

import rx.Observable;

/**
 * Created by zhangkai on 2017/7/26.
 */

public class IndexEngin extends BaseEngin {
    public IndexEngin(Context context) {
        super(context);
    }


    public Observable<ResultInfo<IndexInfo>> getIndexInfo(){
        return rxpost(new TypeReference<ResultInfo<IndexInfo>>(){}.getType(), null, true, true, true);
    }


    @Override
    public String getUrl() {
        return URLConfig.INDEX_URL;
    }
}
