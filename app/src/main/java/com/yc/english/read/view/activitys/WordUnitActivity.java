package com.yc.english.read.view.activitys;

import android.content.Intent;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;
import android.view.View;

import com.blankj.utilcode.util.LogUtils;
import com.chad.library.adapter.base.BaseQuickAdapter;
import com.yc.english.R;
import com.yc.english.base.view.FullScreenActivity;
import com.yc.english.read.model.domain.UnitInfo;
import com.yc.english.read.view.adapter.ReadWordUnitItemClickAdapter;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;

/**
 * Created by admin on 2017/7/26.
 */

public class WordUnitActivity extends FullScreenActivity {
    @BindView(R.id.rv_word_unit_list)
    RecyclerView mWordUnitRecyclerView;

    ReadWordUnitItemClickAdapter mItemAdapter;

    private List<UnitInfo> mBookDatas;

    @Override
    public int getLayoutID() {
        return R.layout.read_activity_word_unit;
    }

    @Override
    public void init() {
        initData();

        mToolbar.setTitle(getString(R.string.read_book_unit_text));
        mToolbar.showNavigationIcon();
        mToolbar.setMenuTitle(getString(R.string.word_book_change_text));
        mToolbar.setOnMenuItemClickListener(new Toolbar.OnMenuItemClickListener() {
            @Override
            public boolean onMenuItemClick(MenuItem item) {
                Intent intent = new Intent(WordUnitActivity.this, AddBookActivity.class);
                startActivity(intent);
                return false;
            }
        });

        mWordUnitRecyclerView.setLayoutManager(new GridLayoutManager(this, 2));

        mItemAdapter = new ReadWordUnitItemClickAdapter(this, mBookDatas);
        mWordUnitRecyclerView.setAdapter(mItemAdapter);

        mItemAdapter.setOnItemClickListener(new BaseQuickAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(BaseQuickAdapter adapter, View view, int position) {
                LogUtils.e("position --->" + position);
                Intent intent = new Intent(WordUnitActivity.this, ReadWordActivity.class);
                startActivity(intent);
            }
        });
    }

    /**
     * 测试数据
     */
    public void initData() {
        mBookDatas = new ArrayList<UnitInfo>();
        for (int i = 0; i < 6; i++) {
            UnitInfo unitInfo = new UnitInfo(UnitInfo.CLICK_ITEM_VIEW);
            unitInfo.setUnitTitle("Unit " + (i + 1));
            unitInfo.setUnitTotal((i + 1) * 2 + "个单词");
            unitInfo.setReciteTotalPersion((i + 1000) * 2 + "个人已背诵");
            mBookDatas.add(unitInfo);
        }
    }

}
