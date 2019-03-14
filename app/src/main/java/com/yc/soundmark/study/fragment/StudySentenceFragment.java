package com.yc.soundmark.study.fragment;

import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.chad.library.adapter.base.BaseQuickAdapter;
import com.yc.english.R;
import com.yc.soundmark.study.adapter.StudySentenceAdapter;
import com.yc.soundmark.study.listener.OnAVManagerListener;
import com.yc.soundmark.study.listener.OnUIApplyControllerListener;
import com.yc.soundmark.study.model.domain.SentenceInfo;
import com.yc.soundmark.study.utils.AVManager;

import java.util.List;

import butterknife.BindView;
import yc.com.base.BaseFragment;

/**
 * Created by wanglin  on 2018/10/26 14:14.
 */
public class StudySentenceFragment extends BaseFragment implements OnUIApplyControllerListener {
    @BindView(R.id.recyclerView)
    RecyclerView recyclerView;



    private boolean isFirst = true;

    private OnAVManagerListener listener;
    private SentenceInfo currentInfo;
    private StudySentenceAdapter studySentenceAdapter;
    private String parent;

    private ImageView playImg;
    private ImageView recordImg;
    private LinearLayout layoutResult;
    private ImageView ivSpeakResult;
    private TextView tvResultHint;
    private ImageView ivRecordPlayback;

    @Override
    public int getLayoutId() {
        return R.layout.fragment_study_apply;
    }

    @Override
    public void init() {

        List<SentenceInfo> sentenceInfos = getArguments().getParcelableArrayList("studyInfo");
        parent = getArguments().getString("pos");

        if (sentenceInfos != null && sentenceInfos.size() > 0) {
            currentInfo = sentenceInfos.get(0);
        }
        recyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));

        recyclerView.setHasFixedSize(true);
        studySentenceAdapter = new StudySentenceAdapter(sentenceInfos);
        recyclerView.setAdapter(studySentenceAdapter);
//        if (isFirst) {
//            recyclerView.addItemDecoration(new ItemDecorationHelper(getActivity(), 10));
//            isFirst = false;
//        }
        initListener();

    }


    private void initListener() {
        studySentenceAdapter.setOnItemClickListener(new BaseQuickAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(BaseQuickAdapter adapter, View view, int position) {
                playImg = (ImageView) adapter.getViewByPosition(recyclerView, position, R.id.iv_play);
                layoutResult = (LinearLayout) adapter.getViewByPosition(recyclerView, position, R.id.layout_result);
                studySentenceAdapter.startAnimation(position);

                studySentenceAdapter.showActionContainer(position);
                resetState();
                view.setSelected(true);
                currentInfo = studySentenceAdapter.getItem(position);
                startPlay();
            }
        });


        studySentenceAdapter.setOnItemChildClickListener(new BaseQuickAdapter.OnItemChildClickListener() {
            @Override
            public void onItemChildClick(BaseQuickAdapter adapter, View view, int position) {
                layoutResult = (LinearLayout) adapter.getViewByPosition(recyclerView, position, R.id.layout_result);
                currentInfo = studySentenceAdapter.getItem(position);
                int i = view.getId();
                if (i == R.id.ll_play) {// TODO: 2018/11/1 播放
                    playImg = (ImageView) adapter.getViewByPosition(recyclerView, position, R.id.iv_play);
                    startPlay();

                } else if (i == R.id.ll_record) {
                    if (currentInfo == null) return;
                    recordImg = (ImageView) adapter.getViewByPosition(recyclerView, position, R.id.iv_record);

                    ivSpeakResult = (ImageView) adapter.getViewByPosition(recyclerView, position, R.id.iv_speak_result);
                    tvResultHint = (TextView) adapter.getViewByPosition(recyclerView, position, R.id.tv_result_hint);
                    if (listener.isRecording()) {
                        listener.stopRecord();
                    } else {
                        listener.startRecordAndSynthesis(currentInfo.getSentence().replaceAll("#", ""), false);
                    }

                } else if (i == R.id.ll_record_playback) {
                    ivRecordPlayback = (ImageView) adapter.getViewByPosition(recyclerView, position, R.id.iv_record_playback);
                    listener.playRecordFile();

                }

            }
        });


    }


    private void startPlay() {
        if (currentInfo == null) return;
        listener.playMusic(currentInfo.getMp3());
    }

    private void resetState() {
        int count = recyclerView.getChildCount();
        for (int i = 0; i < count; i++) {
            recyclerView.getChildAt(i).setSelected(false);
        }
    }

    @Override
    protected void initView() {
        recyclerView = (RecyclerView) getChildView(R.id.recyclerView);
    }

    @Override
    public void fetchData() {
        super.fetchData();
        listener = new AVManager(getActivity(), this, parent + "/sentence");
    }

    @Override
    public void playBeforeUpdateUI() {
        layoutResult.setVisibility(View.GONE);
        playImg.setImageResource(R.mipmap.study_essentials_pause);
        studySentenceAdapter.resetDrawable();
    }


    @Override
    public void playAfterUpdateUI() {
        playImg.setImageResource(R.mipmap.study_essentials_play);
    }

    @Override
    public void recordBeforeUpdateUI() {
        layoutResult.setVisibility(View.GONE);
        Glide.with(getActivity()).asGif().load(R.mipmap.study_essentials_record_stop).into(recordImg);
    }

    @Override
    public void recordAfterUpdataUI() {
        Glide.with(getActivity()).clear(recordImg);
        recordImg.setImageResource(R.mipmap.study_essentials_record_start);
    }

    @Override
    public void playRecordBeforeUpdateUI() {
        ivRecordPlayback.setImageResource(R.mipmap.study_essentials_playback_stop);
    }

    @Override
    public void playRecordAfterUpdateUI() {
        ivRecordPlayback.setImageResource(R.mipmap.study_essentials_playback_start);
    }

    @Override
    public void showEvaluateResult(String percent) {

        int result = (int) Float.parseFloat(percent);
        layoutResult.setVisibility(View.VISIBLE);
        if (result >= 60) {
            ivSpeakResult.setImageResource(R.mipmap.read_item_result_yes);
            tvResultHint.setText(String.format(getString(R.string.evaluating_good_result), result + ""));
        } else {
            ivSpeakResult.setImageResource(R.mipmap.listen_result_no);
            tvResultHint.setText("加油");
        }
    }

    @Override
    public void playErrorUpdateUI() {
        studySentenceAdapter.resetDrawable();
    }


}
