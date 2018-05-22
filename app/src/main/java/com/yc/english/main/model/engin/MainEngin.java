package com.yc.english.main.model.engin;

import android.content.Context;

import com.alibaba.fastjson.TypeReference;
import com.kk.securityhttp.engin.BaseEngin;


import rx.Observable;

/**
 * Created by zhangkai on 2017/7/20.
 */

public class MainEngin extends BaseEngin {


    public MainEngin(Context context) {
        super(context);
    }

    public Observable<String> main() {
        return rxget(new TypeReference<String>(){}.getType(), false);
    }


    @Override
    public String getUrl() {
        return "http://www.baiud.com";
    }
}
