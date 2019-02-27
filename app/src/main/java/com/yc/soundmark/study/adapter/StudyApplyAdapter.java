package com.yc.soundmark.study.adapter;

import android.content.Context;
import android.os.Bundle;
import android.os.Parcelable;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;

import com.yc.english.R;
import com.yc.soundmark.study.model.domain.PhraseInfo;
import com.yc.soundmark.study.model.domain.SentenceInfo;
import com.yc.soundmark.study.model.domain.WordInfo;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by wanglin  on 2018/10/26 14:02.
 */
public class StudyApplyAdapter extends FragmentPagerAdapter {

    private int mPos;
    private List<SentenceInfo> mSentenceInfos;
    private List<WordInfo> mWordInfos;
    private List<PhraseInfo> mPhraseInfos;
    private List<Fragment> mFragmentList;
    private String[] stringArray;

    public StudyApplyAdapter(FragmentManager fm, Context context, List<Fragment> fragmentList, List<WordInfo> wordInfos, List<PhraseInfo> phraseInfos, List<SentenceInfo> sentenceInfos, int pos) {
        super(fm);
        this.mFragmentList = fragmentList;
        this.mWordInfos = wordInfos;
        this.mPhraseInfos = phraseInfos;
        this.mSentenceInfos = sentenceInfos;
        this.mPos = pos;
        stringArray = context.getResources().getStringArray(R.array.study_apply_array);


    }

    @Override
    public Fragment getItem(int position) {
        Fragment fragment = mFragmentList.get(position);
        Bundle bundle = new Bundle();

        if (position == 0) {
            bundle.putParcelableArrayList("studyInfo", (ArrayList<? extends Parcelable>) mWordInfos);
        } else if (position == 1) {
            bundle.putParcelableArrayList("studyInfo", (ArrayList<? extends Parcelable>) mPhraseInfos);

        } else if (position == 2) {
            bundle.putParcelableArrayList("studyInfo", (ArrayList<? extends Parcelable>) mSentenceInfos);
        }
        bundle.putString("pos", mPos + "");
        fragment.setArguments(bundle);
        return fragment;
    }

    @Override
    public int getCount() {
        return mFragmentList == null ? 0 : mFragmentList.size();
    }


    @Override
    public CharSequence getPageTitle(int position) {
        return stringArray[position];
    }


}
