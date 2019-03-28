package com.yc.junior.english.weixin.views.activitys;

import android.content.Intent;
import android.net.Uri;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.Button;
import android.widget.RelativeLayout;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.jakewharton.rxbinding.view.RxView;
import com.kk.utils.LogUtil;
import com.umeng.analytics.MobclickAgent;
import com.yc.junior.english.R;
import com.yc.junior.english.base.view.FullScreenActivity;
import com.yc.junior.english.base.view.StateView;
import com.yc.junior.english.news.view.activity.NewsDetailActivity;
import com.yc.junior.english.weixin.contract.CourseContract;
import com.yc.junior.english.weixin.model.domain.CourseInfo;
import com.yc.junior.english.weixin.presenter.CoursePresenter;
import com.yc.junior.english.weixin.views.adapters.CourseAdapter;

import java.util.List;
import java.util.concurrent.TimeUnit;

import butterknife.BindView;
import rx.functions.Action1;

/**
 * Created by zhangkai on 2017/9/6.
 */

public class CourseActivity extends FullScreenActivity<CoursePresenter> implements CourseContract.View {
    @BindView(R.id.sv_loading)
    StateView mLoadingStateView;

    @BindView(R.id.rv_course)
    RecyclerView mCourseRecyclerView;
    @BindView(R.id.btn_download)
    Button btnDownload;
    @BindView(R.id.rl_container)
    RelativeLayout rlContainer;

    private CourseAdapter mCourseAdapter;
    private String type = "";
    private int page = 1;
    private int pageSize = 20;
    private String downloadUrl = "http://zhushou.360.cn/detail/index/soft_id/3975738?recrefer=SE_D_51%E7%AD%94%E6%A1%88";

    @Override
    public void init() {
        mPresenter = new CoursePresenter(this, this);
        final Intent intent = getIntent();
        if (intent != null) {
            mToolbar.setTitle(intent.getStringExtra("title") + "");
            type = intent.getStringExtra("type") + "";
            if (type.equals("17")) {
                btnDownload.setVisibility(View.VISIBLE);
            }
        }
        mToolbar.showNavigationIcon();

        LinearLayoutManager layoutManager = new LinearLayoutManager(this);

        mCourseRecyclerView.setLayoutManager(layoutManager);
        mCourseAdapter = new CourseAdapter(null);
        mCourseRecyclerView.setAdapter(mCourseAdapter);

        mCourseAdapter.setOnItemClickListener(new BaseQuickAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(BaseQuickAdapter adapter, View view, int position) {
                if (type.equals("3")) {
                    MobclickAgent.onEvent(CourseActivity.this, "toady_hot_click", "今日热点");
                }

                if (type.equals("17")) {
                    MobclickAgent.onEvent(CourseActivity.this, "teacher_answer_click", "教材答案点击详情");

                }
                Intent intent = new Intent(CourseActivity.this, NewsDetailActivity.class);
                intent.putExtra("info", mCourseAdapter.getData().get(position));
                startActivity(intent);
            }
        });

        mCourseAdapter.setOnLoadMoreListener(new BaseQuickAdapter.RequestLoadMoreListener() {
            @Override
            public void onLoadMoreRequested() {
                mPresenter.getWeiXinList(type, page , pageSize);
            }
        }, mCourseRecyclerView);

        mPresenter.getWeiXinList(type, page, pageSize );

        RxView.clicks(btnDownload).throttleFirst(200, TimeUnit.MILLISECONDS).subscribe(new Action1<Void>() {
            @Override
            public void call(Void aVoid) {
                MobclickAgent.onEvent(CourseActivity.this, "download_51answer", "51答案下载");
//                Intent intent1 = new Intent(CourseActivity.this, WebActivity.class);
                Intent intent1 = new Intent();
                intent1.setData(Uri.parse(downloadUrl));
                intent1.setAction(Intent.ACTION_VIEW);
//                intent1.putExtra("url", downloadUrl);
                startActivity(intent1);
            }
        });

    }

    @Override
    public int getLayoutId() {
        return R.layout.weixin_activity_course;
    }

    @Override
    public void hide() {
        mLoadingStateView.hide();
    }

    @Override
    public void showNoNet() {
        mLoadingStateView.showNoNet(rlContainer, "网络不给力", new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mPresenter.getWeiXinList(type, page , pageSize );
            }
        });
    }

    @Override
    public void showNoData() {
        mLoadingStateView.showNoData(rlContainer);
    }

    @Override
    public void showLoading() {
        mLoadingStateView.showLoading(rlContainer);
    }

    @Override
    public void showWeixinList(List<CourseInfo> list) {
        try {

            if (list == null) {
                mCourseAdapter.loadMoreEnd();
                return;
            }

            if (page == 1) {
                mCourseAdapter.setNewData(list);
            } else {
                mCourseAdapter.addData(list);
            }

            if (list.size() == pageSize) {
                page++;
                mCourseAdapter.loadMoreComplete();
            } else {
                mCourseAdapter.loadMoreEnd();
            }
        } catch (Exception e) {
            LogUtil.msg("TAG " + e.getMessage());
        }
    }

    @Override
    public void fail() {
        mCourseAdapter.loadMoreFail();
    }

    @Override
    public void end() {
        mCourseAdapter.loadMoreEnd();
    }

    @Override
    public boolean isStatusBarMateria() {
        return true;
    }
}
