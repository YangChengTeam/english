package yc.com.base;

import android.content.Context;
import android.text.TextUtils;

import java.io.File;

import yc.com.blankj.utilcode.util.FileIOUtils;

/**
 * Created by wanglin  on 2018/2/9 11:10.
 */

public class CacheUtils {

    public static void writeCache(final Context context, final String key, final String json) {
        new ThreadPoolUtils(ThreadPoolUtils.SingleThread, 5).execute(new Runnable() {
            @Override
            public void run() {

                String path = FileUtils.createDir(makeBaseDir(context) + "/cache");
                FileIOUtils.writeFileFromString(path + "/" + key, json, false);
            }
        });

    }

    public abstract static class SubmitRunable implements Runnable {

        private String json;

        public String getJson() {
            return json;
        }

        public void setJson(String json) {
            this.json = json;
        }
    }


    public static void readCache(final Context context, final String key, final SubmitRunable runable) {

        readCache(context, key, runable, null);

    }


    public static void readCache(final Context context, final String key, final SubmitRunable runable, final SubmitRunable errorRunable) {

        new ThreadPoolUtils(ThreadPoolUtils.SingleThread, 5).execute(new Runnable() {
            @Override
            public void run() {
                try {
                    String path = FileUtils.createDir(makeBaseDir(context) + "/cache");
                    String json = FileIOUtils.readFile2String(path + "/" + key, "");
                    if (!TextUtils.isEmpty(json)) {
                        if (runable != null) {
                            runable.setJson(json);
                            runable.run();
                        }
                    }
                } catch (Exception e) {
                    if (errorRunable != null) {
                        errorRunable.setJson(e.getMessage());
                        errorRunable.run();
                    }
                }

            }
        });

    }

    private static String makeBaseDir(Context context) {
        String pathname = context.getExternalCacheDir() + "/" + context.getPackageName();
//        if (Environment.getExternalStorageState().equals(Environment.MEDIA_MOUNTED)) {
//            pathname = Environment.getExternalStorageDirectory() + "/" + context.getPackageName();
//        } else {
//            pathname = context.getExternalCacheDir() + "/" + context.getPackageName();
//        }

        File dir = new File(pathname);
        if (!dir.exists()) {
            dir.mkdir();
        }
        return dir.getAbsolutePath();
    }

    public static String makeBaseDir(Context context, String dirName) {
        String baseDir = makeBaseDir(context);
        File dir = new File(baseDir + "/" + dirName);
        if (!dir.exists()) {
            dir.mkdir();
        }
        return dir.getAbsolutePath();
    }
}
