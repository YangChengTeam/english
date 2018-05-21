package com.yc.junior.english.group.view.adapter;


import android.widget.ImageView;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;
import com.yc.junior.english.R;
import com.yc.junior.english.base.helper.GlideHelper;
import com.yc.junior.english.group.model.bean.StudentInfo;

import java.util.List;


/**
 * Created by wanglin  on 2017/8/29 17:16.
 * 已经禁言的学生
 */

public class GroupForbidTalkAdapter extends BaseQuickAdapter<StudentInfo, BaseViewHolder> {


    public GroupForbidTalkAdapter(List<StudentInfo> data) {
        super(R.layout.group_forbid_member_item, data);
    }


    @Override
    protected void convert(BaseViewHolder holder, final StudentInfo studentInfo) {
        GlideHelper.circleImageView(mContext, (ImageView) holder.getView(R.id.m_iv_forbid), studentInfo.getFace(), R.mipmap.default_avatar);
        String str = mContext.getString(R.string.forbid_time);

        holder.setText(R.id.m_tv_forbid_time, String.format(str, studentInfo.getForbidTime()))
                .setText(R.id.m_tv_forbid_name, studentInfo.getNick_name())
                .addOnClickListener(R.id.m_tv_stop_forbid);
    }
}
