package com.yc.english.group.common;

import android.app.ActivityManager;
import android.app.Application;
import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.os.Vibrator;
import android.text.TextUtils;

import com.blankj.utilcode.util.LogUtils;
import com.facebook.stetho.Stetho;
import com.hwangjr.rxbus.RxBus;
import com.yc.english.base.dao.DaoMaster;
import com.yc.english.base.dao.DaoSession;
import com.yc.english.base.utils.RongIMUtil;
import com.yc.english.group.constant.BusAction;
import com.yc.english.group.constant.GroupConstant;
import com.yc.english.group.plugin.GroupExtensionModule;
import com.yc.english.group.view.provider.CustomMessage;
import com.yc.english.group.view.provider.DoTaskTaskMessageProvider;
import com.yc.english.group.view.provider.PublishTaskMessageProvider;

import org.greenrobot.greendao.query.QueryBuilder;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import io.rong.imkit.DefaultExtensionModule;
import io.rong.imkit.IExtensionModule;
import io.rong.imkit.RongContext;
import io.rong.imkit.RongExtensionManager;
import io.rong.imkit.RongIM;
import io.rong.imlib.IRongCallback;
import io.rong.imlib.MessageTag;
import io.rong.imlib.RongIMClient;
import io.rong.imlib.model.Conversation;
import io.rong.imlib.model.Message;
import io.rong.imlib.model.MessageContent;
import io.rong.message.InformationNotificationMessage;
import io.rong.message.RichContentMessage;
import io.rong.message.TextMessage;
import rx.Observable;
import rx.android.schedulers.AndroidSchedulers;
import rx.functions.Action1;
import rx.schedulers.Schedulers;


/**
 * Created by wanglin  on 2017/7/17 14:49.
 */

public class GroupApp {
    private static final String TAG = "GroupApp";

    private static SQLiteDatabase db;

    private static DaoSession mDaoSession;

    private static List<String> list = new ArrayList<>();

    public static void init(final Application application) {
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

            Conversation.ConversationType[] types = new Conversation.ConversationType[]{
                    Conversation.ConversationType.PRIVATE,
                    Conversation.ConversationType.GROUP,
                    Conversation.ConversationType.DISCUSSION
            };
            RongIM.getInstance().setReadReceiptConversationTypeList(types);

            RongIM.setOnReceiveMessageListener(new MyReceiveMessageListener(application));
            RongIM.getInstance().setSendMessageListener(new MySendMessageListener());
            RongIM.getInstance().setMessageAttachedUserInfo(true);

        }
        setDatabase(application);
        Stetho.initializeWithDefaults(application);

        Vibrator vibrator = (Vibrator) application.getSystemService(Context.VIBRATOR_SERVICE);
        vibrator.cancel();

        Observable.just("").subscribeOn(Schedulers.io()).observeOn(AndroidSchedulers.mainThread()).subscribe(new Action1<String>() {
            @Override
            public void call(String s) {
                InputStream inputStream = null;
                BufferedReader bufferedReader = null;
                try {
                    inputStream = application.getAssets().open("key.txt");
                    bufferedReader = new BufferedReader(new InputStreamReader(inputStream));
                    String str;
                    while ((str = bufferedReader.readLine()) != null) {
                        list.add(str);
                    }

                } catch (IOException e) {
                    e.printStackTrace();
                } finally {

                    if (inputStream != null) {
                        try {
                            inputStream.close();
                        } catch (IOException e) {
                            e.printStackTrace();
                        }
                    }
                    if (bufferedReader != null) {
                        try {
                            bufferedReader.close();
                        } catch (IOException e) {
                            e.printStackTrace();
                        }
                    }
                }
            }
        });


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

    public static void setMyExtensionModule(boolean isMaster, boolean isTask) {
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
                RongExtensionManager.getInstance().registerExtensionModule(new GroupExtensionModule(isMaster, isTask));
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
        private Application mApplication;

        private MyReceiveMessageListener(Application application) {
            this.mApplication = application;
        }

        @Override
        public boolean onReceived(Message message, int i) {

            if (message.getContent() instanceof InformationNotificationMessage) {
                InformationNotificationMessage notificationMessage = (InformationNotificationMessage) message.getContent();
                if (notificationMessage.getMessage().contains("欢迎")) {
                    RxBus.get().post(BusAction.GROUP_LIST, "add new member");
                }
            }


            LogUtils.e(TAG, message.getContent() + "---" + message.getTargetId() + "---" + message.getReceivedStatus().isRead() + "---" + message.getReceivedTime());

            RxBus.get().post(BusAction.UNREAD_MESSAGE, message);

            RongIMUtil.refreshUserInfo(mApplication, message.getSenderUserId());

            return true;
        }
    }


    private static class MySendMessageListener implements RongIM.OnSendMessageListener {

        @Override
        public Message onSend(Message message) {
            return filterKeyWords(message);
        }


        @Override
        public boolean onSent(Message message, RongIM.SentMessageErrorCode sentMessageErrorCode) {
            return true;
        }

        private Message filterKeyWords(Message message) {
            MessageContent messageContent = message.getContent();
            if (messageContent instanceof TextMessage) {
                TextMessage textMessage = (TextMessage) messageContent;

                if (compareKeyWord(textMessage.getContent())) {
                    textMessage = TextMessage.obtain(GroupConstant.KEYWORD);
                    message = Message.obtain(message.getTargetId(), Conversation.ConversationType.GROUP, textMessage);
                }

            } else if (messageContent instanceof RichContentMessage) {
                RichContentMessage richContentMessage = (RichContentMessage) messageContent;
                if (compareKeyWord(richContentMessage.getContent())) {
                    richContentMessage = RichContentMessage.obtain(richContentMessage.getTitle(), GroupConstant.KEYWORD, richContentMessage.getImgUrl(), richContentMessage.getUrl());
                    message = Message.obtain(message.getTargetId(), Conversation.ConversationType.GROUP, richContentMessage);

                }
            } else if (messageContent instanceof CustomMessage) {
                CustomMessage customMessage = (CustomMessage) messageContent;
                if (compareKeyWord(customMessage.getContent())) {
                    customMessage = CustomMessage.obtain(customMessage.getTitle(), GroupConstant.KEYWORD, customMessage.getImgUrl(), customMessage.getUrl());
                    message = Message.obtain(message.getTargetId(), Conversation.ConversationType.GROUP, customMessage);

                }
            }

            return message;
        }

        private boolean compareKeyWord(String s1) {
            boolean flag = false;
            if (list != null && list.size() > 0) {
                for (String s : list) {
                    if (s1.equalsIgnoreCase(s) || s1.contains(s)) {
                        flag = true;
                        break;

                    }
                }
            }
            return flag;

        }


        private boolean containIgnoreCase(String s1, String s2) {

            return s1.toUpperCase().contains(s2.toUpperCase())
                    || s1.toLowerCase().contains(s2.toLowerCase())
                    || s2.toUpperCase().contains(s1.toUpperCase())
                    || s2.toLowerCase().contains(s1.toLowerCase());
        }
    }

    private static String replaceWord(String str) {
        Pattern p = Pattern.compile(GroupConstant.REGEX_SPECIAL_WORD);
        Matcher m = p.matcher(str);
        return m.replaceAll("").trim();
    }

}
