package com.yc.junior.english.read.model.domain;

import com.yc.english.base.model.Config;

/**
 * Created by zhangkai on 2017/8/1.
 */

public class URLConfig {
    public static final boolean DEBUG = Config.DEBUG;

    private static String baseUrl = "http://en.wk2.com/api/";
    private static String debugBaseUrl = "http://en.qqtn.com/api/";

    public static final String BOOK_INFO_URL = getBaseUrl() + "book/book_info";

    public static final String BOOK_LIST_URL = getBaseUrl() + "book/book_list";

    public static final String GRADE_LIST_URL = getBaseUrl() + "book/grade_list";

    public static final String COURSE_VERSION_LIST_URL = getBaseUrl() + "book/version_list";

    public static final String COURSE_LIST_URL = getBaseUrl() + "book/course_list";

    public static final String SENTENCE_LIST_URL = getBaseUrl() + "book/sentence_list";

    public static final String WORD_UNIT_LIST_URL = getBaseUrl() + "book/unit_list";

    public static final String WORD_LIST_URL = getBaseUrl() + "book/words_list";

    public static final String NOTE_LIST_URL = getBaseUrl() + "note/notelist";

    public static final String ADD_NOTE_URL = getBaseUrl() + "note/add";

    public static final String FOLLOW_LIST_URL = getBaseUrl() + "note/followlist";

    public static final String ADD_COMMENT_URL = getBaseUrl() + "note/follow";

    public static final String ADD_AGREE_URL = getBaseUrl() + "note/agree";

    public static final String DELETE_NOTE_URL = getBaseUrl() + "note/delNote";

    public static String getBaseUrl() {
        return (DEBUG ? debugBaseUrl : baseUrl);
    }

}
