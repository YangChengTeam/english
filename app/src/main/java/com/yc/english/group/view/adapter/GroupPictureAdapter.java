package com.yc.english.group.view.adapter;

import android.content.Context;
import android.net.Uri;
import android.view.View;
import android.widget.ImageView;

import com.example.comm_recyclviewadapter.BaseAdapter;
import com.example.comm_recyclviewadapter.BaseViewHolder;
import com.hwangjr.rxbus.RxBus;
import com.yc.english.R;
import com.yc.english.group.constant.BusAction;

import java.util.List;

/**
 * Created by wanglin  on 2017/8/1 20:27.
 */

public class GroupPictureAdapter extends BaseAdapter<Uri> {
    private boolean mIsPublish;

    public GroupPictureAdapter(Context context, boolean isPublish, List<Uri> mList) {
        super(context, mList);
        this.mIsPublish = isPublish;
    }

    @Override
    protected void convert(BaseViewHolder holder, final int position) {
        holder.setVisible(R.id.m_iv_issue_picture_delete, mIsPublish);
        final Uri uri = mList.get(position);
        ((ImageView) holder.getView(R.id.m_iv_issue_result_picture)).setImageURI(uri);
        holder.setOnClickListener(R.id.m_iv_issue_picture_delete, new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                deletePicture(position);
                RxBus.get().post(BusAction.DELETE_PICTURE, uri);
            }
        });


    }

    @Override
    public int getLayoutID(int viewType) {
        return R.layout.group_task_picture_item;
    }

    private void deletePicture(int position) {
        mList.remove(position);
        notifyDataSetChanged();
    }

}
