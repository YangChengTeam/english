package com.yc.junior.english.group.view.adapter;

import android.content.Context;
import android.widget.ImageView;

import com.example.comm_recyclviewadapter.BaseAdapter;
import com.example.comm_recyclviewadapter.BaseViewHolder;
import com.yc.junior.english.R;
import com.yc.junior.english.base.helper.GlideHelper;
import com.yc.junior.english.group.model.bean.StudentLookTaskInfo;

import java.util.List;

/**
 * Created by wanglin  on 2017/7/28 16:00.
 */

public class GroupTaskLookAdapter extends BaseAdapter<StudentLookTaskInfo.ListBean.NoreadListBean> {
    public GroupTaskLookAdapter(Context context, List<StudentLookTaskInfo.ListBean.NoreadListBean> mList) {
        super(context, mList);
    }

    @Override
    protected void convert(BaseViewHolder holder, int position) {
        StudentLookTaskInfo.ListBean.NoreadListBean noreadListBean = mList.get(position);

        GlideHelper.circleImageView(mContext, ((ImageView) holder.getView(R.id.m_iv_picture)), noreadListBean.getUser_face(), R.mipmap.default_avatar);
        holder.setText(R.id.m_tv_task_finish_name, noreadListBean.getNick_name());
        if (noreadListBean.isIs_read()) {
            holder.setText(R.id.m_tv_task_finish_time, noreadListBean.getRead_time());
        } else {
            holder.setVisible(R.id.m_tv_task_finish_time, false);
        }

    }

    @Override
    public int getLayoutID(int viewType) {
        return R.layout.group_task_finished_item;
    }
}
