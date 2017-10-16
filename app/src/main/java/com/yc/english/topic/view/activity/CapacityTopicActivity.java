package com.yc.english.topic.view.activity;

import android.os.Bundle;
import android.support.v7.widget.RecyclerView;
import android.widget.Button;

import com.yc.english.R;
import com.yc.english.base.view.FullScreenActivity;
import com.yc.english.base.view.StateView;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * Created by wanglin  on 2017/9/29 16:52.
 */

public class CapacityTopicActivity extends FullScreenActivity {
    @BindView(R.id.stateView)
    StateView stateView;
    @BindView(R.id.recyclerView)
    RecyclerView recyclerView;
    @BindView(R.id.m_btn_topic_result)
    Button mBtnTopicResult;

    @Override
    public void init() {
        mToolbar.setTitle("");
        mToolbar.showNavigationIcon();

    }

    @Override
    public int getLayoutId() {
        return R.layout.topic_activity_detail;
    }
}
