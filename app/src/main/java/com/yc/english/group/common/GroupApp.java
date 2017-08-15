package com.yc.english.group.common;

import android.app.ActivityManager;
import android.app.Application;
import android.content.Context;
import android.database.sqlite.SQLiteDatabase;

import com.blankj.utilcode.util.LogUtils;
import com.facebook.stetho.Stetho;
import com.hwangjr.rxbus.RxBus;
import com.yc.english.base.dao.DaoMaster;
import com.yc.english.base.dao.DaoSession;
import com.yc.english.base.utils.RongIMUtil;
import com.yc.english.group.constant.BusAction;

import com.yc.english.group.plugin.GroupExtensionModule;
import com.yc.english.group.view.provider.CustomMessage;
import com.yc.english.group.view.provider.DoTaskTaskMessageProvider;
import com.yc.english.group.view.provider.PublishTaskMessageProvider;

import org.greenrobot.greendao.query.QueryBuilder;

import java.util.List;

import io.rong.imkit.DefaultExtensionModule;
import io.rong.imkit.IExtensionModule;
import io.rong.imkit.RongExtensionManager;
import io.rong.imkit.RongIM;

import io.rong.imkit.model.GroupUserInfo;
import io.rong.imlib.IRongCallback;

import io.rong.imlib.RongIMClient;
import io.rong.imlib.model.Conversation;
import io.rong.imlib.model.Message;

import io.rong.imlib.model.MessageContent;
import io.rong.imlib.model.UserInfo;
import io.rong.message.ImageMessage;
import io.rong.message.TextMessage;
import io.rong.message.VoiceMessage;


/**
 * Created by wanglin  on 2017/7/17 14:49.
 */

public class GroupApp {
    private static final String TAG = "GroupApp";

    private static SQLiteDatabase db;

    private static DaoSession mDaoSession;
    private static Application mApplication;

    public static void init(final Application application) {
        mApplication = application;
        /**
         *
         * OnCreate 会被多个进程重入，这段保护代码，确保只有您需要使用 RongIM 的进程和 Push 进程执行了 init。
         * io.rong.push 为融云 push 进程名称，不可修改。
         */
        if (application.getApplicationInfo().packageName.equals(getCurProcessName(application.getApplicationContext())) ||
                "io.rong.push".equals(getCurProcessName(application.getApplicationContext()))) {

            /**
             * IMKit SDK调用第一步 初始化
             */
            RongIM.init(application);

            try {
                RongIM.registerMessageType(CustomMessage.class);

                RongIM.registerMessageTemplate(new PublishTaskMessageProvider());
                RongIM.registerMessageTemplate(new DoTaskTaskMessageProvider());
            } catch (Exception e) {
                e.printStackTrace();
            }

            RongIM.setOnReceiveMessageListener(new MyReceiveMessageListener());
            RongIM.getInstance().setMessageAttachedUserInfo(true);
//            RongIM.getInstance().setSendMessageListener(new MySendMessageListener());

        }
        setDatabase(application);
        Stetho.initializeWithDefaults(application);
    }

    private static class MySendMessageListener implements RongIM.OnSendMessageListener {

        /**
         * 消息发送前监听器处理接口（是否发送成功可以从 SentStatus 属性获取）。
         *
         * @param message 发送的消息实例。
         * @return 处理后的消息实例。
         */
        @Override
        public Message onSend(Message message) {
            //开发者根据自己需求自行处理逻辑
            return message;
        }

        /**
         * 消息在 UI 展示后执行/自己的消息发出后执行,无论成功或失败。
         *
         * @param message              消息实例。
         * @param sentMessageErrorCode 发送消息失败的状态码，消息发送成功 SentMessageErrorCode 为 null。
         * @return true 表示走自己的处理方式，false 走融云默认处理方式。
         */
        @Override
        public boolean onSent(Message message, RongIM.SentMessageErrorCode sentMessageErrorCode) {
            if (message.getSentStatus() == Message.SentStatus.FAILED) {
                if (sentMessageErrorCode == RongIM.SentMessageErrorCode.NOT_IN_GROUP) {

                }
            }
            return true;
        }
    }

    /**
     * 获得当前进程的名字
     *
     * @param context
     * @return
     */
    public static String getCurProcessName(Context context) {

        int pid = android.os.Process.myPid();

        ActivityManager activityManager = (ActivityManager) context
                .getSystemService(Context.ACTIVITY_SERVICE);

        for (ActivityManager.RunningAppProcessInfo appProcess : activityManager
                .getRunningAppProcesses()) {

            if (appProcess.pid == pid) {

                return appProcess.processName;
            }
        }
        return null;
    }

    public static void setMyExtensionModule(boolean isMaster) {
        List<IExtensionModule> moduleList = RongExtensionManager.getInstance().getExtensionModules();
        IExtensionModule defaultModule = null;
        if (moduleList != null) {
            for (IExtensionModule module : moduleList) {
                if (module instanceof DefaultExtensionModule) {
                    defaultModule = module;
                    break;
                }
            }
            if (defaultModule != null) {
                RongExtensionManager.getInstance().unregisterExtensionModule(defaultModule);
                RongExtensionManager.getInstance().registerExtensionModule(new GroupExtensionModule(isMaster));
            }
        }
    }


    /**
     * 设置greenDao
     */
    private static void setDatabase(Context context) {
        // 通过 DaoMaster 的内部类 DevOpenHelper，你可以得到一个便利的 SQLiteOpenHelper 对象。
        // 可能你已经注意到了，你并不需要去编写「CREATE TABLE」这样的 SQL 语句，因为 greenDAO 已经帮你做了。
        // 注意：默认的 DaoMaster.DevOpenHelper 会在数据库升级时，删除所有的表，意味着这将导致数据的丢失。
        // 所以，在正式的项目中，你还应该做一层封装，来实现数据库的安全升级。
        DaoMaster.DevOpenHelper mHelper = new DaoMaster.DevOpenHelper(context, "english-group-db", null);
//        mHelper = MyHelper(this, "technology-db", null)

        db = mHelper.getWritableDatabase();
        // 注意：该数据库连接属于 DaoMaster，所以多个 Session 指的是相同的数据库连接。
        DaoMaster mDaoMaster = new DaoMaster(db);
        mDaoSession = mDaoMaster.newSession();
        QueryBuilder.LOG_SQL = true;
        QueryBuilder.LOG_VALUES = true;
    }


    public static SQLiteDatabase getDb() {
        return db;
    }

    public static DaoSession getmDaoSession() {
        return mDaoSession;
    }


    private static class MyReceiveMessageListener implements RongIMClient.OnReceiveMessageListener {


        @Override
        public boolean onReceived(Message message, int i) {
            if (!message.getReceivedStatus().isRead()) {

                RxBus.get().post(BusAction.UNREAD_MESSAGE, message);
            }

            RongIMUtil.refreshUserInfo(mApplication, message.getSenderUserId());

            LogUtils.e(TAG, message.getContent() + "---" + message.getTargetId() + "---" + message.getReceivedStatus().isRead());
            return true;
        }
    }

    public static Context getApplicationContext() {

        return mApplication.getApplicationContext();
    }

}
