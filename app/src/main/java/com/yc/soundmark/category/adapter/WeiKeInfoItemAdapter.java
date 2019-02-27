package com.yc.soundmark.category.adapter;

import android.text.Html;
import android.widget.ImageView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.bumptech.glide.request.RequestOptions;
import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;
import com.yc.english.R;
import com.yc.soundmark.category.model.domain.WeiKeCategory;

import java.util.List;

/**
 * Created by zhangkai on 2017/8/30.
 */

public class WeiKeInfoItemAdapter extends BaseQuickAdapter<WeiKeCategory, BaseViewHolder> {
    private String mType;

    public WeiKeInfoItemAdapter(List<WeiKeCategory> data, String type) {
        super(R.layout.weike_info_item, data);
        this.mType = type;
    }

    @Override
    protected void convert(BaseViewHolder helper, WeiKeCategory item) {

        helper.setText(R.id.tv_title, item.getTitle())
                .setText(R.id.tv_unit_count, Html.fromHtml("<font color='#FB4C30'>" + item.getUserNum() + "</font>人已购买"));
        Glide.with(mContext).asBitmap().load(item.getImg()).apply(new RequestOptions().diskCacheStrategy(DiskCacheStrategy.DATA)).into((ImageView) helper.getView(R.id.iv_icon));


        if (item.getIsVip() == 0) {
            helper.setVisible(R.id.iv_is_vip, false);
        } else {
            helper.setVisible(R.id.iv_is_vip, true);
        }


    }
}
