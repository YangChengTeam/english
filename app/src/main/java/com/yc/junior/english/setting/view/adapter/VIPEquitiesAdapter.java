package com.yc.junior.english.setting.view.adapter;

import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.animation.ObjectAnimator;
import android.animation.ValueAnimator;
import android.support.v7.widget.CardView;
import android.view.View;

import com.alibaba.fastjson.JSON;
import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;
import com.yc.english.R;

import java.util.List;

/**
 * Created by wanglin  on 2017/11/8 16:28.
 */

public class VIPEquitiesAdapter extends BaseQuickAdapter<Integer, BaseViewHolder> {
    public VIPEquitiesAdapter(List<Integer> data) {
        super(R.layout.activity_open_vip_days, data);
    }

    @Override
    protected void convert(BaseViewHolder helper, Integer item) {
        helper.setText(R.id.tv_open_first_day, item + "");
        startAnimation(helper.itemView);

    }

    private void startAnimation(final View view) {
        ObjectAnimator objectAnimator = ObjectAnimator.ofFloat(view, "rotationX", 0f, 180f);
        objectAnimator.setDuration(500);
        objectAnimator.start();
        objectAnimator.addListener(new AnimatorListenerAdapter() {
            @Override
            public void onAnimationEnd(Animator animation) {
                super.onAnimationEnd(animation);
                ObjectAnimator.ofFloat(view, "rotationX", 180f, 360f).setDuration(500).start();
            }
        });

    }

}
