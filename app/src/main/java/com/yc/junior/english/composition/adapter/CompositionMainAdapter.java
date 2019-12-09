package com.yc.junior.english.composition.adapter;

import android.content.Context;

import com.yc.junior.english.R;

import java.util.List;

import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentPagerAdapter;

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
