package com.yc.english.community.view.activitys;

import android.content.Intent;
import android.net.Uri;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.jakewharton.rxbinding.view.RxView;
import com.yc.english.R;
import com.yc.english.base.utils.DrawableUtils;
import com.yc.english.base.view.FullScreenActivity;
import com.yc.english.community.contract.CommunityInfoContract;
import com.yc.english.community.model.domain.CommunityInfo;
import com.yc.english.community.presenter.CommunityInfoPresenter;
import com.yc.english.community.view.adapter.ImageSelectedAdapter;
import com.zhihu.matisse.Matisse;
import com.zhihu.matisse.MimeType;
import com.zhihu.matisse.engine.impl.PicassoEngine;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.TimeUnit;

import butterknife.BindView;
import rx.functions.Action1;

/**
 * Created by admin on 2017/7/25.
 */

public class CommunityAddActivity extends FullScreenActivity<CommunityInfoPresenter> implements CommunityInfoContract.View {

    private static final int REQUEST_CODE_CHOOSE = 23;

    @BindView(R.id.et_community_content)
    EditText mCommunityContextEditText;

    @BindView(R.id.all_note_image_list)
    RecyclerView mAllNoteImagesRecyclerView;

    @BindView(R.id.btn_send_note)
    Button mSendNoteButton;

    ImageSelectedAdapter mImageSelectedAdapter;

    List<Uri> mSelectedImages;

    @Override
    public int getLayoutId() {
        return R.layout.community_note_add;
    }

    @Override
    public void init() {
        mToolbar.setTitle(getString(R.string.add_note_name));
        mToolbar.showNavigationIcon();

        mSelectedImages = new ArrayList<Uri>();

        mSelectedImages.add(DrawableUtils.getAddUri(this));

        mImageSelectedAdapter = new ImageSelectedAdapter(this, mSelectedImages);
        mAllNoteImagesRecyclerView.setLayoutManager(new GridLayoutManager(this, 3));
        mAllNoteImagesRecyclerView.setAdapter(mImageSelectedAdapter);

        //发布
        RxView.clicks(mSendNoteButton).throttleFirst(200, TimeUnit.MILLISECONDS).subscribe(new Action1<Void>() {
            @Override
            public void call(Void aVoid) {

            }
        });

        mImageSelectedAdapter.setOnItemClickListener(new BaseQuickAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(BaseQuickAdapter adapter, View view, int position) {
                if (position == 0) {
                    selectImages();
                }
            }
        });

        mImageSelectedAdapter.setOnItemChildClickListener(new BaseQuickAdapter.OnItemChildClickListener() {
            @Override
            public boolean onItemChildClick(BaseQuickAdapter adapter, View view, int position) {

                mImageSelectedAdapter.getData().remove(position);

                if (mImageSelectedAdapter.getData() != null && mImageSelectedAdapter.getData().size() < 3) {

                    if(!mImageSelectedAdapter.getData().contains(DrawableUtils.getAddUri(CommunityAddActivity.this))){
                        mImageSelectedAdapter.getData().add(0, DrawableUtils.getAddUri(CommunityAddActivity.this));
                    }
                }
                mImageSelectedAdapter.notifyDataSetChanged();
                return false;
            }
        });
    }

    public void selectImages() {
        Matisse.from(CommunityAddActivity.this)
                .choose(MimeType.allOf())
                .theme(R.style.Matisse_Dracula)
                .countable(false)
                .maxSelectable(3)
                .imageEngine(new PicassoEngine())
                .forResult(REQUEST_CODE_CHOOSE);
    }

    @Override
    public void hideStateView() {
        //mStateView.hide();
    }

    @Override
    public void showNoNet() {
        /*mStateView.showNoNet(mLayoutContext, "网络不给力", new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //mPresenter.getBookInfoById(bookId);
            }
        });*/
    }

    @Override
    public void showNoData() {
        //mStateView.showNoData(mLayoutContext);
    }

    @Override
    public void showLoading() {
        //mStateView.showLoading(mLayoutContext);
    }

    @Override
    public void showCommunityInfoListData(List<CommunityInfo> list) {

    }

    @Override
    public void addCommunityInfo(CommunityInfo communityInfo) {

    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == REQUEST_CODE_CHOOSE && resultCode == RESULT_OK) {
            mSelectedImages = Matisse.obtainResult(data);

            if (mSelectedImages != null && mSelectedImages.size() < 3) {
                mSelectedImages.add(0, DrawableUtils.getAddUri(this));
            }
            mImageSelectedAdapter.setNewData(mSelectedImages);
        }
    }
}
