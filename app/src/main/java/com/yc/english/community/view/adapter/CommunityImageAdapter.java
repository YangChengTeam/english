package com.yc.english.community.view.adapter;

import android.content.Context;
import android.widget.ImageView;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;
import com.yc.english.R;
import com.yc.english.base.helper.GlideHelper;

import java.util.List;

public class CommunityImageAdapter extends BaseQuickAdapter<String, BaseViewHolder> {

    private Context mContext;

    public CommunityImageAdapter(Context context, List<String> datas) {
        super(R.layout.community_image_item, datas);
        this.mContext = context;
    }

    @Override
    protected void convert(final BaseViewHolder helper, final String imgUrl) {
        //Glide.with(mContext).load(imgUrl).into((ImageView) helper.getConvertView().findViewById(R.id.iv_community_note));
        GlideHelper.imageView(mContext, (ImageView) helper.getConvertView().findViewById(R.id.iv_community_note), imgUrl, 0);
    }
}