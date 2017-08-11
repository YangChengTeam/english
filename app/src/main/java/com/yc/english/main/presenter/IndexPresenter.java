package com.yc.english.main.presenter;

import android.content.Context;

import com.kk.securityhttp.engin.HttpCoreEngin;
import com.kk.utils.UIUitls;
import com.yc.english.base.presenter.BasePresenter;
import com.yc.english.main.contract.IndexContract;
import com.yc.english.main.hepler.UserInfoHelper;
import com.yc.english.main.model.domain.UserInfo;
import com.yc.english.main.model.engin.IndexEngin;

import java.util.ArrayList;
import java.util.List;

import rx.functions.Action1;

/**
 * Created by zhangkai on 2017/7/26.
 */

public class IndexPresenter extends BasePresenter<IndexEngin, IndexContract.View> implements IndexContract.Presenter {
    public IndexPresenter(Context context, IndexContract.View view) {
        super(context, view);
        mEngin = new IndexEngin(context);
    }

    @Override
    public void loadData(boolean forceUpdate, boolean showLoadingUI) {
        if (!forceUpdate) return;

        mView.showLoading();
        UIUitls.postDelayed(1000, new Runnable() {
            @Override
            public void run() {
                loadData();
                getAvatar();
                mView.hideStateView();
            }
        });
    }


    @Override
    public void loadData() {
        List<String> images = new ArrayList<>();
        images.add("http://7xio5j.com1.z0.glb.clouddn.com/0014.jpg");
        images.add("http://7xio5j.com1.z0.glb.clouddn.com/0016.jpg");
        images.add("http://7xio5j.com1.z0.glb.clouddn.com/0015.jpg");
        mView.showBanner(images);
    }


    @Override
    public void getAvatar() {
        UserInfoHelper.getUserInfoDo(new UserInfoHelper.Callback() {
            @Override
            public void showUserInfo(UserInfo userInfo) {
                mView.showAvatar(userInfo);
            }

            @Override
            public void showNoLogin() {

            }
        });
    }
}

