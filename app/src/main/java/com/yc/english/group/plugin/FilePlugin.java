package com.yc.english.group.plugin;

import android.content.Context;
import android.content.Intent;
import android.graphics.drawable.Drawable;
import android.support.v4.app.Fragment;

import io.rong.imkit.RongExtension;
import io.rong.imkit.plugin.IPluginModule;

/**
 * Created by wanglin  on 2017/7/24 15:54.
 * 文件
 */

public class FilePlugin implements IPluginModule {
    @Override
    public Drawable obtainDrawable(Context context) {
        return null;
    }

    @Override
    public String obtainTitle(Context context) {
        return "文件";
    }

    @Override
    public void onClick(Fragment fragment, RongExtension rongExtension) {

    }

    @Override
    public void onActivityResult(int i, int i1, Intent intent) {

    }
}
