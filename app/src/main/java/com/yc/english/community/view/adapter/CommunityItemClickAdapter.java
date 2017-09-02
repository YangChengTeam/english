package com.yc.english.community.view.adapter;

import android.content.Context;
import android.content.Intent;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.ImageView;

import com.blankj.utilcode.util.StringUtils;
import com.blankj.utilcode.util.TimeUtils;
import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;
import com.yc.english.R;
import com.yc.english.base.helper.GlideHelper;
import com.yc.english.community.model.domain.CommunityInfo;
import com.yc.english.community.view.activitys.CommunityImageShowActivity;

import java.io.Serializable;
import java.util.List;

public class CommunityItemClickAdapter extends BaseQuickAdapter<CommunityInfo, BaseViewHolder> {

    private Context mContext;

    public CommunityItemClickAdapter(Context context, List<CommunityInfo> datas) {
        super(R.layout.community_note_list_item, datas);
        this.mContext = context;
    }

    @Override
    protected void convert(final BaseViewHolder helper, final CommunityInfo item) {
        helper.setText(R.id.tv_note_title, item.getContent())
                .setText(R.id.tv_note_user_name, item.getUserName())
                .setText(R.id.tv_comment_count, item.getFollowCount())
                .setText(R.id.tv_praise_count, item.getAgreeCount());

        if (!StringUtils.isEmpty(item.getAddTime())) {
            long addTime = Long.parseLong(item.getAddTime()) * 1000;
            helper.setText(R.id.tv_note_date, TimeUtils.millis2String(addTime));
        }
        GlideHelper.circleImageView(mContext, (ImageView) helper.getConvertView().findViewById(R.id.iv_note_user_img), item.getFace(), R.mipmap.main_tab_my);

        helper.addOnClickListener(R.id.comment_layout).addOnClickListener(R.id.praise_count_layout);

        CommunityImageAdapter communityImageAdapter = new CommunityImageAdapter(mContext, item.getImages());
        RecyclerView imagesRecyclerView = (RecyclerView) helper.getConvertView().findViewById(R.id.imgs_list);
        imagesRecyclerView.setLayoutManager(new GridLayoutManager(mContext, 3));
        imagesRecyclerView.setAdapter(communityImageAdapter);

        communityImageAdapter.setOnItemClickListener(new OnItemClickListener() {
            @Override
            public void onItemClick(BaseQuickAdapter adapter, View view, int position) {
                Intent intent = new Intent(mContext, CommunityImageShowActivity.class);
                intent.putExtra("images", (Serializable) item.getImages());
                mContext.startActivity(intent);
            }
        });
    }
}