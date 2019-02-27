package com.yc.junior.english.news.contract;

import android.graphics.Bitmap;

import yc.com.base.IPresenter;
import yc.com.base.IView;

/**
 * Created by wanglin  on 2018/10/18 16:05.
 */
public interface QRCodeContract {

    interface View extends IView {
        void showBitmap(Bitmap bitmap);
    }

    interface Presenter extends IPresenter {}

}
