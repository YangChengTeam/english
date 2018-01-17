package com.yc.junior.english.intelligent.contract

import com.yc.junior.english.base.presenter.IPresenter
import com.yc.junior.english.base.view.*
import com.yc.junior.english.intelligent.model.domain.QuestionInfoWrapper

/**
 * Created by zhangkai on 2017/12/6.
 */
interface IntelligentQuestionContract {
    interface View : IView, ILoading, INoNet,IDialog {
        fun showInfo(list: List<QuestionInfoWrapper.QuestionInfo>,  use_time: String?)
        fun showNoData(message: String);
    }

    interface Presenter : IPresenter {

    }
}