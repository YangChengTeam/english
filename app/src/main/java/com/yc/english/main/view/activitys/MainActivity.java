package com.yc.english.main.view.activitys;

import android.text.Html;
import android.widget.TextView;

import com.yc.english.R;
import com.yc.english.base.view.FullScreenActivity;
import com.yc.english.main.contract.MainContract;
import com.yc.english.main.presenter.MainPresenter;

import butterknife.BindView;


public class MainActivity extends FullScreenActivity<MainPresenter> implements MainContract.View {
    @BindView(R.id.tv_main)
    TextView mMainTextView;

    @Override
    public int getLayoutID() {
        return R.layout.activity_main;
    }

    @Override
    public void init() {
        mPresenter = new MainPresenter(this, this);
        mToolbar.showNavigationIcon();
    }

    @Override
    public void show(String html) {
        mMainTextView.setText(Html.fromHtml(html));
    }

}
