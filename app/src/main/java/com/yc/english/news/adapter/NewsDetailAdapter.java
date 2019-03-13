package com.yc.english.news.adapter;


import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.ImageView;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;
import com.yc.english.R;
import com.yc.english.base.helper.GlideHelper;
import com.yc.english.news.view.activity.NewsDetailActivity;
import com.yc.english.weixin.model.domain.CourseInfo;

import java.text.SimpleDateFormat;
import java.util.List;
import java.util.Locale;

import yc.com.blankj.utilcode.util.TimeUtils;

/**
 * Created by wanglin  on 2017/9/6 09:55.
 */

public class NewsDetailAdapter extends BaseQuickAdapter<CourseInfo, BaseViewHolder> {


    public NewsDetailAdapter(List<CourseInfo> data) {
        super(R.layout.index_aritle_item, data);
    }


    @Override
    protected void convert(BaseViewHolder helper, final CourseInfo courseInfo) {
        int position = helper.getAdapterPosition();
        long addTime = Long.parseLong(courseInfo.getAdd_time()) * 1000;
        helper.setText(R.id.tv_title, courseInfo.getTitle())
                .setText(R.id.tv_time, TimeUtils.millis2String(addTime, new SimpleDateFormat("yyyy-MM-dd HH:mm:ss", Locale.getDefault())))
                .setGone(R.id.iv_microclass_type, false);
        GlideHelper.imageView(mContext, (ImageView) helper.getView(R.id.iv_icon), courseInfo.getImg(), 0);
        if (getData().size() - 1 == position) {
            helper.setVisible(R.id.line, false);
        } else {
            helper.setVisible(R.id.line, true);
        }

        helper.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(mContext, NewsDetailActivity.class);
                intent.putExtra("info", courseInfo);
                intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                mContext.startActivity(intent);
                if (mContext instanceof AppCompatActivity) {
                    AppCompatActivity activity = (AppCompatActivity) mContext;
                    activity.finish();
                }
            }
        });
    }
}
