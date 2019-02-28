package com.yc.junior.english.speak.view.fragment;

import android.content.Intent;
import android.graphics.Rect;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.kk.securityhttp.net.contains.HttpConfig;
import com.yc.junior.english.R;
import com.yc.junior.english.base.view.BaseFragment;
import com.yc.junior.english.base.view.StateView;
import com.yc.junior.english.speak.contract.SpeakEnglishContract;
import com.yc.junior.english.speak.model.bean.SpeakAndReadInfo;
import com.yc.junior.english.speak.model.bean.SpeakEnglishBean;
import com.yc.junior.english.speak.presenter.SpeakEnglishListPresenter;
import com.yc.junior.english.speak.view.activity.SpeakMoreActivity;
import com.yc.junior.english.speak.view.adapter.SpeakEnglishAdapter;

import net.lucode.hackware.magicindicator.buildins.UIUtil;

import java.util.List;

import butterknife.BindView;

/**
 * Created by wanglin  on 2017/10/12 15:00.
 */

public class SpeakFragment extends BaseFragment<SpeakEnglishListPresenter> implements SpeakEnglishContract.View

{
    private static final String TAG = "SpeakFragment";

    @BindView(R.id.recyclerView)
    RecyclerView recyclerView;
    @BindView(R.id.stateView)
    StateView stateView;

    private int type;
    private SpeakEnglishAdapter speakEnglishAdapter;


    @Override
    public void init() {
        mPresenter = new SpeakEnglishListPresenter(getActivity(), this);
        recyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));

        speakEnglishAdapter = new SpeakEnglishAdapter(null, getType());

        recyclerView.setAdapter(speakEnglishAdapter);
        recyclerView.addItemDecoration(new MyItemDecoration());
        initListener();
        getData();

    }

    private void initListener() {
        speakEnglishAdapter.setOnItemChildClickListener(new BaseQuickAdapter.OnItemChildClickListener() {
            @Override
            public boolean onItemChildClick(BaseQuickAdapter adapter, View view, int position) {
                SpeakAndReadInfo speakAndReadInfo = (SpeakAndReadInfo) adapter.getItem(position);
                Intent intent = new Intent(getActivity(), SpeakMoreActivity.class);
                intent.putExtra("speakAndReadInfo", speakAndReadInfo);
                intent.putExtra("type", type);
                startActivity(intent);

                return false;

            }
        });


        final int paddingTop = recyclerView.getPaddingTop();
        recyclerView.addOnScrollListener(new RecyclerView.OnScrollListener() {
            @Override
            public void onScrollStateChanged(RecyclerView recyclerView, int newState) {
                super.onScrollStateChanged(recyclerView, newState);
            }

            @Override
            public void onScrolled(RecyclerView recyclerView, int dx, int dy) {
                super.onScrolled(recyclerView, dx, dy);

                int scrollY = getScollYDistance(recyclerView);
//                LogUtils.e("dy: " + dy + "  scrollY: " + scrollY + "  paddingTop: " + paddingTop + "--" + recyclerView.getPaddingTop());


                //dy>0 向上
                if (dy >= 0) {
                    if (scrollY > paddingTop) {
                        scrollY = paddingTop;
                    }
                    recyclerView.setPadding(recyclerView.getPaddingLeft(), -scrollY, recyclerView.getPaddingRight(), recyclerView.getPaddingBottom());
                } else {

                    if (scrollY > 0) {
                        scrollY = 0;
                    } else {
                        if (Math.abs(scrollY) <= paddingTop) {
                            scrollY = paddingTop;
                        }
                    }
                    recyclerView.setPadding(recyclerView.getPaddingLeft(), scrollY, recyclerView.getPaddingRight(), recyclerView.getPaddingBottom());
                }

            }
        });

    }


    @Override
    public int getLayoutId() {
        return R.layout.speak_fragment_list;
    }

    public void setType(int type) {
        this.type = type;
    }

    public int getType() {
        return type;
    }

    @Override
    public void hide() {
        stateView.hide();
    }

    @Override
    public void showNoNet() {
        stateView.showNoNet(recyclerView, HttpConfig.NET_ERROR, new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                getData();
            }
        });
    }

    @Override
    public void showNoData() {
        stateView.showNoData(recyclerView);
    }

    @Override
    public void showLoading() {
        stateView.showLoading(recyclerView);
    }


    @Override
    public void shoReadAndSpeakMorList(List<SpeakAndReadInfo> list, int page, boolean isFitst) {
        speakEnglishAdapter.setNewData(list);
    }


    private class MyItemDecoration extends RecyclerView.ItemDecoration {
        @Override
        public void getItemOffsets(Rect outRect, View view, RecyclerView parent, RecyclerView.State state) {
            super.getItemOffsets(outRect, view, parent, state);
            outRect.set(0, 0, 0, UIUtil.dip2px(getActivity(), 10));
        }
    }

    private void getData() {
        mPresenter.getReadAndSpeakList(String.valueOf(getType()), "", 1, true);
    }

    @Override
    public void showSpeakEnglishDetail(List<SpeakEnglishBean> list) {

    }

    public int getScollYDistance(RecyclerView recyclerView) {
        LinearLayoutManager layoutManager = (LinearLayoutManager) recyclerView.getLayoutManager();
        int position = layoutManager.findFirstVisibleItemPosition();
        View firstVisiableChildView = layoutManager.findViewByPosition(position);
        int itemHeight = firstVisiableChildView.getHeight();
        return (position) * itemHeight - firstVisiableChildView.getTop();
    }


}
