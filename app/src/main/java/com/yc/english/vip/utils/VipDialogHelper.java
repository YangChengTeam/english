package com.yc.english.vip.utils;

import android.content.Context;
import android.os.Bundle;
import android.support.v4.app.FragmentManager;

import com.yc.english.vip.views.fragments.BasePayDialogFragment;

/**
 * Created by wanglin  on 2017/12/1 10:57.
 * vip支付dialog弹出框
 */

public class VipDialogHelper {

    public static void showVipDialog(FragmentManager manager, String tag, Bundle bundle) {


        BasePayDialogFragment basePayDialogFragment = new BasePayDialogFragment();

        if (bundle != null && !bundle.isEmpty()) {

            basePayDialogFragment.setArguments(bundle);
        }
        basePayDialogFragment.show(manager, tag);


    }

}
