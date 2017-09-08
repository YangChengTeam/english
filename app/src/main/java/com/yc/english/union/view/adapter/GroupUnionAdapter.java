package com.yc.english.union.view.adapter;

import android.widget.ImageView;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;
import com.yc.english.R;
import com.yc.english.base.helper.GlideHelper;
import com.yc.english.group.model.bean.ClassInfo;

import java.util.List;

/**
 * Created by wanglin  on 2017/7/29 10:11.
 */

public class GroupUnionAdapter extends BaseQuickAdapter<ClassInfo, BaseViewHolder> {
    private static final String TAG = "GroupUnionAdapter";


    public GroupUnionAdapter(List<ClassInfo> data) {
        super(R.layout.group_class_item, data);
    }


    @Override
    protected void convert(final BaseViewHolder helper, final ClassInfo classInfo) {
        helper.setText(R.id.m_tv_group_name, classInfo.getClassName())
                .setText(R.id.m_tv_member_count, String.format(mContext.getString(R.string.member_count), Integer.parseInt(classInfo.getCount())))
                .setText(R.id.m_tv_group_number, String.format(mContext.getString(R.string.groupId), classInfo.getGroupId()));
        GlideHelper.circleImageView(mContext, (ImageView) helper.getView(R.id.m_iv_group_img), classInfo.getImageUrl(), R.mipmap.default_avatar);
        helper.setVisible(R.id.m_iv_pay_money, classInfo.getFee_type() == 1);

    }


}
