package com.yc.english.group.view.adapter;

import android.content.Context;
import android.support.v4.view.ViewPager;
import android.view.View;

import com.yc.english.R;

import net.lucode.hackware.magicindicator.buildins.commonnavigator.abs.CommonNavigatorAdapter;
import net.lucode.hackware.magicindicator.buildins.commonnavigator.abs.IPagerIndicator;
import net.lucode.hackware.magicindicator.buildins.commonnavigator.abs.IPagerTitleView;
import net.lucode.hackware.magicindicator.buildins.commonnavigator.indicators.LinePagerIndicator;
import net.lucode.hackware.magicindicator.buildins.commonnavigator.titles.ColorTransitionPagerTitleView;

/**
 * Created by wanglin  on 2017/7/28 14:05.
 */

public class CommonAdapter extends CommonNavigatorAdapter {


    private int mFinished;//已完成
    private int mUnFinished;//未完成
    private Context mContext;
    private ViewPager mViewPage;
    private final String[] result_title;

    public CommonAdapter(Context context, ViewPager viewPager, int finished, int unFinished) {

        this.mContext = context;
        this.mViewPage = viewPager;
        result_title = context.getResources().getStringArray(R.array.task_finish_result);
        this.mFinished = finished;
        this.mUnFinished = unFinished;
    }


    @Override
    public int getCount() {
        return result_title.length;
    }

    @Override
    public IPagerTitleView getTitleView(Context context, final int i) {
        ColorTransitionPagerTitleView colorTransitionPagerTitleView = new ColorTransitionPagerTitleView(context);
        if (i == 0)
            colorTransitionPagerTitleView.setText(String.format(result_title[0], mFinished));
        else {
            colorTransitionPagerTitleView.setText(String.format(result_title[1], mUnFinished));
        }

        colorTransitionPagerTitleView.setNormalColor(context.getResources().getColor(R.color.black_333333));
        colorTransitionPagerTitleView.setSelectedColor(context.getResources().getColor(R.color.primary));


        colorTransitionPagerTitleView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mViewPage.setCurrentItem(i);
            }
        });

        return colorTransitionPagerTitleView;
    }

    @Override
    public IPagerIndicator getIndicator(Context context) {

        return null;
    }
}
