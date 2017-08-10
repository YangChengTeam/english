package com.yc.english.group.model.engin;

import android.content.Context;

import com.kk.securityhttp.engin.BaseEngin;
import com.yc.english.base.dao.ClassInfoDao;
import com.yc.english.group.common.GroupApp;

import com.yc.english.group.model.bean.ClassInfo;

import java.util.List;

import rx.Observable;
import rx.functions.Func1;
import rx.schedulers.Schedulers;

/**
 * Created by wanglin  on 2017/7/31 17:45.
 * 群组列表
 */

public class GroupListEngine extends BaseEngin<ClassInfo> {
    private ClassInfoDao infoDao = GroupApp.getmDaoSession().getClassInfoDao();

    public GroupListEngine(Context context) {
        super(context);
    }

    @Override
    public String getUrl() {
        return null;
    }


    public Observable<List<ClassInfo>> getGroupList() {

        return Observable.just("").subscribeOn(Schedulers.io()).map(new Func1<String, List<ClassInfo>>() {

            @Override
            public List<ClassInfo> call(String s) {
                return infoDao.queryBuilder().list();
            }
        });


    }

}
