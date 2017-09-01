package com.yc.english.community.model.engin;

import android.content.Context;

import com.alibaba.fastjson.TypeReference;
import com.kk.securityhttp.domain.ResultInfo;
import com.kk.securityhttp.engin.HttpCoreEngin;
import com.kk.securityhttp.net.entry.UpFileInfo;
import com.yc.english.base.model.BaseEngin;
import com.yc.english.community.model.domain.CommunityInfo;
import com.yc.english.community.model.domain.CommunityInfoList;
import com.yc.english.read.model.domain.URLConfig;

import java.util.HashMap;
import java.util.Map;

import rx.Observable;

import static com.umeng.socialize.utils.DeviceConfig.context;

/**
 * Created by admin on 2017/8/30.
 */

public class CommunityInfoEngin extends BaseEngin {

    public CommunityInfoEngin(Context context) {
        super(context);
    }

    public Observable<ResultInfo<CommunityInfoList>> communityInfoList(int currentPage, int pageCount) {
        Map<String, String> params = new HashMap<>();
        params.put("current_page", currentPage + "");
        params.put("page_count", pageCount + "");

        return HttpCoreEngin.get(context).rxpost(URLConfig.WORD_UNIT_LIST_URL, new TypeReference<ResultInfo<CommunityInfo>>() {
                }.getType(), params,
                true, true,
                true);
    }
    public Observable<ResultInfo<CommunityInfo>> addCommunityInfo(CommunityInfo communityInfo, UpFileInfo upFileInfo) {
        Map<String, String> params = new HashMap<>();
        params.put("user_id", communityInfo.getUserId());
        params.put("content", communityInfo.getContent());
        params.put("type", communityInfo.getcType());

        return HttpCoreEngin.get(context).rxuploadFile(URLConfig.ADD_NOTE_URL, new TypeReference<ResultInfo<CommunityInfo>>() {
                }.getType(), upFileInfo, params,
                true);
    }



}
