package com.yc.english.main.view.adapters;

import android.media.Image;
import android.widget.ImageView;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;
import com.yc.english.R;
import com.yc.english.base.helper.GlideHelper;
import com.yc.english.main.model.domain.CommunityInfo;

import java.util.List;

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
        GlideHelper.circleImageView(mContext, (ImageView) helper.getView(R.id.iv_icon), "http://www.baidu.com", R.mipmap
                .default_avatar);
        if (getData().size() - 1 == position) {
            helper.setVisible(R.id.line, false);
        }
    }
}
