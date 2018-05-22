package com.yc.junior.english.group.view.adapter;

import android.content.Context;
import android.content.Intent;
import android.view.View;
import android.widget.ImageView;

import com.bumptech.glide.Glide;
import com.example.comm_recyclviewadapter.BaseAdapter;
import com.example.comm_recyclviewadapter.BaseViewHolder;
import com.hwangjr.rxbus.RxBus;
import com.yc.junior.english.R;
import com.yc.junior.english.group.constant.BusAction;
import com.yc.junior.english.group.view.activitys.GroupPictureDetailActivity;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by wanglin  on 2017/8/1 20:27.
 */

public class GroupPictureAdapter extends BaseAdapter<String> {
    private boolean mIsPublish;

    public GroupPictureAdapter(Context context, boolean isPublish, List<String> mList) {
        super(context, mList);
        this.mIsPublish = isPublish;
    }

    @Override
    protected void convert(BaseViewHolder holder, final int position) {
        holder.setVisible(R.id.m_iv_issue_picture_delete, mIsPublish);
        final String path = mList.get(position);

        Glide.with(mContext).load(path).into(((ImageView) holder.getView(R.id.m_iv_issue_result_picture)));

        holder.setOnClickListener(R.id.m_iv_issue_picture_delete, new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                deletePicture(position);
                RxBus.get().post(BusAction.DELETE_PICTURE, path);
            }
        });
        holder.setOnClickListener(R.id.m_iv_issue_result_picture, new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(mContext, GroupPictureDetailActivity.class);
                intent.putExtra("mList", (ArrayList<String>) mList);
                intent.putExtra("position", position);
                mContext.startActivity(intent);
            }
        });


    }

    @Override
    public int getLayoutID(int viewType) {
        return R.layout.group_task_picture_detail;
    }

    private void deletePicture(int position) {
        mList.remove(position);
        notifyDataSetChanged();
    }

}
