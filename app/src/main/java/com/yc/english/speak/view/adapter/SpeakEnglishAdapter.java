package com.yc.english.speak.view.adapter;

import android.content.Intent;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;
import com.yc.english.R;
import com.yc.english.speak.model.bean.SpeakAndReadInfo;
import com.yc.english.speak.model.bean.SpeakAndReadItemInfo;
import com.yc.english.speak.view.activity.ListenEnglishActivity;
import com.yc.english.speak.view.activity.SpeakEnglishActivity;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

/**
 * Created by wanglin  on 2017/10/12 15:24.
 */

public class SpeakEnglishAdapter extends BaseQuickAdapter<SpeakAndReadInfo, BaseViewHolder> {
    private int mType;

    public SpeakEnglishAdapter(List<SpeakAndReadInfo> data, int type) {
        super(R.layout.speak_fragment_list_detail, data);
        this.mType = type;
    }

    @Override
    protected void convert(BaseViewHolder helper, SpeakAndReadInfo item) {

        helper.setText(R.id.english_tv_title, item.getType_name()).addOnClickListener(R.id.rl_more);
        if (mType == 1) {
            helper.setImageResource(R.id.iv_speak_icon,
                    item.getType_name().equals(mContext.getString(R.string.tv)) ? R.mipmap.speak_tv_orange :
                            item.getType_name().equals(mContext.getString(R.string.cartoon)) ? R.mipmap.speak_cartoon :
                                    item.getType_name().equals(mContext.getString(R.string.live)) ? R.mipmap.speak_live : R.mipmap.default_avatar);
        } else if (mType == 2) {
            helper.setImageResource(R.id.iv_speak_icon,
                    item.getType_name().equals(mContext.getString(R.string.song)) ? R.mipmap.speak_music :
                            item.getType_name().equals(mContext.getString(R.string.tv)) ? R.mipmap.speak_tv_red :
                                    item.getType_name().equals(mContext.getString(R.string.poetry)) ? R.mipmap.speak_poetry : R.mipmap.default_avatar);
        }

        RecyclerView recyclerView = helper.getView(R.id.recyclerView_speak);
        GridLayoutManager gridLayoutManager = new GridLayoutManager(mContext, 2);

        recyclerView.setLayoutManager(gridLayoutManager);


        List<SpeakAndReadItemInfo> itemInfoList = item.getData();
        Collections.sort(itemInfoList, new Comparator<SpeakAndReadItemInfo>() {
            @Override
            public int compare(SpeakAndReadItemInfo o1, SpeakAndReadItemInfo o2) {
                return Integer.parseInt(o1.getSort()) - Integer.parseInt(o2.getSort());
            }
        });

        SpeakEnglishItemAdapter itemAdapter = new SpeakEnglishItemAdapter(itemInfoList.size() <= 7 ? itemInfoList : itemInfoList.subList(0, 7), false);
        recyclerView.setAdapter(itemAdapter);


        /**
         * 该方法设置gridLayoutManager布局中每个item占据的列数
         * 这个方法必须放在setAdapter之后才生效，否则不起作用
         *
         */
        gridLayoutManager.setSpanSizeLookup(new GridLayoutManager.SpanSizeLookup() {
            @Override
            public int getSpanSize(int position) {
                if (position == 0) {
                    return 2;
                }
                return 1;

            }
        });


        initListener(item, itemAdapter);


    }

    private void initListener(final SpeakAndReadInfo info, SpeakEnglishItemAdapter itemAdapter) {
        itemAdapter.setOnItemChildClickListener(new OnItemChildClickListener() {
            @Override
            public boolean onItemChildClick(BaseQuickAdapter adapter, View view, int position) {
                int pos = mData.indexOf(info);
                SpeakAndReadItemInfo speakAndReadItemInfo = (SpeakAndReadItemInfo) adapter.getItem(position);
                speakAndReadItemInfo.setOutPos(pos);
                speakAndReadItemInfo.setInnerPos(position);
                Intent intent = null;
                if (mType == 1) {//说英语
                    intent = new Intent(mContext, SpeakEnglishActivity.class);
                    intent.putExtra("itemInfo", speakAndReadItemInfo);
                    intent.putParcelableArrayListExtra("infoList", (ArrayList) mData);
                } else if (mType == 2) {//听英语
                    intent = new Intent(mContext, ListenEnglishActivity.class);
                    intent.putExtra("itemInfo", speakAndReadItemInfo);
                    intent.putParcelableArrayListExtra("infoList", (ArrayList) mData);
                }
                mContext.startActivity(intent);

                return false;

            }
        });


    }


}
