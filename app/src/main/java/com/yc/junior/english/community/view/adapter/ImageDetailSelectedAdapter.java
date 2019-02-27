package com.yc.junior.english.community.view.adapter;

import android.content.Context;
import android.widget.ImageView;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;
import com.yc.english.R;
import com.yc.english.base.helper.GlideHelper;

import java.util.List;

public class ImageDetailSelectedAdapter extends BaseQuickAdapter<String, BaseViewHolder> {

    private Context mContext;

    public ImageDetailSelectedAdapter(Context context, List<String> datas) {
        super(R.layout.community_image_seelected_item, datas);
        this.mContext = context;
    }

    @Override
    protected void convert(final BaseViewHolder helper, final String imgUrl) {
        GlideHelper.imageView(mContext, (ImageView) helper.getConvertView().findViewById(R.id.iv_community_add_note), imgUrl, 0);
        helper.setVisible(R.id.iv_delete_image, false);
    }
}