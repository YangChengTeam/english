package com.yc.junior.english.community.view.adapter;

import android.content.Context;
import android.widget.ImageView;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;
import com.yc.junior.english.R;
import com.yc.junior.english.base.helper.GlideHelper;
import com.yc.junior.english.community.model.domain.CommentInfo;

import java.net.URLDecoder;
import java.text.SimpleDateFormat;
import java.util.List;
import java.util.Locale;

import yc.com.blankj.utilcode.util.StringUtils;
import yc.com.blankj.utilcode.util.TimeUtils;

public class CommentItemAdapter extends BaseQuickAdapter<CommentInfo, BaseViewHolder> {

    private Context mContext;

    public CommentItemAdapter(Context context, List<CommentInfo> datas) {
        super(R.layout.comment_list_item, datas);
        this.mContext = context;
    }

    @Override
    protected void convert(final BaseViewHolder helper, final CommentInfo item) {
        try {
            helper.setText(R.id.tv_comment_user_name, item.getUserName())
                    .setText(R.id.tv_comment_content, URLDecoder.decode(item.getContent(),"UTF-8"));
        } catch (Exception e) {
            e.printStackTrace();
        }
        if (!StringUtils.isEmpty(item.getAddTime())) {
            long addTime = Long.parseLong(item.getAddTime()) * 1000;
            helper.setText(R.id.tv_comment_date, TimeUtils.millis2String(addTime, new SimpleDateFormat("MM-dd HH:mm",
                    Locale.getDefault())));
        }

        GlideHelper.circleImageView(mContext, (ImageView) helper.getConvertView().findViewById(R.id.iv_comment_user_img), item.getFace(), R.mipmap.main_tab_my);
    }
}