package com.yc.english.community.view.activitys;

import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;

import com.yc.english.R;
import com.yc.english.base.view.FullScreenActivity;
import com.yc.english.community.contract.CommunityInfoContract;
import com.yc.english.community.model.domain.CommunityInfo;
import com.yc.english.community.presenter.CommunityInfoPresenter;
import com.yc.english.community.view.adapter.CommunityItemClickAdapter;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;

/**
 * Created by admin on 2017/7/25.
 */

public class CommunityActivity extends FullScreenActivity<CommunityInfoPresenter> implements CommunityInfoContract.View {

    @BindView(R.id.all_note_list)
    RecyclerView mAllNoteRecyclerView;

    CommunityItemClickAdapter mAdapter;

    @Override
    public int getLayoutId() {
        return R.layout.community_all_note;
    }

    @Override
    public void init() {
        mToolbar.setTitle(getString(R.string.community_name));
        mToolbar.showNavigationIcon();

        //mPresenter = new CommunityInfoPresenter(this, this);
        List<CommunityInfo> list = new ArrayList<CommunityInfo>();
        for (int i = 0; i < 10; i++) {
            CommunityInfo communityInfo = new CommunityInfo(CommunityInfo.CLICK_ITEM_VIEW);
            communityInfo.setCommunityNoteTitle("测试发帖标题");
            list.add(communityInfo);
        }

        mAdapter = new CommunityItemClickAdapter(this, list);
        mAllNoteRecyclerView.setLayoutManager(new LinearLayoutManager(this));
        mAllNoteRecyclerView.setAdapter(mAdapter);
    }

    @Override
    public void hideStateView() {
        //mStateView.hide();
    }

    @Override
    public void showNoNet() {
        /*mStateView.showNoNet(mLayoutContext, "网络不给力", new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //mPresenter.getBookInfoById(bookId);
            }
        });*/
    }

    @Override
    public void showNoData() {
        //mStateView.showNoData(mLayoutContext);
    }

    @Override
    public void showLoading() {
        //mStateView.showLoading(mLayoutContext);
    }

    @Override
    public void showCommunityInfoListData(List<CommunityInfo> list) {

    }
}
