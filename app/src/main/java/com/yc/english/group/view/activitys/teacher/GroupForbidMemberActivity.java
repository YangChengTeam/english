package com.yc.english.group.view.activitys.teacher;

import android.content.Intent;
import android.graphics.Color;
import android.support.v7.widget.LinearLayoutManager;
import android.view.View;
import android.widget.ImageView;

import com.kk.securityhttp.net.contains.HttpConfig;
import com.yc.english.R;
import com.yc.english.base.view.BaseToolBar;
import com.yc.english.base.view.FullScreenActivity;
import com.yc.english.base.view.StateView;
import com.yc.english.group.contract.GroupGetForbidMemberContract;
import com.yc.english.group.listener.OnCheckedChangeListener;
import com.yc.english.group.model.bean.GroupInfoHelper;
import com.yc.english.group.model.bean.StudentInfo;
import com.yc.english.group.presenter.GroupGetForbidMemberPresenter;
import com.yc.english.group.rong.models.GagGroupUser;
import com.yc.english.group.view.adapter.GroupForbidMemberAdapter;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import me.yokeyword.indexablerv.IndexableLayout;

/**
 * Created by wanglin  on 2017/8/29 10:06.
 */

public class GroupForbidMemberActivity extends FullScreenActivity<GroupGetForbidMemberPresenter> implements GroupGetForbidMemberContract.View {
    @BindView(R.id.mIndexableLayout)
    IndexableLayout mIndexableLayout;
    @BindView(R.id.stateView)
    StateView stateView;
    private GroupForbidMemberAdapter adapter;


    @Override
    public void init() {
        mPresenter = new GroupGetForbidMemberPresenter(this, this);
        mToolbar.setTitle(getString(R.string.group_member));
        mToolbar.showNavigationIcon();
        mIndexableLayout.setLayoutManager(new LinearLayoutManager(this));
        adapter = new GroupForbidMemberAdapter(this);
        mIndexableLayout.setAdapter(adapter);
        mIndexableLayout.setOverlayStyle_Center();

    }

    private int curCount;
    private int count;
    private ArrayList<StudentInfo> studentInfos = new ArrayList<>();

    private void initListener() {
        curCount = count;
        studentInfos.clear();
        adapter.setOnCheckedChangeListener(new OnCheckedChangeListener<StudentInfo>() {
            @Override
            public void onClick(View view, boolean isClicked, StudentInfo studentInfo) {
                if (view instanceof ImageView) {
                    if (isClicked) {
                        ((ImageView) view).setImageDrawable(getResources().getDrawable(R.mipmap.group24));
                        count++;
                        studentInfos.add(studentInfo);
                    } else {
                        ((ImageView) view).setImageDrawable(getResources().getDrawable(R.mipmap.group23));
                        count--;
                        studentInfos.remove(studentInfo);
                    }
                }

                setMenuTitle(totalList.size(), count, count > curCount ? R.color.group_blue_21b5f8 : R.color.group_gray_999);
            }
        });

        mToolbar.setOnItemClickLisener(new BaseToolBar.OnItemClickLisener() {
            @Override
            public void onClick() {
                if (studentInfos.size() > 0) {
                    Intent intent = getIntent();
                    intent.putParcelableArrayListExtra("studentList", studentInfos);
                    setResult(RESULT_OK, intent);
                    finish();
                }
            }
        });
    }

    @Override
    public int getLayoutId() {
        return R.layout.group_activity_forbid_member;
    }


    @Override
    public void hideStateView() {
        stateView.hide();
    }

    @Override
    public void showNoNet() {
        stateView.showNoNet(mIndexableLayout, HttpConfig.NET_ERROR, new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                getData();
            }
        });
    }

    @Override
    public void showNoData() {
        stateView.showNoData(mIndexableLayout);
    }

    @Override
    public void showLoading() {
        stateView.showLoading(mIndexableLayout);
    }


    private List<StudentInfo> totalList = new ArrayList<>();

    @Override
    public void showGagUserResult(List<GagGroupUser> users, List<StudentInfo> list) {
        list.remove(0);
        setMenuTitle(list.size(), users == null ? 0 : users.size(), R.color.group_gray_999);
        for (StudentInfo studentInfo : list) {
            if (users != null && users.size() > 0) {
                for (GagGroupUser user : users) {

                    if (user.getUserId().equals(studentInfo.getUser_id())) {
                        studentInfo.setIsForbid(true);
                        break;
                    }
                }
                count = users.size();
            }
            totalList.add(studentInfo);
        }

        adapter.setDatas(totalList);
        initListener();
    }

    private void getData() {
        mPresenter.getMemberList(GroupInfoHelper.getGroupInfo().getId(), "1", "", GroupInfoHelper.getClassInfo().getFlag());
    }

    private void setMenuTitle(int totalSize, int selectSize, int colorId) {
        String str = getString(R.string.forbid_confirm);
        mToolbar.setMenuTitle(String.format(str, selectSize, totalSize));
        mToolbar.setMenuTitleColor(getResources().getColor(colorId));
        invalidateOptionsMenu();
    }

}
