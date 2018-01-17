package com.yc.junior.english.community.contract;

import com.kk.securityhttp.net.entry.UpFileInfo;
import com.yc.junior.english.base.presenter.IPresenter;
import com.yc.junior.english.base.view.IDialog;
import com.yc.junior.english.base.view.IFinish;
import com.yc.junior.english.base.view.ILoading;
import com.yc.junior.english.base.view.INoData;
import com.yc.junior.english.base.view.INoNet;
import com.yc.junior.english.base.view.IView;
import com.yc.junior.english.community.model.domain.CommentInfo;
import com.yc.junior.english.community.model.domain.CommunityInfo;

import java.util.List;

/**
 * Created by zhangkai on 2017/7/25.
 */

public interface CommunityInfoContract {
    interface View extends IView, IDialog, IFinish, ILoading, INoData, INoNet {
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
