package com.yc.english.group.view.adapter;

import android.content.Context;
import android.content.Intent;
import android.graphics.BitmapFactory;
import android.view.View;

import com.blankj.utilcode.util.ImageUtils;
import com.example.comm_recyclviewadapter.BaseAdapter;
import com.example.comm_recyclviewadapter.BaseViewHolder;
import com.yc.english.R;
import com.yc.english.group.model.bean.TaskFinishedInfo;
import com.yc.english.group.view.activitys.teacher.GroupTaskFinishDetailActivity;

import java.util.List;

/**
 * Created by wanglin  on 2017/7/28 16:00.
 */

public class GroupTaskFinishedAdapter extends BaseAdapter<TaskFinishedInfo> {
    public GroupTaskFinishedAdapter(Context context, List<TaskFinishedInfo> mList) {
        super(context, mList);
    }

    @Override
    protected void convert(BaseViewHolder holder, int position) {
        TaskFinishedInfo info = mList.get(position);
        holder.setImageBitmap(R.id.m_iv_picture, ImageUtils.toRound(BitmapFactory.decodeResource(mContext.getResources(), R.mipmap.portial)));
        holder.setText(R.id.m_tv_task_finish_name, info.getName());
        if (info.getFinishedTime() != null) {
            holder.setText(R.id.m_tv_task_finish_time, info.getFinishedTime());
        } else {
            holder.setVisible(R.id.m_tv_task_finish_time, false);
        }

        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mContext.startActivity(new Intent(mContext, GroupTaskFinishDetailActivity.class));
            }
        });


    }

    @Override
    public int getLayoutID(int viewType) {
        return R.layout.group_task_finished_item;
    }
}
