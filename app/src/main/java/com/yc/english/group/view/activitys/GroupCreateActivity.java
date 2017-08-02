package com.yc.english.group.view.activitys;

import android.util.Log;
import android.widget.Button;
import android.widget.EditText;

import com.blankj.utilcode.util.LogUtils;
import com.hwangjr.rxbus.RxBus;
import com.jakewharton.rxbinding.view.RxView;
import com.yc.english.R;
import com.yc.english.base.view.FullScreenActivity;
import com.yc.english.group.common.GroupApp;
import com.yc.english.group.constant.BusAction;
import com.yc.english.group.contract.GroupCreateContract;
import com.yc.english.group.dao.ClassInfoDao;
import com.yc.english.group.model.bean.ClassInfo;
import com.yc.english.group.presenter.GroupCreatePresenter;
import com.yc.english.group.rong.ImUtils;
import com.yc.english.group.rong.models.CodeSuccessResult;
import com.yc.english.main.hepler.UserInfoHelper;

import java.util.List;
import java.util.Random;
import java.util.concurrent.TimeUnit;

import butterknife.BindView;
import rx.Observable;
import rx.android.schedulers.AndroidSchedulers;
import rx.functions.Action1;
import rx.schedulers.Schedulers;

/**
 * Created by wanglin  on 2017/7/24 18:36.
 */

public class GroupCreateActivity extends FullScreenActivity<GroupCreatePresenter> implements GroupCreateContract.View {

    private static final String TAG = "GroupCreateActivity";
    @BindView(R.id.et_class_group)
    EditText etClassGroup;
    @BindView(R.id.btn_create)
    Button btnCreate;
    //正式环境下换服务器返回的数据
    private String username = "username";
    private String userId = "userId1";
    private String userPorait = "http://www.rongcloud.cn/images/logo.png";
    private ClassInfoDao classInfoDao;//获取数据库操作类的实例

    @Override
    public void init() {

        mPresenter = new GroupCreatePresenter(this, this);

        mToolbar.showNavigationIcon();
        mToolbar.setTitle(getResources().getString(R.string.create_group));
        classInfoDao = GroupApp.getmDaoSession().getClassInfoDao();

        initListener();

//        ImUtils.login(username, GoagalInfo.get().uuid, userPorait).observeOn(AndroidSchedulers.mainThread()).subscribe(new Action1<String>() {
//            @Override
//            public void call(String s) {
//                try {
//                    final JSONObject jsonObject = new JSONObject(s);
//                    if (jsonObject.getInt("code") == 200) {
//                        connect(jsonObject.getString("token"));
//
//                        SPUtils.getInstance().put("DEMO_TOKEN", jsonObject.getString("token"));
//                        SPUtils.getInstance().put("userId", jsonObject.getString("userId"));
//                    }
//                } catch (JSONException e) {
//                    e.printStackTrace();
//                }
//            }
//        });
    }

    private void initListener() {
        LogUtils.e(TAG, "initListener:" + UserInfoHelper.getUserInfo().getUid());

        RxView.clicks(btnCreate).throttleFirst(200, TimeUnit.MILLISECONDS).subscribe(new Action1<Void>() {
            @Override
            public void call(Void aVoid) {
                mPresenter.createGroup(UserInfoHelper.getUserInfo().getUid(), etClassGroup.getText().toString().trim(), "");
            }
        });

    }

    @Override
    public int getLayoutId() {
        return R.layout.group_activity_create_class;
    }



    @Override
    public void showCreateResult(ClassInfo data) {
        LogUtils.e(data.getGroupId());
    }


}
