package com.yc.english.group.view.activitys;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.FragmentActivity;
import android.view.View;
import android.view.WindowManager;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.yc.english.R;
import com.yc.english.group.rong.models.GroupInfo;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;


/**
 * Created by wanglin  on 2017/7/17 17:06.
 */

public class ChatActivity extends FragmentActivity {

    private static final String TAG = "ChatActivity";
    @BindView(R.id.img1)
    ImageView img1;
    @BindView(R.id.txt1)
    TextView txt1;
    @BindView(R.id.back)
    RelativeLayout back;
    @BindView(R.id.img3)
    ImageView img3;
    @BindView(R.id.rl)
    RelativeLayout rl;

    private GroupInfo group;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_ADJUST_UNSPECIFIED);
        setContentView(R.layout.group_activity_chat);
        ButterKnife.bind(this);
        initData();


    }

    private void initData() {
        Intent intent = getIntent();
        if (intent != null && intent.getData() != null && intent.getData().getScheme().equals("rong")) {
            //rong://com.yc.english/conversation/group?targetId=654321&title=%E9%BE%99
            String groupId = null;
            String title = null;
            Uri data = intent.getData();
            if (data.getQueryParameter("title") != null) {
                title = data.getQueryParameter("title");
                txt1.setText(title);
            }
            if (data.getQueryParameter("targetId") != null) {
                groupId = data.getQueryParameter("title");
            }
            group = new GroupInfo(groupId, title);

        }


    }

    @OnClick({R.id.img1, R.id.img3})
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.img1:
                finish();
                break;
            case R.id.img3:

                Intent intent = new Intent(this, GroupMemberActivity.class);
//                Bundle bundle =new Bundle();
//                bundle.putSerializable("group",group);
                intent.putExtra("group", group);
                startActivity(intent);
                break;
        }
    }

}
