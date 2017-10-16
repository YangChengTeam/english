package com.yc.english.speak.view.adapter;

import android.view.ViewGroup;
import android.widget.ImageView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.RequestOptions;
import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;
import com.yc.english.R;
import com.yc.english.group.utils.GlideRoundTransform;
import com.yc.english.speak.model.bean.EnglishItemInfo;

import net.lucode.hackware.magicindicator.buildins.UIUtil;

import java.util.List;

/**
 * Created by wanglin  on 2017/10/12 15:36.
 */

public class SpeakEnglishItemAdapter extends BaseQuickAdapter<EnglishItemInfo, BaseViewHolder> {

    private boolean mIsMore;

    public SpeakEnglishItemAdapter(List<EnglishItemInfo> data, boolean isMore) {
        super(R.layout.speak_fragment_item, data);
        this.mIsMore = isMore;
    }

    @Override
    protected void convert(BaseViewHolder helper, EnglishItemInfo item) {
        helper.setText(R.id.tv_play_count, item.getPlay_count() + "次")
                .setText(R.id.tv_update_date, "更新" + item.getUpdate_date())
                .setText(R.id.tv_sub_title, item.getSub_title()).addOnClickListener(R.id.fl_play_item);
        int position = helper.getLayoutPosition();

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


        Glide.with(mContext).load(item.getUrl()).apply(new RequestOptions()
                .transform(new GlideRoundTransform(mContext)).placeholder(R.mipmap.base_loading)).into(view);
    }
}
