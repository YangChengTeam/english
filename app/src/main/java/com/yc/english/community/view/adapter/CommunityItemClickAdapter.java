package com.yc.english.community.view.adapter;

import android.content.Context;
import android.widget.ImageView;

import com.chad.library.adapter.base.BaseMultiItemQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;
import com.yc.english.R;
import com.yc.english.base.helper.GlideHelper;
import com.yc.english.community.model.domain.CommunityInfo;
import com.yc.english.read.model.domain.BookInfo;

import java.util.List;


public class CommunityItemClickAdapter extends BaseMultiItemQuickAdapter<CommunityInfo, BaseViewHolder> {

    private Context mContext;

    public CommunityItemClickAdapter(Context mContext, List<CommunityInfo> data) {
        super(data);
        this.mContext = mContext;
        addItemType(BookInfo.CLICK_ITEM_VIEW, R.layout.community_note_list_item);
    }

    @Override
    protected void convert(final BaseViewHolder helper, final CommunityInfo item) {
        helper.setText(R.id.tv_note_title, item.getCommunityNoteTitle());
        GlideHelper.circleImageView(mContext,(ImageView) helper.getConvertView().findViewById(R.id.iv_note_user_img),null,R.mipmap.main_tab_my);
    }
}