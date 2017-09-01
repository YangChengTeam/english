package com.yc.english.group.view.adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.blankj.utilcode.util.SPUtils;
import com.yc.english.R;
import com.yc.english.base.helper.GlideHelper;
import com.yc.english.base.helper.TipsHelper;
import com.yc.english.group.listener.OnCheckedChangeListener;
import com.yc.english.group.model.bean.GroupInfoHelper;
import com.yc.english.group.model.bean.StudentInfo;

import butterknife.BindView;
import butterknife.ButterKnife;
import me.yokeyword.indexablerv.IndexableAdapter;

/**
 * Created by wanglin  on 2017/8/29 10:23.
 */

public class GroupForbidMemberAdapter extends IndexableAdapter<StudentInfo> {


    private LayoutInflater mInflater;
    private Context mContext;

    public GroupForbidMemberAdapter(Context context) {
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
        View view = mInflater.inflate(R.layout.group_delete_member_item, parent, false);
        return new MemberContentVH(view);
    }

    @Override
    public void onBindTitleViewHolder(RecyclerView.ViewHolder holder, String indexTitle) {
        MemberTitleVH memberTitleVH = (MemberTitleVH) holder;
        memberTitleVH.tvFirstWord.setText(indexTitle);
    }

    @Override
    public void onBindContentViewHolder(RecyclerView.ViewHolder holder, final StudentInfo entity) {
        final MemberContentVH memberContentVH = (MemberContentVH) holder;
        memberContentVH.viewTopDivider.setVisibility(View.GONE);
        GlideHelper.circleImageView(mContext, memberContentVH.ivMemberImg, entity.getFace(), R.mipmap.default_avatar);
        memberContentVH.tvMemberName.setText(entity.getNick_name());
        memberContentVH.tvMemberOwner.setVisibility(View.GONE);
        memberContentVH.viewDivider.setVisibility(View.VISIBLE);

        memberContentVH.ivDeleteSelect.setImageDrawable(entity.getIsForbid() ? mContext.getResources().getDrawable(R.mipmap.group24) : mContext.getResources().getDrawable(R.mipmap.group23));

        memberContentVH.ivDeleteSelect.setTag(entity.getIsForbid());

        if (!entity.getIsForbid()) {
            memberContentVH.llContainer.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {

                    if (onCheckedChangeListener != null) {
                        boolean flag = !(boolean) memberContentVH.ivDeleteSelect.getTag();
                        onCheckedChangeListener.onClick(memberContentVH.ivDeleteSelect, flag, entity);
                        memberContentVH.ivDeleteSelect.setTag(flag);
                    }
                }
            });
        }
    }

    public class MemberTitleVH extends RecyclerView.ViewHolder {
        @BindView(R.id.tv_first_word)
        TextView tvFirstWord;

        public MemberTitleVH(View itemView) {
            super(itemView);
            ButterKnife.bind(this, itemView);
        }
    }

    public class MemberContentVH extends RecyclerView.ViewHolder {

        @BindView(R.id.view_top_divider)
        View viewTopDivider;
        @BindView(R.id.iv_delete_select)
        ImageView ivDeleteSelect;
        @BindView(R.id.iv_member_img)
        ImageView ivMemberImg;
        @BindView(R.id.tv_member_name)
        TextView tvMemberName;
        @BindView(R.id.tv_member_owner)
        TextView tvMemberOwner;
        @BindView(R.id.ll_container)
        LinearLayout llContainer;
        @BindView(R.id.view_divider)
        View viewDivider;

        public MemberContentVH(View itemView) {
            super(itemView);
            ButterKnife.bind(this, itemView);
        }
    }

    private OnCheckedChangeListener<StudentInfo> onCheckedChangeListener;

    public void setOnCheckedChangeListener(OnCheckedChangeListener<StudentInfo> onCheckedChangeListener) {
        this.onCheckedChangeListener = onCheckedChangeListener;
    }
}
