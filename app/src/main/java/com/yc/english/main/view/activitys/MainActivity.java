package com.yc.english.main.view.activitys;

import com.yc.english.R;
import com.yc.english.base.view.FullScreenActivity;
import com.yc.english.main.contract.MainContract;
import com.yc.english.main.presenter.MainPresenter;
import com.yc.english.main.view.wdigets.TabBar;

import butterknife.BindView;


public class MainActivity extends FullScreenActivity<MainPresenter> implements MainContract.View {

    @BindView(R.id.tabbar)
    TabBar tabBar;


    @Override
    public int getLayoutID() {
        return R.layout.main_activity_main;
    }

    @Override
    public void init() {
        mPresenter = new MainPresenter(this, this);
        mToolbar.showNavigationIcon();
        tabBar.tab(0);
    }

    @Override
    public void show(String html) {

    }

}
