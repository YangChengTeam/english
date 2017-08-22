package com.yc.english.group.view.adapter;

import android.content.Context;
import android.content.Intent;
import android.text.TextUtils;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.example.comm_recyclviewadapter.BaseAdapter;
import com.example.comm_recyclviewadapter.BaseViewHolder;
import com.yc.english.R;
import com.yc.english.group.constant.GroupConstant;
import com.yc.english.group.model.bean.TaskAllInfoWrapper;
import com.yc.english.group.view.activitys.teacher.GroupTaskFinishAndUnfinshActivity;

import java.util.List;

/**
 * Created by wanglin  on 2017/7/28 09:03.
 */

public class GroupTaskListAdapter extends BaseAdapter<TaskAllInfoWrapper.TaskAllInfo> {
    public GroupTaskListAdapter(Context context, List<TaskAllInfoWrapper.TaskAllInfo> mList) {
        super(context, mList);
    }

    @Override
    protected void convert(BaseViewHolder holder, int position) {
        final TaskAllInfoWrapper.TaskAllInfo taskInfo = mList.get(position);

        holder.setText(R.id.m_tv_task_time, taskInfo.getAdd_date() + " " + taskInfo.getAdd_week() + " " + taskInfo.getAdd_time());
        int type = taskInfo.getType();
        LinearLayout view = holder.getView(R.id.m_ll_task_extra);
        TextView tvContent = holder.getView(R.id.m_tv_task_content);
        tvContent.setText(taskInfo.getDesp());
        tvContent.setVisibility(TextUtils.isEmpty(taskInfo.getDesp()) ? View.GONE : View.VISIBLE);
        TextView tv = new TextView(mContext);
        tv.setTextSize(16);
        tv.setTextColor(mContext.getResources().getColor(R.color.black_333333));
        view.removeAllViews();
        switch (type) {
            case GroupConstant.TASK_TYPE_CHARACTER://纯文本
                holder.setImageResource(R.id.m_iv_task_picture, R.mipmap.group36);

                break;
            case GroupConstant.TASK_TYPE_PICTURE:

                holder.setImageResource(R.id.m_iv_task_picture, R.mipmap.group40);

                tv.setText("[图片]");
                view.addView(tv);
                break;
            case GroupConstant.TASK_TYPE_VOICE:

                holder.setImageResource(R.id.m_iv_task_picture, R.mipmap.group38);
                tv.setText("[语音]");
                view.addView(tv);
                break;
            case GroupConstant.TASK_TYPE_WORD:
                holder.setImageResource(R.id.m_iv_task_picture, R.mipmap.group42);
                tv.setText("[文档]");
                view.addView(tv);

                break;
            case GroupConstant.TASK_TYPE_SYNTHESIZE://综合
                holder.setImageResource(R.id.m_iv_task_picture, R.mipmap.group44);
                tvContent.setVisibility(TextUtils.isEmpty(taskInfo.getDesp()) ? View.GONE : View.VISIBLE);
                LinearLayout.MarginLayoutParams layoutParams = (LinearLayout.MarginLayoutParams) view.getLayoutParams();
                layoutParams.width = LinearLayout.LayoutParams.WRAP_CONTENT;
                layoutParams.height = LinearLayout.LayoutParams.WRAP_CONTENT;
                if (taskInfo.getBody().getImgs() != null && taskInfo.getBody().getImgs().size() > 0) {
                    TextView tv1 = getTextView();
                    layoutParams.rightMargin = 25;
                    tv1.setLayoutParams(layoutParams);
                    tv1.setText("[图片]");
                    view.addView(tv1);
                }

                if (taskInfo.getBody().getDocs() != null && taskInfo.getBody().getDocs().size() > 0) {
                    TextView tv1 = getTextView();
                    layoutParams.rightMargin = 25;
                    tv1.setLayoutParams(layoutParams);
                    tv1.setText("[文档]");
                    view.addView(tv1);
                }
                if (taskInfo.getBody().getVoices() != null && taskInfo.getBody().getVoices().size() > 0) {
                    TextView tv1 = getTextView();
                    layoutParams.rightMargin = 25;
                    tv1.setLayoutParams(layoutParams);
                    tv1.setText("[语音]");
                    view.addView(tv1);
                }

                break;
        }

        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(mContext, GroupTaskFinishAndUnfinshActivity.class);
                intent.putExtra("taskId", taskInfo.getTask_id());
                intent.putExtra("classId", taskInfo.getClass_id());
                intent.putExtra("masterId", taskInfo.getPublisher());
                mContext.startActivity(intent);
            }
        });


    }

    @Override
    public int getLayoutID(int viewType) {
        return R.layout.group_publish_task_item;
    }


    private TextView getTextView() {
        TextView tv = new TextView(mContext);
        tv.setTextSize(16);
        tv.setTextColor(mContext.getResources().getColor(R.color.black_333333));
        return tv;
    }

}
