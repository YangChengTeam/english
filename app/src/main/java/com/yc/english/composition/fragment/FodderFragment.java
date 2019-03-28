package com.yc.english.composition.fragment;

import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;

import com.yc.english.R;
import com.yc.english.composition.adapter.FodderAdapter;
import com.yc.english.composition.contract.FodderContract;
import com.yc.english.composition.model.bean.FodderInfo;
import com.yc.english.composition.presenter.FodderPresenter;

import java.util.List;

import butterknife.BindView;
import yc.com.base.BaseFragment;

/**
 * Created by wanglin  on 2019/3/22 16:35.
 * 写作素材
 */
public class FodderFragment extends BaseFragment<FodderPresenter> implements FodderContract.View {
    @BindView(R.id.fodder_recyclerView)
    RecyclerView fodderRecyclerView;
    private FodderAdapter fodderAdapter;

    @Override
    public int getLayoutId() {
        return R.layout.fragment_fodder;
    }

    @Override
    public void init() {
        mPresenter = new FodderPresenter(getActivity(), this);
        fodderRecyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));
        fodderAdapter = new FodderAdapter(null);
        fodderRecyclerView.setAdapter(fodderAdapter);
    }



    @Override
    public void showFodderInfos(List<FodderInfo> fodderInfos) {
        fodderAdapter.setNewData(fodderInfos);
    }
}
