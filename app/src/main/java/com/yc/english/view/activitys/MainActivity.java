package com.yc.english.view.activitys;

import android.text.Html;
import android.widget.TextView;

import com.yc.english.R;
import com.yc.english.contract.MainContract;
import com.yc.english.presenter.MainPresenter;
import com.yc.base.view.BaseActivity;

import butterknife.BindView;


public class MainActivity extends BaseActivity<MainPresenter> implements MainContract.View {
    @BindView(R.id.tv_main)
    TextView mMainTextView;

    @Override
    public int getLayoutID() {
        return R.layout.activity_main;
    }

    @Override
    public void init() {
        mPresenter = new MainPresenter(this, this);
    }


    @Override
    public void show(String html) {
        mMainTextView.setText(Html.fromHtml(html));
    }
}
