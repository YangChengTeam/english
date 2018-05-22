package com.yc.english.community.view.wdigets;

import android.content.Context;
import android.graphics.drawable.Drawable;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.AttributeSet;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.blankj.utilcode.util.StringUtils;
import com.blankj.utilcode.util.TimeUtils;
import com.chad.library.adapter.base.BaseQuickAdapter;
import com.jakewharton.rxbinding.view.RxView;
import com.yc.english.R;
import com.yc.english.base.helper.GlideHelper;
import com.yc.english.base.view.BaseView;
import com.yc.english.community.model.domain.CommunityInfo;
import com.yc.english.community.view.adapter.ImageDetailSelectedAdapter;

import java.net.URLDecoder;
import java.util.List;
import java.util.concurrent.TimeUnit;

import butterknife.BindView;
import rx.functions.Action1;

/**
 * Created by admin on 2017/9/8.
 */

public class CommunityHeadView extends BaseView {

    private Context mContext;

    @BindView(R.id.iv_note_user_img)
    ImageView noteUserImageView;

    @BindView(R.id.tv_note_user_name)
    TextView mUserNameTextView;

    @BindView(R.id.tv_note_date)
    TextView mNoteDateTextView;

    @BindView(R.id.tv_note_title)
    TextView mNoteTitleTextView;

    @BindView(R.id.tv_comment_count)
    TextView mCommentCountTextView;

    @BindView(R.id.tv_praise_count)
    TextView mPraiseCountTextView;

    @BindView(R.id.note_detail_image_list)
    RecyclerView mNoteDetailImagesRecyclerView;

    ImageDetailSelectedAdapter mImageDetailSelectedAdapter;

    List<String> imageList;

    public CommunityDetailListener listener;

    public void setListener(CommunityDetailListener listener) {
        this.listener = listener;
    }

    public interface CommunityDetailListener {
        void praiseClick();

        void imageShow(int position);
    }

    public CommunityHeadView(Context context) {
        super(context);
        this.mContext = context;
    }

    public CommunityHeadView(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    @Override
    public void init() {
        super.init();
    }

    @Override
    public int getLayoutId() {
        return R.layout.community_note_detail_head;
    }

    public void showHeadInfo(CommunityInfo communityInfo) {
        imageList = communityInfo.getImages();
        GlideHelper.circleImageView(mContext, noteUserImageView, communityInfo.getFace(), R.mipmap.main_tab_my);
        mNoteDetailImagesRecyclerView.setLayoutManager(new GridLayoutManager(mContext, 3));
        mImageDetailSelectedAdapter = new ImageDetailSelectedAdapter(mContext, imageList);
        mNoteDetailImagesRecyclerView.setAdapter(mImageDetailSelectedAdapter);

        mUserNameTextView.setText(communityInfo.getUserName());

        if (!StringUtils.isEmpty(communityInfo.getAddTime())) {
            long addTime = Long.parseLong(communityInfo.getAddTime()) * 1000;
            mNoteDateTextView.setText(TimeUtils.millis2String(addTime));
        }
        try {
            mNoteTitleTextView.setText(URLDecoder.decode(communityInfo.getContent(), "UTF-8"));
        } catch (Exception e) {
            e.printStackTrace();
        }

        mCommentCountTextView.setText(communityInfo.getFollowCount());
        mPraiseCountTextView.setText(communityInfo.getAgreeCount());

        RxView.clicks(mPraiseCountTextView).throttleFirst(200, TimeUnit.MILLISECONDS).subscribe(new Action1<Void>() {
            @Override
            public void call(Void aVoid) {
                listener.praiseClick();
            }
        });

        mImageDetailSelectedAdapter.setOnItemClickListener(new BaseQuickAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(BaseQuickAdapter adapter, View view, int position) {
                listener.imageShow(position);
            }
        });
    }

    public void updateCommentCount(int count) {
        mCommentCountTextView.setText(count + "");
    }

    public void updatePraiseCount(int count) {
        mPraiseCountTextView.setText(count + "");
    }

    public void updatePraiseState(Drawable state) {
        mPraiseCountTextView.setCompoundDrawables(state, null, null, null);
    }

}