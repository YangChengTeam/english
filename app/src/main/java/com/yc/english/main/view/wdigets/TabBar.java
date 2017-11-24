package com.yc.english.main.view.wdigets;

import android.content.Context;
import android.graphics.drawable.Drawable;
import android.support.v4.content.ContextCompat;
import android.util.AttributeSet;

import com.yc.english.R;
import com.yc.english.base.view.BaseView;

import butterknife.BindView;
import butterknife.OnClick;

/**
 * Created by zhangkai on 2017/7/24.
 */

public class TabBar extends BaseView {
    @BindView(R.id.item_index)
    TabItem mIndexItem;

    @BindView(R.id.item_task)
    TabItem mTaskItem;

    @BindView(R.id.item_intelligent)
    TabItem mIntelligent;

    @BindView(R.id.item_my)
    TabItem mMyItem;

    public TabBar(Context context, AttributeSet attrs) {
        super(context, attrs);

        mIndexItem.setTag(0);
        mTaskItem.setTag(1);
        mIntelligent.setTag(2);
        mMyItem.setTag(3);

    }

    @Override
    public int getLayoutId() {
        return R.layout.main_view_tab_bar;
    }

    @OnClick({R.id.item_index, R.id.item_task, R.id.item_intelligent ,R.id.item_my})
    public void OnClick(TabItem item){
        if(onTabSelectedListener == null) throw  new NullPointerException("listener == null");

        int index = (int)item.getTag();
        clearSelectedItem();
        item.selected(getSelectedIconDrawable(index));
        onTabSelectedListener.onSelected(index);
    }

    public void tab(int idx){
        clearSelectedItem();
        getTabItem(idx).selected(getSelectedIconDrawable(idx));
        onTabSelectedListener.onSelected(idx);
    }

    private TabItem getTabItem(int idx){
        switch (idx){
            case 0:
                return mIndexItem;
            case 1:
                return mTaskItem;
            case 2:
                return mIntelligent;
            case 3:
                return mMyItem;
        }
        return mIndexItem;
    }

    private void clearSelectedItem(){
        mIndexItem.normal(getIconDrawable(0));
        mTaskItem.normal(getIconDrawable(1));
        mIntelligent.normal(getIconDrawable(2));
        mMyItem.normal(getIconDrawable(3));
    }

    private Drawable getIconDrawable(int idx){
        switch (idx){
            case 0:
                return ContextCompat.getDrawable(getContext(),R.mipmap.main_tab_index);
            case 1:
                return ContextCompat.getDrawable(getContext(),R.mipmap.main_tab_task);
            case 2:
                return ContextCompat.getDrawable(getContext(),R.mipmap.main_tab_intelligent);
            case 3:
                return ContextCompat.getDrawable(getContext(),R.mipmap.main_tab_my);

        }
        return ContextCompat.getDrawable(getContext(), R.mipmap.main_tab_index_selected);
    }

    private Drawable getSelectedIconDrawable(int idx){
        switch (idx){
            case 0:
                return ContextCompat.getDrawable(getContext(),R.mipmap.main_tab_index_selected);
            case 1:
                return ContextCompat.getDrawable(getContext(),R.mipmap.main_tab_task_selected);
            case 2:
                return ContextCompat.getDrawable(getContext(),R.mipmap.main_tab_index_selected);
            case 3:
                return ContextCompat.getDrawable(getContext(),R.mipmap.main_tab_my_selected);
        }
        return ContextCompat.getDrawable(getContext(), R.mipmap.main_tab_index);
    }

    private OnTabSelectedListener onTabSelectedListener;

    public void setOnTabSelectedListener(OnTabSelectedListener onTabSelectedListener){
        this.onTabSelectedListener = onTabSelectedListener;
    }

    public interface OnTabSelectedListener {
        void onSelected(int idx);
    }


}
