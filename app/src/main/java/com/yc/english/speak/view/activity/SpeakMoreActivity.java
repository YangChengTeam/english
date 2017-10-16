package com.yc.english.speak.view.activity;

import android.content.Intent;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.Toast;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.yc.english.R;
import com.yc.english.base.view.FullScreenActivity;
import com.yc.english.base.view.StateView;
import com.yc.english.speak.model.bean.EnglishInfo;
import com.yc.english.speak.view.adapter.SpeakEnglishItemAdapter;

import butterknife.BindView;

/**
 * Created by wanglin  on 2017/10/13 10:24.
 */

public class SpeakMoreActivity extends FullScreenActivity {
    @BindView(R.id.stateView)
    StateView stateView;
    @BindView(R.id.recyclerView)
    RecyclerView recyclerView;
    @BindView(R.id.ll_container)
    LinearLayout llContainer;
    @BindView(R.id.swipeRefreshLayout)
    SwipeRefreshLayout swipeRefreshLayout;
    private EnglishInfo englishInfo;
    private SpeakEnglishItemAdapter speakEnglishItemAdapter;

    @Override
    public void init() {
        swipeRefreshLayout.setEnabled(false);
        if (getIntent() != null) {
            englishInfo = getIntent().getParcelableExtra("englishInfo");
            mToolbar.setTitle(englishInfo.getTitle());
        }
        mToolbar.showNavigationIcon();

        recyclerView.setLayoutManager(new GridLayoutManager(this, 2));
        speakEnglishItemAdapter = new SpeakEnglishItemAdapter(englishInfo.getItem_list(), true);
        recyclerView.setAdapter(speakEnglishItemAdapter);

        initListener();


    }

    private void initListener() {
        speakEnglishItemAdapter.setOnItemChildClickListener(new BaseQuickAdapter.OnItemChildClickListener() {
            @Override
            public boolean onItemChildClick(BaseQuickAdapter adapter, View view, int position) {
                Toast.makeText(SpeakMoreActivity.this, view.getClass().getSimpleName() + "--" + position, Toast.LENGTH_SHORT).show();
                // TODO: 2017/10/13 视频或音频点击跳转
//                startActivity(new Intent(SpeakMoreActivity.this, ListenEnglishActivity.class));

                return false;
            }
        });

    }

    @Override
    public int getLayoutId() {
        return R.layout.speak_activity_more_list;
    }


}
