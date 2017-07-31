package com.yc.english.group.view.activitys;

import android.content.Intent;
import android.net.Uri;
import android.view.WindowManager;

import com.yc.english.R;
import com.yc.english.base.view.BaseToolBar;
import com.yc.english.base.view.FullScreenActivity;
import com.yc.english.group.rong.models.GroupInfo;


/**
 * Created by wanglin  on 2017/7/17 17:06.
 */

public class ChatActivity extends FullScreenActivity implements BaseToolBar.OnItemClickLisener {

    private static final String TAG = "ChatActivity";


    private GroupInfo group;

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
                groupId = data.getQueryParameter("title");
            }
            group = new GroupInfo(groupId, title);

        }


    }

    @Override
    public void init() {

        initData();
        mToolbar.showNavigationIcon();
        mToolbar.setMenuIcon(R.mipmap.group9);


        mToolbar.setOnItemClickLisener(this);
        mToolbar.setOnMenuItemClickListener();

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
