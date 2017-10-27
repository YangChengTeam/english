package com.yc.english.speak.view.adapter;

import android.widget.ImageView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.RequestOptions;
import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;
import com.yc.english.R;
import com.yc.english.group.utils.GlideRoundTransform;
import com.yc.english.speak.model.bean.SpeakAndReadItemInfo;

import java.util.List;

/**
 * Created by wanglin  on 2017/10/12 15:36.
 */

public class SpeakEnglishItemAdapter extends BaseQuickAdapter<SpeakAndReadItemInfo, BaseViewHolder> {

    private boolean mIsMore;

    public SpeakEnglishItemAdapter(List<SpeakAndReadItemInfo> data, boolean isMore) {
        super(R.layout.speak_fragment_item, data);
        this.mIsMore = isMore;
    }

    @Override
    protected void convert(BaseViewHolder helper, SpeakAndReadItemInfo item) {

        helper.setText(R.id.tv_play_count, String.format(mContext.getString(R.string.count), item.getView_num()))
                .setText(R.id.tv_update_date, String.format(mContext.getString(R.string.update_date), item.getAdd_date_format()))
                .setText(R.id.tv_sub_title, item.getTitle()).addOnClickListener(R.id.fl_play_item);
        int position = helper.getLayoutPosition() - getHeaderLayoutCount();

        final ImageView view = helper.getView(R.id.iv_thumb);
        int pos = getData().indexOf(item);
        if (mIsMore) {
            helper.setVisible(R.id.tv_update_date, false);
        } else {
            if (position == 0) {
                helper.setVisible(R.id.tv_update_date, true);
            } else {
                helper.setVisible(R.id.tv_update_date, false);
            }
        }


//        final ObjectAnimator animator = ObjectAnimator.ofInt(view, "ImageLevel", 0, 10000);
//        animator.setDuration(800);
//        animator.setRepeatCount(ObjectAnimator.INFINITE);
//        animator.start();
        if (mIsMore) {
            view.setTag(R.id.img_id, item.getImg());
            if (view.getTag(R.id.img_id).equals(item.getImg())) {
                Glide.with(mContext).load(item.getImg()).apply(new RequestOptions()
                        .transform(new GlideRoundTransform(mContext, pos, true)).placeholder(R.mipmap.base_loading).error(R.mipmap.pic_example)).into(view);
            }
        } else {
            Glide.with(mContext).load(item.getImg()).apply(new RequestOptions()
                    .transform(new GlideRoundTransform(mContext, pos, false)).placeholder(R.mipmap.base_loading).error(R.mipmap.pic_example)).into(view);
        }


    }
}
