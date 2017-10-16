package com.yc.english.group.plugin;

import android.content.Context;
import android.content.Intent;
import android.graphics.drawable.Drawable;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.support.v4.content.ContextCompat;

import com.blankj.utilcode.util.LogUtils;
import com.yc.english.R;
import com.yc.english.base.helper.TipsHelper;
import com.yc.english.group.constant.GroupConstant;
import com.yc.english.group.model.bean.GroupInfoHelper;
import com.yc.english.group.view.activitys.teacher.GroupPublishTaskListActivity;
import com.yc.english.group.view.provider.CustomMessage;

import io.rong.imkit.RongExtension;
import io.rong.imkit.RongIM;
import io.rong.imkit.plugin.IPluginModule;
import io.rong.imkit.utilities.PermissionCheckUtil;
import io.rong.imlib.IRongCallback;
import io.rong.imlib.RongIMClient;
import io.rong.imlib.model.Conversation;
import io.rong.imlib.model.Message;


/**
 * Created by wanglin  on 2017/7/17 18:30.
 * 查看作业
 */

public class TeacherLookTaskPlugin implements IPluginModule {
    private String title = "查看作业";
    private Conversation.ConversationType conversationType;
    private String targetId;
    private FragmentActivity activity;


    @Override
    public Drawable obtainDrawable(Context context) {
        return ContextCompat.getDrawable(context, R.drawable.group_look_task_selector);
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
        if (GroupInfoHelper.getClassInfo().getIs_allow_talk()==0){
            TipsHelper.tips(fragment.getActivity(), GroupConstant.FORBID_CONTENT);
            return;
        }
        String[] permissions = new String[]{"android.permission.WRITE_EXTERNAL_STORAGE", "android.permission.READ_EXTERNAL_STORAGE"};
        if (PermissionCheckUtil.requestPermissions(fragment, permissions)) {
            Intent intent = new Intent(fragment.getActivity(), GroupPublishTaskListActivity.class);
            intent.putExtra("targetId", targetId);
            rongExtension.startActivityForPluginResult(intent, 100, this);
        }

    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent intent) {
        if (requestCode == 100 && resultCode == 700 && intent != null) {
            String task = intent.getStringExtra("task");

            CustomMessage customMessage = CustomMessage.obtain("我的作业", task, "");
            Message message = Message.obtain(targetId, conversationType, customMessage);
            RongIM.getInstance().sendMessage(message, "app:custom", null, new IRongCallback.ISendMessageCallback() {

                @Override
                public void onAttached(Message message) {

                }

                @Override
                public void onSuccess(Message message) {
                    LogUtils.e(message);
                }

                @Override
                public void onError(Message message, RongIMClient.ErrorCode errorCode) {

                }
            });

        }

    }
}
