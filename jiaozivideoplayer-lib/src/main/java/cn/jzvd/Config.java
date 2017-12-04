package cn.jzvd;

import android.content.Context;
import android.os.Environment;

public class Config {

    public static String getDefaultCacheDir(Context context) {
        String cache_dir = "";
        if (Environment.getExternalStorageState().equals(Environment.MEDIA_MOUNTED) || !Environment.isExternalStorageRemovable()) {
            cache_dir = Environment.getExternalStorageDirectory().getAbsolutePath();
        } else {
            cache_dir = context.getExternalFilesDir(Environment.DIRECTORY_MOVIES).getAbsolutePath();
        }
        return cache_dir + "/PLDroidPlayer";

    }

}
