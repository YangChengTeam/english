package com.yc.junior.english.composition.widget;

import com.chad.library.adapter.base.loadmore.LoadMoreView;
import com.yc.junior.english.R;

/**
 * Created by wanglin  on 2019/3/28 10:24.
 */
public class MyLoadMoreView extends LoadMoreView {
    @Override
    public int getLayoutId() {
        return R.layout.load_more_view;
    }

    @Override
    protected int getLoadingViewId() {
        return R.id.load_more_loading_view;
    }

    @Override
    protected int getLoadFailViewId() {
        return R.id.load_more_load_fail_view;
    }

    @Override
    protected int getLoadEndViewId() {
        return R.id.load_more_load_end_view;
    }
}
