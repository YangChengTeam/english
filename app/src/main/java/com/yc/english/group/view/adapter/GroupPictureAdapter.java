package com.yc.english.group.view.adapter;

import android.content.Context;
import android.net.Uri;
import android.widget.ImageView;

import com.example.comm_recyclviewadapter.BaseAdapter;
import com.example.comm_recyclviewadapter.BaseViewHolder;
import com.yc.english.R;

import java.util.List;

/**
 * Created by wanglin  on 2017/8/1 20:27.
 */

public class GroupPictureAdapter extends BaseAdapter<Uri> {
    public GroupPictureAdapter(Context context, List<Uri> mList) {
        super(context, mList);
    }

    @Override
    protected void convert(BaseViewHolder holder, int position) {
        Uri uri = mList.get(position);
        ((ImageView) holder.getView(R.id.m_iv_issue_result_picture)).setImageURI(uri);
    }

    @Override
    public int getLayoutID(int viewType) {
        return R.layout.group_task_picture_item;
    }
}
