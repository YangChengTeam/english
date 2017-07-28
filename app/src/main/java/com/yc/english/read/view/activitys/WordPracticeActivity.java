package com.yc.english.read.view.activitys;

import android.annotation.SuppressLint;
import android.support.v4.content.ContextCompat;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.blankj.utilcode.util.StringUtils;
import com.blankj.utilcode.util.ToastUtils;
import com.chad.library.adapter.base.BaseQuickAdapter;
import com.yc.english.R;
import com.yc.english.base.utils.DrawableUtils;
import com.yc.english.base.view.FullScreenActivity;
import com.yc.english.read.model.domain.LetterInfo;
import com.yc.english.read.view.adapter.ReadLetterItemClickAdapter;
import com.yc.english.read.view.wdigets.CountDown;
import com.yc.english.read.view.wdigets.GridSpacingItemDecoration;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.OnClick;

/**
 * Created by admin on 2017/7/26.
 */

public class WordPracticeActivity extends FullScreenActivity {

    @BindView(R.id.rv_letter_list)
    RecyclerView mLetterRecyclerView;

    @BindView(R.id.et_word_input)
    TextView mWordInputTextView;

    @BindView(R.id.iv_word_delete)
    ImageView mWordDeleteImageView;

    @BindView(R.id.tv_word_count_down)
    TextView mWordCountDownTextView;

    @BindView(R.id.btn_check_word)
    Button mCheckWordButton;

    @BindView(R.id.layout_letter_list)
    RelativeLayout mLetterListLayout;

    @BindView(R.id.layout_right)
    RelativeLayout mRightLayout;

    @BindView(R.id.layout_error)
    RelativeLayout mErrorLayout;

    @BindView(R.id.btn_error_again_word)
    Button mErrorAgainButton;

    @BindView(R.id.btn_error_next_word)
    Button mErrorNextButton;

    private String[] mLetterListValues;

    private ReadLetterItemClickAdapter mLetterAdapter;

    private List<LetterInfo> mLetterDatas;

    @Override
    public int getLayoutID() {
        return R.layout.read_activity_word_practice;
    }

    CountDown countDown;

    @Override
    public void init() {

        mToolbar.setTitle("5/10");
        mToolbar.showNavigationIcon();

        mLetterListValues = new String[]{"m", "p", "a", "o", "g", "s", "e", "p", "w", "y", "k", "b", "s", "o", "c"};

        mLetterDatas = new ArrayList<LetterInfo>();

        for (int i = 0; i < mLetterListValues.length; i++) {
            LetterInfo letterInfo = new LetterInfo(LetterInfo.CLICK_ITEM_VIEW);
            letterInfo.setLetterName(mLetterListValues[i]);
            mLetterDatas.add(letterInfo);
        }

        GridLayoutManager layoutManager = new GridLayoutManager(this, 5, GridLayoutManager.VERTICAL, false);
        mLetterRecyclerView.setLayoutManager(layoutManager);
        mLetterRecyclerView.addItemDecoration(new GridSpacingItemDecoration(5, getResources().getDimensionPixelSize(R.dimen.dp_6), true));
        mLetterRecyclerView.setHasFixedSize(true);

        mLetterRecyclerView.setLayoutManager(layoutManager);
        mLetterAdapter = new ReadLetterItemClickAdapter(this, mLetterDatas);
        mLetterRecyclerView.setAdapter(mLetterAdapter);
        mLetterAdapter.setOnItemClickListener(new BaseQuickAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(BaseQuickAdapter adapter, View view, int position) {
                mWordInputTextView.setText(mWordInputTextView.getText() + mLetterDatas.get(position).getLetterName());
                if (!StringUtils.isEmpty(mWordInputTextView.getText())) {
                    mWordDeleteImageView.setVisibility(View.VISIBLE);
                }
            }
        });

        countDown = new CountDown(120000, 1000) {
            @Override
            public void onTick(long millisUntilFinished) {
                mWordCountDownTextView.setText(toClock(millisUntilFinished));
            }

            @Override
            public String toClock(long millis) {
                return super.toClock(millis);
            }
        };
        countDown.start();
    }

    @OnClick(R.id.iv_word_delete)
    public void deleteWordInput() {
        mWordInputTextView.setText("");
    }

    @SuppressLint("NewApi")
    @OnClick(R.id.btn_check_word)
    public void checkWord() {
        if (StringUtils.isEmpty(mWordInputTextView.getText())) {
            ToastUtils.showLong("请输入单词");
            return;
        }

        if ("book".equals(mWordInputTextView.getText())) {
            mRightLayout.setVisibility(View.VISIBLE);
            mErrorLayout.setVisibility(View.INVISIBLE);
            mLetterListLayout.setVisibility(View.GONE);
            mWordInputTextView.setTextColor(ContextCompat.getColor(this,R.color.right_word_color));
        } else {
            mRightLayout.setVisibility(View.INVISIBLE);
            mErrorLayout.setVisibility(View.VISIBLE);
            mLetterListLayout.setVisibility(View.GONE);
            mWordInputTextView.setTextColor(ContextCompat.getColor(this,R.color.read_word_share_btn_color));
            mErrorAgainButton.setBackground(DrawableUtils.getBgColor(this,3,R.color.right_word_btn_again_color));
        }
    }

    @OnClick(R.id.btn_error_again_word)
    public void inputAgain() {
        nextWord(0);
    }

    @OnClick(R.id.btn_error_next_word)
    public void nextWordInError() {
        nextWord(0);
    }

    @OnClick(R.id.btn_right_next_word)
    public void nextWordInERight() {
        nextWord(0);
    }

    public void nextWord(int wordPosition){
        mLetterListLayout.setVisibility(View.VISIBLE);
        mRightLayout.setVisibility(View.INVISIBLE);
        mErrorLayout.setVisibility(View.INVISIBLE);
        mWordInputTextView.setText("");
        mWordInputTextView.setTextColor(ContextCompat.getColor(this,R.color.black_333));
        countDown.start();
    }
}
