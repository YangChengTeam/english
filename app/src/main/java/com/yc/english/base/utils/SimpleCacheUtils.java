package com.yc.english.base.utils;

import android.content.Context;
import android.text.TextUtils;

import com.blankj.subutil.util.ThreadPoolUtils;
import com.blankj.utilcode.util.FileIOUtils;
import com.kk.utils.PathUtils;

/**
 * Created by zhangkai on 2017/11/22.
 */

public class SimpleCacheUtils {
    public static void writeCache(final Context context, final String key, final String json) {
        new ThreadPoolUtils(ThreadPoolUtils.SingleThread, 5).execute(new Runnable() {
            @Override
            public void run() {
                String path = PathUtils.makeDir(context, "cache");
                FileIOUtils.writeFileFromString(path + "/" + key, json);
            }
        });
    }

    public static abstract class CacheRunnable implements Runnable {
        private String json;

        public void setJson(String json) {
            this.json = json;
        }

        public String getJson() {
            return json;
        }
    }

    public static void readCache(final Context context, final String key, final CacheRunnable runnable) {
        new ThreadPoolUtils(ThreadPoolUtils.SingleThread, 5).execute(new Runnable() {
            @Override
            public void run() {
                String path = PathUtils.makeDir(context, "cache");
                String json = FileIOUtils.readFile2String(path + "/" + key);
                if (!TextUtils.isEmpty(json)) {
                    if (!TextUtils.isEmpty(json)) {
                        if (runnable != null) {
                            runnable.setJson(json);
                            runnable.run();
                        }
                    }
                }
            }
        });
    }
}
