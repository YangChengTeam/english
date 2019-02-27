package yc.com.soundmark.study.fragment;

import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.hwangjr.rxbus.annotation.Subscribe;
import com.hwangjr.rxbus.annotation.Tag;
import com.hwangjr.rxbus.thread.EventThread;
import com.jakewharton.rxbinding.view.RxView;
import com.kk.utils.ScreenUtil;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.TimeUnit;

import butterknife.BindView;
import rx.functions.Action1;
import yc.com.base.BaseDialogFragment;
import yc.com.soundmark.R;
import yc.com.soundmark.base.constant.BusAction;
import yc.com.soundmark.base.fragment.BasePayFragment;
import yc.com.soundmark.category.utils.ItemDecorationHelper;
import yc.com.soundmark.index.utils.UserInfoHelper;
import yc.com.soundmark.study.adapter.StudyVowelAdapter;
import yc.com.soundmark.study.contract.StudyVowelContract;
import yc.com.soundmark.study.model.domain.WordInfo;
import yc.com.soundmark.study.presenter.StudyVowelPresenter;

/**
 * Created by wanglin  on 2018/11/1 09:01.
 */
public class StudyVowelFragment extends BaseDialogFragment<StudyVowelPresenter> implements StudyVowelContract.View {


    private RecyclerView vowelRecyclerView;
    private LinearLayout llContainer;
    private ImageView ivVowelClose;

    private List<StudyVowelAdapter> vowelAdapterList;


    @Override
    protected float getWidth() {
        return 0.8f;
    }

    @Override
    public int getAnimationId() {
        return 0;
    }

    @Override
    public int getHeight() {
        return ScreenUtil.getHeight(getActivity()) * 7 / 10;
    }

    @Override
    protected void initView() {
        vowelRecyclerView = (RecyclerView) getView(R.id.vowel_recyclerView);
        llContainer = (LinearLayout) getView(R.id.ll_container);
        ivVowelClose = (ImageView) getView(R.id.iv_vowel_close);
    }

    @Override
    public int getLayoutId() {
        return R.layout.fragment_study_vowel;

    }

    @Override
    public void init() {
        mPresenter = new StudyVowelPresenter(getActivity(), this);

//        GridLayoutManager layoutManager = new GridLayoutManager(getActivity(), 2);
//        vowelRecyclerView.setLayoutManager(layoutManager);
//        vowelRecyclerView.setHasFixedSize(true);
//
//        List<WordInfo> wordInfos = SoundmarkHelper.getWordInfos();
//        studyVowelAdapter = new StudyVowelAdapter(wordInfos);
//
//        vowelRecyclerView.setAdapter(studyVowelAdapter);
//        vowelRecyclerView.addItemDecoration(new ItemDecorationHelper(getActivity(), 10));


        initListener();


    }


    private void initListener() {

        RxView.clicks(ivVowelClose).throttleFirst(200, TimeUnit.MILLISECONDS).subscribe(new Action1<Void>() {
            @Override
            public void call(Void aVoid) {
                dismiss();
            }
        });
    }


    @Override
    public void showVowelInfoList(List<WordInfo> infoList) {

    }

    @Override
    public void shoVowelNewInfos(List<List<WordInfo>> wordInfoList) {
        if (wordInfoList != null) {
            vowelAdapterList = new ArrayList<>();
            LinearLayout.LayoutParams layoutParams = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.WRAP_CONTENT);
            for (List<WordInfo> wordInfos : wordInfoList) {
                RecyclerView recyclerView = new RecyclerView(getActivity());
                recyclerView.setLayoutManager(new GridLayoutManager(getActivity(), 2));
                final StudyVowelAdapter studyVowelAdapter = new StudyVowelAdapter(wordInfos);
                recyclerView.setAdapter(studyVowelAdapter);
                recyclerView.setNestedScrollingEnabled(false);
                recyclerView.addItemDecoration(new ItemDecorationHelper(getActivity(), 10));
                vowelAdapterList.add(studyVowelAdapter);

                studyVowelAdapter.setOnItemClickListener(new BaseQuickAdapter.OnItemClickListener() {
                    @Override
                    public void onItemClick(BaseQuickAdapter adapter, View view, int position) {
                        WordInfo wordInfo = studyVowelAdapter.getItem(position);
                        if (UserInfoHelper.isPhonogramVip() || wordInfo.getIs_vip() == 0) {
                            if (clickListener != null) {
                                clickListener.onClick(wordInfo.getPage());
                                dismiss();
                            }
                        } else {
                            BasePayFragment basePayFragment = new BasePayFragment();
                            basePayFragment.show(getChildFragmentManager(), "");
                        }

                    }
                });

                View view = LayoutInflater.from(getActivity()).inflate(R.layout.vowel_header_view, recyclerView, false);
                TextView tvTitle = view.findViewById(R.id.tv_title);
                tvTitle.setText(wordInfos.get(0).getType_text());
                studyVowelAdapter.addHeaderView(view);

                llContainer.addView(recyclerView, layoutParams);
            }
        }
    }

    private onClickListener clickListener;

    public void setOnClickListener(onClickListener clickListener) {
        this.clickListener = clickListener;
    }


    public interface onClickListener {
        void onClick(int pos);
    }

    @Subscribe(
            thread = EventThread.MAIN_THREAD,
            tags = {
                    @Tag(BusAction.PAY_SUCCESS)
            }
    )
    public void paySuccess(String info) {
        if (vowelAdapterList != null) {
            for (StudyVowelAdapter studyVowelAdapter : vowelAdapterList) {
                studyVowelAdapter.notifyDataSetChanged();
            }
        }
    }
}
