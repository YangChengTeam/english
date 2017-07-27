package com.yc.english.group.view.activitys;

import android.content.pm.PackageManager;
import android.text.TextUtils;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

import com.blankj.utilcode.util.LogUtils;
import com.blankj.utilcode.util.SPUtils;
import com.blankj.utilcode.util.ToastUtils;
import com.yc.english.R;
import com.yc.english.base.view.FullScreenActivity;
import com.yc.english.group.rong.ImUtils;
import com.yc.english.group.rong.models.CodeSuccessResult;

import org.json.JSONException;
import org.json.JSONObject;

import butterknife.BindView;
import butterknife.OnClick;
import io.rong.imkit.RongIM;
import io.rong.imlib.RongIMClient;
import rx.android.schedulers.AndroidSchedulers;
import rx.functions.Action1;

/**
 * Created by wanglin  on 2017/7/24 18:36.
 */

public class GroupCreateActivity extends FullScreenActivity {

    private static final String TAG = "GroupCreateActivity";
    @BindView(R.id.et_class_group)
    EditText etClassGroup;
    //正式环境下换服务器返回的数据
    private String username = "username";
    private String userId = "userId1";
    private String userPorait = "http://www.rongcloud.cn/images/logo.png";
    private int groupId = 654321;

    @Override
    public void init() {
        mToolbar.showNavigationIcon();
        mToolbar.setTitle(getResources().getString(R.string.create_group));

//        if (TextUtils.isEmpty(SPUtils.getInstance().getString("TOKEN", ""))) {

        //初始化获取token，连接融云服务器
        ImUtils.login(username, userId, userPorait).observeOn(AndroidSchedulers.mainThread()).subscribe(new Action1<String>() {
            @Override
            public void call(String s) {

                LogUtils.e(TAG, s);

                try {
                    JSONObject jsonObject = new JSONObject(s);
                    if (jsonObject.getInt("code") == 200) {
                        connect(jsonObject.getString("token"));
                        SPUtils.getInstance().put("TOKEN", jsonObject.getString("token"));
                        SPUtils.getInstance().put("userId", jsonObject.getString("userId"));
                    }

                } catch (JSONException e) {
                    e.printStackTrace();
                }

            }
        });
//        }
    }

    @Override
    public int getLayoutID() {
        return R.layout.group_activity_create_class;
    }


    @OnClick(R.id.btn_create)
    public void onClick(View view) {
        String groupInfo = etClassGroup.getText().toString().trim();
        if (TextUtils.isEmpty(groupInfo)) {
            ToastUtils.showShort("请输入班级名称...");
            return;
        }
        createGroup(groupInfo);

    }

    /**
     * 创建班级
     */
    private void createGroup(final String groupName) {
        final String userId = SPUtils.getInstance().getString("userId");
        final String[] userIds = new String[]{userId};

        ImUtils.createGroup(userIds, groupId + "", groupName).observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Action1<CodeSuccessResult>() {
                    @Override
                    public void call(CodeSuccessResult codeSuccessResult) {
                        if (codeSuccessResult.getCode() == 200) {

                            RongIM.getInstance().startGroupChat(GroupCreateActivity.this, groupId + "", groupName);

//                            ImUtils.queryGroup(groupId + "").observeOn(AndroidSchedulers.mainThread())
//                                    .subscribe(new Action1<GroupUserQueryResult>() {
//                                        @Override
//                                        public void call(GroupUserQueryResult groupUserQueryResult) {
//                                            LogUtils.e(TAG, "call: " + groupUserQueryResult.toString());
//                                        }
//                                    });

                            groupId++;
                        }
                        LogUtils.e(TAG, "call: " + codeSuccessResult.getCode());
                    }
                });

    }

    /**
     * <p>连接服务器，在整个应用程序全局，只需要调用一次，需在 {@link #} 之后调用。</p>
     * <p>如果调用此接口遇到连接失败，SDK 会自动启动重连机制进行最多10次重连，分别是1, 2, 4, 8, 16, 32, 64, 128, 256, 512秒后。
     * 在这之后如果仍没有连接成功，还会在当检测到设备网络状态变化时再次进行重连。</p>
     *
     * @param token 从服务端获取的用户身份令牌（Token）。
     * @param
     * @return RongIM  客户端核心类的实例。
     */
    private void connect(String token) {

        try {

            final String packageName = getPackageManager().getPackageInfo(getPackageName(), 0).packageName;//获取包对象信息;)
            if (getApplicationInfo().packageName.equals(packageName)) {

                RongIM.connect(token, new RongIMClient.ConnectCallback() {


                    /**
                     * Token 错误。可以从下面两点检查 1.  Token 是否过期，如果过期您需要向 App Server 重新请求一个新的 Token
                     *                  2.  token 对应的 appKey 和工程里设置的 appKey 是否一致
                     */
                    @Override
                    public void onTokenIncorrect() {
                        LogUtils.e(TAG, "onTokenIncorrect: ");
                    }

                    /**
                     * 连接融云成功
                     * @param userid 当前 token 对应的用户 id
                     */
                    @Override
                    public void onSuccess(String userid) {
                        LogUtils.d(TAG, "--onSuccess" + userid);
                        Toast.makeText(GroupCreateActivity.this, "连接成功", Toast.LENGTH_SHORT).show();
                    }

                    /**
                     * 连接融云失败
                     * @param errorCode 错误码，可到官网 查看错误码对应的注释
                     */
                    @Override
                    public void onError(RongIMClient.ErrorCode errorCode) {
                        LogUtils.e(TAG, "onError: " + errorCode.getMessage());

                    }
                });
            }

        } catch (PackageManager.NameNotFoundException e) {
            e.printStackTrace();
        }

    }

}
