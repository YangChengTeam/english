package com.yc.english.group.view.adapter;

import android.content.Context;
import android.graphics.BitmapFactory;
import android.view.MotionEvent;
import android.view.View;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.ImageView;
import android.widget.LinearLayout;

import com.blankj.utilcode.util.ConvertUtils;
import com.blankj.utilcode.util.ImageUtils;
import com.blankj.utilcode.util.ToastUtils;
import com.example.comm_recyclviewadapter.BaseAdapter;
import com.example.comm_recyclviewadapter.BaseViewHolder;
import com.hwangjr.rxbus.annotation.Subscribe;
import com.hwangjr.rxbus.annotation.Tag;
import com.hwangjr.rxbus.thread.EventThread;
import com.yc.english.R;
import com.yc.english.group.constant.BusAction;
import com.yc.english.group.listener.OnCheckedChangeListener;
import com.yc.english.group.model.bean.GroupMemberInfo;
import com.yc.english.group.model.bean.StudentInfo;
import com.yc.english.main.hepler.UserInfoHelper;

import java.util.List;

/**
 * Created by wanglin  on 2017/7/27 08:55.
 */

public class GroupDeleteAdapter extends BaseAdapter<StudentInfo> {
    public GroupDeleteAdapter(Context context, List<StudentInfo> mList) {
        super(context, mList);
    }

    @Override
    protected void convert(final BaseViewHolder holder, final int position) {
        final StudentInfo studentInfo = mList.get(position);
        if (position == 0) {
            LinearLayout.LayoutParams layoutParams = (LinearLayout.LayoutParams) holder.getView(R.id.ll_container).getLayoutParams();
            layoutParams.bottomMargin = ConvertUtils.dp2px(10);
            holder.itemView.setLayoutParams(layoutParams);
            holder.getView(R.id.iv_delete_select).setVisibility(View.GONE);
        } else {
            holder.getView(R.id.iv_delete_select).setVisibility(View.VISIBLE);

            final ImageView view = holder.getView(R.id.iv_delete_select);

            view.setTag(mIsClick);
            holder.setOnClickListener(R.id.ll_container, new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (listener != null) {
                        boolean flag = !(boolean) view.getTag();
                        listener.onClick(position, holder.getView(R.id.iv_delete_select), flag, studentInfo);
                        view.setTag(flag);

                    }
                }
            });

        }
        holder.setImageBitmap(R.id.iv_member_img, ImageUtils.toRound(BitmapFactory.decodeResource(mContext.getResources(), R.mipmap.portial)));
        holder.setText(R.id.tv_member_name, studentInfo.getUser_name());
        holder.setText(R.id.tv_member_owner, studentInfo.getUser_id().equals(UserInfoHelper.getUserInfo().getUid()) ? "群主" : "");
    }

    boolean mIsClick;



    @Override
    public int getLayoutID(int viewType) {
        return R.layout.group_delete_member_item;
    }

    private OnCheckedChangeListener listener;


    public void setListener(OnCheckedChangeListener listener) {
        this.listener = listener;
    }
}
