package com.yc.english.group.view.adapter;

import android.content.Context;
import android.content.Intent;
import android.view.View;
import android.widget.ImageView;

import com.example.comm_recyclviewadapter.BaseAdapter;
import com.example.comm_recyclviewadapter.BaseViewHolder;
import com.yc.english.R;
import com.yc.english.base.helper.GlideHelper;
import com.yc.english.group.model.bean.StudentFinishTaskInfo;
import com.yc.english.group.view.activitys.teacher.GroupTaskFinishDetailActivity;

import java.util.List;

/**
 * Created by wanglin  on 2017/7/28 16:00.
 */

public class GroupTaskFinishedAdapter extends BaseAdapter<StudentFinishTaskInfo.ListBean.NoDoneListBean> {
    private String mTaskId, mClassId, mMasterId;

    public GroupTaskFinishedAdapter(Context context, List<StudentFinishTaskInfo.ListBean.NoDoneListBean> mList) {
        super(context, mList);
    }

    @Override
    protected void convert(BaseViewHolder holder, int position) {
        final StudentFinishTaskInfo.ListBean.NoDoneListBean noDoneListBean = mList.get(position);
        GlideHelper.circleImageView(mContext, ((ImageView) holder.getView(R.id.m_iv_picture)), noDoneListBean.getUser_face(), R.mipmap.default_avatar);

        holder.setText(R.id.m_tv_task_finish_name, noDoneListBean.getNick_name());
        if (noDoneListBean.is_done()) {
            holder.setText(R.id.m_tv_task_finish_time, noDoneListBean.getDone_time());
            holder.itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Intent intent = new Intent(mContext, GroupTaskFinishDetailActivity.class);
                    intent.putExtra("mTaskId", mTaskId);
                    intent.putExtra("mClassId", mClassId);
                    intent.putExtra("doneId", noDoneListBean.getDone_id());
                    intent.putExtra("userId",noDoneListBean.getUser_id());

                    mContext.startActivity(intent);
                }
            });
        } else {
            holder.setVisible(R.id.m_tv_task_finish_time, false);
        }

    }

    @Override
    public int getLayoutID(int viewType) {
        return R.layout.group_task_finished_item;
    }

    public void setData(String taskId, String classId, String masterId) {
        this.mTaskId = taskId;
        this.mClassId = classId;
        this.mMasterId = masterId;
    }

}
