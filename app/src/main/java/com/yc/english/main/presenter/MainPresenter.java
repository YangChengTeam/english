package com.yc.english.main.presenter;

import android.content.Context;

import com.yc.english.R;
import com.yc.english.base.presenter.BasePresenter;
import com.yc.english.main.contract.MainContract;
import com.yc.english.main.model.engin.MainEngin;


/**
 * Created by zhangkai on 2017/7/20.
 */

public class MainPresenter extends BasePresenter<MainEngin, MainContract.View> implements MainContract.Presenter {

    private Context mContext;
    public MainPresenter(Context context, MainContract.View view) {
        super(view);
        mContext = context;
        mEngin = new MainEngin(context);
    }

    public void loadData(final boolean forceUpdate, final boolean showLoadingUI) {
        if(!forceUpdate) return;
    }

    @Override
    public String getTitle(int index) {
        String title = mContext.getResources().getString(R.string.main_tab_index);
        switch (index){
            case 1:
                 title = mContext.getResources().getString(R.string.main_tab_grade);
                 break;
            case 2:
                title = mContext.getResources().getString(R.string.main_tab_my);
                break;
            default:
                break;
        }
        return title;
    }
}
