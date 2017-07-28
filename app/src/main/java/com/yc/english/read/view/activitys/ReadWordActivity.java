package com.yc.english.read.view.activitys;

import android.content.Intent;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.widget.Button;

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
import butterknife.OnClick;

/**
 * Created by admin on 2017/7/27.
 */

public class ReadWordActivity extends FullScreenActivity {

    @BindView(R.id.rv_word_list)
    RecyclerView mReadWordRecyclerView;

    @BindView(R.id.btn_word_practice)
    Button mWordPracticeButton;

    ReadWordItemClickAdapter mReadWordItemClickAdapter;

    List<MultiItemEntity> mDatas;

    @Override
    public int getLayoutID() {
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

    @OnClick(R.id.btn_word_practice)
    public void wordPractice() {
        Intent intent = new Intent(ReadWordActivity.this, WordPracticeActivity.class);
        startActivity(intent);
    }

}
