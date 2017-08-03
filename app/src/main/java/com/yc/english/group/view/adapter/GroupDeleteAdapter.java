package com.yc.english.group.view.adapter;

import android.content.Context;
import android.graphics.BitmapFactory;
import android.view.MotionEvent;
import android.view.View;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.LinearLayout;

import com.blankj.utilcode.util.ConvertUtils;
import com.blankj.utilcode.util.ImageUtils;
import com.blankj.utilcode.util.ToastUtils;
import com.example.comm_recyclviewadapter.BaseAdapter;
import com.example.comm_recyclviewadapter.BaseViewHolder;
import com.yc.english.R;
import com.yc.english.group.listener.OnCheckedChangeListener;
import com.yc.english.group.model.bean.GroupMemberInfo;

import java.util.List;

/**
 * Created by wanglin  on 2017/7/27 08:55.
 */

public class GroupDeleteAdapter extends BaseAdapter<GroupMemberInfo> {
    public GroupDeleteAdapter(Context context, List<GroupMemberInfo> mList) {
        super(context, mList);
    }

    @Override
    protected void convert(BaseViewHolder holder, final int position) {
        GroupMemberInfo memberInfo = mList.get(position);
        if (position == 0) {
            LinearLayout.LayoutParams layoutParams = (LinearLayout.LayoutParams) holder.getView(R.id.ll_container).getLayoutParams();
            layoutParams.bottomMargin = ConvertUtils.dp2px(10);
            holder.itemView.setLayoutParams(layoutParams);
            holder.getView(R.id.cb_delete_select).setVisibility(View.GONE);
        } else {
            holder.getView(R.id.cb_delete_select).setVisibility(View.VISIBLE);


            holder.setOnTouchListener(R.id.ll_container, new View.OnTouchListener(){
                @Override
                public boolean onTouch(View v, MotionEvent event) {
                    ToastUtils.showShort("" + position);
                    return false;
                }
            });




//            ((CheckBox) holder.getView(R.id.cb_delete_select)).setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
//                @Override
//                public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
//                    if (listener != null) {
//                        listener.onCheckedChange(position, buttonView, isChecked);
//                    }
//                }
//            });

        }
        holder.setImageBitmap(R.id.iv_member_img, ImageUtils.toRound(BitmapFactory.decodeResource(mContext.getResources(), R.mipmap.portial)));
        holder.setText(R.id.tv_member_name, memberInfo.getName());
        holder.setText(R.id.tv_member_owner, memberInfo.isGroupOwner() ? "群主" : "");

    }

    @Override
    public int getLayoutID(int viewType) {
        return R.layout.group_delete_member_item;
    }

    private OnCheckedChangeListener listener;


    public void setListener(OnCheckedChangeListener listener) {
        this.listener = listener;
    }
}
