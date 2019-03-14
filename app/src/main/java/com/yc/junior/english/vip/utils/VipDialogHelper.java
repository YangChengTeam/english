package com.yc.junior.english.vip.utils;

import android.os.Bundle;
import android.support.v4.app.FragmentManager;

import com.yc.junior.english.vip.views.fragments.PayNewFragment;


/**
 * Created by wanglin  on 2017/12/1 10:57.
 * vip支付dialog弹出框
 */

public class VipDialogHelper {

    //    private static BasePayDialogFragment basePayDialogFragment;
    private static PayNewFragment basePayDialogFragment;

    public static void showVipDialog(FragmentManager manager, String tag, Bundle bundle) {


//        basePayDialogFragment = new BasePayDialogFragment();
        basePayDialogFragment = new PayNewFragment();

        if (bundle != null && !bundle.isEmpty()) {

            basePayDialogFragment.setArguments(bundle);
        }
        basePayDialogFragment.show(manager, tag);


    }

    public static void dismissVipDialog() {
        if (basePayDialogFragment != null) {
            basePayDialogFragment.dismiss();
        }
    }

}
