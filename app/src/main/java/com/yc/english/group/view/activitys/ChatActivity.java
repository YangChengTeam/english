package com.yc.english.group.view.activitys;

import android.content.Intent;
import android.media.MediaRecorder;
import android.net.Uri;
import android.text.TextUtils;
import android.view.WindowManager;

import com.blankj.utilcode.util.EmptyUtils;
import com.yc.english.R;
import com.yc.english.base.view.BaseToolBar;
import com.yc.english.base.view.FullScreenActivity;
import com.yc.english.group.common.GroupApp;
import com.yc.english.group.dao.ClassInfoDao;
import com.yc.english.group.model.bean.ClassInfo;
import com.yc.english.group.rong.models.GroupInfo;
import com.yc.english.group.view.activitys.teacher.GroupMemberActivity;


/**
 * Created by wanglin  on 2017/7/17 17:06.
 */

public class ChatActivity extends FullScreenActivity implements BaseToolBar.OnItemClickLisener {

    private static final String TAG = "ChatActivity";


    private GroupInfo group;
    private ClassInfoDao infoDao;

    private void initData() {
        Intent intent = getIntent();
        if (intent != null && intent.getData() != null && intent.getData().getScheme().equals("rong")) {

            //rong://com.yc.english/conversation/group?targetId=654321&title=%E9%BE%99
            String groupId = null;
            String title = null;
            Uri data = intent.getData();
            if (data.getQueryParameter("title") != null) {
                title = data.getQueryParameter("title");
                mToolbar.setTitle(title);
            }
            if (data.getQueryParameter("targetId") != null) {
                groupId = data.getQueryParameter("targetId");
            }

            group = new GroupInfo(groupId, title);

        }


    }

    @Override
    public void init() {

        infoDao = GroupApp.getmDaoSession().getClassInfoDao();
        initData();
        if(EmptyUtils.isNotEmpty(group)) {
            ClassInfo classInfo = infoDao.queryBuilder().where(ClassInfoDao.Properties.GroupId.eq(group.getId())).unique();

            if (classInfo != null && !TextUtils.isEmpty(classInfo.getMaster_id())) {
                mToolbar.setMenuIcon(R.mipmap.group9);
                mToolbar.setOnItemClickLisener(this);
            }
        }

        mToolbar.showNavigationIcon();


    }

    @Override
    public int getLayoutId() {
        getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_ADJUST_UNSPECIFIED);
        return R.layout.group_activity_chat;
    }

    @Override
    public void onClick() {
        Intent intent = new Intent(this, GroupMemberActivity.class);
//                Bundle bundle =new Bundle();
//                bundle.putSerializable("group",group);
        intent.putExtra("group", group);
        startActivity(intent);
    }
}
