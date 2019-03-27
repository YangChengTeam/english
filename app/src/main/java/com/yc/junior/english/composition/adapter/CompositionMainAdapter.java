package com.yc.junior.english.composition.adapter;

import android.content.Context;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;

import com.yc.junior.english.R;

import java.util.List;

/**
 * Created by wanglin  on 2019/3/22 16:37.
 */
public class CompositionMainAdapter extends FragmentPagerAdapter {
    private List<Fragment> mFragmentLists;
    private String[] mTitles;

    public CompositionMainAdapter(FragmentManager fm, Context context, List<Fragment> fragmentList) {
        super(fm);
        this.mFragmentLists = fragmentList;
        mTitles = context.getResources().getStringArray(R.array.composition_array);
    }

    @Override
    public Fragment getItem(int position) {
        return mFragmentLists.get(position);
    }

    @Override
    public int getCount() {
        return mFragmentLists == null ? 0 : mFragmentLists.size();
    }

    @Override
    public CharSequence getPageTitle(int position) {
        return mTitles[position];
    }
}
