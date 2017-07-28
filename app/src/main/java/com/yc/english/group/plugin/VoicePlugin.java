package com.yc.english.group.plugin;

import android.content.Context;
import android.content.Intent;
import android.graphics.drawable.Drawable;
import android.support.v4.app.Fragment;

import com.yc.english.R;
import com.yc.english.group.view.activitys.GroupPublishTaskListActivity;

import io.rong.imkit.RongExtension;
import io.rong.imkit.plugin.IPluginModule;

/**
 * Created by wanglin  on 2017/7/24 15:54.
 * 语音
 */

public class VoicePlugin implements IPluginModule {
    @Override
    public Drawable obtainDrawable(Context context) {
        return context.getResources().getDrawable(R.drawable.group_voice_selector);
    }

    @Override
    public String obtainTitle(Context context) {
        return "语音";
    }

    @Override
    public void onClick(Fragment fragment, RongExtension rongExtension) {
        Intent intent = new Intent(fragment.getActivity(), GroupPublishTaskListActivity.class);
        rongExtension.startActivityForPluginResult(intent,200,this);

    }

    @Override
    public void onActivityResult(int i, int i1, Intent intent) {

    }
}
