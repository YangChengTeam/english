package com.yc.english.pay.alipay;

import android.view.animation.Animation;
import android.view.animation.LinearInterpolator;
import android.view.animation.RotateAnimation;

/**
 * Created by zhangkai on 16/9/23.
 */
public class AnimationUtil {

    public static Animation rotaAnimation() {
        RotateAnimation ra = new RotateAnimation(0, 360,
                Animation.RELATIVE_TO_SELF, 0.5f, Animation.RELATIVE_TO_SELF,
                0.5f);
        ra.setInterpolator(new LinearInterpolator());
        ra.setDuration(1500);
        ra.setRepeatCount(-1);
        ra.setStartOffset(0);
        ra.setRepeatMode(Animation.RESTART);
        return ra;
    }
}
