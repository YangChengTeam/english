package com.yc.english.group.view.adapter;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.view.View;

import com.example.comm_recyclviewadapter.BaseAdapter;
import com.example.comm_recyclviewadapter.BaseViewHolder;
import com.hwangjr.rxbus.RxBus;
import com.yc.english.R;
import com.yc.english.base.helper.RxUtils;
import com.yc.english.group.constant.BusAction;

import java.io.File;
import java.util.List;

import io.rong.imkit.activity.FilePreviewActivity;
import io.rong.imkit.model.FileInfo;
import io.rong.imlib.model.Conversation;
import io.rong.imlib.model.Message;
import io.rong.message.FileMessage;
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
                final Intent intent = new Intent(mContext, FilePreviewActivity.class);
                if (mIsPublish) {
                    Uri uri = Uri.parse("file://" + result.getFilePath());
                    FileMessage fileMessage = FileMessage.obtain(uri);
                    Message message = Message.obtain("", Conversation.ConversationType.GROUP, fileMessage);
                    intent.putExtra("FileMessage", fileMessage);
                    intent.putExtra("Message", message);
                } else {
                    RxUtils.getFile(mContext, result.getFilePath()).observeOn(AndroidSchedulers.mainThread()).subscribe(new Action1<File>() {
                        @Override
                        public void call(File file) {
                            Uri uri = Uri.parse("file://" + file.getAbsolutePath());
                            FileMessage fileMessage = FileMessage.obtain(uri);
                            Message message = Message.obtain("", Conversation.ConversationType.GROUP, fileMessage);
                            intent.putExtra("FileMessage", fileMessage);
                            intent.putExtra("Message", message);
                        }
                    });
                }
                mContext.startActivity(intent);
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
