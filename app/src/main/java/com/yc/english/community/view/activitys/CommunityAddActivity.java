package com.yc.english.community.view.activitys;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import com.blankj.utilcode.util.StringUtils;
import com.blankj.utilcode.util.ToastUtils;
import com.chad.library.adapter.base.BaseQuickAdapter;
import com.hwangjr.rxbus.RxBus;
import com.jakewharton.rxbinding.view.RxView;
import com.kk.securityhttp.net.entry.UpFileInfo;
import com.yc.english.R;
import com.yc.english.base.helper.TipsHelper;
import com.yc.english.base.utils.DrawableUtils;
import com.yc.english.base.view.FullScreenActivity;
import com.yc.english.community.contract.CommunityInfoContract;
import com.yc.english.community.model.domain.CommentInfo;
import com.yc.english.community.model.domain.CommunityInfo;
import com.yc.english.community.presenter.CommunityInfoPresenter;
import com.yc.english.community.view.adapter.ImageSelectedAdapter;
import com.yc.english.main.hepler.UserInfoHelper;
import com.yc.english.main.model.domain.Constant;
import com.zhihu.matisse.Matisse;
import com.zhihu.matisse.MimeType;
import com.zhihu.matisse.engine.impl.PicassoEngine;

import java.io.File;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
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

    private Uri mAddUri;

    private String noteType;

    @Override
    public int getLayoutId() {
        return R.layout.community_note_add;
    }

    @Override
    public void init() {

        Intent intent = getIntent();

        Bundle bundle = intent.getExtras();
        if (bundle != null) {
            noteType = bundle.getString("note_type");
        }

        mPresenter = new CommunityInfoPresenter(this, this);
        mAddUri = DrawableUtils.getAddUri(CommunityAddActivity.this);

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
                if (StringUtils.isEmpty(mCommunityContextEditText.getText())) {
                    TipsHelper.tips(CommunityAddActivity.this, "请输入发帖内容");
                    return;
                }

                if (mImageSelectedAdapter.getData() != null) {
                    if (mImageSelectedAdapter.getData().size() == 1 && mImageSelectedAdapter.getData().get(0).compareTo(mAddUri) == 0) {
                        TipsHelper.tips(CommunityAddActivity.this, "请选择图片");
                        return;
                    }

                    UpFileInfo upFileInfo = new UpFileInfo();
                    try {
                        upFileInfo.filename = "file.jpg";
                        upFileInfo.name = "file";
                        List<File> files = new ArrayList<File>();
                        for (int i = 0; i < mImageSelectedAdapter.getData().size(); i++) {
                            File tempFile = new File(DrawableUtils.getPathBuUri(CommunityAddActivity.this, mImageSelectedAdapter.getData().get(i)));
                            files.add(tempFile);
                        }
                        upFileInfo.files = files;
                    } catch (Exception e) {
                        e.printStackTrace();
                    }

                    CommunityInfo tempCommunityInfo = new CommunityInfo();
                    tempCommunityInfo.setUserId(UserInfoHelper.getUserInfo() != null ? UserInfoHelper.getUserInfo().getUid() : "");
                    tempCommunityInfo.setContent(mCommunityContextEditText.getText().toString());
                    tempCommunityInfo.setcType(noteType != null ? noteType : "");

                    mPresenter.addCommunityInfo(tempCommunityInfo, upFileInfo);

                } else {
                    ToastUtils.showLong("发帖失败");
                }
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

                    if (!mImageSelectedAdapter.getData().contains(mAddUri)) {
                        mImageSelectedAdapter.getData().add(0, mAddUri);
                    }
                }
                mImageSelectedAdapter.notifyDataSetChanged();
                return false;
            }
        });
    }

    public void selectImages() {
        Set<MimeType> sets = new HashSet<MimeType>();
        sets.add(MimeType.PNG);
        sets.add(MimeType.JPEG);

        Matisse.from(CommunityAddActivity.this)
                .choose(sets)
                .theme(R.style.Matisse_Dracula)
                .countable(true)
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
    public void showAddCommunityInfo(CommunityInfo communityInfo) {
        if (communityInfo != null) {
            RxBus.get().post(Constant.COMMUNITY_REFRESH, "from add communityInfo");
            finish();
        }
    }

    @Override
    public void showCommentList(List<CommentInfo> list) {

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

    @Override
    public void showAddComment(CommentInfo commentInfo) {

    }

    @Override
    public void showAgreeInfo(boolean flag) {

    }
}
