package com.yc.junior.english.news.utils;

import android.app.Activity;
import android.content.Intent;

import com.jakewharton.rxbinding.view.RxView;
import com.yc.junior.english.group.constant.GroupConstant;
import com.yc.junior.english.main.view.activitys.MainActivity;
import com.yc.junior.english.read.common.ReadApp;
import com.yc.junior.english.read.view.activitys.BookActivity;
import com.yc.junior.english.vip.views.fragments.BasePayItemView;
import com.yc.junior.english.weixin.views.activitys.CourseClassifyActivity;

import java.util.concurrent.TimeUnit;

import rx.functions.Action1;

/**
 * Created by wanglin  on 2017/12/15 10:37.
 */

public class ViewUtil {

    public static void switchActivity(final Activity context, BasePayItemView basePayItemView, final int i) {

        RxView.clicks(basePayItemView).throttleFirst(200, TimeUnit.MILLISECONDS).subscribe(new Action1<Void>() {
            @Override
            public void call(Void aVoid) {
                Intent intent = new Intent();
                if (i == 0) {
                    intent.setClass(context, BookActivity.class);
                    ReadApp.READ_COMMON_TYPE = 1;

                } else if (i == 1) {
                    intent.setClass(context, BookActivity.class);
                    ReadApp.READ_COMMON_TYPE = 2;

                } else if (i == 2) {

                    intent.setClass(context, MainActivity.class);
                    intent.putExtra("appraisal", true);

                } else if (i == 3) {
//                    intent.setClass(context, VipScoreTutorshipActivity.class);
                    SmallProcedureUtils.switchSmallProcedure(context, GroupConstant.assistant_originid, GroupConstant.appid);
                    return;
                } else if (i == 4) {
                    intent.setClass(context, CourseClassifyActivity.class);
                    intent.putExtra("type", 8);
//                    intent.putExtra("weike", true);
                }
                context.startActivity(intent);
            }
        });


    }
}
