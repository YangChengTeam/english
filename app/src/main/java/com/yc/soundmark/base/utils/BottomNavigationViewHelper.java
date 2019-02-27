package com.yc.soundmark.base.utils;

import android.annotation.SuppressLint;
import android.support.design.internal.BottomNavigationItemView;
import android.support.design.internal.BottomNavigationMenuView;
import android.support.design.widget.BottomNavigationView;

import com.kk.utils.LogUtil;

import java.lang.reflect.Field;

/**
 * Created by wanglin  on 2018/10/24 10:37.
 */
public class BottomNavigationViewHelper {
    private static final String TAG = "BottomNavigationViewHel";

    @SuppressLint("RestrictedApi")
    public static void disableShiftMode(BottomNavigationView view) {
        BottomNavigationMenuView menuView = (BottomNavigationMenuView) view.getChildAt(0);
        try {
            Field shiftingMode = menuView.getClass().getDeclaredField("mShiftingMode");
            shiftingMode.setAccessible(true);
            shiftingMode.setBoolean(menuView, false);
            shiftingMode.setAccessible(false);
            for (int i = 0; i < menuView.getChildCount(); i++) {
                BottomNavigationItemView item = (BottomNavigationItemView) menuView.getChildAt(i);
                //noinspection RestrictedApi
                item.setShiftingMode(false);
                // set once again checked value, so view will be updated
                //noinspection RestrictedApi
                item.setChecked(item.getItemData().isChecked());
            }
        } catch (NoSuchFieldException e) {
            LogUtil.msg(TAG + "  Unable to get shift mode field  " + e.getMessage());
        } catch (IllegalAccessException e) {
            LogUtil.msg(TAG + "  Unable to change value of shift mode" + e.getMessage());
        }
    }
}
