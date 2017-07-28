package com.yc.english.read.view.activitys;

import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;

import com.chad.library.adapter.base.entity.MultiItemEntity;
import com.yc.english.R;
import com.yc.english.base.view.FullScreenActivity;
import com.yc.english.read.model.domain.WordDetailInfo;
import com.yc.english.read.model.domain.WordInfo;
import com.yc.english.read.view.adapter.ReadWordItemClickAdapter;
import com.yc.english.read.view.wdigets.SpaceItemDecoration;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;

/**
 * Created by admin on 2017/7/27.
 */

public class ReadWordActivity extends FullScreenActivity {

    @BindView(R.id.rv_word_list)
    RecyclerView mReadWordRecyclerView;

    ReadWordItemClickAdapter mReadWordItemClickAdapter;

    List<MultiItemEntity> mDatas;

    @Override
    public int getLayoutId() {
        return R.layout.read_activity_word_play;
    }

    @Override
    public void init() {
        mToolbar.setTitle("Unit 1");
        mToolbar.showNavigationIcon();
        mDatas = new ArrayList<MultiItemEntity>();

        for (int i = 0; i < 9; i++) {
            WordInfo wordInfo = new WordInfo("Book", "书，书本");
            WordDetailInfo wordDetailInfo = new WordDetailInfo("I just took her book home and let him play", "我只是把她的书带回家，让他去玩了");
            wordInfo.addSubItem(wordDetailInfo);
            mDatas.add(wordInfo);
        }

        mReadWordRecyclerView.setLayoutManager(new LinearLayoutManager(this));
        mReadWordItemClickAdapter = new ReadWordItemClickAdapter(ReadWordActivity.this, mDatas);
        mReadWordRecyclerView.setAdapter(mReadWordItemClickAdapter);
        mReadWordRecyclerView.addItemDecoration(new SpaceItemDecoration(1));
    }
}
