package com.yc.junior.english.group.listener;



import com.example.comm_recyclviewadapter.BaseViewHolder;


/**
 * Created by wanglin  on 2017/8/3 08:45.
 */

public interface OnItemClickListener<T> {
    void onItemClick(BaseViewHolder holder, int position, T t);
}
