package com.yc.english.group.view.adapter;

import android.content.ActivityNotFoundException;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.view.View;
import android.webkit.MimeTypeMap;

import com.example.comm_recyclviewadapter.BaseAdapter;
import com.example.comm_recyclviewadapter.BaseViewHolder;
import com.hwangjr.rxbus.RxBus;
import com.yc.english.R;
import com.yc.english.base.helper.RxUtils;
import com.yc.english.base.helper.TipsHelper;
import com.yc.english.group.constant.BusAction;

import java.io.File;
import java.util.List;

import io.rong.imkit.model.FileInfo;
import rx.android.schedulers.AndroidSchedulers;
import rx.functions.Action1;

/**
 * Created by wanglin  on 2017/8/8 11:29.
 */

public class GroupFileAdapter extends BaseAdapter<FileInfo> {

    private boolean mIsPublish;

    public GroupFileAdapter(Context context, boolean isPublish, List<FileInfo> mList) {
        super(context, mList);
        this.mIsPublish = isPublish;
    }

    @Override
    protected void convert(BaseViewHolder holder, final int position) {
        holder.setVisible(R.id.m_iv_issue_file_delete, mIsPublish);

        final FileInfo result = mList.get(position);
        holder.setText(R.id.m_tv_issue_result_file_title, result.getFileName());
        holder.setOnClickListener(R.id.m_iv_issue_file_delete, new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                deleteFile(position);
                RxBus.get().post(BusAction.DELETE_FILE, result);
            }


        });
        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (mIsPublish) {
                    MimeTypeMap myMime = MimeTypeMap.getSingleton();
                    Intent newIntent = new Intent(Intent.ACTION_VIEW);
                    String mimeType = null;
                    if (result.getFilePath().lastIndexOf(".") != -1) {
                        mimeType = myMime.getMimeTypeFromExtension(result.getFilePath().substring(result.getFilePath().lastIndexOf(".") + 1));
                    }
                    newIntent.setDataAndType(Uri.parse("file://" + result.getFilePath()), mimeType);
                    newIntent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                    try {
                        mContext.startActivity(newIntent);
                    } catch (ActivityNotFoundException e) {
                        TipsHelper.tips(mContext, "未知文件类型");
                    }
                } else {
                    RxUtils.getFile(mContext, result.getFilePath()).observeOn(AndroidSchedulers.mainThread()).subscribe(new Action1<File>() {
                        @Override
                        public void call(File file) {
                            MimeTypeMap myMime = MimeTypeMap.getSingleton();
                            Intent newIntent = new Intent(Intent.ACTION_VIEW);
                            String mimeType = null;
                            if (file.getPath().lastIndexOf(".") != -1) {
                                mimeType = myMime.getMimeTypeFromExtension(file.getPath().substring(file.getPath().lastIndexOf(".") + 1));
                            }
                            newIntent.setDataAndType(Uri.fromFile(file), mimeType);
                            newIntent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                            try {
                                mContext.startActivity(newIntent);
                            } catch (ActivityNotFoundException e) {
                                TipsHelper.tips(mContext, "未知文件类型");
                            }
                        }
                    });
                }

            }
        });


    }

    private void deleteFile(int position) {
        mList.remove(position);
        notifyDataSetChanged();
    }

    @Override
    public int getLayoutID(int viewType) {
        return R.layout.group_file_item;
    }

}
