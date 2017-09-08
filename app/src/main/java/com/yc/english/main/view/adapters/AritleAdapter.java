package com.yc.english.main.view.adapters;

import android.widget.ImageView;

import com.blankj.utilcode.util.TimeUtils;
import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;
import com.yc.english.R;
import com.yc.english.base.helper.GlideHelper;
import com.yc.english.weixin.model.domain.CourseInfo;

import java.text.SimpleDateFormat;
import java.util.List;
import java.util.Locale;

/**
 * Created by zhangkai on 2017/8/30.
 */

public class AritleAdapter extends BaseQuickAdapter<CourseInfo, BaseViewHolder> {

    private int mType;  // 0 文章  1 微课

    public AritleAdapter(List<CourseInfo> data, int type) {
        super(R.layout.index_aritle_item, data);
        this.mType = type;
    }

    @Override
    protected void convert(BaseViewHolder helper, CourseInfo item) {
        int position = helper.getAdapterPosition();
        long addTime = Long.parseLong(item.getAdd_time()) * 1000;
        helper.setText(R.id.tv_time, TimeUtils.millis2String(addTime, new SimpleDateFormat("yyyy-MM-dd " +
                "HH:mm:ss",
                Locale.getDefault())));
        helper.setText(R.id.tv_title, item.getTitle());
        GlideHelper.imageView(mContext, (ImageView) helper.getView(R.id.iv_icon), item.getImg(), R.drawable.sample);
        helper.setVisible(R.id.iv_microclass_type, false);
        if (mType == 1) {
            helper.setVisible(R.id.iv_microclass_type, true);
            if (getData().size() - 1 == position) {
                helper.setVisible(R.id.line, false);
            }
            if (item.getType_id().equals("7")) {
                helper.setImageResource(R.id.iv_icon, R.mipmap.index_microclass_audio);
            } else if (item.getType_id().equals("8")) {
                helper.setImageResource(R.id.iv_icon, R.mipmap.index_microclass_video);
            }
        }
    }
}
