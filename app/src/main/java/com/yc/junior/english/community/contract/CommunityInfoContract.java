package com.yc.junior.english.community.contract;

import com.kk.securityhttp.net.entry.UpFileInfo;
import com.yc.junior.english.community.model.domain.CommentInfo;
import com.yc.junior.english.community.model.domain.CommunityInfo;

import java.util.List;

import yc.com.base.IDialog;
import yc.com.base.IFinish;
import yc.com.base.IHide;
import yc.com.base.ILoading;
import yc.com.base.INoData;
import yc.com.base.INoNet;
import yc.com.base.IPresenter;
import yc.com.base.IView;

/**
 * Created by zhangkai on 2017/7/25.
 */

public interface CommunityInfoContract {
    interface View extends IView, IDialog, IFinish, ILoading, INoData, INoNet,IHide {
        void showCommunityInfoListData(List<CommunityInfo> list);

        void showAddCommunityInfo(CommunityInfo communityInfo);

        void showCommentList(List<CommentInfo> list);

        void showAddComment(CommentInfo commentInfo);

        void showAgreeInfo(boolean flag);

        void showNoteDelete(boolean flag);
    }

    interface Presenter extends IPresenter {
        void communityInfoList(String userId, int type, int currentPage, int pageCount);

        void addCommunityInfo(CommunityInfo communityInfo, UpFileInfo upFileInfo);

        void commentInfoList(int nid, int currentPage, int pageCount);

        void addCommentInfo(CommentInfo commentInfo);

        void addAgreeInfo(String userId, String noteId);

        void deleteNote(String userId, String noteId);
    }
}
