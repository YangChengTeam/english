package com.yc.english.union.view.adapter;

import android.content.Context;
import android.graphics.BitmapFactory;
import android.view.View;
import android.widget.ImageView;

import com.blankj.utilcode.util.ImageUtils;
import com.blankj.utilcode.util.SPUtils;
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
    protected void convert(final BaseViewHolder holder, final int position) {
        final ClassInfo classInfo = mList.get(position);
        holder.setText(R.id.m_tv_group_name, classInfo.getClassName());
        holder.setImageBitmap(R.id.m_iv_group_img, ImageUtils.toRound(BitmapFactory.decodeResource(mContext.getResources(), R.mipmap.default_avatar)));
        final ImageView view = holder.getView(R.id.m_iv_group_select);

        boolean aBoolean = SPUtils.getInstance().getBoolean(classInfo.getClass_id() + "class");
        if (aBoolean) {
            view.setImageDrawable(mContext.getResources().getDrawable(R.mipmap.group24));
        } else {
            view.setImageDrawable(mContext.getResources().getDrawable(R.mipmap.group23));
        }
        view.setTag(aBoolean);


        holder.setOnClickListener(R.id.ll_container, new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (listener != null) {
                    boolean flag = !(boolean) view.getTag();
                    listener.onClick(holder.getView(R.id.m_iv_group_select), flag, classInfo);
                    view.setTag(flag);

                }
            }
        });

    }


    @Override
    public int getLayoutID(int viewType) {
        return R.layout.group_sync_list_item;
    }

    private OnCheckedChangeListener<ClassInfo> listener;

    public void setListener(OnCheckedChangeListener<ClassInfo> listener) {
        this.listener = listener;
    }

}
