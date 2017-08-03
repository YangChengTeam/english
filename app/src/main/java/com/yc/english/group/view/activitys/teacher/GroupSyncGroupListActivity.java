package com.yc.english.group.view.activitys.teacher;

import android.content.Intent;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.CompoundButton;
import android.widget.TextView;

import com.jakewharton.rxbinding.view.RxView;
import com.yc.english.R;
import com.yc.english.base.view.FullScreenActivity;
import com.yc.english.group.listener.OnCheckedChangeListener;
import com.yc.english.group.model.bean.ClassInfo;
import com.yc.english.group.view.adapter.GroupSyncListAdapter;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.TimeUnit;

import butterknife.BindView;
import rx.functions.Action1;

/**
 * Created by wanglin  on 2017/7/27 16:17.
 */

public class GroupSyncGroupListActivity extends FullScreenActivity implements OnCheckedChangeListener {
    @BindView(R.id.m_recyclerView)
    RecyclerView mRecyclerView;
    @BindView(R.id.m_tv_confirm_sync_group)
    TextView mTvConfirmSyncGroup;
    private GroupSyncListAdapter adapter;

    @Override
    public void init() {
        mToolbar.setTitle(getString(R.string.group_list));
        mToolbar.showNavigationIcon();

        mRecyclerView.setLayoutManager(new LinearLayoutManager(this));

        adapter = new GroupSyncListAdapter(this, null);
        mRecyclerView.setAdapter(adapter);
        initData();
        initListener();

    }

    private void initListener() {
        adapter.setListener(this);
        RxView.clicks(mTvConfirmSyncGroup).throttleFirst(200, TimeUnit.MILLISECONDS).subscribe(new Action1<Void>() {
            @Override
            public void call(Void aVoid) {
                Intent intent = new Intent();
                intent.putParcelableArrayListExtra("selectedList", selectedList);
                setResult(RESULT_OK,intent);
                finish();
            }
        });
    }

    private List<ClassInfo> mList = new ArrayList<>();

    private ArrayList<ClassInfo> selectedList = new ArrayList<>();

    private void initData() {
        mList.add(new ClassInfo("", "武汉街道口小学三年级六班", 0));
        mList.add(new ClassInfo("", "武汉洪山区小学二年级四班", 0));
        mList.add(new ClassInfo("", "武汉江夏区小学六年级五班", 0));
        adapter.setData(mList);

    }

    @Override
    public int getLayoutId() {
        return R.layout.group_activity_group_list;
    }

    private int count;


    @Override
    public void onCheckedChange(int position, CompoundButton buttonView, boolean isChecked) {
        if (isChecked) {
            count++;
            selectedList.add(mList.get(position));
        } else {
            count--;
            selectedList.remove(mList.get(position));
        }
        mTvConfirmSyncGroup.setText(String.format(getString(R.string.confirm_sync), count));
        mTvConfirmSyncGroup.setVisibility(count > 0 ? View.VISIBLE : View.GONE);
    }


}
