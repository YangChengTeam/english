package com.yc.soundmark.study.utils;

import android.content.Context;
import android.os.Environment;

import java.io.File;

/**
 * Created by wanglin  on 2018/11/2 14:01.
 */
public class FileUtils {


    public static String makeFilePath(Context context, String parent, String child) {
        File tempFile;
        if (Environment.getExternalStorageState().equals(Environment.MEDIA_MOUNTED)) {
            tempFile = Environment.getExternalStorageDirectory();
        } else {
            tempFile = context.getExternalCacheDir();
        }

        String audioFilePath = tempFile + File.separator + "record" + File.separator + parent + File.separator + child + "/msc/iat.wav";
//        File file = new File(tempFile, File.separator + "record" + File.separator + parent + File.separator + child + "/msc/iat.wav");
//
//        if (!file.exists()) {
//            file.mkdirs();
//        }
        return audioFilePath;
    }


}
