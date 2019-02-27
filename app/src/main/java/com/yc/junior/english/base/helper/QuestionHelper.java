package com.yc.junior.english.base.helper;

import com.yc.english.base.dao.QuestionInfoBeanDao;
import com.yc.english.read.common.ReadApp;
import com.yc.english.speak.model.bean.QuestionInfoBean;
import com.yc.junior.english.read.common.ReadApp;
import com.yc.junior.english.speak.model.bean.QuestionInfoBean;

import java.util.List;

/**
 * Created by admin on 2017/11/6.
 * 购物车辅助类
 */

public class QuestionHelper {

    private static QuestionInfoBeanDao questionInfoBeanDao = ReadApp.getDaoSession().getQuestionInfoBeanDao();

    public static List<QuestionInfoBean> getQuestionInfoBeanListFromDB() {
        return questionInfoBeanDao.queryBuilder().list();
    }

    public static void saveQuestionInfoBeanToDB(QuestionInfoBean cardInfo) {
        if (cardInfo != null) {
            questionInfoBeanDao.insertOrReplace(cardInfo);
        }
    }

    public static void saveQuestionInfoBeanListToDB(List<QuestionInfoBean> list) {
        if (list != null) {
            for (QuestionInfoBean info : list) {
                questionInfoBeanDao.insertOrReplace(info);
            }
        }
    }

    public static void deleteQuestionInfoBeanInDB(QuestionInfoBean cardInfo) {
        questionInfoBeanDao.delete(cardInfo);
    }

    public static void deleteAllQuestionInfoBeanList() {
        questionInfoBeanDao.deleteAll();
    }

}
