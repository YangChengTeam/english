package com.yc.english.group.view.activitys;

import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.blankj.utilcode.util.ImageUtils;
import com.blankj.utilcode.util.LogUtils;
import com.blankj.utilcode.util.ToastUtils;
import com.kk.securityhttp.domain.GoagalInfo;
import com.yc.english.R;
import com.yc.english.base.view.FullScreenActivity;
import com.yc.english.group.common.GroupApp;
import com.yc.english.group.dao.ClassInfoDao;
import com.yc.english.group.model.bean.ClassInfo;
import com.yc.english.group.rong.ImUtils;
import com.yc.english.group.rong.models.CodeSuccessResult;
import com.yc.english.group.rong.models.GroupUser;
import com.yc.english.group.rong.models.GroupUserQueryResult;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import io.rong.imkit.RongExtension;
import io.rong.imkit.RongIM;
import io.rong.imlib.IRongCallback;
import io.rong.imlib.MessageTag;
import io.rong.imlib.RongIMClient;
import io.rong.imlib.model.Conversation;
import io.rong.imlib.model.Message;
import io.rong.message.ContactNotificationMessage;
import rx.Observable;
import rx.android.schedulers.AndroidSchedulers;
import rx.functions.Action1;
import rx.schedulers.Schedulers;

/**
 * Created by wanglin  on 2017/7/25 08:30.
 */

public class GroupJoinActivity extends FullScreenActivity {
    private static final String TAG = "GroupJoinActivity";
    @BindView(R.id.et_class_group)
    EditText etClassGroup;
    @BindView(R.id.ib_delete)
    ImageButton ibDelete;

    @BindView(R.id.tv_class_name)
    TextView tvClassName;
    @BindView(R.id.ll_class_name)
    LinearLayout llClassName;
    @BindView(R.id.btn_join)
    Button btnJoin;
    @BindView(R.id.roundView)
    ImageView roundView;
    @BindView(R.id.m_tv_tint)
    TextView mTvTint;

    private ClassInfoDao infoDao = GroupApp.getmDaoSession().getClassInfoDao();

    @Override
    public void init() {
        mToolbar.showNavigationIcon();
        roundView.setImageBitmap(ImageUtils.toRound(BitmapFactory.decodeResource(getResources(), R.mipmap.portial)));
        initListener();
    }

    private void initListener() {
        etClassGroup.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(Editable s) {
                showOrHideView(s);

            }
        });

    }

    @Override
    public int getLayoutId() {
        return R.layout.group_activity_join_class;
    }

    @OnClick({R.id.btn_join, R.id.ib_delete})
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.btn_join:
                joinGroup(currentGroupId + "", tvClassName.getText().toString());
                break;
            case R.id.ib_delete:
                etClassGroup.setText("");
                break;
        }

    }


    public void showOrHideView(Editable s) {
        if (!TextUtils.isEmpty(s.toString())) {
            ibDelete.setVisibility(View.VISIBLE);
            llClassName.setVisibility(View.VISIBLE);
            btnJoin.setVisibility(View.VISIBLE);
//            getGroup(Integer.parseInt(s.toString()));
        } else {
            ibDelete.setVisibility(View.GONE);
            llClassName.setVisibility(View.GONE);
            btnJoin.setVisibility(View.GONE);
//            mTvTint.setVisibility(View.GONE);
        }


    }

    private int currentGroupId;

    /**
     * 根据群号查询班群
     *
     * @param groupId
     */

    public void getGroup(final int groupId) {

        Observable.just(groupId).subscribeOn(Schedulers.io()).observeOn(AndroidSchedulers.mainThread()).subscribe(new Action1<Integer>() {
            @Override
            public void call(Integer integer) {
                ClassInfo classInfo = infoDao.queryBuilder().where(ClassInfoDao.Properties.GroupId.eq(integer)).unique();
                if (classInfo != null) {
                    currentGroupId = groupId;
                    tvClassName.setText(classInfo.getClassName());
                    llClassName.setVisibility(View.VISIBLE);
                    btnJoin.setVisibility(View.VISIBLE);
                    mTvTint.setVisibility(View.GONE);
                } else {
                    mTvTint.setVisibility(View.VISIBLE);
                    llClassName.setVisibility(View.GONE);
                }

            }

        });
    }

    public void joinGroup(final String groupId, final String groupName) {
        final String[] userIds = new String[]{GoagalInfo.get().uuid};

        ContactNotificationMessage contactNotificationMessage = ContactNotificationMessage.obtain(ContactNotificationMessage.CONTACT_OPERATION_REQUEST, "", groupId, "加一下好友");
        Message message = Message.obtain(groupId, null, contactNotificationMessage);

        RongIM.getInstance().sendMessage(message, null, null, new IRongCallback.ISendMessageCallback() {


            @Override
            public void onAttached(Message message) {

            }

            @Override
            public void onSuccess(Message message) {
                LogUtils.e(message.getContent());
            }

            @Override
            public void onError(Message message, RongIMClient.ErrorCode errorCode) {

            }
        });


//        ImUtils.queryGroup(groupId).observeOn(AndroidSchedulers.mainThread()).subscribe(new Action1<GroupUserQueryResult>() {
//            @Override
//            public void call(GroupUserQueryResult groupUserQueryResult) {
//                if (groupUserQueryResult.getCode() == 200) {
//                    final List<GroupUser> users = groupUserQueryResult.getUsers();
//                    ImUtils.joinGroup(userIds, groupId, groupName).observeOn(AndroidSchedulers.mainThread()).subscribe(new Action1<CodeSuccessResult>() {
//                        @Override
//                        public void call(CodeSuccessResult codeSuccessResult) {
//                            if (codeSuccessResult.getCode() == 200) {//加入成功
//
//                                ToastUtils.showShort("加入成功");
//                                RongIM.getInstance().startGroupChat(GroupJoinActivity.this, groupId, groupName);
//
//                                ClassInfo info = new ClassInfo("", groupName, users.size(), Integer.parseInt(groupId));
//                                infoDao.insert(info);
//
//                            }
//                        }
//                    });
//
//
//                } else {
//                    ToastUtils.showShort("没有该群组，请重新输入");
//                }
//            }
//        });

    }


}
