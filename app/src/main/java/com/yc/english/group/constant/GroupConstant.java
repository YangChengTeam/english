package com.yc.english.group.constant;

/**
 * Created by wanglin  on 2017/7/31 11:05.
 */

public interface GroupConstant {
    /**
     * 好友验证条件
     */
    int CONDITION_ALL_ALLOW = 0;//允许所有人加入
    int CONDITION_VERIFY_JOIN = 1;//通过验证才能加入
    int CONDITION_ALL_FORBID = 2;//不允许任何人加入

    /**
     * 作业类型
     * 0：纯文字，1：图片，2：语音，3：文档，4：综合
     */

    int TASK_TYPE_CHARACTER = 0;
    int TASK_TYPE_PICTURE = 1;
    int TASK_TYPE_VOICE = 2;
    int TASK_TYPE_WORD = 3;
    int TASK_TYPE_SYNTHESIZE = 4;

    /**
     * sp保存的作业key
     */
    String TEXT_TASK = "text_task";
    String PICTUE_TASK = "pictue_task";
    String VOICE_TASK = "voice_task";
    String WORD_TASK = "word_task";

    String CLASS_INFO = "class_info";

    String GROUP_INFO = "group_info";

    String ALL_GROUP_FORBID_STATE = "ALL_GROUP_FORBID_STATE";

    String FORBID_MEMBER = "forbid_member";


    String REGEX_NAME = "^[a-zA-Z0-9\\u4e00-\\u9fa5]+$";

    String KEYWORD = "该消息违反国家规定，不能发送";

    String REGEX_SPECIAL_WORD = "[`~!@#$%^&*()+=|{}':;',\\[\\].<>/?~！@#￥%……& amp;*（）——+|{}【】‘；：”“’。，、？]";

    String FORBID_CONTENT = "该群开启了全员禁言，请稍候在发言...";

    String appid = "wx675cae9b4a8b26b0";
    String originid = "gh_5732adc868b8";

    String assistant_originid = "gh_ae8f66b61fcb";//英语学习助手小程序

    String tool_originid = "gh_8aa3ea375bde";//学习工具小程序

    String EVERY_DAY_DIALOG = "every_day_dialog";//每天弹窗key

    String SOUND_INFO = "sound_info";


    //  wx12cb08c1129462cd
    //  gh_5732adc868b8

}
