package com.yc.english.group.view.activitys.teacher;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.SwitchCompat;
import android.widget.CompoundButton;
import android.widget.LinearLayout;

import com.example.comm_recyclviewadapter.BaseItemDecoration;
import com.jakewharton.rxbinding.view.RxView;
import com.yc.english.R;
import com.yc.english.base.view.FullScreenActivity;
import com.yc.english.group.model.bean.StudentInfo;
import com.yc.english.group.view.adapter.GroupForbidedMemberAdapter;

import java.util.List;
import java.util.concurrent.TimeUnit;

import butterknife.BindView;
import rx.functions.Action1;

/**
 * Created by wanglin  on 2017/8/29 09:13.
 */

public class GroupForbidTalkActivity extends FullScreenActivity implements CompoundButton.OnCheckedChangeListener {
    @BindView(R.id.mRecyclerView)
    RecyclerView mRecyclerView;
    @BindView(R.id.m_ll_add_forbid_member)
    LinearLayout mLlAddForbidMember;
    @BindView(R.id.mSwitchCompat)
    SwitchCompat mSwitchCompat;
    private GroupForbidedMemberAdapter adapter;

    @Override
    public void init() {
        mToolbar.setTitle(getString(R.string.group_forbid));
        mToolbar.showNavigationIcon();
        mRecyclerView.setLayoutManager(new LinearLayoutManager(this));
        adapter = new GroupForbidedMemberAdapter(this, null);
        mRecyclerView.setAdapter(adapter);
        BaseItemDecoration itemDecoration = new BaseItemDecoration(this);
        mRecyclerView.addItemDecoration(itemDecoration);
        initListener();

    }

    private void initListener() {
        mSwitchCompat.setOnCheckedChangeListener(this);
        RxView.clicks(mLlAddForbidMember).throttleFirst(200, TimeUnit.MILLISECONDS).subscribe(new Action1<Void>() {
            @Override
            public void call(Void aVoid) {
                startActivityForResult(new Intent(GroupForbidTalkActivity.this, GroupForbidMemberActivity.class), 200);
            }
        });
    }

    @Override
    public int getLayoutId() {
        return R.layout.group_activity_forbid_talk;
    }


    @Override
    public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {

    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == 200 && resultCode == RESULT_OK && data != null) {
            List<StudentInfo> studentList = data.getParcelableArrayListExtra("studentList");
            showDialog(studentList);

        }
    }


    public void showDialog(final List<StudentInfo> studentList) {


        // 创建数据
        final String[] items = getResources().getStringArray(R.array.forbid_time);
        // 创建对话框构建器

        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        // 设置参数
        builder.setTitle(getString(R.string.add_forbid_member))
                .setItems(items, new DialogInterface.OnClickListener() {

                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        adapter.setData(studentList);
                        adapter.setForbidTime(items[which]);
                    }
                });
        builder.create().show();
    }
}
