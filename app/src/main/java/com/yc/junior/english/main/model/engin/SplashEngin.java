package com.yc.junior.english.main.model.engin;

import android.content.Context;

import com.alibaba.fastjson.TypeReference;
import com.kk.securityhttp.domain.ResultInfo;
import com.kk.securityhttp.engin.HttpCoreEngin;
import com.yc.english.base.model.BaseEngin;
import com.yc.english.main.model.domain.IndexDialogInfoWrapper;
import com.yc.english.main.model.domain.URLConfig;
import com.yc.junior.english.main.model.domain.IndexDialogInfoWrapper;
import com.yc.junior.english.main.model.domain.URLConfig;

import rx.Observable;

/**
 * Created by zhangkai on 2017/8/1.
 */

public class SplashEngin extends BaseEngin {

    public SplashEngin(Context context) {
        super(context);
    }

    public Observable<ResultInfo<IndexDialogInfoWrapper>> getDialogInfo() {

        return HttpCoreEngin.get(mContext).rxpost(URLConfig.INDEX_DIALOG_URL, new TypeReference<ResultInfo<IndexDialogInfoWrapper>>() {
        }.getType(), null, true, true, true);
    }

    public Observable<ResultInfo<IndexDialogInfoWrapper>> getIndexMenuInfo() {

        return HttpCoreEngin.get(mContext).rxpost(URLConfig.INDEX_MENU_URL, new TypeReference<ResultInfo<IndexDialogInfoWrapper>>() {
        }.getType(), null, true, true, true);
    }

}
