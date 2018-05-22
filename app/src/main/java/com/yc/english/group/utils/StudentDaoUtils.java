package com.yc.english.group.utils;

import com.yc.english.base.dao.StudentInfoDao;
import com.yc.english.group.common.GroupApp;

import com.yc.english.group.model.bean.StudentInfo;

import java.util.List;

import rx.Observable;
import rx.functions.Action1;
import rx.schedulers.Schedulers;

/**
 * Created by wanglin  on 2017/8/3 09:52.
 * 操作学生数据库的工具类
 */

public class StudentDaoUtils {

    private static StudentInfoDao infoDao = GroupApp.getmDaoSession().getStudentInfoDao();

    /**
     * 插入数据库
     */
    public static void insert(final List<StudentInfo> studentInfos) {
        Observable.just("").subscribeOn(Schedulers.io()).subscribe(new Action1<String>() {
            @Override
            public void call(String s) {
                if (studentInfos!=null&&studentInfos.size()>0){
                    for (StudentInfo studentInfo : studentInfos) {
                        infoDao.saveInTx(studentInfo);
                    }
                }
                List<StudentInfo> infos = infoDao.queryBuilder().list();
                if (infos != null && infos.size() > 0) {



                }

            }
        });

    }


}
