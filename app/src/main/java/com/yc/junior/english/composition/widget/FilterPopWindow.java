package com.yc.junior.english.composition.widget;

import android.app.Activity;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.view.View;

import com.alibaba.fastjson.TypeReference;
import com.chad.library.adapter.base.BaseQuickAdapter;
import com.yc.junior.english.composition.adapter.FilterItemAdapter;
import com.yc.junior.english.composition.model.bean.VersionDetailInfo;
import com.yc.junior.english.composition.model.bean.VersionInfo;
import com.yc.junior.english.R;
import com.yc.soundmark.base.constant.SpConstant;
import com.yc.soundmark.category.utils.ItemDecorationHelper;

import java.util.List;

import butterknife.BindView;
import yc.com.base.BasePopwindow;
import yc.com.base.CommonInfoHelper;
import yc.com.blankj.utilcode.util.SPUtils;


/**
 * Created by wanglin  on 2018/3/8 15:53.
 */

public class FilterPopWindow extends BasePopwindow {


    @BindView(R.id.subject_recyclerView)
    RecyclerView subjectRecyclerView;
    private String mFlag;


    private FilterItemAdapter subjectFilterItemAdapter;

    private VersionDetailInfo subjectDetailInfo;
    private String simple_flag = "";

    public FilterPopWindow(Activity context, String flag) {
        super(context);
        this.mFlag = flag;
    }


    @Override
    public int getLayoutId() {
        return R.layout.fragment_filter_view;
    }

    @Override
    public void init() {

        setOutsideTouchable(true);

        subjectRecyclerView.setLayoutManager(new GridLayoutManager(mContext, 4));
        CommonInfoHelper.getO(mContext, SpConstant.INDEX_VERSION, new TypeReference<VersionInfo>() {
        }.getType(), new CommonInfoHelper.onParseListener<VersionInfo>() {

            @Override
            public void onParse(VersionInfo versionInfo) {

                List<VersionDetailInfo> subjectList = null;
                if (TextUtils.equals(mContext.getString(R.string.grade), mFlag)) {
//                    subjectList = createNewList(versionInfo.getGrade());
                    subjectList = versionInfo.getGrade();
                    simple_flag = "grade_composition";
                } else if (TextUtils.equals(mContext.getString(R.string.topic), mFlag)) {
                    subjectList = versionInfo.getTopic();
                    simple_flag = "subject_composition";
                } else if (TextUtils.equals(mContext.getString(R.string.type), mFlag)) {
                    subjectList = versionInfo.getType();
                    simple_flag = "part_composition";
                } else if (TextUtils.equals(mContext.getString(R.string.theme), mFlag)) {
                    subjectList = versionInfo.getTheme();
                    simple_flag = "version_composition";
                }

                if (subjectList != null)
                    subjectDetailInfo = subjectList.get(0);

                subjectFilterItemAdapter = new FilterItemAdapter(subjectList, simple_flag);

                subjectRecyclerView.setAdapter(subjectFilterItemAdapter);
                subjectRecyclerView.addItemDecoration(new ItemDecorationHelper(mContext, 10, 10));

                subjectFilterItemAdapter.setOnItemClickListener(new BaseQuickAdapter.OnItemClickListener() {
                    @Override
                    public void onItemClick(BaseQuickAdapter adapter, View view, int position) {
                        subjectFilterItemAdapter.onClick(position);
                        subjectDetailInfo = subjectFilterItemAdapter.getItem(position);
                        SPUtils.getInstance().put(simple_flag, subjectDetailInfo.getName());
                        if (listener != null) {
                            listener.onPopClick(subjectDetailInfo.getName(), subjectDetailInfo.getId());
                        }

                    }
                });
            }

            @Override
            public void onFail(String json) {

            }
        });


    }

    @Override
    public int getAnimationID() {
        return 0;
    }

    private List<VersionDetailInfo> createNewList(List<VersionDetailInfo> oldList) {

        if (oldList != null) {
            if (oldList.size() > 0 && !TextUtils.equals(mContext.getString(R.string.all), oldList.get(0).getName()))
                oldList.add(0, new VersionDetailInfo("", mContext.getString(R.string.all)));
        }
        return oldList;
    }


    private OnPopClickListener listener;

    public void setOnPopClickListener(OnPopClickListener listener) {
        this.listener = listener;
    }

    public interface OnPopClickListener {
        void onPopClick(String name, String id);
    }

}
