package com.yc.junior.english.group.view.adapter;

import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;

import com.blankj.utilcode.util.ConvertUtils;
import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;
import com.yc.junior.english.R;
import com.yc.junior.english.base.helper.GlideHelper;
import com.yc.junior.english.group.model.bean.GroupInfoHelper;
import com.yc.junior.english.group.model.bean.StudentInfo;
import com.yc.junior.english.main.hepler.UserInfoHelper;

import java.util.List;

/**
 * Created by wanglin  on 2017/7/27 08:55.
 */

public class GroupDeleteAdapter extends BaseQuickAdapter<StudentInfo, BaseViewHolder> {

    public GroupDeleteAdapter(List<StudentInfo> data) {
        super(R.layout.group_delete_member_item, data);
    }

    @Override
    protected void convert(BaseViewHolder holder, final StudentInfo studentInfo) {
        final ImageView view = holder.getView(R.id.iv_delete_select);
        int position = holder.getLayoutPosition() - getHeaderLayoutCount();
        LinearLayout.LayoutParams layoutParams = (LinearLayout.LayoutParams) holder.getView(R.id.ll_container).getLayoutParams();
        if (position == 0) {
            layoutParams.bottomMargin = ConvertUtils.dp2px(10);
            view.setVisibility(View.GONE);
        } else {
            layoutParams.bottomMargin = 0;
            view.setVisibility(View.VISIBLE);
        }
        holder.itemView.setLayoutParams(layoutParams);
        view.setTag(studentInfo.getIsSelected());

        GlideHelper.circleImageView(mContext, (ImageView) holder.getView(R.id.iv_member_img), studentInfo.getFace(), R.mipmap.default_avatar);
        holder.setText(R.id.tv_member_name, studentInfo.getNick_name());
        if (GroupInfoHelper.getClassInfo().getType().equals("1")) {
            holder.setText(R.id.tv_member_owner, studentInfo.getUser_id().equals(UserInfoHelper.getUserInfo().getUid()) ? "会主" : "");
        } else {
            holder.setText(R.id.tv_member_owner, studentInfo.getUser_id().equals(UserInfoHelper.getUserInfo().getUid()) ? "老师" : "");
        }
    }


}
