package com.yc.english.group.view.adapter;

import android.content.Context;
import android.content.Intent;
import android.support.v4.content.ContextCompat;
import android.support.v7.widget.helper.ItemTouchHelper;
import android.text.TextUtils;
import android.view.MotionEvent;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.blankj.utilcode.util.EmptyUtils;
import com.bumptech.glide.Glide;
import com.bumptech.glide.request.RequestOptions;
import com.example.comm_recyclviewadapter.BaseAdapter;
import com.example.comm_recyclviewadapter.BaseViewHolder;
import com.yc.english.R;
import com.yc.english.base.helper.GlideHelper;
import com.yc.english.base.helper.TipsHelper;
import com.yc.english.base.view.WebActivity;
import com.yc.english.group.common.GroupApp;
import com.yc.english.group.model.bean.ClassInfo;
import com.yc.english.group.utils.GlideRoundTransform;
import com.yc.english.group.utils.ItemTouchHelperAdapter;
import com.yc.english.group.utils.ItemTouchHelperCallback;
import com.yc.english.main.hepler.UserInfoHelper;

import java.util.Collections;
import java.util.List;

import io.rong.imkit.RongIM;
import io.rong.imlib.RongIMClient;
import io.rong.imlib.model.Conversation;
import io.rong.imlib.model.Message;

/**
 * Created by wanglin  on 2017/7/29 10:11.
 */

public class GroupGroupAdapter extends BaseAdapter<ClassInfo> implements ItemTouchHelperAdapter {
    private static final String TAG = "GroupUnionAdapter";

    private boolean mIsJoin;
    private Message mMessage;

    public GroupGroupAdapter(Context context, boolean isJoin, List<ClassInfo> mList) {
        super(context, mList);
        this.mIsJoin = isJoin;
    }

    @Override
    protected void convert(final BaseViewHolder holder, int position) {
        final ClassInfo classInfo = mList.get(position);
        holder.setText(R.id.m_tv_group_name, classInfo.getClassName())
                .setText(R.id.m_tv_member_count, String.format(mContext.getString(R.string.member_count), Integer.parseInt(classInfo.getCount())))
                .setText(R.id.m_tv_group_number, String.format(mContext.getString(R.string.groupId), classInfo.getGroupId()))
                .setImageDrawable(R.id.m_iv_pay_money, ContextCompat.getDrawable(mContext, R.mipmap.group74))
                .setVisible(R.id.m_iv_pay_money, classInfo.getFee_type() == 1);

        GlideHelper.circleImageView(mContext, (ImageView) holder.getView(R.id.m_iv_group_img), classInfo.getImageUrl(), R.mipmap.default_avatar);

        holder.setOnClickListener(R.id.btn_introduce, new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //介绍
                String title = classInfo.getTitle();
                if (EmptyUtils.isEmpty(title)) {
                    title = "名师辅导介绍";
                }
                String url = classInfo.getDesp_url();
                if (EmptyUtils.isEmpty(url)) {
                    url = "http://en.upkao.com/teacher/teacher_detail.html";
                }
                Intent intent = new Intent(mContext, WebActivity.class);
                intent.putExtra("title", title);
                intent.putExtra("url", url);
                mContext.startActivity(intent);
            }
        });


        RongIM.getInstance().getUnreadCount(Conversation.ConversationType.GROUP, classInfo.getClass_id(), new RongIMClient.ResultCallback<Integer>() {
            @Override
            public void onSuccess(Integer integer) {
                if (integer <= 0) {
                    holder.getView(R.id.m_tv_notification_count).setVisibility(View.INVISIBLE);
                    holder.getView(R.id.m_tv_notification_content).setVisibility(View.INVISIBLE);
                } else {
                    holder.getView(R.id.m_tv_notification_count).setVisibility(View.VISIBLE);

                    if (mMessage != null && classInfo.getClass_id().equals(mMessage.getTargetId()) && !mMessage.getReceivedStatus().isRead()) {
                        holder.getView(R.id.m_tv_notification_content).setVisibility(View.VISIBLE);
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
                if (mIsJoin) {
                    if (classInfo.getType().equals("1")) {
                        GroupApp.setMyExtensionModule(false, false);
                    } else {
                        if (UserInfoHelper.getUserInfo() != null && !TextUtils.isEmpty(UserInfoHelper.getUserInfo().getUid())) {
                            if (classInfo.getMaster_id().equals(UserInfoHelper.getUserInfo().getUid())) {
                                GroupApp.setMyExtensionModule(true, true);
                            } else {
                                GroupApp.setMyExtensionModule(false, true);
                            }
                        }
                    }
                    RongIM.getInstance().startGroupChat(mContext, classInfo.getClass_id(), classInfo.getClassName());
                    if (mMessage != null) {
                        mMessage.getReceivedStatus().setRead();
                        RongIM.getInstance().setMessageReceivedStatus(mMessage.getMessageId(), mMessage.getReceivedStatus(), null);
                    }

                } else {
                    if (onJoinListener != null) {
                        onJoinListener.onJoin(classInfo);
                    }
                }
                holder.getView(R.id.m_tv_notification_count).setVisibility(View.INVISIBLE);
                holder.getView(R.id.m_tv_notification_content).setVisibility(View.INVISIBLE);
                mMessage = null;
            }
        });


    }

    @Override
    public int getLayoutID(int viewType) {
        return R.layout.group_class_item;
    }


    public void setMessage(Message message) {
        this.mMessage = message;
    }

    private OnJoinListener onJoinListener;

    public void setOnJoinListener(OnJoinListener onJoinListener) {
        this.onJoinListener = onJoinListener;
    }

    @Override
    public void onMove(int fromPosition, int toPosition) {
        /**
         * 在这里进行给原数组数据的移动
         */
        Collections.swap(mList, fromPosition, toPosition);
        /**
         * 通知数据移动
         */
        notifyItemMoved(fromPosition, toPosition);
    }

    @Override
    public void onSwipe(int position) {
        /**
         * 原数据移除数据
         */
        mList.remove(position);
        /**
         * 通知移除
         */
        notifyItemRemoved(position);

    }


    public interface OnJoinListener {
        void onJoin(ClassInfo classInfo);
    }

}
