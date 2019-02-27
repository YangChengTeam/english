package com.yc.soundmark.study.presenter;

import android.content.Context;
import android.text.TextUtils;

import com.kk.securityhttp.domain.ResultInfo;
import com.kk.securityhttp.net.contains.HttpConfig;
import com.yc.soundmark.study.contract.StudyVowelContract;
import com.yc.soundmark.study.model.domain.VowelInfoWrapper;
import com.yc.soundmark.study.model.domain.WordInfo;
import com.yc.soundmark.study.model.engine.StudyVowelEngine;
import com.yc.soundmark.study.utils.SoundmarkHelper;

import java.util.ArrayList;
import java.util.List;

import rx.Subscriber;
import rx.Subscription;
import yc.com.base.BasePresenter;

/**
 * Created by wanglin  on 2018/11/1 09:36.
 */
public class StudyVowelPresenter extends BasePresenter<StudyVowelEngine, StudyVowelContract.View> implements StudyVowelContract.Presenter {


    public StudyVowelPresenter(Context context, StudyVowelContract.View view) {
        super(context, view);
        mEngine = new StudyVowelEngine(context);
    }

    @Override
    public void loadData(boolean isForceUI, boolean isLoadingUI) {
        if (!isForceUI) return;
        getVowelInfos();


    }

    private void getVowelInfos() {
//        List<WordInfo> wordInfos = SoundmarkHelper.getWordInfos();
//
//        if (wordInfos != null) {
//            combinationData(wordInfos, new ArrayList<List<WordInfo>>());
//        }


        Subscription subscription = mEngine.getVowelInfos().subscribe(new Subscriber<ResultInfo<VowelInfoWrapper>>() {
            @Override
            public void onCompleted() {

            }

            @Override
            public void onError(Throwable e) {
            }

            @Override
            public void onNext(ResultInfo<VowelInfoWrapper> vowelInfoWrapperResultInfo) {

                if (vowelInfoWrapperResultInfo != null && vowelInfoWrapperResultInfo.code == HttpConfig.STATUS_OK && vowelInfoWrapperResultInfo.data != null) {
                    List<WordInfo> infoList = vowelInfoWrapperResultInfo.data.getList();
//                    mView.showVowelInfoList(infoList);
                    SoundmarkHelper.setWordInfos(infoList);
                    List<List<WordInfo>> listList = new ArrayList<>();
                    combinationData(infoList, listList);


                }

            }
        });

        mSubscriptions.add(subscription);
    }


    private void combinationData(List<WordInfo> infoList, List<List<WordInfo>> listList) {
        if (infoList == null || infoList.size() == 0) return;

        if (listList == null) {
            listList = new ArrayList<>();
        }
        int temp = 0;
        for (int i = 0; i < infoList.size(); ) {
            WordInfo wordInfo = infoList.get(i);
//            LogUtil.msg("i-->" + i + "--size==" + infoList.size() + "---" + wordInfo.getType_text());
            List<WordInfo> wordInfos = new ArrayList<>();
            wordInfos.add(wordInfo);
            for (int j = i + 1; j < infoList.size(); j++) {

                WordInfo wordInfo1 = infoList.get(j);
                if (TextUtils.equals(wordInfo.getType_text(), wordInfo1.getType_text())) {
                    wordInfos.add(wordInfo1);
                } else {
                    temp = j;
                    listList.add(wordInfos);
                    break;
                }

            }
            i = temp;
            if (i == infoList.size() - 2) {
                int size = infoList.size();
                List<WordInfo> wordInfoList = new ArrayList<>();
                wordInfoList.add(infoList.get(size - 2));
                wordInfoList.add(infoList.get(size - 1));
                listList.add(wordInfoList);
                break;
            }
        }
        mView.shoVowelNewInfos(listList);


    }


}
