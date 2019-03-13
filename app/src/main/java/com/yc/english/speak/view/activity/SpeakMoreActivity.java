package com.yc.english.speak.view.activity;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.content.ContextCompat;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.LinearLayout;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.kk.securityhttp.net.contains.HttpConfig;
import com.scwang.smartrefresh.layout.SmartRefreshLayout;
import com.scwang.smartrefresh.layout.api.RefreshLayout;
import com.scwang.smartrefresh.layout.header.ClassicsHeader;
import com.scwang.smartrefresh.layout.listener.OnRefreshListener;
import com.yc.english.R;
import com.yc.english.base.view.FullScreenActivity;
import com.yc.english.base.view.StateView;
import com.yc.english.speak.contract.SpeakEnglishContract;
import com.yc.english.speak.model.bean.SpeakAndReadInfo;
import com.yc.english.speak.model.bean.SpeakAndReadItemInfo;
import com.yc.english.speak.model.bean.SpeakEnglishBean;
import com.yc.english.speak.presenter.SpeakEnglishListPresenter;
import com.yc.english.speak.view.adapter.SpeakEnglishItemAdapter;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * Created by wanglin  on 2017/10/13 10:24.
 */

public class SpeakMoreActivity extends FullScreenActivity<SpeakEnglishListPresenter> implements SpeakEnglishContract.View {
    @BindView(R.id.stateView)
    StateView stateView;
    @BindView(R.id.recyclerView)
    RecyclerView recyclerView;
    @BindView(R.id.ll_container)
    LinearLayout llContainer;
    @BindView(R.id.swipeRefreshLayout)
    SmartRefreshLayout swipeRefreshLayout;

    private SpeakAndReadInfo speakAndReadInfo;
    private SpeakEnglishItemAdapter speakEnglishItemAdapter;
    private int page = 1;
    private int page_size = 20;
    private int type;

    @Override
    public void init() {
        mPresenter = new SpeakEnglishListPresenter(this, this);
        if (getIntent() != null) {
            speakAndReadInfo = getIntent().getParcelableExtra("speakAndReadInfo");
            type = getIntent().getIntExtra("type", 0);
            mToolbar.setTitle(speakAndReadInfo.getType_name());
        }
        mToolbar.showNavigationIcon();

        recyclerView.setLayoutManager(new GridLayoutManager(this, 2));
        speakEnglishItemAdapter = new SpeakEnglishItemAdapter(null, true);

        recyclerView.setAdapter(speakEnglishItemAdapter);
        recyclerView.setHasFixedSize(true);

        initListener();

        getData(false, true);
    }

    private void initListener() {

        speakEnglishItemAdapter.setOnItemChildClickListener(new BaseQuickAdapter.OnItemChildClickListener() {
            @Override
            public void onItemChildClick(BaseQuickAdapter adapter, View view, int position) {
                // TODO: 2017/10/13 视频或音频点击跳转
                List<SpeakAndReadItemInfo> dataList = speakEnglishItemAdapter.getData();
                SpeakAndReadItemInfo speakAndReadItemInfo = (SpeakAndReadItemInfo) adapter.getItem(position);

                speakAndReadItemInfo.setInnerPos(position);
                speakAndReadItemInfo.setOutPos(0);
                Intent intent = null;
                List<SpeakAndReadInfo> list = new ArrayList<>();
                SpeakAndReadInfo speakAndReadInfo = new SpeakAndReadInfo();
                speakAndReadInfo.setData(dataList);
                list.add(speakAndReadInfo);

                if (type == 1) {
                    intent = new Intent(SpeakMoreActivity.this, SpeakEnglishActivity.class);
                    intent.putExtra("itemInfo", speakAndReadItemInfo);
                    intent.putParcelableArrayListExtra("infoList", (ArrayList) list);

                } else if (type == 2) {//todo 随时替换
                    intent = new Intent(SpeakMoreActivity.this, ListenEnglishActivity.class);
                    intent.putExtra("itemInfo", speakAndReadItemInfo);
                    intent.putParcelableArrayListExtra("infoList", (ArrayList) list);
                }
                startActivity(intent);
            }
        });
        speakEnglishItemAdapter.setOnLoadMoreListener(new BaseQuickAdapter.RequestLoadMoreListener() {
            @Override
            public void onLoadMoreRequested() {
                getData(true, false);
            }
        }, recyclerView);

        initRefresh();
    }

    private void initRefresh() {
        //设置 Header 为 贝塞尔雷达 样式
        swipeRefreshLayout.setRefreshHeader(new ClassicsHeader(this));
        swipeRefreshLayout.setPrimaryColorsId(R.color.primaryDark);
        swipeRefreshLayout.setEnableLoadMore(false);

        swipeRefreshLayout.setOnRefreshListener(new OnRefreshListener() {
            @Override
            public void onRefresh(@NonNull RefreshLayout refreshLayout) {
                page = 1;
                getData(false, false);
            }
        });
    }

    @Override
    public int getLayoutId() {
        return R.layout.speak_activity_more_list;
    }


    @Override
    public void hide() {
        stateView.hide();
    }

    @Override
    public void showNoNet() {
        stateView.showNoNet(swipeRefreshLayout, HttpConfig.NET_ERROR, new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                getData(false, true);
            }
        });
        if (swipeRefreshLayout!=null) {
            swipeRefreshLayout.finishRefresh();
        }
    }

    @Override
    public void showNoData() {
        stateView.showNoData(swipeRefreshLayout);
        if (swipeRefreshLayout!=null) {
            swipeRefreshLayout.finishRefresh();
        }
    }

    @Override
    public void showLoading() {
        stateView.showLoading(swipeRefreshLayout);
    }


    @Override
    public void shoReadAndSpeakMorList(List<SpeakAndReadInfo> list, int page, boolean isFitst) {
        if (list != null && list.size() > 0) {
            if (page == 1) {
                speakEnglishItemAdapter.setNewData(list.get(0).getData());

            } else {
                speakEnglishItemAdapter.addData(list.get(0).getData());
            }
            speakEnglishItemAdapter.loadMoreComplete();
        } else {
            speakEnglishItemAdapter.loadMoreEnd();
        }

        if (swipeRefreshLayout!=null) {
            swipeRefreshLayout.finishRefresh();
        }
    }

    private void getData(boolean isLoadMore, boolean isFirst) {
        if (isLoadMore) {
            page++;
        }
        String type_id = speakAndReadInfo.getData().get(0).getType_id();
        mPresenter.getReadAndSpeakList(type_id, "", page, isFirst);
    }

    @Override
    public void showSpeakEnglishDetail(List<SpeakEnglishBean> list) {

    }

    @Override
    public boolean isStatusBarMateria() {
        return true;
    }
}
