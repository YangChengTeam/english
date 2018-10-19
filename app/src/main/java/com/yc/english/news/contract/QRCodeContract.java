package com.yc.english.news.contract;

import android.graphics.Bitmap;

import com.yc.english.base.presenter.IPresenter;
import com.yc.english.base.view.IView;

/**
 * Created by wanglin  on 2018/10/18 16:05.
 */
public interface QRCodeContract {

    interface View extends IView{
        void showBitmap(Bitmap bitmap);
    }

    interface Presenter extends IPresenter{}

}
