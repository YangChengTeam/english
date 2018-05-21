package com.yc.junior.english.speak.view.adapter;

import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.support.v7.widget.CardView;
import android.view.ViewGroup;
import android.widget.ImageView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.bumptech.glide.request.RequestOptions;
import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;
import com.yc.junior.english.R;
import com.yc.junior.english.group.utils.BitmapUtils;
import com.yc.junior.english.speak.model.bean.SpeakAndReadItemInfo;

import net.lucode.hackware.magicindicator.buildins.UIUtil;

import java.util.List;

import static com.bumptech.glide.load.resource.drawable.DrawableTransitionOptions.withCrossFade;

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
        setItemLayoutParams(helper, helper.getAdapterPosition());
        helper.setText(R.id.tv_play_count, String.format(mContext.getString(R.string.count), item.getView_num()))
                .setText(R.id.tv_update_date, String.format(mContext.getString(R.string.update_date), item.getAdd_date_format()))
                .setText(R.id.tv_sub_title, item.getTitle()).addOnClickListener(R.id.fl_play_item);
        int position = helper.getLayoutPosition() - getHeaderLayoutCount();

        final ImageView view = helper.getView(R.id.iv_thumb);
        CardView.LayoutParams layoutParams = (CardView.LayoutParams) view.getLayoutParams();
        if (mIsMore) {
            helper.setVisible(R.id.tv_update_date, false);
        } else {
            if (position == 0) {
                helper.setVisible(R.id.tv_update_date, true);
                layoutParams.height = CardView.LayoutParams.WRAP_CONTENT;
            } else {
                helper.setVisible(R.id.tv_update_date, false);
                layoutParams.height = UIUtil.dip2px(mContext, 110);
            }
            view.setLayoutParams(layoutParams);
        }
        Drawable drawable = new BitmapDrawable(mContext.getResources(), BitmapUtils.transformRoundDrawable(mContext, R.mipmap.pic_example, 5));

        Glide.with(mContext).load(item.getImg()).apply(new RequestOptions().diskCacheStrategy(DiskCacheStrategy.DATA)
                .skipMemoryCache(true).error(drawable).dontAnimate()).thumbnail(0.1f).into(view);

    }

    private void setItemLayoutParams(BaseViewHolder helper, int position) {
        ViewGroup.MarginLayoutParams layoutParams = (ViewGroup.MarginLayoutParams) helper.itemView.getLayoutParams();
        if (mIsMore) {
            if (position % 2 == 0) {
                layoutParams.leftMargin = UIUtil.dip2px(mContext, 2);
            } else {
                layoutParams.rightMargin = UIUtil.dip2px(mContext, 2);
            }
        } else {
            if (position == 0) {
                layoutParams.leftMargin = UIUtil.dip2px(mContext, 2);
                layoutParams.rightMargin = UIUtil.dip2px(mContext, 2);

            } else if (position % 2 == 0) {
                layoutParams.rightMargin = UIUtil.dip2px(mContext, 2);
            } else {
                layoutParams.leftMargin = UIUtil.dip2px(mContext, 2);
            }
        }
        helper.itemView.setLayoutParams(layoutParams);
    }

}
