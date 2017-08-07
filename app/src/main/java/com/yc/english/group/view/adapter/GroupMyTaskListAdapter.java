package com.yc.english.group.view.adapter;

import android.content.Context;
import android.content.Intent;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.example.comm_recyclviewadapter.BaseAdapter;
import com.example.comm_recyclviewadapter.BaseViewHolder;
import com.yc.english.R;
import com.yc.english.group.model.bean.TaskItem;
import com.yc.english.group.view.activitys.student.GroupMyTaskDetailActivity;

import java.util.List;

/**
 * Created by wanglin  on 2017/7/28 09:03.
 */

public class GroupMyTaskListAdapter extends BaseAdapter<TaskItem> {
    public GroupMyTaskListAdapter(Context context, List<TaskItem> mList) {
        super(context, mList);
    }

    @Override
    protected void convert(BaseViewHolder holder, int position) {
        TaskItem taskInfo = mList.get(position);
        holder.setText(R.id.m_tv_task_time, taskInfo.getPublishTime());
        List<Integer> types = taskInfo.getTypes();
        LinearLayout view = holder.getView(R.id.m_ll_task_extra);
        TextView tvContent = holder.getView(R.id.m_tv_task_content);

        if (types != null && types.size() > 0) {
            if (types.size() > 1) {
                holder.setImageResource(R.id.m_iv_task_picture, R.mipmap.group44);
                boolean flag = false;
                for (int i = 0; i < types.size(); i++) {
                    LinearLayout.MarginLayoutParams layoutParams = (LinearLayout.MarginLayoutParams) view.getLayoutParams();
                    layoutParams.width = LinearLayout.LayoutParams.WRAP_CONTENT;
                    layoutParams.height = LinearLayout.LayoutParams.WRAP_CONTENT;

                    TextView tv = new TextView(mContext);
                    tv.setTextSize(16);
                    tv.setLayoutParams(layoutParams);
                    tv.setTextColor(mContext.getResources().getColor(R.color.black_333333));
                    layoutParams.rightMargin = 25;
                    view.setVisibility(View.VISIBLE);
                    switch (types.get(i)) {
                        case 0:
                            flag = true;
                            tvContent.setText(taskInfo.getContent());
                            break;
                        case 1:
                            tv.setText("[语音]");
                            break;
                        case 2:
                            tv.setText("[图片]");
                            break;
                        case 3:
                            tv.setText("[文档]");
                            break;
                    }
                    view.addView(tv);

                    tvContent.setVisibility(flag ? View.VISIBLE : View.GONE);
                }

            } else {
                Integer type = types.get(0);
                TextView tv = new TextView(mContext);
                tv.setTextSize(16);
                tv.setTextColor(mContext.getResources().getColor(R.color.black_333333));
                switch (type) {
                    case 0://纯文本
                        holder.setImageResource(R.id.m_iv_task_picture, R.mipmap.group36);
                        tvContent.setText(taskInfo.getContent());
                        break;
                    case 1://语音
                        holder.setImageResource(R.id.m_iv_task_picture, R.mipmap.group38);
                        tvContent.setVisibility(View.GONE);
                        view.setVisibility(View.VISIBLE);
                        tv.setText("[语音]");
                        view.addView(tv);
                        break;
                    case 2://图片
                        holder.setImageResource(R.id.m_iv_task_picture, R.mipmap.group40);
                        tvContent.setVisibility(View.GONE);
                        view.setVisibility(View.VISIBLE);
                        tv.setText("[图片]");
                        view.addView(tv);
                        break;
                    case 3://文档
                        holder.setImageResource(R.id.m_iv_task_picture, R.mipmap.group42);
                        tvContent.setVisibility(View.GONE);
                        view.setVisibility(View.VISIBLE);
                        tv.setText("[文档]");
                        view.addView(tv);
                        break;
                }
            }

        }
        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mContext.startActivity(new Intent(mContext, GroupMyTaskDetailActivity.class));
            }
        });


    }

    @Override
    public int getLayoutID(int viewType) {
        return R.layout.group_publish_task_item;
    }


}
