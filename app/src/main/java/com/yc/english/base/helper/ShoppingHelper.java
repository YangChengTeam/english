package com.yc.english.base.helper;

import com.yc.english.base.dao.CourseInfoDao;
import com.yc.english.read.common.ReadApp;
import com.yc.english.weixin.model.domain.CourseInfo;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by admin on 2017/11/6.
 * 购物车辅助类
 */

public class ShoppingHelper {

    private static CourseInfoDao courseInfoDao = ReadApp.getDaoSession().getCourseInfoDao();

    public static List<CourseInfo> getCourseInfoListFromDB() {
        return (ArrayList<CourseInfo>) courseInfoDao.queryBuilder().list();
    }

    public static void saveCourseInfoToDB(CourseInfo cardInfo) {
        if (cardInfo != null) {
            courseInfoDao.insertOrReplace(cardInfo);
        }
    }

    public static void saveCourseInfoListToDB(List<CourseInfo> list) {
        if (list != null) {
            for (CourseInfo info : list) {
                courseInfoDao.insertOrReplace(info);
            }
        }
    }

    public static void deleteCourseInfoInDB(CourseInfo cardInfo) {
        courseInfoDao.delete(cardInfo);
    }

    public static void deleteAllCourseInfoList() {
        courseInfoDao.deleteAll();
    }

}
