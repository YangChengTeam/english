package com.yc.english.base.utils;

import android.os.Build;
import android.text.TextUtils;

/**
 * Created by wanglin  on 2019/4/30 13:30.
 */
public class BrandUtils {

    private static String[] brands = {"Xiaomi", "xiaomi", "Gionee", "gionee"};


    public static boolean isRelatedBrand() {
        boolean isRelated = false;
        for (String brand : brands) {
            if (TextUtils.equals(Build.BRAND, brand)) {
                isRelated = true;
                break;
            }
        }
        return isRelated;
    }


    public static boolean isNotRelatedBrand() {
        return !isRelatedBrand();
    }

}
