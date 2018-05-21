package com.yc.junior.english.group.view.adapter;

import android.widget.ImageView;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;
import com.yc.junior.english.R;
import com.yc.junior.english.base.helper.GlideHelper;
import com.yc.junior.english.group.model.bean.StudentInfo;

import java.util.List;

/**
 * Created by wanglin  on 2017/7/29 12:01.
 */

public class GroupVerifyAdapter extends BaseQuickAdapter<StudentInfo, BaseViewHolder> {


    public GroupVerifyAdapter(List<StudentInfo> data) {
        super(R.layout.group_verify_item, data);
    }

    @Override
    protected void convert(BaseViewHolder holder, StudentInfo studentInfo) {

        holder.setText(R.id.m_tv_join_name, studentInfo.getNick_name())
                .setText(R.id.m_tv_join_class, String.format(mContext.getString(R.string.apply_join), studentInfo.getClass_name()))
                .addOnClickListener(R.id.m_tv_accept);

        GlideHelper.circleImageView(mContext, (ImageView) holder.getView(R.id.m_iv_join_img), studentInfo.getFace(), R.mipmap.default_big_avatar);
        int position = holder.getLayoutPosition() - getHeaderLayoutCount();
        if (position == getData().size() - 1) {
            holder.setVisible(R.id.view_divider, false);
        } else {
            holder.setVisible(R.id.view_divider, true);
        }

    }


}
