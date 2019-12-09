package com.yc.junior.english.topic.view.widget;

import android.content.Context;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.yc.junior.english.R;
import com.yc.junior.english.topic.model.bean.TopicInfo;
import com.yc.junior.english.topic.view.adapter.CapacityTopicDetailAdapter;

import androidx.annotation.Nullable;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

/**
 * Created by wanglin  on 2017/9/29 17:43.
 * 题库详情view
 */

public class TopicDetailView extends LinearLayout {

    private TextView mTopicTitle;//作业标题
    private RecyclerView subRecyclerView;//作业答案选项
    private TextView mTopicAnswer;//作业答案
    private TextView mTopicAnalysis;//作业解析

    private TopicInfo topicInfo;

    public TopicDetailView(Context context) {
        this(context, null);
    }

    public TopicDetailView(Context context, @Nullable AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public TopicDetailView(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init(context);
    }

    private void init(Context context) {
        LayoutInflater inflater = LayoutInflater.from(context);
        View view = inflater.inflate(R.layout.topic_item, this);

        mTopicTitle = (TextView) view.findViewById(R.id.tv_topic_title);
        subRecyclerView = (RecyclerView) view.findViewById(R.id.sub_recyclerView);
        mTopicAnswer = (TextView) view.findViewById(R.id.tv_topic_answer);
        mTopicAnalysis = (TextView) view.findViewById(R.id.tv_topic_analysis);

        CapacityTopicDetailAdapter adapter = new CapacityTopicDetailAdapter(0, null);
        subRecyclerView.setLayoutManager(new LinearLayoutManager(context));
        subRecyclerView.setAdapter(adapter);


//        addView(view);
    }

    public TopicInfo getTopicInfo() {
        return topicInfo;
    }

    public void setTopicInfo(TopicInfo topicInfo) {
        this.topicInfo = topicInfo;
        mTopicTitle.setText(topicInfo.getSubTitle());
        mTopicAnswer.setText(topicInfo.getAnswer());
        mTopicAnalysis.setText(topicInfo.getAnalysis());


    }
}
