package com.yc.english.group.plugin;

import android.content.Context;
import android.content.Intent;
import android.graphics.drawable.Drawable;
import android.os.Parcelable;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.text.TextUtils;
import android.util.Log;

import com.alibaba.fastjson.JSONObject;
import com.blankj.utilcode.util.LogUtils;
import com.yc.english.R;
import com.yc.english.group.model.bean.TaskInfo;
import com.yc.english.group.view.activitys.teacher.GroupIssueTaskActivity;

import io.rong.imkit.RongExtension;
import io.rong.imkit.RongIM;
import io.rong.imkit.plugin.IPluginModule;
import io.rong.imkit.utilities.PermissionCheckUtil;
import io.rong.imlib.IRongCallback;
import io.rong.imlib.RongIMClient;
import io.rong.imlib.model.Conversation;
import io.rong.imlib.model.Message;
import io.rong.message.RichContentMessage;


/**
 * Created by wanglin  on 2017/7/17 18:30.
 * 布置作业
 */

public class AssignTaskPlugin implements IPluginModule {
    private String title = "布置作业";
    private Conversation.ConversationType conversationType;
    private String targetId;
    private FragmentActivity activity;


    @Override
    public Drawable obtainDrawable(Context context) {
        return context.getResources().getDrawable(R.drawable.group_task_selector);
    }

    @Override
    public String obtainTitle(Context context) {
        return title;
    }

    @Override
    public void onClick(final Fragment fragment, RongExtension rongExtension) {
        //示例获取 会话类型、targetId、Context,此处可根据产品需求自定义逻辑，如:开启新的 Activity 等。
        conversationType = rongExtension.getConversationType();
        targetId = rongExtension.getTargetId();

        activity = fragment.getActivity();
        String[] permissions = new String[]{"android.permission.WRITE_EXTERNAL_STORAGE", "android.permission.READ_EXTERNAL_STORAGE"};
        if (PermissionCheckUtil.requestPermissions(fragment, permissions)) {
            Intent intent = new Intent(fragment.getActivity(), GroupIssueTaskActivity.class);
            intent.putExtra("targetId", targetId);
            rongExtension.startActivityForPluginResult(intent, 100, this);
        }


    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent intent) {
        if (requestCode == 100 && resultCode == 700 && intent != null) {

            TaskInfo taskInfo = intent.getParcelableExtra("task");

            String desp = taskInfo.getDesp();
            if (TextUtils.isEmpty(desp)){
                desp="点击查看详情";
            }

            RichContentMessage customMessage = RichContentMessage.obtain("家庭作业", desp, "");
            customMessage.setExtra(JSONObject.toJSONString(taskInfo));
//
            Message message = Message.obtain(targetId, conversationType, customMessage);
//        Message message = Message.obtain("654321", conversationType, CustomMessage.obtain("家庭作业", "今天读一百个单词", ""));
            RongIM.getInstance().sendMessage(message, null, null, new IRongCallback.ISendMessageCallback() {


                @Override
                public void onAttached(Message message) {

                }

                @Override
                public void onSuccess(Message message) {
//                    Toast.makeText(activity, "消息发送成功, 示例获取 Context", Toast.LENGTH_SHORT).show();
                }

                @Override
                public void onError(Message message, RongIMClient.ErrorCode errorCode) {

                }
            });

        }

    }
}
