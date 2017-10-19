package com.yc.english.speak.model.engine;

import android.content.Context;
import android.text.TextUtils;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.TypeReference;
import com.kk.securityhttp.domain.ResultInfo;
import com.kk.securityhttp.engin.HttpCoreEngin;
import com.yc.english.base.model.BaseEngin;
import com.yc.english.group.constant.NetConstant;
import com.yc.english.speak.model.bean.EnglishInfoWrapper;
import com.yc.english.speak.model.bean.SpeakAndReadInfoWrapper;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.lang.reflect.Type;
import java.util.HashMap;
import java.util.Map;

import rx.Observable;
import rx.functions.Func1;
import rx.schedulers.Schedulers;

/**
 * Created by wanglin  on 2017/10/13 09:08.
 */

public class SpeakEnglishListEngine extends BaseEngin {


    public SpeakEnglishListEngine(Context context) {
        super(context);
    }


    public Observable<EnglishInfoWrapper> getEnglishInfoList(String type) {
        return Observable.just(type).subscribeOn(Schedulers.io()).map(new Func1<String, EnglishInfoWrapper>() {
            @Override
            public EnglishInfoWrapper call(String s) {
                try {
                    StringBuilder sb = new StringBuilder();
                    InputStream inputStream = mContext.getAssets().open("english_info.json");
                    BufferedReader reader = new BufferedReader(new InputStreamReader(inputStream));
                    String str;
                    while ((str = reader.readLine()) != null) {
                        sb.append(str);
                    }
                    return JSON.parseObject(sb.toString(), EnglishInfoWrapper.class);
                } catch (IOException e) {
                    e.printStackTrace();
                }
                return null;

            }
        });

    }

    /**
     * 获取听说英语列表
     *
     * @param type_id 说英语 type_id = 1 听英语 type_id = 2
     * @return
     */
    public Observable<ResultInfo<SpeakAndReadInfoWrapper>> getReadAndSpeakList(String type_id, String page) {
        Map<String, String> params = new HashMap<>();
        params.put("type_id", type_id);
        if (!TextUtils.isEmpty(page)) {
            params.put("page", page);
        }

        return HttpCoreEngin.get(mContext).rxpost(NetConstant.read_getReadList, new TypeReference<ResultInfo<SpeakAndReadInfoWrapper>>() {
        }.getType(), params, true, true, true);


    }


}
