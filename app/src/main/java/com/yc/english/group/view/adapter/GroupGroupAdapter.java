package com.yc.english.group.view.adapter;

import android.content.Context;
import android.graphics.BitmapFactory;
import android.view.View;

import com.blankj.utilcode.util.ImageUtils;
import com.example.comm_recyclviewadapter.BaseAdapter;
import com.example.comm_recyclviewadapter.BaseViewHolder;
import com.yc.english.R;
import com.yc.english.group.model.bean.ClassInfo;

import java.util.List;

import io.rong.imkit.RongIM;

/**
 * Created by wanglin  on 2017/7/29 10:11.
 */

public class GroupGroupAdapter extends BaseAdapter<ClassInfo> {
    public GroupGroupAdapter(Context context, List<ClassInfo> mList) {
        super(context, mList);
    }

    @Override
    protected void convert(BaseViewHolder holder, int position) {
        final ClassInfo classInfo = mList.get(position);
        holder.setImageBitmap(R.id.m_iv_group_img, ImageUtils.toRound(BitmapFactory.decodeResource(mContext.getResources(), R.mipmap.portial)));
        holder.setText(R.id.m_tv_group_name, classInfo.getClassName());
        holder.setText(R.id.m_tv_member_count, String.format(mContext.getString(R.string.member_count), classInfo.getCount()));
        holder.setText(R.id.m_tv_group_number, String.format(mContext.getString(R.string.groupId), classInfo.getGroupId()));

        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                RongIM.getInstance().startGroupChat(mContext, classInfo.getGroupId() + "", classInfo.getClassName());
            }
        });

    }

    @Override
    public int getLayoutID(int viewType) {
        return R.layout.group_class_item;
    }
}
