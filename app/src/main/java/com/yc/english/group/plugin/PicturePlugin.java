package com.yc.english.group.plugin;

import android.content.Context;
import android.content.Intent;
import android.graphics.drawable.Drawable;
import android.support.v4.app.Fragment;
import android.support.v4.content.ContextCompat;

import com.yc.english.R;
import com.yc.english.base.helper.TipsHelper;
import com.yc.english.group.constant.GroupConstant;
import com.yc.english.group.model.bean.GroupInfoHelper;

import io.rong.imkit.RongExtension;
import io.rong.imkit.plugin.ImagePlugin;

/**
 * Created by wanglin  on 2017/7/24 15:54.
 */

public class PicturePlugin extends ImagePlugin {

    @Override
    public Drawable obtainDrawable(Context context) {
        return ContextCompat.getDrawable(context,R.drawable.group_picture_selector);
    }


    @Override
    public void onClick(Fragment fragment, RongExtension rongExtension) {
        if (GroupInfoHelper.getClassInfo().getIs_allow_talk()==0){
            TipsHelper.tips(fragment.getActivity(), GroupConstant.FORBID_CONTENT);
            return;
        }
        super.onClick(fragment,rongExtension);
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent intent) {

    }
}
