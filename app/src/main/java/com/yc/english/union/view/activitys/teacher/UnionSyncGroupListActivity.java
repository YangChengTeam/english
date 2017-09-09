package com.yc.english.union.view.activitys.teacher;

import android.content.Intent;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.blankj.utilcode.util.SPUtils;
import com.jakewharton.rxbinding.view.RxView;
import com.kk.securityhttp.net.contains.HttpConfig;
import com.yc.english.R;
import com.yc.english.base.view.FullScreenActivity;
import com.yc.english.base.view.StateView;
import com.yc.english.group.contract.GroupSyncGroupContract;
import com.yc.english.group.listener.OnCheckedChangeListener;
import com.yc.english.group.model.bean.ClassInfo;
import com.yc.english.group.model.bean.GroupInfoHelper;
import com.yc.english.group.presenter.GroupSyncGroupPresenter;
import com.yc.english.main.hepler.UserInfoHelper;
import com.yc.english.union.view.adapter.GroupSyncListAdapter;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.TimeUnit;

import butterknife.BindView;
import rx.functions.Action1;

/**
 * Created by wanglin  on 2017/7/27 16:17.
 */

public class UnionSyncGroupListActivity extends FullScreenActivity<GroupSyncGroupPresenter> implements OnCheckedChangeListener<ClassInfo>, GroupSyncGroupContract.View {
    @BindView(R.id.m_recyclerView)
    RecyclerView mRecyclerView;
    @BindView(R.id.m_tv_confirm_sync_group)
    TextView mTvConfirmSyncGroup;
    @BindView(R.id.stateView)
    StateView stateView;
    @BindView(R.id.ll_container)
    LinearLayout llContainer;
    private GroupSyncListAdapter adapter;
    private static final String TAG = "GroupSyncGroupListActiv";
    private String classId;

    @Override
    public void init() {
        mPresenter = new GroupSyncGroupPresenter(this, this);
        mToolbar.setTitle(getString(R.string.group_list));
        mToolbar.showNavigationIcon();
        if (getIntent() != null) {
            classId = getIntent().getStringExtra("classId");
        }

        mRecyclerView.setLayoutManager(new LinearLayoutManager(this));

        adapter = new GroupSyncListAdapter(this, null);
        mRecyclerView.setAdapter(adapter);
        getData();
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
        mToolbar.setBackOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                setSyncResult();
            }
        });
    }

    private void setSyncResult() {
        Intent intent = new Intent();
        intent.putParcelableArrayListExtra("selectedList", classInfos);
        setResult(RESULT_OK, intent);

        if (classInfos.size() > 0) {
            for (ClassInfo classInfo : classInfos) {
                SPUtils.getInstance().put(classInfo.getClass_id() + "class", true);
            }
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
    public void onClick(View view, boolean isClicked, ClassInfo classInfo) {
        if (view instanceof ImageView) {
            if (isClicked) {
                ((ImageView) view).setImageDrawable(getResources().getDrawable(R.mipmap.group24));
                count++;
                classInfos.add(classInfo);
            } else {
                ((ImageView) view).setImageDrawable(getResources().getDrawable(R.mipmap.group23));
                count--;
                classInfos.remove(classInfo);
                SPUtils.getInstance().remove(classInfo.getClass_id() + "class");
            }
        }
        mTvConfirmSyncGroup.setText(String.format(getString(R.string.confirm_sync), count));
        mTvConfirmSyncGroup.setVisibility(count > 0 ? View.VISIBLE : View.GONE);
    }

    private ClassInfo currentClassInfo;

    @Override
    public void showMyGroupList(List<ClassInfo> list) {

        if (list != null && list.size() > 0) {
            for (ClassInfo classInfo : list) {
                if (classInfo.getClass_id().equals(classId)) {
                    currentClassInfo = classInfo;
                    break;
                }
            }
            list.remove(currentClassInfo);
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
    }

    @Override
    public void onBackPressed() {
        setSyncResult();
    }

    @Override
    public void hideStateView() {
        stateView.hide();
    }

    @Override
    public void showNoNet() {
        stateView.showNoNet(llContainer, HttpConfig.NET_ERROR, new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                getData();
            }
        });
    }

    @Override
    public void showNoData() {
        stateView.showNoData(llContainer);
    }

    @Override
    public void showLoading() {
        stateView.showLoading(llContainer);
    }

    private void getData() {
        String uid = UserInfoHelper.getUserInfo().getUid();
        mPresenter.getGroupList(this, uid, "1", GroupInfoHelper.getClassInfo().getType());
    }


}
