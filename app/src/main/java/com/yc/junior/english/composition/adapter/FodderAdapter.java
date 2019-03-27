package com.yc.junior.english.composition.adapter;

import android.support.annotation.Nullable;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;
import com.yc.junior.english.R;
import com.yc.junior.english.composition.activity.CompositionMoreActivity;
import com.yc.junior.english.composition.model.bean.CompositionInfo;
import com.yc.junior.english.composition.model.bean.FodderInfo;
import com.yc.soundmark.category.utils.ItemDecorationHelper;

import java.util.List;

/**
 * Created by wanglin  on 2019/3/25 09:56.
 */
public class FodderAdapter extends BaseQuickAdapter<FodderInfo, BaseViewHolder> {
    public FodderAdapter(@Nullable List<FodderInfo> data) {
        super(R.layout.fodder_view, data);
    }

    @Override
    protected void convert(BaseViewHolder helper, FodderInfo item) {
        helper.setText(R.id.tv_fodder_title, item.getTitle());

        RecyclerView recyclerView = helper.getView(R.id.exam_recyclerView);

        recyclerView.setLayoutManager(new LinearLayoutManager(mContext));
        final FodderItemAdapter fodderItemAdapter = new FodderItemAdapter(item.getCompositionInfos());
        recyclerView.setAdapter(fodderItemAdapter);

        recyclerView.addItemDecoration(new ItemDecorationHelper(mContext, 8));

        fodderItemAdapter.setOnItemClickListener(new OnItemClickListener() {
            @Override
            public void onItemClick(BaseQuickAdapter adapter, View view, int position) {
                CompositionInfo fodderInfo = fodderItemAdapter.getItem(position);
                if (fodderInfo != null)
                    CompositionMoreActivity.startActivity(mContext, fodderInfo.getSub_title(),fodderInfo.getAttrid());
            }
        });

    }
}
