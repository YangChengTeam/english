package com.yc.english.base.utils;

import android.content.Context;

import com.kk.utils.LogUtil;

import java.io.InputStream;
import java.util.Properties;

/**
 * Created by wanglin  on 2018/12/21 15:26.
 */
public class PropertyUtil {

    public static Properties getProperties(Context context, String fileName) {

        Properties properties = new Properties();

        try {

            InputStream is = context.getAssets().open(fileName);

            properties.load(is);

        } catch (Exception e) {
            LogUtil.msg("read properties error " + e.getMessage());
        }

        return properties;

    }


    public static Properties getProperties(Context context) {

        return getProperties(context, "devices.properties");

    }
}
