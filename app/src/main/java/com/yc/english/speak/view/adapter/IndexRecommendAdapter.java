package com.yc.english.speak.view.adapter;

import android.widget.ImageView;

import com.blankj.utilcode.util.TimeUtils;
import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;
import com.qq.e.ads.nativ.NativeExpressADView;
import com.yc.english.R;
import com.yc.english.base.helper.GlideHelper;
import com.yc.english.weixin.model.domain.CourseInfo;

import java.text.SimpleDateFormat;
import java.util.List;
import java.util.Locale;

/**
 * Created by wanglin  on 2017/10/27 13:49.
 */

public class IndexRecommendAdapter extends BaseQuickAdapter<CourseInfo, BaseViewHolder> {
    public IndexRecommendAdapter(List<CourseInfo> data) {
        super(R.layout.index_aritle_item, data);
    }

    @Override
    protected void convert(BaseViewHolder helper, CourseInfo item) {
        int position = helper.getAdapterPosition();
        long addTime = Long.parseLong(item.getAdd_time()) * 1000;

        helper.setText(R.id.tv_title, item.getTitle())
                .setText(R.id.tv_time, TimeUtils.millis2String(addTime, new SimpleDateFormat("yyyy-MM-dd HH:mm:ss", Locale.getDefault())))
                .setVisible(R.id.iv_microclass_type, false);
        GlideHelper.imageView(mContext, (ImageView) helper.getView(R.id.iv_icon), item.getImg(), 0);
        if (getData().size() - 1 == position) {
            helper.setVisible(R.id.line, false);
        } else {
            helper.setVisible(R.id.line, true);
        }

    }



}
