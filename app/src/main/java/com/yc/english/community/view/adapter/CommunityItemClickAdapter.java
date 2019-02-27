package com.yc.english.community.view.adapter;

import android.content.Context;
import android.content.Intent;
import android.graphics.drawable.Drawable;
import android.support.v4.content.ContextCompat;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.TextView;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;
import com.yc.english.R;
import com.yc.english.base.helper.GlideHelper;
import com.yc.english.community.model.domain.CommunityInfo;
import com.yc.english.community.view.activitys.CommunityImageShowActivity;

import java.io.Serializable;
import java.net.URLDecoder;
import java.util.List;

import yc.com.blankj.utilcode.util.SizeUtils;
import yc.com.blankj.utilcode.util.StringUtils;
import yc.com.blankj.utilcode.util.TimeUtils;

public class CommunityItemClickAdapter extends BaseQuickAdapter<CommunityInfo, BaseViewHolder> {

    private Context mContext;
    private boolean isShowDelete;

    public interface PraiseListener{
        void praiseItem(int parentPosition);
    }

    public PraiseListener praiseListener;

    public void setPraiseListener(PraiseListener praiseListener) {
        this.praiseListener = praiseListener;
    }

    public CommunityItemClickAdapter(Context context, List<CommunityInfo> datas, boolean isDelete) {
        super(R.layout.community_note_list_item, datas);
        this.mContext = context;
        this.isShowDelete = isDelete;
    }

    @Override
    protected void convert(final BaseViewHolder helper, final CommunityInfo item) {
        try {
            helper.setText(R.id.tv_note_title, URLDecoder.decode(item.getContent(), "UTF-8"))
                    .setText(R.id.tv_note_user_name, item.getUserName())
                    .setText(R.id.tv_comment_count, item.getFollowCount())
                    .setText(R.id.tv_praise_count, item.getAgreeCount());
        } catch (Exception e) {
            e.printStackTrace();
        }

        //第一条设计距离头部的距离
        if (helper.getAdapterPosition() == 0) {
            FrameLayout.LayoutParams layoutParams = new FrameLayout.LayoutParams(FrameLayout.LayoutParams.MATCH_PARENT, FrameLayout.LayoutParams.WRAP_CONTENT);
            layoutParams.setMargins(SizeUtils.dp2px(6), SizeUtils.dp2px(6), SizeUtils.dp2px(6), SizeUtils.dp2px(6));
            helper.getConvertView().setLayoutParams(layoutParams);
        }

        if (!StringUtils.isEmpty(item.getAddTime())) {
            long addTime = Long.parseLong(item.getAddTime()) * 1000;
            helper.setText(R.id.tv_note_date, TimeUtils.millis2String(addTime));
        }

        if (isShowDelete) {
            helper.setVisible(R.id.line_delete, true);
            helper.setVisible(R.id.delete_layout, true);
        } else {
            helper.setVisible(R.id.line_delete, false);
            helper.setVisible(R.id.delete_layout, false);
        }

        GlideHelper.circleImageView(mContext, (ImageView) helper.getConvertView().findViewById(R.id.iv_note_user_img), item.getFace(), R.mipmap.main_tab_my);
        helper.addOnClickListener(R.id.tv_comment_count).addOnClickListener(R.id.iv_delete);

        helper.getConvertView().findViewById(R.id.tv_praise_count).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                praiseListener.praiseItem(helper.getAdapterPosition());
            }
        });

        CommunityImageAdapter communityImageAdapter = new CommunityImageAdapter(mContext, item.getImages());
        RecyclerView imagesRecyclerView = (RecyclerView) helper.getConvertView().findViewById(R.id.imgs_list);
        imagesRecyclerView.setLayoutManager(new GridLayoutManager(mContext, 3));
        imagesRecyclerView.setAdapter(communityImageAdapter);

        TextView praiseTextView = (TextView) helper.getConvertView().findViewById(R.id.tv_praise_count);

        if (item.getAgreed().equals("1")) {
            Drawable isZan = mContext.getResources().getDrawable(R.mipmap.is_zan_icon);
            isZan.setBounds(0, 0, isZan.getMinimumWidth(), isZan.getMinimumHeight());
            praiseTextView.setCompoundDrawables(isZan, null, null, null);
        } else {
            Drawable isZan = mContext.getResources().getDrawable(R.mipmap.no_zan_icon);
            isZan.setBounds(0, 0, isZan.getMinimumWidth(), isZan.getMinimumHeight());
            praiseTextView.setCompoundDrawables(isZan, null, null, null);
        }

        communityImageAdapter.setOnItemClickListener(new OnItemClickListener() {
            @Override
            public void onItemClick(BaseQuickAdapter adapter, View view, int position) {
                Intent intent = new Intent(mContext, CommunityImageShowActivity.class);
                intent.putExtra("current_position", position);
                intent.putExtra("images", (Serializable) item.getImages());
                mContext.startActivity(intent);
            }
        });
    }

    public void changeView(View cView, int pos) {
        TextView praiseTextView = (TextView) cView.findViewById(R.id.tv_praise_count);
        String isPraise = this.getData().get(pos).getAgreed();
        if (isPraise.equals("1")) {
            Drawable isZan = ContextCompat.getDrawable(mContext, R.mipmap.is_zan_icon);
            isZan.setBounds(0, 0, isZan.getMinimumWidth(), isZan.getMinimumHeight());
            praiseTextView.setCompoundDrawables(isZan, null, null, null);
            praiseTextView.setText(this.getData().get(pos).getAgreeCount());
        } else {
            Drawable isZan = ContextCompat.getDrawable(mContext, R.mipmap.no_zan_icon);
            isZan.setBounds(0, 0, isZan.getMinimumWidth(), isZan.getMinimumHeight());
            praiseTextView.setCompoundDrawables(isZan, null, null, null);
        }
    }

}