package com.yc.english.group.view.adapter;

import android.content.Context;
import android.graphics.BitmapFactory;
import android.widget.CheckBox;
import android.widget.CompoundButton;

import com.blankj.utilcode.util.ImageUtils;
import com.example.comm_recyclviewadapter.BaseAdapter;
import com.example.comm_recyclviewadapter.BaseViewHolder;
import com.yc.english.R;
import com.yc.english.group.listener.OnCheckedChangeListener;
import com.yc.english.group.model.bean.ClassInfo;

import java.util.List;

/**
 * Created by wanglin  on 2017/7/27 16:28.
 */

public class GroupSyncListAdapter extends BaseAdapter<ClassInfo> {
    public GroupSyncListAdapter(Context context, List<ClassInfo> mList) {
        super(context, mList);
    }

    @Override
    protected void convert(BaseViewHolder holder, final int position) {
        ClassInfo classInfo = mList.get(position);
        holder.setText(R.id.m_tv_group_name, classInfo.getClassName());
        holder.setImageBitmap(R.id.m_iv_group_img, ImageUtils.toRound(BitmapFactory.decodeResource(mContext.getResources(), R.mipmap.portial)));
        ((CheckBox) holder.getView(R.id.m_cb_group_select)).setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if (listener != null) {
                    listener.onCheckedChange(position, buttonView,false);
                }
            }
        });
    }

    @Override
    public int getLayoutID(int viewType) {
        return R.layout.group_sync_list_item;
    }

    private OnCheckedChangeListener listener;

    public void setListener(OnCheckedChangeListener listener) {
        this.listener = listener;
    }
}
