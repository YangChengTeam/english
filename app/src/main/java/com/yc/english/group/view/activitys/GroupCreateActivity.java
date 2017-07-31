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
import com.yc.english.group.common.GroupApp;
import com.yc.english.group.contract.TokenContract;
import com.yc.english.group.dao.ClassInfoDao;
import com.yc.english.group.model.bean.ClassInfo;
import com.yc.english.group.model.bean.TokenInfo;
import com.yc.english.group.presenter.TokenPresenter;
import com.yc.english.group.rong.ImUtils;
import com.yc.english.group.rong.models.CodeSuccessResult;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.List;

import butterknife.BindView;
import butterknife.OnClick;
import io.rong.imkit.RongIM;
import io.rong.imlib.RongIMClient;
import rx.Observable;
import rx.android.schedulers.AndroidSchedulers;
import rx.functions.Action1;
import rx.schedulers.Schedulers;

/**
 * Created by wanglin  on 2017/7/24 18:36.
 */

public class GroupCreateActivity extends FullScreenActivity<TokenPresenter> implements TokenContract.View {

    private static final String TAG = "GroupCreateActivity";
    @BindView(R.id.et_class_group)
    EditText etClassGroup;
    //正式环境下换服务器返回的数据
//    private String username = "username";
//    private String userId = "userId1";
//    private String userPorait = "http://www.rongcloud.cn/images/logo.png";
    private int groupId = 456668;
    private int user_id;
    private ClassInfoDao classInfoDao;//获取数据库操作类的实例


    @Override
    public void init() {
        mPresenter = new TokenPresenter(this, this);
        mToolbar.showNavigationIcon();
        mToolbar.setTitle(getResources().getString(R.string.create_group));

        classInfoDao = GroupApp.getmDaoSession().getClassInfoDao();
    }

    @Override
    public int getLayoutId() {
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

        final String[] userIds = new String[]{user_id + ""};

        ImUtils.createGroup(userIds, groupId + "", groupName).observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Action1<CodeSuccessResult>() {
                    @Override
                    public void call(CodeSuccessResult codeSuccessResult) {
                        if (codeSuccessResult.getCode() == 200) {

                            finish();
                            Observable.just("").subscribeOn(Schedulers.io()).subscribe(new Action1<String>() {
                                private ClassInfo info;

                                @Override
                                public void call(String s) {
                                    List<ClassInfo> classInfos = classInfoDao.queryBuilder().build().list();
                                    if (classInfos != null && classInfos.size() > 0) {
                                        for (ClassInfo classInfo : classInfos) {
                                            if (classInfo.getGroupId() != groupId) {
                                                info = new ClassInfo("", groupName, userIds.length, groupId);
                                                classInfoDao.insert(info);
                                            }
                                        }
                                    } else {
                                        info = new ClassInfo("", groupName, userIds.length, groupId);
                                        classInfoDao.insert(info);
                                    }
                                }
                            });


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
     * @param tokenInfo 从服务端获取的用户身份令牌（Token）。
     * @param
     * @return RongIM  客户端核心类的实例。
     */
    @Override
    public void contact(final TokenInfo tokenInfo) {
        try {
            SPUtils.getInstance().put("TOKEN", tokenInfo.getToken());
            final String packageName = getPackageManager().getPackageInfo(getPackageName(), 0).packageName;//获取包对象信息;)
            if (getApplicationInfo().packageName.equals(packageName)) {
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
                        user_id = tokenInfo.getUser_id();
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
