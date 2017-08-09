package com.yc.english.group.view.adapter;

import android.content.Context;
import android.graphics.BitmapFactory;
import android.view.View;
import android.widget.ImageView;

import com.blankj.subutil.util.PinyinUtils;
import com.blankj.utilcode.util.ImageUtils;
import com.blankj.utilcode.util.LogUtils;
import com.blankj.utilcode.util.RegexUtils;
import com.example.comm_recyclviewadapter.BaseAdapter;
import com.example.comm_recyclviewadapter.BaseViewHolder;
import com.yc.english.R;
import com.yc.english.base.helper.GlideHelper;
import com.yc.english.group.model.bean.StudentInfo;
import com.yc.english.main.hepler.UserInfoHelper;

import java.util.List;

import butterknife.BindView;

/**
 * Created by wanglin  on 2017/7/26 15:01.
 */

public class GroupMemberAdapter extends BaseAdapter<StudentInfo> {
    private static final String TAG = "GroupMemberAdapter";

    @BindView(R.id.view_divider)
    View viewDivider;

    public GroupMemberAdapter(Context context, List<StudentInfo> mList) {
        super(context, mList);
    }

    @Override
    protected void convert(BaseViewHolder holder, int position) {
        StudentInfo studentInfo = mList.get(position);
        String userName = studentInfo.getNick_name();
        holder.setText(R.id.tv_member_name, userName);
        GlideHelper.circleImageView(mContext, (ImageView) holder.getView(R.id.iv_member_img),studentInfo.getFace(),R.mipmap.default_big_avatar);
        holder.setText(R.id.tv_member_owner, studentInfo.getUser_id().equals(UserInfoHelper.getUserInfo().getUid()) ? "老师" : "");

        if (RegexUtils.isZh(userName)) {

            String firstLetter = PinyinUtils.getSurnameFirstLetter(userName);

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

            LogUtils.e(TAG, firstLetter + "----" + firstLetter.toUpperCase());
        } else {
            holder.setText(R.id.tv_first_word, "#");
            if (position == 1) {
                holder.getView(R.id.tv_first_word).setVisibility(View.VISIBLE);
            } else {
                holder.getView(R.id.tv_first_word).setVisibility(View.GONE);
            }
        }
    }

    @Override
    public int getLayoutID(int viewType) {
        return R.layout.group_member_item;
    }

    private boolean isSameMember(int postion) {
        if (postion == 0) return true;
        StudentInfo preMem = mList.get(postion - 1);
        StudentInfo curMem = mList.get(postion);
        String preName = PinyinUtils.getSurnameFirstLetter(preMem.getNick_name());
        String curName = PinyinUtils.getSurnameFirstLetter(curMem.getNick_name());

        if (preName.equals(curName)) {
            return true;
        }
        return false;
    }

}
