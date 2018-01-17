package com.yc.junior.english.community.view.adapter;

import android.content.Context;
import android.net.Uri;
import android.widget.ImageView;

import com.bumptech.glide.Glide;
import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;
import com.yc.junior.english.R;
import com.yc.junior.english.base.utils.DrawableUtils;

import java.util.List;

public class ImageSelectedAdapter extends BaseQuickAdapter<Uri, BaseViewHolder> {

    private Context mContext;

    public ImageSelectedAdapter(Context context, List<Uri> datas) {
        super(R.layout.community_image_seelected_item, datas);
        this.mContext = context;
    }

    @Override
    protected void convert(final BaseViewHolder helper, final Uri imgUrl) {
        Glide.with(mContext).load(imgUrl).into((ImageView) helper.getConvertView().findViewById(R.id.iv_community_add_note));
        if (helper.getAdapterPosition() == 0 && DrawableUtils.getAddUri(mContext).compareTo(imgUrl) == 0) {
            helper.setVisible(R.id.iv_delete_image, false);
        }else{
            helper.setVisible(R.id.iv_delete_image, true);
        }
        helper.setBackgroundRes(R.id.iv_community_add_note,R.color.transparent);
        helper.addOnClickListener(R.id.iv_delete_image);
    }
}