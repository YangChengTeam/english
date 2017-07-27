package com.yc.english.group.view.adapter;

import android.content.Context;
import android.graphics.BitmapFactory;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;

import com.blankj.utilcode.util.ConvertUtils;
import com.blankj.utilcode.util.ImageUtils;
import com.example.comm_recyclviewadapter.BaseAdapter;
import com.example.comm_recyclviewadapter.BaseViewHolder;
import com.yc.english.R;
import com.yc.english.group.model.bean.GroupMemberInfo;

import java.util.List;

/**
 * Created by wanglin  on 2017/7/27 08:55.
 */

public class GroupDeleteAdapter extends BaseAdapter<GroupMemberInfo> {
    public GroupDeleteAdapter(Context context, List<GroupMemberInfo> mList) {
        super(context, mList);
    }

    @Override
    protected void convert(BaseViewHolder holder, int position) {
        GroupMemberInfo memberInfo = mList.get(position);
        if (position == 0) {
            LinearLayout.LayoutParams layoutParams = (LinearLayout.LayoutParams) holder.itemView.getLayoutParams();
            layoutParams.bottomMargin = ConvertUtils.dp2px(10);
            holder.itemView.setLayoutParams(layoutParams);
            holder.getView(R.id.ib_delete_select).setVisibility(View.GONE);
        } else {
            holder.getView(R.id.ib_delete_select).setVisibility(View.VISIBLE);
        }
        holder.setImageBitmap(R.id.iv_member_img, ImageUtils.toRound(BitmapFactory.decodeResource(mContext.getResources(), R.mipmap.portial)));
        holder.setText(R.id.tv_member_name, memberInfo.getName());
        holder.setText(R.id.tv_member_owner, memberInfo.isGroupOwner() ? "群主" : "");

    }

    @Override
    public int getLayoutID(int viewType) {
        return R.layout.group_delete_member_item;
    }
}
