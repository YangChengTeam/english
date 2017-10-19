package com.yc.english.speak.view.fragment;

import android.content.Intent;
import android.graphics.Rect;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;

import com.blankj.utilcode.util.LogUtils;
import com.chad.library.adapter.base.BaseQuickAdapter;
import com.kk.securityhttp.net.contains.HttpConfig;
import com.yc.english.R;
import com.yc.english.base.view.BaseFragment;
import com.yc.english.base.view.StateView;
import com.yc.english.speak.contract.SpeakEnglishContract;
import com.yc.english.speak.model.bean.SpeakAndReadInfo;
import com.yc.english.speak.model.bean.SpeakAndReadItemInfo;
import com.yc.english.speak.presenter.SpeakEnglishListPresenter;
import com.yc.english.speak.view.activity.SpeakMoreActivity;
import com.yc.english.speak.view.adapter.SpeakEnglishAdapterNew;

import net.lucode.hackware.magicindicator.buildins.UIUtil;

import java.util.List;

import butterknife.BindView;

/**
 * Created by wanglin  on 2017/10/12 15:00.
 */

public class SpeakFragment extends BaseFragment<SpeakEnglishListPresenter> implements SpeakEnglishContract.View

{


    @BindView(R.id.recyclerView)
    RecyclerView recyclerView;
    @BindView(R.id.stateView)
    StateView stateView;

    private int type;
    private SpeakEnglishAdapterNew adapter;


    @Override
    public void init() {
        mPresenter = new SpeakEnglishListPresenter(getActivity(), this);
        recyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));


        adapter = new SpeakEnglishAdapterNew(null, getType());

        recyclerView.setAdapter(adapter);
        recyclerView.addItemDecoration(new MyItemDecoration());

        initListener();
        getData();

    }

    private void initListener() {
        adapter.setOnItemChildClickListener(new BaseQuickAdapter.OnItemChildClickListener() {
            @Override
            public boolean onItemChildClick(BaseQuickAdapter adapter, View view, int position) {
//                Toast.makeText(getActivity(), view.getClass().getSimpleName() + "---" + position, Toast.LENGTH_SHORT).show();
                SpeakAndReadInfo speakAndReadInfo = (SpeakAndReadInfo) adapter.getItem(position);
                Intent intent = new Intent(getActivity(), SpeakMoreActivity.class);
                intent.putExtra("speakAndReadInfo", speakAndReadInfo);
                startActivity(intent);

                return false;

            }
        });

    }

    @Override
    public int getLayoutId() {
        return R.layout.speak_fragment_list;
    }

    public void setType(int type) {
        this.type = type;
    }

    public int getType() {
        return type;
    }

    @Override
    public void hideStateView() {
        stateView.hide();
    }

    @Override
    public void showNoNet() {
        stateView.showNoNet(recyclerView, HttpConfig.NET_ERROR, new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                getData();
            }
        });
    }

    @Override
    public void showNoData() {
        stateView.showNoData(recyclerView);
    }

    @Override
    public void showLoading() {
        stateView.showLoading(recyclerView);
    }


    @Override
    public void showReadAndSpeakList(List<SpeakAndReadInfo> data) {
        adapter.setNewData(data);
        LogUtils.e(data.toString());

    }

    @Override
    public void shoReadAndSpeakMorList(List<SpeakAndReadItemInfo> list, int page, boolean isFitst) {

    }


    private class MyItemDecoration extends RecyclerView.ItemDecoration {
        @Override
        public void getItemOffsets(Rect outRect, View view, RecyclerView parent, RecyclerView.State state) {
            super.getItemOffsets(outRect, view, parent, state);
            outRect.set(0, 0, 0, UIUtil.dip2px(getActivity(), 15));
        }
    }

    private void getData() {
        mPresenter.getReadAndSpeakList(String.valueOf(getType()), false, 1, true);
    }


}
