package com.yc.english.speak.model.engine;

import android.content.Context;

import com.alibaba.fastjson.JSON;
import com.kk.securityhttp.domain.ResultInfo;
import com.yc.english.base.model.BaseEngin;
import com.yc.english.speak.model.bean.EnglishInfoWrapper;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;

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
}
