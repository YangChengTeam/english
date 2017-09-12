package com.yc.english.main.view.adapters;

import android.widget.ImageView;

import com.blankj.utilcode.util.TimeUtils;
import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;
import com.yc.english.R;
import com.yc.english.base.helper.GlideHelper;
import com.yc.english.community.model.domain.CommunityInfo;

import java.net.URLDecoder;
import java.text.SimpleDateFormat;
import java.util.List;
import java.util.Locale;

/**
 * Created by zhangkai on 2017/9/6.
 */

public class CommunityAdapter extends BaseQuickAdapter<CommunityInfo, BaseViewHolder> {

    public CommunityAdapter(List<CommunityInfo> data) {
        super(R.layout.index_item_community, data);
    }

    @Override
    protected void convert(BaseViewHolder helper, CommunityInfo item) {
        int position = helper.getAdapterPosition();

        helper.setText(R.id.tv_name, item.getUserName());

        String content = item.getContent();
        try {
            content = URLDecoder.decode(item.getContent(), "UTF-8");
        } catch (Exception e) {
        }
        helper.setText(R.id
                .tv_desc, content);
        long addTime = Long.parseLong(item.getAddTime()) * 1000;
        helper.setText(R.id.tv_time, TimeUtils.millis2String(addTime, new SimpleDateFormat("yyyy-MM-dd " +
                "HH:mm:ss",
                Locale.getDefault())));

        GlideHelper.circleImageView(mContext, (ImageView) helper.getView(R.id.iv_icon), item.getFace(), R.mipmap.default_avatar);
        if (getData().size() - 1 == position) {
            helper.setVisible(R.id.line, false);
        }
    }
}
