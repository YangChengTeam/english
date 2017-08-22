package com.yc.english.group.plugin;

import android.content.Context;
import android.content.Intent;
import android.graphics.drawable.Drawable;
import android.support.v4.app.Fragment;

import com.yc.english.R;

import io.rong.imkit.RongExtension;
import io.rong.imkit.plugin.ImagePlugin;
import io.rong.imlib.model.Conversation;

/**
 * Created by wanglin  on 2017/7/24 15:54.
 */

public class PicturePlugin extends ImagePlugin {

    @Override
    public Drawable obtainDrawable(Context context) {
        return context.getResources().getDrawable(R.drawable.group_picture_selector);
    }


    @Override
    public void onClick(Fragment fragment, RongExtension rongExtension) {
        super.onClick(fragment,rongExtension);
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent intent) {

    }
}
