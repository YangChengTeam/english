package com.yc.english.read.common;

import android.app.Application;
import android.content.Context;
import android.database.sqlite.SQLiteDatabase;

import com.yc.english.base.dao.DaoMaster;
import com.yc.english.base.dao.DaoSession;

import org.greenrobot.greendao.query.QueryBuilder;

/**
 * Created by admin on 2017/8/7.
 */

public class ReadApp {

    /**
     * 页面展示数据类型
     * 1:课本点读，2:单词宝典
     */
    public static int READ_COMMON_TYPE = 1;

    private static SQLiteDatabase db;

    private static DaoSession daoSession;

    public static void init(Application application) {
        setDatabase(application);
    }

    /**
     * 设置greenDao
     */
    private static void setDatabase(Context context) {
        // 通过 DaoMaster 的内部类 DevOpenHelper，你可以得到一个便利的 SQLiteOpenHelper 对象。
        // 可能你已经注意到了，你并不需要去编写「CREATE TABLE」这样的 SQL 语句，因为 greenDAO 已经帮你做了。
        // 注意：默认的 DaoMaster.DevOpenHelper 会在数据库升级时，删除所有的表，意味着这将导致数据的丢失。
        // 所以，在正式的项目中，你还应该做一层封装，来实现数据库的安全升级。
        DaoMaster.DevOpenHelper mHelper = new DaoMaster.DevOpenHelper(context, "english-read-db", null);

        db = mHelper.getWritableDatabase();
        // 注意：该数据库连接属于 DaoMaster，所以多个 Session 指的是相同的数据库连接。
        DaoMaster mDaoMaster = new DaoMaster(db);
        daoSession = mDaoMaster.newSession();
        QueryBuilder.LOG_SQL = true;
        QueryBuilder.LOG_VALUES = true;
    }


    public static SQLiteDatabase getDb() {
        return db;
    }

    public static DaoSession getDaoSession() {
        return daoSession;
    }
}
