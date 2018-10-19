package com.yc.english.weixin.presenter;

import android.content.Context;

import com.alibaba.fastjson.JSON;
import com.blankj.utilcode.util.UIUitls;
import com.yc.english.base.model.BaseEngin;
import com.yc.english.base.presenter.BasePresenter;
import com.yc.english.base.utils.SimpleCacheUtils;
import com.yc.english.base.view.IView;
import com.yc.english.main.model.domain.Constant;
import com.yc.english.main.model.domain.IndexInfo;
import com.yc.english.main.model.domain.SlideInfo;
import com.yc.english.weixin.contract.CourseTypeContract;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by zhangkai on 2017/8/30.
 */

public class CourseTypePresenter extends BasePresenter<BaseEngin, CourseTypeContract.View> implements CourseTypeContract.Presenter {


    public CourseTypePresenter(Context context, CourseTypeContract.View view) {
        super(context, view);
    }

    @Override
    public void loadData(boolean forceUpdate, boolean showLoadingUI) {
        if (!forceUpdate) return;
        showIndexInfo();
    }


    private void showIndexInfo() {
        SimpleCacheUtils.readCache(mContext, Constant.INDEX_INFO, new SimpleCacheUtils.CacheRunnable() {
            @Override
            public void run() {
                final IndexInfo indexInfo = JSON.parseObject(this.getJson(), IndexInfo.class);
                UIUitls.post(new Runnable() {
                    @Override
                    public void run() {
                        List<String> images = new ArrayList<String>();
                        slideInfos = indexInfo.getSlideInfo();
                        for (SlideInfo slideInfo : indexInfo.getSlideInfo()) {
                            images.add(slideInfo.getImg());
                        }
                        mView.showBanner(images);
                    }
                });
            }
        });
    }


    private List<SlideInfo> slideInfos;


    public SlideInfo getSlideInfo(int position) {
        if (slideInfos != null && slideInfos.size() > position) {
            return slideInfos.get(position);
        }
        return null;
    }
}
