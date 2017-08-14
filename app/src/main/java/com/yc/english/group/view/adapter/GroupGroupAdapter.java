package com.yc.english.group.view.adapter;

import android.content.Context;
import android.graphics.BitmapFactory;
import android.text.TextUtils;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.blankj.utilcode.util.ImageUtils;
import com.blankj.utilcode.util.LogUtils;
import com.example.comm_recyclviewadapter.BaseAdapter;
import com.example.comm_recyclviewadapter.BaseViewHolder;
import com.yc.english.R;
import com.yc.english.base.helper.GlideHelper;
import com.yc.english.group.common.GroupApp;
import com.yc.english.group.model.bean.ClassInfo;
import com.yc.english.main.hepler.UserInfoHelper;

import java.util.List;

import io.rong.imkit.RongIM;
import io.rong.imlib.RongIMClient;
import io.rong.imlib.model.Conversation;

/**
 * Created by wanglin  on 2017/7/29 10:11.
 */

public class GroupGroupAdapter extends BaseAdapter<ClassInfo> {
    private static final String TAG = "GroupGroupAdapter";
    private String mTargetId;
    private boolean mIsRead;

    public GroupGroupAdapter(Context context, List<ClassInfo> mList) {
        super(context, mList);
    }

    @Override
    protected void convert(final BaseViewHolder holder, int position) {
        final ClassInfo classInfo = mList.get(position);
        holder.setText(R.id.m_tv_group_name, classInfo.getClassName());
        holder.setText(R.id.m_tv_member_count, String.format(mContext.getString(R.string.member_count), Integer.parseInt(classInfo.getCount())));
        holder.setText(R.id.m_tv_group_number, String.format(mContext.getString(R.string.groupId), classInfo.getGroupId()));
        GlideHelper.circleImageView(mContext, (ImageView) holder.getView(R.id.m_iv_group_img),classInfo.getImageUrl(),R.mipmap.default_avatar);

        RongIM.getInstance().getUnreadCount(Conversation.ConversationType.GROUP, classInfo.getClass_id(), new RongIMClient.ResultCallback<Integer>() {
            @Override
            public void onSuccess(Integer integer) {

                LogUtils.e(TAG, integer);
                if (integer <= 0) {
                    holder.getView(R.id.m_tv_notification_count).setVisibility(View.INVISIBLE);
                    holder.getView(R.id.m_tv_notification_content).setVisibility(View.INVISIBLE);
                } else {

                    holder.getView(R.id.m_tv_notification_count).setVisibility(View.VISIBLE);
                    if (classInfo.getClass_id().equals(mTargetId) && !mIsRead) {
                        holder.getView(R.id.m_tv_notification_content).setVisibility(View.VISIBLE);
                    } else {
                        holder.getView(R.id.m_tv_notification_content).setVisibility(View.INVISIBLE);
                    }

                    TextView view = holder.getView(R.id.m_tv_notification_count);
                    if (integer > 9) {
                        view.setPadding(10, 5, 10, 5);
                        view.setBackgroundResource(R.drawable.group_notification_oval_bg);
                        holder.setText(R.id.m_tv_notification_count, integer.toString() + "+");
                    } else {
                        view.setPadding(0, 0, 0, 0);
                        view.setBackgroundResource(R.drawable.group_notification_circle_bg);
                        holder.setText(R.id.m_tv_notification_count, integer.toString());
                    }
                }

            }

            @Override
            public void onError(RongIMClient.ErrorCode errorCode) {

            }
        });

        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if (classInfo.getMaster_id().equals(UserInfoHelper.getUserInfo().getUid())) {
                    GroupApp.setMyExtensionModule(true);
                } else {
                    GroupApp.setMyExtensionModule(false);
                }
                RongIM.getInstance().startGroupChat(mContext, classInfo.getClass_id(), classInfo.getClassName());
                holder.getView(R.id.m_tv_notification_count).setVisibility(View.INVISIBLE);
            }
        });

    }

    @Override
    public int getLayoutID(int viewType) {
        return R.layout.group_class_item;
    }

    public void setMessage(String targetId, boolean isRead) {
        this.mTargetId = targetId;
        this.mIsRead = isRead;
    }


}
