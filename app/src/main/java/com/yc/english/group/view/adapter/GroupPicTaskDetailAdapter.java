package com.yc.english.group.view.adapter;


import android.support.v4.view.PagerAdapter;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import com.bumptech.glide.Glide;
import com.yc.english.R;

import java.util.List;


/**
 * Created by wanglin  on 2017/7/24 17:37.
 * 显示发布图片作业详情的信息
 */

public class GroupPicTaskDetailAdapter extends PagerAdapter {

    private AppCompatActivity mActivity;
    private List<String> mList;

    public GroupPicTaskDetailAdapter(AppCompatActivity activity, List<String> list) {
        this.mActivity = activity;
        this.mList = list;
    }


    @Override
    public int getCount() {
        return mList == null ? 0 : mList.size();
    }

    @Override
    public boolean isViewFromObject(View view, Object object) {
        return view == object;
    }

    @Override
    public void destroyItem(ViewGroup container, int position, Object object) {
        container.removeView((View) object);

    }

    @Override
    public Object instantiateItem(ViewGroup container, int position) {

        View view = View.inflate(mActivity, R.layout.group_task_picture_item, null);

        ImageView imageView = (ImageView) view.findViewById(R.id.iv_picture_detail);
        container.addView(view);
        String path = mList.get(position);

        Glide.with(mActivity).load(path).into(imageView);
        view.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mActivity.finish();
            }
        });

        return view;
    }


}
