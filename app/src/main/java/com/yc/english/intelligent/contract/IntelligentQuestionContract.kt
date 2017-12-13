package com.yc.english.intelligent.contract

import com.yc.english.base.presenter.IPresenter
import com.yc.english.base.view.ILoading
import com.yc.english.base.view.INoData
import com.yc.english.base.view.INoNet
import com.yc.english.base.view.IView
import com.yc.english.intelligent.model.domain.QuestionInfoWrapper

/**
 * Created by zhangkai on 2017/12/6.
 */
interface IntelligentQuestionContract {
    interface View : IView, ILoading, INoNet, INoData {
        fun showInfo(list: List<QuestionInfoWrapper.QuestionInfo>)
    }

    interface Presenter : IPresenter {

    }
}