package com.yc.english.speak.utils;

import android.graphics.Rect;
import android.support.v7.widget.RecyclerView;
import android.view.View;

import com.blankj.utilcode.util.ScreenUtils;

/**
 * Created by wanglin  on 2017/10/12 17:18.
 */

public class MyLayoutManager extends RecyclerView.LayoutManager {
    private int totalHeight;

    @Override
    public RecyclerView.LayoutParams generateDefaultLayoutParams() {
        return null;
    }

    @Override
    public void onLayoutChildren(RecyclerView.Recycler recycler, RecyclerView.State state) {
        super.onLayoutChildren(recycler, state);
        // 先把所有的View先从RecyclerView中detach掉，然后标记为"Scrap"状态，表示这些View处于可被重用状态(非显示中)。
        // 实际就是把View放到了Recycler中的一个集合中。
        detachAndScrapAttachedViews(recycler);
        calculateChildrenSite(recycler);

    }

    private void calculateChildrenSite(RecyclerView.Recycler recycler) {
        totalHeight = 0;
        for (int i = 0; i < getItemCount(); i++) {
            // 遍历Recycler中保存的View取出来
            View view = recycler.getViewForPosition(i);
            addView(view); // 因为刚刚进行了detach操作，所以现在可以重新添加
            if (i == 0) {
                measureChildWithMargins(view, 0, 0);
            } else {
                //我们自己指定ItemView的尺寸。
                measureChildWithMargins(view, ScreenUtils.getScreenWidth() / 2, 0);
            }
            // 通知测量view的margin值}
            int width = getDecoratedMeasuredWidth(view); // 计算view实际大小，包括了ItemDecorator中设置的偏移量。
            int height = getDecoratedMeasuredHeight(view);

            Rect mTmpRect = new Rect();
            //调用这个方法能够调整ItemView的大小，以除去ItemDecorator。
            calculateItemDecorationsForChild(view, mTmpRect);

            // 调用这句我们指定了该View的显示区域，并将View显示上去，此时所有区域都用于显示View，
            //包括ItemDecorator设置的距离。
            if (i==0){
            layoutDecorated(view, 0, totalHeight, width, totalHeight + height);}
            else {
                if (i % 2 == 0) { //当i能被2整除时，是左，否则是右。
                    //左
                    layoutDecoratedWithMargins(view, 0, totalHeight, ScreenUtils.getScreenWidth() / 2,
                            totalHeight + height);
                } else {
                    //右，需要换行
                    layoutDecoratedWithMargins(view, ScreenUtils.getScreenWidth() / 2, totalHeight,
                            ScreenUtils.getScreenWidth(), totalHeight + height);
                    totalHeight = totalHeight + height;
                }
            }
            totalHeight += height;
        }

    }
}
