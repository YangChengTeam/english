package com.yc.english.group.plugin;

import android.content.Context;
import android.content.Intent;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.support.v4.app.Fragment;

import com.blankj.utilcode.util.LogUtils;
import com.yc.english.R;
import com.yc.english.group.view.activitys.GroupVerifyActivity;

import io.rong.imkit.RongExtension;
import io.rong.imkit.RongIM;
import io.rong.imkit.plugin.IPluginModule;
import io.rong.imkit.plugin.ImagePlugin;
import io.rong.imkit.plugin.image.PictureSelectorActivity;
import io.rong.imkit.utilities.PermissionCheckUtil;
import io.rong.imlib.model.Conversation;

/**
 * Created by wanglin  on 2017/7/24 15:54.
 */

public class PicturePlugin extends ImagePlugin {
    Conversation.ConversationType conversationType;
    String targetId;

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
