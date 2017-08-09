package com.yc.english.group.view.widget;

import android.content.Context;
import android.support.annotation.Nullable;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.yc.english.R;
import com.yc.english.group.constant.GroupConstant;


/**
 * Created by wanglin  on 2017/8/9 11:20.
 */

public class MultifunctionLinearLayout extends LinearLayout {
    private Context mContext;
    private TextView textView;
    private View pictureView;
    private View voiceView;
    private View wordView;
    private View synthesizeView;

    private int currentType = -1;

    private LayoutInflater inflater;


    public MultifunctionLinearLayout(Context context) {
        this(context, null);
    }

    public MultifunctionLinearLayout(Context context, @Nullable AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public MultifunctionLinearLayout(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        this.mContext = context;
        init();
    }

    private void init() {
        inflater = LayoutInflater.from(mContext);
    }

    public void showTextView(String text) {
        currentType = GroupConstant.TASK_TYPE_CHARACTER;
        if (null == textView) {
            textView = new TextView(mContext);
            textView.setText(text);
            textView.setTextColor(mContext.getResources().getColor(R.color.black_333333));
            textView.setTextSize(14);
            MarginLayoutParams layoutParams = (MarginLayoutParams) getLayoutParams();
            layoutParams.leftMargin = 15;
            layoutParams.topMargin = 15;
            textView.setLayoutParams(layoutParams);
            addView(textView);
        }
    }

    private boolean isClick;

    public void showPictureView() {
        currentType = GroupConstant.TASK_TYPE_PICTURE;
        if (null == pictureView) {
            pictureView = inflater.inflate(R.layout.group_publish_task_detail_picture, null);
            RecyclerView recyclerView = (RecyclerView) pictureView.findViewById(R.id.recyclerView);
            recyclerView.setLayoutManager(new LinearLayoutManager(mContext));

            final TextView tv = (TextView) pictureView.findViewById(R.id.tv_look_detail);
            final ImageView iv = (ImageView) pictureView.findViewById(R.id.iv_look_detail);
            pictureView.findViewById(R.id.ll_task_detail).setOnClickListener(new OnClickListener() {
                @Override
                public void onClick(View v) {
                    isClick = !isClick;
                    tv.setText(isClick ? mContext.getString(R.string.pack_up) : getResources().getString(R.string.look_detail));
                    iv.setImageDrawable(isClick ? mContext.getResources().getDrawable(R.mipmap.group3) : mContext.getResources().getDrawable(R.mipmap.group4));
                }
            });
            addView(pictureView);
        }

    }

    public void showVoiceView() {
        currentType = GroupConstant.TASK_TYPE_VOICE;
        if (null == voiceView) {
            voiceView = inflater.inflate(R.layout.group_publish_task_detail_picture, null);
            RecyclerView recyclerView = (RecyclerView) voiceView.findViewById(R.id.recyclerView);
            recyclerView.setLayoutManager(new LinearLayoutManager(mContext));
            voiceView.findViewById(R.id.ll_task_detail).setVisibility(GONE);


            addView(voiceView);
        }
    }

    public void showWordView(){
        currentType=GroupConstant.TASK_TYPE_WORD;
        if (null==wordView){
            wordView =inflater.inflate(R.layout.group_publish_task_detail_picture, null);
            RecyclerView recyclerView = (RecyclerView) wordView.findViewById(R.id.recyclerView);
            recyclerView.setLayoutManager(new LinearLayoutManager(mContext));
            wordView.findViewById(R.id.ll_task_detail).setVisibility(GONE);


            addView(wordView);

        }
    }

    public void  showSynthesizeView(){
        currentType=GroupConstant.TASK_TYPE_SYNTHESIZE;
        if (null==synthesizeView){

        }

    }



}
