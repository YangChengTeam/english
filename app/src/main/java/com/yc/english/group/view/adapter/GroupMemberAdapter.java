package com.yc.english.group.view.adapter;

import android.content.Context;
import android.graphics.BitmapFactory;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.blankj.subutil.util.PinyinUtils;
import com.blankj.utilcode.util.ImageUtils;
import com.blankj.utilcode.util.LogUtils;
import com.example.comm_recyclviewadapter.BaseAdapter;
import com.example.comm_recyclviewadapter.BaseViewHolder;
import com.yc.english.R;
import com.yc.english.group.model.bean.GroupMemberInfo;

import java.util.List;

import butterknife.BindView;

/**
 * Created by wanglin  on 2017/7/26 15:01.
 */

public class GroupMemberAdapter extends BaseAdapter<GroupMemberInfo> {
    private static final String TAG = "GroupMemberAdapter";

    @BindView(R.id.view_divider)
    View viewDivider;

    public GroupMemberAdapter(Context context, List<GroupMemberInfo> mList) {
        super(context, mList);
    }

    @Override
    protected void convert(BaseViewHolder holder, int position) {
        GroupMemberInfo groupMemberInfo = mList.get(position);
        holder.setText(R.id.tv_member_name, groupMemberInfo.getName());
//        holder.setImageUrl(mContext,R.id.iv_member_img,groupMemberInfo.getImgUrl());
        holder.setImageBitmap(R.id.iv_member_img, ImageUtils.toRound(BitmapFactory.decodeResource(mContext.getResources(), R.mipmap.portial)));
        holder.setText(R.id.tv_member_owner, groupMemberInfo.isGroupOwner() ? "老师" : "");
        String firstLetter = PinyinUtils.getSurnameFirstLetter(groupMemberInfo.getName());

        holder.setText(R.id.tv_first_word, firstLetter.toUpperCase());
        if (isSameMember(position)) {
            holder.getView(R.id.tv_first_word).setVisibility(View.GONE);
            holder.getView(R.id.view_divider).setVisibility(View.VISIBLE);
            holder.getView(R.id.view_divider_last).setVisibility(View.GONE);
            holder.getView(R.id.view_divider_first).setVisibility(View.GONE);
        } else {
            holder.getView(R.id.tv_first_word).setVisibility(View.VISIBLE);
            holder.getView(R.id.view_divider).setVisibility(View.GONE);
            holder.getView(R.id.view_divider_last).setVisibility(View.VISIBLE);
            holder.getView(R.id.view_divider_first).setVisibility(View.VISIBLE);
        }
//        holder.getView(R.id.tv_first_word).setVisibility(position == 0 ? View.GONE : View.VISIBLE);
        LogUtils.e(TAG, firstLetter + "----" + firstLetter.toUpperCase());

    }

    @Override
    public int getLayoutID(int viewType) {
        return R.layout.group_member_item;
    }

    public boolean isSameMember(int postion) {
        if (postion == 0) return true;
        GroupMemberInfo preMem = mList.get(postion - 1);
        GroupMemberInfo curMem = mList.get(postion);
        String preName = PinyinUtils.getSurnameFirstLetter(preMem.getName());
        String curName = PinyinUtils.getSurnameFirstLetter(curMem.getName());

        if (preName.equals(curName)) {
            return true;
        }
        return false;
    }

}
