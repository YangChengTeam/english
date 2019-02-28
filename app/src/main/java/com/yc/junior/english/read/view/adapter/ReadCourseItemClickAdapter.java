package com.yc.junior.english.read.view.adapter;

import android.app.Activity;
import android.content.Context;
import android.support.v4.content.ContextCompat;
import android.view.View;
import android.widget.ImageView;

import com.app.hubert.guide.NewbieGuide;
import com.app.hubert.guide.core.Controller;
import com.app.hubert.guide.listener.OnLayoutInflatedListener;
import com.app.hubert.guide.model.GuidePage;
import com.app.hubert.guide.model.HighLight;
import com.bumptech.glide.Glide;
import com.chad.library.adapter.base.BaseMultiItemQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;
import com.yc.junior.english.R;
import com.yc.junior.english.read.model.domain.EnglishCourseInfo;

import java.util.List;

import yc.com.blankj.utilcode.util.LogUtils;
import yc.com.blankj.utilcode.util.StringUtils;


public class ReadCourseItemClickAdapter extends BaseMultiItemQuickAdapter<EnglishCourseInfo, BaseViewHolder> {

//    private Context mContext;

    private int languageType = 1;

    private int lastPosition;

    public int getLanguageType() {
        return languageType;
    }

    public void setLanguageType(int languageType) {
        this.languageType = languageType;
    }

    public int getLastPosition() {
        return lastPosition;
    }

    public void setLastPosition(int lastPosition) {
        this.lastPosition = lastPosition;
    }

    public ReadCourseItemClickAdapter(Context mContext, List<EnglishCourseInfo> data) {
        super(data);
//        this.mContext = mContext;
        addItemType(EnglishCourseInfo.CLICK_ITEM_VIEW, R.layout.read_course_play_item);
    }

    @Override
    protected void convert(final BaseViewHolder helper, final EnglishCourseInfo item) {
        helper.setText(R.id.tv_chinese_title, item.getMeans())
                .setText(R.id.tv_english_title, item.getSubTitle())
                .addOnClickListener(R.id.layout_play)
                .addOnClickListener(R.id.iv_tape).addOnClickListener(R.id.iv_play).addOnClickListener(R.id.iv_play_tape);

        if (helper.getAdapterPosition() == 0) {
            ImageView guideLeftView = helper.getView(R.id.iv_play);
            ImageView guideCenterView = helper.getView(R.id.iv_tape);
            ImageView guideRightView = helper.getView(R.id.iv_play_tape);

            //设置引导视图
            NewbieGuide.with((Activity) mContext)
                    .setLabel("guide1")
                    .addGuidePage(GuidePage.newInstance()
                            .addHighLight(guideLeftView, HighLight.Shape.ROUND_RECTANGLE, 16)
                            .setLayoutRes(R.layout.read_guide_left_view)
                            .setOnLayoutInflatedListener(new OnLayoutInflatedListener() {
                                @Override
                                public void onLayoutInflated(View view, Controller controller) {

                                }

                            }))
                    .addGuidePage(GuidePage.newInstance()
                            .addHighLight(guideCenterView, HighLight.Shape.ROUND_RECTANGLE, 16)
                            .setLayoutRes(R.layout.read_guide_center_view)
                            .setOnLayoutInflatedListener(new OnLayoutInflatedListener() {
                                @Override
                                public void onLayoutInflated(View view, Controller controller) {

                                }


                            }))
                    .addGuidePage(GuidePage.newInstance()
                            .addHighLight(guideRightView, HighLight.Shape.ROUND_RECTANGLE, 16)
                            .setLayoutRes(R.layout.read_guide_right_view)
                            .setOnLayoutInflatedListener(new OnLayoutInflatedListener() {
                                @Override
                                public void onLayoutInflated(View view, Controller controller) {

                                }

                            }))
                    .show();
        }

        if (item.isPlay()) {
            helper.setVisible(R.id.iv_audio_gif_play, true);
            Glide.with(mContext).load(R.mipmap.read_audio_gif_play).into((ImageView) helper.getView(R.id.iv_audio_gif_play));
            helper.setTextColor(R.id.tv_chinese_title, ContextCompat.getColor(mContext, R.color.black_333)).setTextColor(R.id.tv_english_title, ContextCompat.getColor(mContext, R.color.black_333));
            //helper.setVisible(R.id.layout_tape, true);
            helper.setVisible(R.id.iv_speak_result, true);
            helper.setVisible(R.id.tv_result_hint, true);
            //helper.setVisible(R.id.iv_result, false);
            Glide.with(mContext).load(R.mipmap.item_read_press_icon).into(((ImageView) helper.getView(R.id.iv_play)));
        } else {
            Glide.with(mContext).load(R.mipmap.item_read_normal_icon).into(((ImageView) helper.getView(R.id.iv_play)));

            helper.setVisible(R.id.iv_audio_gif_play, false);
            helper.setTextColor(R.id.tv_chinese_title, ContextCompat.getColor(mContext, R.color.gray_999)).setTextColor(R.id.tv_english_title, ContextCompat.getColor(mContext, R.color.gray_999));
            if (helper.getAdapterPosition() == getLastPosition()) {
                helper.setVisible(R.id.iv_speak_result, true);
                helper.setVisible(R.id.tv_result_hint, true);
            } else {
                helper.setVisible(R.id.iv_speak_result, false);
                helper.setVisible(R.id.tv_result_hint, false);
            }
        }

        LogUtils.i("item--->current position" + helper.getAdapterPosition() + "---last---" + getLastPosition());

        int percent = 0;
        if (!StringUtils.isEmpty(item.getPercent())) {
            percent = (int) Double.parseDouble(item.getPercent());
        }

        if (item.isShow()) {
            helper.setVisible(R.id.iv_speak_result, true);
            if (item.isSpeakResult()) {
                helper.setText(R.id.tv_result_hint, percent + "分,Good");
                helper.setBackgroundRes(R.id.iv_speak_result, R.mipmap.read_item_result_yes);
            } else {
                helper.setText(R.id.tv_result_hint, percent + "分,加油");
                helper.setBackgroundRes(R.id.iv_speak_result, R.mipmap.listen_result_no);
            }
        } else {
            helper.setText(R.id.tv_result_hint, "");
            helper.setVisible(R.id.iv_speak_result, false);
        }

        if (helper.getAdapterPosition() == getLastPosition()) {
            helper.setVisible(R.id.item_play_layout, true);
        } else {
            helper.setVisible(R.id.item_play_layout, false);
        }

        switch (languageType) {
            case 1:
                helper.setVisible(R.id.tv_chinese_title, true).setVisible(R.id.tv_english_title, true);
                break;
            case 2:
                helper.setVisible(R.id.tv_chinese_title, false).setVisible(R.id.tv_english_title, true);
                break;
            case 3:
                helper.setVisible(R.id.tv_chinese_title, true).setVisible(R.id.tv_english_title, false);
                break;
            default:
                break;
        }
    }
}