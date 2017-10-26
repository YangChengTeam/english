package com.yc.english.speak.view.activity;

import android.content.Intent;
import android.support.v4.content.ContextCompat;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.Toast;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.kk.securityhttp.net.contains.HttpConfig;
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
    SwipeRefreshLayout swipeRefreshLayout;
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

        initListener();

        getData(false, true);
    }

    private void initListener() {

        speakEnglishItemAdapter.setOnItemChildClickListener(new BaseQuickAdapter.OnItemChildClickListener() {
            @Override
            public boolean onItemChildClick(BaseQuickAdapter adapter, View view, int position) {
                // TODO: 2017/10/13 视频或音频点击跳转
                List<SpeakAndReadItemInfo> dataList = speakEnglishItemAdapter.getData();
                SpeakAndReadItemInfo speakAndReadItemInfo = (SpeakAndReadItemInfo) adapter.getItem(position);
                speakAndReadItemInfo.setInnerPos(position);
                Intent intent = null;

                if (type == 1) {
                    intent = new Intent(SpeakMoreActivity.this, SpeakEnglishActivity.class);
                    intent.putExtra("itemInfo", speakAndReadItemInfo);
                    intent.putParcelableArrayListExtra("infoList", (ArrayList) dataList);

                } else if (type == 2) {
                    intent = new Intent(SpeakMoreActivity.this, ListenEnglishActivity.class);
                    intent.putExtra("itemInfo", speakAndReadItemInfo);
                    intent.putParcelableArrayListExtra("infoList", (ArrayList) dataList);
                }
                startActivity(intent);
                return false;
            }
        });
        speakEnglishItemAdapter.setOnLoadMoreListener(new BaseQuickAdapter.RequestLoadMoreListener() {
            @Override
            public void onLoadMoreRequested() {

                getData(true, false);
            }
        }, recyclerView);
        swipeRefreshLayout.setColorSchemeColors(ContextCompat.getColor(this, R.color.primary));
        swipeRefreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
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
    public void hideStateView() {
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
        if (swipeRefreshLayout.isRefreshing()) {
            swipeRefreshLayout.setRefreshing(false);
        }
    }

    @Override
    public void showNoData() {
        stateView.showNoData(swipeRefreshLayout);
        if (swipeRefreshLayout.isRefreshing()) {
            swipeRefreshLayout.setRefreshing(false);
        }
    }

    @Override
    public void showLoading() {
        stateView.showLoading(swipeRefreshLayout);
    }


    @Override
    public void shoReadAndSpeakMorList(List<SpeakAndReadInfo> list, int page, boolean isFitst) {
        if (page == 1) {
            speakEnglishItemAdapter.setNewData(list.get(0).getData());

        } else {
            speakEnglishItemAdapter.addData(list.get(0).getData());
        }
        if (list.size() == page_size) {
            speakEnglishItemAdapter.loadMoreComplete();
        } else {
            speakEnglishItemAdapter.loadMoreEnd();
        }

        if (swipeRefreshLayout.isRefreshing()) {
            swipeRefreshLayout.setRefreshing(false);
        }
    }

    private void getData(boolean isLoadMore, boolean isFirst) {
        if (isLoadMore) {
            page++;
        }
        mPresenter.getReadAndSpeakList(speakAndReadInfo.getData().get(0).getType_id(), "", page, isFirst);
    }

    @Override
    public void showSpeakEnglishDetail(List<SpeakEnglishBean> list) {

    }
}
