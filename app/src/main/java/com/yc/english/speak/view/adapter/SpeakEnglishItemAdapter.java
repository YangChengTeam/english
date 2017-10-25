package com.yc.english.speak.view.adapter;

import android.view.ViewGroup;
import android.widget.ImageView;

import com.blankj.utilcode.util.ScreenUtils;
import com.bumptech.glide.Glide;
import com.bumptech.glide.request.RequestOptions;
import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;
import com.yc.english.R;
import com.yc.english.group.model.bean.StudentInfo;
import com.yc.english.group.utils.GlideRoundTransform;
import com.yc.english.speak.model.bean.SpeakAndReadItemInfo;

import net.lucode.hackware.magicindicator.buildins.UIUtil;

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

        ImageView view = helper.getView(R.id.iv_thumb);
        ViewGroup.LayoutParams layoutParams = view.getLayoutParams();
        if (mIsMore) {
            helper.setVisible(R.id.tv_update_date, false);
        } else {
            if (position == 0) {
                helper.setVisible(R.id.tv_update_date, true);
                layoutParams.height = UIUtil.dip2px(mContext, 180);//225
                view.setLayoutParams(layoutParams);

            } else {
                helper.setVisible(R.id.tv_update_date, false);
                layoutParams.height = ViewGroup.LayoutParams.WRAP_CONTENT;
                view.setLayoutParams(layoutParams);
            }
        }
//        final ObjectAnimator animator = ObjectAnimator.ofInt(view, "ImageLevel", 0, 10000);
//        animator.setDuration(800);
//        animator.setRepeatCount(ObjectAnimator.INFINITE);
//        animator.start();
        if (mIsMore) {
            view.setTag(R.id.img_id, item.getImg());
        }


        Glide.with(mContext).load(item.getImg()).apply(new RequestOptions()
                .transform(new GlideRoundTransform()).error(R.mipmap.pic_example)).into(view);
    }
}
