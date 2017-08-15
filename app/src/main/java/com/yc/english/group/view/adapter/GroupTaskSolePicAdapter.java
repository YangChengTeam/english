package com.yc.english.group.view.adapter;


import android.content.Context;
import android.widget.ImageView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.example.comm_recyclviewadapter.BaseAdapter;
import com.example.comm_recyclviewadapter.BaseViewHolder;
import com.yc.english.R;

import java.util.List;

/**
 * Created by wanglin  on 2017/8/14 11:12.
 */

public class GroupTaskSolePicAdapter extends BaseAdapter<String> {
    public GroupTaskSolePicAdapter(Context context, List<String> mList) {
        super(context, mList);
    }

    @Override
    protected void convert(BaseViewHolder holder, int position) {
        String path = mList.get(position);
        Glide.with(mContext).load(path).into(((ImageView) holder.getView(R.id.iv_picture_detail)));
    }

    @Override
    public int getLayoutID(int viewType) {
        return R.layout.group_task_picture_item;
    }
}
