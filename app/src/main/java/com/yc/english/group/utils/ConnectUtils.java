package com.yc.english.group.utils;

import android.content.Context;
import android.content.pm.PackageManager;

import com.blankj.utilcode.util.LogUtils;
import com.blankj.utilcode.util.SPUtils;
import com.yc.english.group.model.bean.TokenInfo;

import io.rong.imkit.RongIM;
import io.rong.imlib.RongIMClient;

/**
 * Created by wanglin  on 2017/8/1 20:22.
 */

public class ConnectUtils {
    private static final String TAG = "ConnectUtils";


    public static void contact(Context context, final TokenInfo tokenInfo) {
        try {
            SPUtils.getInstance().put("TOKEN", tokenInfo.getToken());
            final String packageName = context.getPackageManager().getPackageInfo(context.getPackageName(), 0).packageName;//获取包对象信息;)
            if (context.getApplicationInfo().packageName.equals(packageName)) {
                RongIM.connect(tokenInfo.getToken(), new RongIMClient.ConnectCallback() {

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

                        //成功保存用户ID
                        SPUtils.getInstance().put("user_id", userid);
//                        Toast.makeText(GroupCreateActivity.this, "连接成功", Toast.LENGTH_SHORT).show();
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
