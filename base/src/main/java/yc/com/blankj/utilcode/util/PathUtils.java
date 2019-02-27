package yc.com.blankj.utilcode.util;

import android.content.Context;
import android.os.Environment;

import java.io.File;

/**
 * Created by zhangkai on 2017/2/16.
 */

public class PathUtils {
    public static String makeConfigDir(Context context) {
        return makeDir(context, "config");
    }

    public static String makeDir(Context context, String dirName) {
        String baseDir = makeBaseDir(context);
        File dir = new File(baseDir + "/" + dirName);
        if (!dir.exists()) {
            dir.mkdir();
        }
        return dir.getAbsolutePath();
    }

    private static String makeBaseDir(Context context) {
        //Environment.getExternalStorageDirectory()

        File file;
        if (Environment.getExternalStorageState().equals(Environment.MEDIA_MOUNTED)) {
            file = context.getExternalFilesDir(null);
        } else {
            file = Environment.getExternalStorageDirectory();
        }
        File dir = new File(file + "/" + context.getPackageName());
        if (!dir.exists()) {
            dir.mkdir();
        }
        return dir.getAbsolutePath();
    }
}
