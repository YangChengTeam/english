package com.yc.english.group.view.adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.yc.english.R;
import com.yc.english.base.helper.GlideHelper;
import com.yc.english.group.model.bean.GroupInfoHelper;
import com.yc.english.group.model.bean.StudentInfo;

import butterknife.BindView;
import butterknife.ButterKnife;
import me.yokeyword.indexablerv.IndexableAdapter;

/**
 * Created by wanglin  on 2017/7/26 15:01.
 */

public class GroupMemberAdapter extends IndexableAdapter<StudentInfo> {
    private static final String TAG = "UnionMemberAdapter";


    private LayoutInflater mInflater;
    private Context mContext;

    public GroupMemberAdapter(Context context) {
        mInflater = LayoutInflater.from(context);
        this.mContext = context;
    }


    @Override
    public RecyclerView.ViewHolder onCreateTitleViewHolder(ViewGroup parent) {
        View view = mInflater.inflate(R.layout.group_member_title, parent, false);
        return new MemberTitleVH(view);
    }

    @Override
    public RecyclerView.ViewHolder onCreateContentViewHolder(ViewGroup parent) {
        View view = mInflater.inflate(R.layout.group_member_content, parent, false);
        return new MemberContentVH(view);
    }

    @Override
    public void onBindTitleViewHolder(RecyclerView.ViewHolder holder, String indexTitle) {
        MemberTitleVH memberTitleVH = (MemberTitleVH) holder;
        memberTitleVH.tv.setText(indexTitle);
    }

    @Override
    public void onBindContentViewHolder(RecyclerView.ViewHolder holder, StudentInfo entity) {
        MemberContentVH memberContentVH = (MemberContentVH) holder;
        GlideHelper.circleImageView(mContext, memberContentVH.ivMemberImg, entity.getFace(), R.mipmap.default_avatar);
        memberContentVH.tvMemberName.setText(entity.getNick_name());
        if (GroupInfoHelper.getClassInfo().getType().equals("0")){
            memberContentVH.tvMemberOwner.setText(entity.getUser_id().equals(entity.getMaster_id()) ? "老师" : "");
        }else if (GroupInfoHelper.getClassInfo().getType().equals("1")){
            memberContentVH.tvMemberOwner.setText(entity.getUser_id().equals(entity.getMaster_id()) ? "会主" : "");
        }
        memberContentVH.tvMemberPhone.setText(entity.getUser_name());
        memberContentVH.tvMemberPhone.setVisibility(entity.getUser_name().equals(entity.getNick_name()) ? View.GONE : View.VISIBLE);
    }

    private class MemberTitleVH extends RecyclerView.ViewHolder {
        TextView tv;

        public MemberTitleVH(View itemView) {
            super(itemView);
            tv = (TextView) itemView.findViewById(R.id.tv_first_word);
        }
    }

    public class MemberContentVH extends RecyclerView.ViewHolder {
        @BindView(R.id.iv_member_img)
        ImageView ivMemberImg;
        @BindView(R.id.tv_member_name)
        TextView tvMemberName;
        @BindView(R.id.tv_member_owner)
        TextView tvMemberOwner;
        @BindView(R.id.view_divider)
        View viewDivider;
        @BindView(R.id.tv_member_phone)
        TextView tvMemberPhone;

        public MemberContentVH(View itemView) {
            super(itemView);
            ButterKnife.bind(this, itemView);
        }
    }


}
