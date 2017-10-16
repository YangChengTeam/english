package com.yc.english.speak.view.adapter;

import android.content.Intent;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.Toast;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;
import com.yc.english.R;
import com.yc.english.speak.model.bean.EnglishInfo;

import java.util.List;

/**
 * Created by wanglin  on 2017/10/12 15:24.
 */

public class SpeakEnglishAdapter extends BaseQuickAdapter<EnglishInfo, BaseViewHolder> {

    public SpeakEnglishAdapter(List<EnglishInfo> data) {
        super(R.layout.speak_fragment_list_detail, data);
    }

    @Override
    protected void convert(BaseViewHolder helper, EnglishInfo item) {
        helper.setText(R.id.english_tv_title, item.getTitle()).addOnClickListener(R.id.rl_more);

        helper.setImageResource(R.id.iv_speak_icon, item.getType() == 1 ? R.mipmap.speak_tv_orange : item.getType() == 2 ? R.mipmap.speak_cartoon : R.mipmap.speak_live);

        RecyclerView recyclerView = helper.getView(R.id.recyclerView_speak);
        GridLayoutManager layout = new GridLayoutManager(mContext, 2);

        recyclerView.setLayoutManager(layout);
        SpeakEnglishItemAdapter itemAdapter = new SpeakEnglishItemAdapter(item.getItem_list(), false);
        recyclerView.setAdapter(itemAdapter);
        /**
         * 该方法设置gridLayoutManager布局中每个item占据的列数
         * 这个方法必须放在setAdapter之后才生效，否则不起作用
         *
         */
        layout.setSpanSizeLookup(new GridLayoutManager.SpanSizeLookup() {
            @Override
            public int getSpanSize(int position) {
                if (position == 0) {
                    return 2;
                }
                return 1;

            }
        });
        initListener(itemAdapter);

    }

    private void initListener(SpeakEnglishItemAdapter itemAdapter) {
        itemAdapter.setOnItemChildClickListener(new OnItemChildClickListener() {
            @Override
            public boolean onItemChildClick(BaseQuickAdapter adapter, View view, int position) {
                Toast.makeText(mContext, view.getClass().getSimpleName() + "--" + position, Toast.LENGTH_SHORT).show();
                // TODO: 2017/10/13 视频或音频跳转
//                mContext.startActivity(new Intent(mContext,ListenEnglishActivity.class));
                return false;

            }
        });

    }
}
