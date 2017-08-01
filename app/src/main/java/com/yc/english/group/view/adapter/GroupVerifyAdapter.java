package com.yc.english.group.view.adapter;

import android.content.Context;
import android.view.View;

import com.example.comm_recyclviewadapter.BaseAdapter;
import com.example.comm_recyclviewadapter.BaseViewHolder;
import com.yc.english.R;
import com.yc.english.group.model.bean.GroupMemberInfo;

import java.util.List;

/**
 * Created by wanglin  on 2017/7/29 12:01.
 */

public class GroupVerifyAdapter extends BaseAdapter<GroupMemberInfo> {
    public GroupVerifyAdapter(Context context, List<GroupMemberInfo> mList) {
        super(context, mList);
    }

    @Override
    protected void convert(BaseViewHolder holder, int position) {
        GroupMemberInfo memberInfo = mList.get(position);
        holder.setText(R.id.m_tv_join_name, memberInfo.getName());
        holder.setText(R.id.m_tv_join_class, String.format(mContext.getString(R.string.apply_join), memberInfo.getGroupName()));
        if (position == mList.size() - 1) {
            holder.setVisible(R.id.view_divider, false);
        }else {
            holder.setVisible(R.id.view_divider, true);
        }

        holder.setOnClickListener(R.id.m_tv_accept, new View.OnClickListener() {
            @Override
            public void onClick(View v) {

            }
        });

    }

    @Override
    public int getLayoutID(int viewType) {
        return R.layout.group_verify_item;
    }
}
