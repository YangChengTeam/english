package com.yc.english.union.view.adapter;

import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;
import com.yc.english.R;
import com.yc.english.base.helper.GlideHelper;
import com.yc.english.group.model.bean.ClassInfo;

import java.util.List;

import io.rong.imkit.RongIM;
import io.rong.imlib.RongIMClient;
import io.rong.imlib.model.Conversation;
import io.rong.imlib.model.Message;

/**
 * Created by wanglin  on 2017/7/29 10:11.
 */

public class GroupUnionAdapter extends BaseQuickAdapter<ClassInfo, BaseViewHolder> {
    private static final String TAG = "GroupUnionAdapter";


    private Message mMessage;

    public GroupUnionAdapter(List<ClassInfo> data) {
        super(R.layout.group_class_item, data);
    }


    @Override
    protected void convert(final BaseViewHolder helper, final ClassInfo classInfo) {
        helper.setText(R.id.m_tv_group_name, classInfo.getClassName())
                .setText(R.id.m_tv_member_count, String.format(mContext.getString(R.string.member_count), Integer.parseInt(classInfo.getCount())))
                .setText(R.id.m_tv_group_number, String.format(mContext.getString(R.string.groupId), classInfo.getGroupId()));
        GlideHelper.circleImageView(mContext, (ImageView) helper.getView(R.id.m_iv_group_img), classInfo.getImageUrl(), R.mipmap.default_avatar);
        helper.setVisible(R.id.m_iv_pay_money, classInfo.getFee_type() == 1);
        RongIM.getInstance().getUnreadCount(Conversation.ConversationType.GROUP, classInfo.getClass_id(), new RongIMClient.ResultCallback<Integer>() {
            @Override
            public void onSuccess(Integer integer) {
                if (integer <= 0) {

                    helper.getView(R.id.m_tv_notification_count).setVisibility(View.INVISIBLE);
                    helper.getView(R.id.m_tv_notification_content).setVisibility(View.INVISIBLE);

                } else {
                    helper.getView(R.id.m_tv_notification_count).setVisibility(View.VISIBLE);

                    if (mMessage != null && classInfo.getClass_id().equals(mMessage.getTargetId()) && !mMessage.getReceivedStatus().isRead()) {
                        helper.getView(R.id.m_tv_notification_content).setVisibility(View.VISIBLE);
                    }

                    TextView view = helper.getView(R.id.m_tv_notification_count);
                    if (integer > 9) {
                        view.setPadding(10, 5, 10, 5);
                        view.setBackgroundResource(R.drawable.group_notification_oval_bg);
                        helper.setText(R.id.m_tv_notification_count, integer.toString() + "+");
                    } else {
                        view.setPadding(0, 0, 0, 0);
                        view.setBackgroundResource(R.drawable.group_notification_circle_bg);
                        helper.setText(R.id.m_tv_notification_count, integer.toString());
                    }
                }

            }


            @Override
            public void onError(RongIMClient.ErrorCode errorCode) {

            }
        });

        helper.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (onJoinListener != null) {
                    onJoinListener.onJoin(classInfo);
                }



                helper.getView(R.id.m_tv_notification_count).setVisibility(View.INVISIBLE);
                helper.getView(R.id.m_tv_notification_content).setVisibility(View.INVISIBLE);
                mMessage = null;
            }
        });

    }


    public void setMessage(Message message) {
        this.mMessage = message;
    }

    private OnJoinListener onJoinListener;

    public void setOnJoinListener(OnJoinListener onJoinListener) {
        this.onJoinListener = onJoinListener;
    }


    public interface OnJoinListener {
        void onJoin(ClassInfo classInfo);
    }

}
