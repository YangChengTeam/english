package com.yc.english.group.view.provider;

import android.content.Context;
import android.content.Intent;
import android.text.Spannable;
import android.text.SpannableString;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.blankj.utilcode.util.LogUtils;
import com.yc.english.R;
import com.yc.english.group.view.activitys.student.GroupMyTaskDetailActivity;
import com.yc.english.group.view.activitys.teacher.GroupTaskLookAndUnLookActivity;

import io.rong.imkit.model.ProviderTag;
import io.rong.imkit.model.UIMessage;
import io.rong.imkit.widget.provider.IContainerItemProvider;
import io.rong.imlib.model.Message;
import io.rong.message.RichContentMessage;

/**
 * Created by wanglin  on 2017/7/26 08:34.
 * 自定义消息以及消息展示
 */
@ProviderTag(messageContent = RichContentMessage.class)
public class PublishTaskMessageProvider extends IContainerItemProvider.MessageProvider<RichContentMessage> {
    private Context mContext;


    //初始化 View。
    @Override
    public View newView(Context context, ViewGroup viewGroup) {
        this.mContext = context;
        View view = LayoutInflater.from(context).inflate(R.layout.group_custom_message, null);
        ViewHolder holder = new ViewHolder(view);
        holder.message = (TextView) view.findViewById(R.id.tv_message_content);
        holder.title = (TextView) view.findViewById(R.id.tv_message_title);
        holder.llMessage = (LinearLayout) view.findViewById(R.id.ll_message);
        holder.messageType = (TextView) view.findViewById(R.id.tv_message_type);

        view.setTag(holder);
        return view;
    }

    // 将数据填充 View 上。
    @Override
    public void bindView(View v, int i, RichContentMessage customMessage, UIMessage uiMessage) {
        ViewHolder holder = (ViewHolder) v.getTag();

        if (uiMessage.getMessageDirection() == Message.MessageDirection.SEND) {//消息方向，自己发送的
            holder.llMessage.setBackgroundResource(R.mipmap.group_message_bg);
        } else {
            holder.llMessage.setBackgroundResource(io.rong.imkit.R.drawable.rc_ic_bubble_left);
        }
        holder.message.setText(customMessage.getContent());
        holder.title.setText(customMessage.getTitle());


//        AndroidEmoji.ensure((Spannable) holder.message.getText());//显示消息中的 Emoji 表情。
    }

    //该条消息为该会话的最后一条消息时，会话列表要显示的内容，通过该方法进行定义。
    @Override
    public Spannable getContentSummary(RichContentMessage customMessage) {
        return new SpannableString("这是一条自定义消息CustomizeMessage");
    }


    @Override
    public void onItemClick(View view, int position, RichContentMessage richContentMessage, UIMessage uiMessage) {

        LogUtils.e("onItemClick", richContentMessage.getExtra() + "---" + uiMessage.getTargetId());


        Intent intent;

        if (uiMessage.getMessageDirection() == Message.MessageDirection.SEND) {

            intent = new Intent(mContext, GroupTaskLookAndUnLookActivity.class);

        } else {
            intent = new Intent(mContext, GroupMyTaskDetailActivity.class);
        }
        intent.putExtra("extra", richContentMessage.getExtra());

        mContext.startActivity(intent);
    }

    private class ViewHolder {
        TextView message;
        TextView title;
        TextView messageType;
        View view;
        LinearLayout llMessage;

        private ViewHolder(View view) {
            this.view = view;
        }
    }


}
