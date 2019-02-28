package com.yc.junior.english.base.helper;


import com.yc.junior.english.base.dao.CourseInfoDao;
import com.yc.junior.english.read.common.ReadApp;
import com.yc.junior.english.weixin.model.domain.CourseInfo;

import org.greenrobot.greendao.query.QueryBuilder;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by admin on 2017/11/6.
 * 购物车辅助类
 */

public class ShoppingHelper {

    private static CourseInfoDao courseInfoDao = ReadApp.getDaoSession().getCourseInfoDao();

    public static List<CourseInfo> getCourseInfoListFromDB(String userId) {
        QueryBuilder queryBuilder = courseInfoDao.queryBuilder();
        queryBuilder.where(CourseInfoDao.Properties.UserId.eq(userId));
        return (ArrayList<CourseInfo>) queryBuilder.list();
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

    public static void deleteCourseInfoByUser(String userId, String id) {
        QueryBuilder queryBuilder = courseInfoDao.queryBuilder();
        queryBuilder.where(CourseInfoDao.Properties.UserId.eq(userId)).where(CourseInfoDao.Properties.Id.eq(id));
        queryBuilder.buildDelete().executeDeleteWithoutDetachingEntities();
    }

    public static void deleteCourseListByUser(String userId, List<CourseInfo> list) {
        if (list != null) {
            courseInfoDao.deleteInTx(list);
        }
    }

}
