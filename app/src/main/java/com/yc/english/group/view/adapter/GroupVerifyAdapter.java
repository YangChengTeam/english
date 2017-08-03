package com.yc.english.group.view.adapter;

import android.content.Context;
import android.view.View;

import com.example.comm_recyclviewadapter.BaseAdapter;
import com.example.comm_recyclviewadapter.BaseViewHolder;
import com.jakewharton.rxbinding.view.RxView;
import com.yc.english.R;
import com.yc.english.base.view.IFinish;
import com.yc.english.group.listener.OnItemClickListener;
import com.yc.english.group.model.bean.StudentInfo;

import java.util.List;
import java.util.concurrent.TimeUnit;

import rx.Observer;
import rx.functions.Action1;
import rx.functions.Func1;

/**
 * Created by wanglin  on 2017/7/29 12:01.
 */

public class GroupVerifyAdapter extends BaseAdapter<StudentInfo> {
    public GroupVerifyAdapter(Context context, List<StudentInfo> mList) {
        super(context, mList);
    }

    @Override
    protected void convert(final BaseViewHolder holder, final int position) {
        final StudentInfo studentInfo = mList.get(position);
        holder.setText(R.id.m_tv_join_name, studentInfo.getNick_name());
        holder.setText(R.id.m_tv_join_class, String.format(mContext.getString(R.string.apply_join), studentInfo.getClass_name()));
        if (position == mList.size() - 1) {
            holder.setVisible(R.id.view_divider, false);
        } else {
            holder.setVisible(R.id.view_divider, true);
        }

        holder.setOnClickListener(R.id.m_tv_accept, new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (onItemClickListener != null) {
                    onItemClickListener.onItemClick(holder, position, studentInfo);
                }
            }
        });

    }

    @Override
    public int getLayoutID(int viewType) {
        return R.layout.group_verify_item;
    }

    private OnItemClickListener<StudentInfo> onItemClickListener;

    public void setOnItemClickListener(OnItemClickListener<StudentInfo> onItemClickListener) {
        this.onItemClickListener = onItemClickListener;
    }
}
