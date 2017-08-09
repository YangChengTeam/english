package com.yc.english.group.utils;

import android.content.Context;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.widget.Toast;

import com.blankj.utilcode.util.LogUtils;
import com.blankj.utilcode.util.SPUtils;
import com.kk.utils.UIUitls;
import com.yc.english.base.utils.RongIMUtil;
import com.yc.english.main.hepler.UserInfoHelper;

import io.rong.imkit.RongIM;
import io.rong.imlib.RongIMClient;
import io.rong.imlib.model.UserInfo;

/**
 * Created by wanglin  on 2017/8/1 20:22.
 */

public class ConnectUtils {
    private static final String TAG = "ConnectUtils";
    public static final String TOKEN = "TOKEN";

    /**
     * <p>连接服务器，在整个应用程序全局，只需要调用一次，需在 {@link #} 之后调用。</p>
     * <p>如果调用此接口遇到连接失败，SDK 会自动启动重连机制进行最多10次重连，分别是1, 2, 4, 8, 16, 32, 64, 128, 256, 512秒后。
     * 在这之后如果仍没有连接成功，还会在当检测到设备网络状态变化时再次进行重连。</p>
     *
     * @param token 从服务端获取的用户身份令牌（Token）。
     * @param
     * @return RongIM  客户端核心类的实例。
     */
    public static void contact(final Context context, final String token) {

        try {
            final String packageName = context.getPackageManager().getPackageInfo(context.getPackageName(), 0).packageName;//获取包对象信息;)
            if (context.getApplicationInfo().packageName.equals(packageName)) {
                RongIM.connect(token, new RongIMClient.ConnectCallback() {

                    /**
                     * Token 错误。可以从下面两点检查 1.  Token 是否过期，如果过期您需要向 App Server 重新请求一个新的 Token
                     *                  2.  token 对应的 appKey 和工程里设置的 appKey 是否一致
                     */
                    @Override
                    public void onTokenIncorrect() {
                        LogUtils.e(TAG, "onTokenIncorrect: ");
                        SPUtils.getInstance().put(TOKEN, "");
                    }

                    /**
                     * 连接融云成功
                     * @param userid 当前 token 对应的用户 id
                     */


                    @Override
                    public void onSuccess(String userid) {
                        SPUtils.getInstance().put(TOKEN, token);
                        RongIMUtil.setUserInfo();
                        UIUitls.post(new Runnable() {
                            @Override
                            public void run() {
                                Toast.makeText(context, "连接成功", Toast.LENGTH_SHORT).show();
                            }
                        });
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
