package com.yc.english.composition.fragment;

import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.TextView;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.jakewharton.rxbinding.view.RxView;
import com.yc.english.R;
import com.yc.english.composition.activity.CompositionDetailActivity;
import com.yc.english.composition.activity.CompositionMoreActivity;
import com.yc.english.composition.adapter.EssayItemAdapter;
import com.yc.english.composition.contract.EssayContract;
import com.yc.english.composition.model.bean.CompositionInfo;
import com.yc.english.composition.model.bean.CompositionInfoWrapper;
import com.yc.english.composition.presenter.EssayPresenter;
import com.yc.soundmark.category.utils.ItemDecorationHelper;

import java.util.List;
import java.util.concurrent.TimeUnit;

import butterknife.BindView;
import rx.functions.Action1;
import yc.com.base.BaseFragment;

/**
 * Created by wanglin  on 2019/3/22 16:32.
 * 精选范文
 */
public class EssayFragment extends BaseFragment<EssayPresenter> implements EssayContract.View {
    @BindView(R.id.exam_recyclerView)
    RecyclerView examRecyclerView;
    @BindView(R.id.exercise_recyclerView)
    RecyclerView exerciseRecyclerView;
    @BindView(R.id.tv_exam_more)
    TextView tvExamMore;
    @BindView(R.id.tv_exercise_more)
    TextView tvExerciseMore;

    private EssayItemAdapter essayAdapter;
    private EssayItemAdapter essayAdapter1;


    @Override
    public int getLayoutId() {
        return R.layout.fragment_essay;
    }

    @Override
    public void init() {
        mPresenter = new EssayPresenter(getActivity(), this);


        examRecyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));
        essayAdapter = new EssayItemAdapter(null, false);
        examRecyclerView.setAdapter(essayAdapter);
        examRecyclerView.addItemDecoration(new ItemDecorationHelper(getActivity(), 10));
        exerciseRecyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));
        essayAdapter1 = new EssayItemAdapter(null, false);
        exerciseRecyclerView.setAdapter(essayAdapter1);
        exerciseRecyclerView.addItemDecoration(new ItemDecorationHelper(getActivity(), 10));

        mPresenter.getCompositionIndexInfo();

        initListener();
    }


    private void initListener() {
        RxView.clicks(tvExamMore).throttleFirst(200, TimeUnit.MILLISECONDS).subscribe(new Action1<Void>() {
            @Override
            public void call(Void aVoid) {
                CompositionMoreActivity.startActivity(getActivity(), getString(R.string.exam_sat), examId);
            }
        });
        RxView.clicks(tvExerciseMore).throttleFirst(200, TimeUnit.MILLISECONDS).subscribe(new Action1<Void>() {
            @Override
            public void call(Void aVoid) {
                CompositionMoreActivity.startActivity(getActivity(), getString(R.string.everyday_exercise), exerciseId);
            }
        });
        essayAdapter.setOnItemClickListener(new BaseQuickAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(BaseQuickAdapter adapter, View view, int position) {
                CompositionInfo compositionInfo = essayAdapter.getItem(position);
                if (compositionInfo != null) {
                    CompositionDetailActivity.startActivity(getActivity(), compositionInfo.getId());
                    mPresenter.statisticsReadCount(compositionInfo.getId());
                }
            }
        });
        essayAdapter1.setOnItemClickListener(new BaseQuickAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(BaseQuickAdapter adapter, View view, int position) {
                CompositionInfo compositionInfo = essayAdapter1.getItem(position);
                if (compositionInfo != null) {
                    CompositionDetailActivity.startActivity(getActivity(), compositionInfo.getId());
                    mPresenter.statisticsReadCount(compositionInfo.getId());
                }
            }
        });

    }

    @Override
    public void showCompositionInfos(List<CompositionInfo> compositionInfos) {

    }

    private String examId;
    private String exerciseId;

    @Override
    public void showCompositionIndexInfo(CompositionInfoWrapper data) {
        List<CompositionInfo> kaoshi = data.getKaoshi();
        if (kaoshi != null && kaoshi.size() > 0) {
            examId = kaoshi.get(0).getAttrid();
        }
        essayAdapter.setNewData(kaoshi);
        List<CompositionInfo> richang = data.getRichang();
        if (richang != null && richang.size() > 0) {
            exerciseId = richang.get(0).getAttrid();
        }
        essayAdapter1.setNewData(richang);
    }

    @Override
    public void showBanner(List<String> images) {

    }


    @Override
    public void hide() {

    }

    @Override
    public void showLoading() {

    }

    @Override
    public void showNoData() {

    }

    @Override
    public void showNoNet() {

    }
}
