package com.yc.english.group.view.activitys.teacher;

import android.content.Intent;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.view.View;
import android.widget.CompoundButton;
import android.widget.ImageView;
import android.widget.TextView;

import com.alibaba.fastjson.JSONObject;
import com.blankj.utilcode.util.LogUtils;
import com.blankj.utilcode.util.SPUtils;
import com.jakewharton.rxbinding.view.RxView;
import com.yc.english.R;
import com.yc.english.base.view.BaseToolBar;
import com.yc.english.base.view.FullScreenActivity;
import com.yc.english.group.constant.GroupConstant;
import com.yc.english.group.contract.GroupSyncGroupContract;
import com.yc.english.group.listener.OnCheckedChangeListener;
import com.yc.english.group.model.bean.ClassInfo;
import com.yc.english.group.model.bean.StudentInfo;
import com.yc.english.group.presenter.GroupSyncGroupPresenter;
import com.yc.english.group.view.adapter.GroupSyncListAdapter;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.TimeUnit;

import butterknife.BindView;
import rx.functions.Action1;

/**
 * Created by wanglin  on 2017/7/27 16:17.
 */

public class GroupSyncGroupListActivity extends FullScreenActivity<GroupSyncGroupPresenter> implements OnCheckedChangeListener<ClassInfo>, GroupSyncGroupContract.View {
    @BindView(R.id.m_recyclerView)
    RecyclerView mRecyclerView;
    @BindView(R.id.m_tv_confirm_sync_group)
    TextView mTvConfirmSyncGroup;
    private GroupSyncListAdapter adapter;
    private static final String TAG = "GroupSyncGroupListActiv";

    @Override
    public void init() {
        mPresenter = new GroupSyncGroupPresenter(this, this);
        mToolbar.setTitle(getString(R.string.group_list));
        mToolbar.showNavigationIcon();


        mRecyclerView.setLayoutManager(new LinearLayoutManager(this));

        adapter = new GroupSyncListAdapter(this, null);
        mRecyclerView.setAdapter(adapter);

        initListener();

    }

    private void initListener() {
        adapter.setListener(this);
        RxView.clicks(mTvConfirmSyncGroup).throttleFirst(200, TimeUnit.MILLISECONDS).subscribe(new Action1<Void>() {
            @Override
            public void call(Void aVoid) {

                setSyncResult();
            }
        });
    }

    private void setSyncResult() {
        Intent intent = new Intent();
        intent.putParcelableArrayListExtra("selectedList", classInfos);
        setResult(RESULT_OK, intent);
        SPUtils.getInstance().clear();
        for (ClassInfo classInfo : classInfos) {
            SPUtils.getInstance().put(classInfo.getClass_id()+ "class", true);
        }
        finish();
    }


    @Override
    public int getLayoutId() {
        return R.layout.group_activity_group_list;
    }

    private int count;

    private ArrayList<ClassInfo> classInfos = new ArrayList<>();

    @Override
    public void onClick(int position, View view, boolean isClicked, ClassInfo classInfo) {
        if (view instanceof ImageView) {
            if (isClicked) {
                ((ImageView) view).setImageDrawable(getResources().getDrawable(R.mipmap.group24));
                count++;
                classInfos.add(classInfo);
            } else {
                ((ImageView) view).setImageDrawable(getResources().getDrawable(R.mipmap.group23));
                count--;
                classInfos.remove(classInfo);
            }
        }
        mTvConfirmSyncGroup.setText(String.format(getString(R.string.confirm_sync), count));
        mTvConfirmSyncGroup.setVisibility(count > 0 ? View.VISIBLE : View.GONE);
    }

    @Override
    public void showMyGroupList(List<ClassInfo> list) {

        adapter.setData(list);
        for (ClassInfo classInfo : list) {
            boolean aBoolean = SPUtils.getInstance().getBoolean(classInfo.getClass_id() + "class");
            if (aBoolean) {
                count++;
                classInfos.add(classInfo);
            }
        }
        mTvConfirmSyncGroup.setText(String.format(getString(R.string.confirm_sync), count));
        mTvConfirmSyncGroup.setVisibility(count > 0 ? View.VISIBLE : View.GONE);

    }

    @Override
    public void onBackPressed() {
        setSyncResult();
    }
}
