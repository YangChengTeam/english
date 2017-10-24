package com.yc.english.speak.model.engine;

import android.content.Context;

import com.alibaba.fastjson.TypeReference;
import com.kk.securityhttp.domain.ResultInfo;
import com.kk.securityhttp.engin.HttpCoreEngin;
import com.yc.english.base.model.BaseEngin;
import com.yc.english.speak.model.bean.SpeakEnglishBean;
import com.yc.english.speak.model.domain.URLConfig;

import java.util.HashMap;
import java.util.Map;

import rx.Observable;

import static com.umeng.socialize.utils.DeviceConfig.context;

/**
 * Created by admin on 2017/10/16.
 */

public class ListenEnglishEngin extends BaseEngin{

    public ListenEnglishEngin(Context context){
        super(context);
    }

    public Observable<ResultInfo<SpeakEnglishBean>> getListenEnglish(String id, int currentPage, int pageCount) {
        Map<String, String> params = new HashMap<>();
        params.put("id", id);
        params.put("page", currentPage + "");
        params.put("limit", pageCount + "");

        return HttpCoreEngin.get(context).rxpost(URLConfig.LISTEN_ENGLISH_URL, new TypeReference<ResultInfo<SpeakEnglishBean>>() {
                }.getType(), params,
                true, true,
                true);
    }

}
