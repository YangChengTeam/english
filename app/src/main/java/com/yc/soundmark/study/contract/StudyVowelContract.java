package com.yc.soundmark.study.contract;

import com.yc.soundmark.study.model.domain.WordInfo;

import java.util.List;

import yc.com.base.IPresenter;
import yc.com.base.IView;

/**
 * Created by wanglin  on 2018/11/1 09:37.
 */
public interface StudyVowelContract {
    interface View extends IView{
        void showVowelInfoList(List<WordInfo> infoList);

        void shoVowelNewInfos(List<List<WordInfo>> soundmarkInfo);
    }

    interface Presenter extends IPresenter {
    }
}
