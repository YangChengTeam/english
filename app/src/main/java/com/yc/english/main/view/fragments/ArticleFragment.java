package com.yc.english.main.view.fragments;

import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.yc.english.R;
import com.yc.english.base.view.BaseFragment;
import com.yc.english.main.model.domain.ArticleInfo;
import com.yc.english.main.view.adapters.AritleAdapter;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;

/**
 * Created by zhangkai on 2017/9/5.
 */

public class ArticleFragment extends BaseFragment {

    @BindView(R.id.recycler_view)
    RecyclerView mRecyclerView;

    private AritleAdapter mAritleAdapter;

    @Override
    public void init() {

        List<ArticleInfo> articleInfos = new ArrayList<>();
        ArticleInfo articleInfo = new ArticleInfo();
        ArticleInfo articleInfo2 = new ArticleInfo();
        ArticleInfo articleInfo3 = new ArticleInfo();
        articleInfos.add(articleInfo);
        articleInfos.add(articleInfo2);
        articleInfos.add(articleInfo3);


        mRecyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));
        mAritleAdapter = new AritleAdapter(articleInfos, 0);
        mRecyclerView.setAdapter(mAritleAdapter);

        mAritleAdapter.setOnItemClickListener(new BaseQuickAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(BaseQuickAdapter adapter, View view, int position) {

            }
        });


    }

    @Override
    public int getLayoutId() {
        return R.layout.index_fragment_aritle;
    }
}
