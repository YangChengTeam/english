package com.yc.english.group.view.widget;

import android.content.Context;
import android.support.annotation.Nullable;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.blankj.utilcode.util.EmptyUtils;
import com.yc.english.R;
import com.yc.english.group.constant.GroupConstant;
import com.yc.english.group.model.bean.Voice;
import com.yc.english.group.view.adapter.GroupFileAdapter;
import com.yc.english.group.view.adapter.GroupPictureAdapter;
import com.yc.english.group.view.adapter.GroupVoiceAdapter;

import java.util.List;

import io.rong.imkit.model.FileInfo;


/**
 * Created by wanglin  on 2017/8/9 11:20.
 */

public class MultifunctionLinearLayout extends LinearLayout {

    private Context mContext;

    private View synthesizeView;

    private LayoutInflater inflater;
    private GroupVoiceAdapter groupVoiceAdapter;
    private GroupFileAdapter groupFileAdapter;
    private GroupPictureAdapter groupPictureAdapter;


    private  LinearLayout llPicture;
    private  LinearLayout llFile;
    private  LinearLayout llVoice;
    private  TextView textView;

    private Type type;

    public Type getType() {
        return type;
    }

    public void setType(Type type) {
        this.type = type;
    }

    public enum Type {
        PUSHLISH,
        DONE
    }

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
        showSynthesizeView();
    }



    public void showSynthesizeView() {
        if (null == synthesizeView) {
            synthesizeView = inflater.inflate(R.layout.group_publish_task_detail_synthesis, null);

            RecyclerView pictureRecycleView = (RecyclerView) synthesizeView.findViewById(R.id.recyclerView_picture_base);
            RecyclerView voiceRecycleView = (RecyclerView) synthesizeView.findViewById(R.id.voice_recyclerView_base);
            RecyclerView fileRecycleView = (RecyclerView) synthesizeView.findViewById(R.id.file_recyclerView_base);

            textView = (TextView) synthesizeView.findViewById(R.id.m_et_issue_task);
            llPicture = (LinearLayout) synthesizeView.findViewById(R.id.ll_picture);
            llVoice = (LinearLayout) synthesizeView.findViewById(R.id.ll_voice);
            llFile = (LinearLayout) synthesizeView.findViewById(R.id.ll_file);


            groupPictureAdapter = new GroupPictureAdapter(mContext, false, null);
            pictureRecycleView.setLayoutManager(new LinearLayoutManager(mContext, LinearLayoutManager.HORIZONTAL, false));
            pictureRecycleView.setAdapter(groupPictureAdapter);

            groupVoiceAdapter = new GroupVoiceAdapter(mContext, false, null);
            voiceRecycleView.setLayoutManager(new LinearLayoutManager(mContext));
            voiceRecycleView.setAdapter(groupVoiceAdapter);


            groupFileAdapter = new GroupFileAdapter(mContext, false, null);
            fileRecycleView.setLayoutManager(new LinearLayoutManager(mContext));
            fileRecycleView.setAdapter(groupFileAdapter);

            addView(synthesizeView);
        }

    }


    private void showView(View view, List list){
        if (list != null && list.size() > 0) {
            view.setVisibility(VISIBLE);
        } else {
            view.setVisibility(GONE);
        }
    }

    public void showFileView(List<FileInfo> fileInfos) {
        showView(llFile, fileInfos);
        groupFileAdapter.setData(fileInfos);
    }

    public void showUrlView(List<String> uriList) {
        showView(llPicture, uriList);
        groupPictureAdapter.setData(uriList);

    }

    public void showVioceView(List<Voice> voices) {
        showView(llVoice, voices);
        groupVoiceAdapter.setData(voices);
    }



    public void setText(String text) {
        if(TextUtils.isEmpty(text)){
            textView.setVisibility(View.GONE);
        }else {
            textView.setVisibility(View.VISIBLE);
        }
       textView.setText(text);
    }

}
