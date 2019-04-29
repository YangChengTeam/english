package com.yc.junior.english.base.utils;

import android.Manifest;

/**
 * Created by wanglin  on 2019/4/25 13:30.
 */
public class PermissionGroup {


    public enum GroupType {
        CAMERA_GROUP, CALENDAR_GROUP, CONTACT_GROUP, LOCATION_GROUP, MICROPHONE_GROUP, PHONE_GROUP, SENSORS_GROUP, SMS_GROUP, STORAGE_GROUP
    }


    public static String[] getPermissionGroup(GroupType type) {
        String[] strs = null;

        switch (type) {
            case CAMERA_GROUP:
                strs = new String[]{Manifest.permission.CAMERA};
                break;
            case CALENDAR_GROUP:
                strs = new String[]{Manifest.permission.READ_CALENDAR, Manifest.permission.WRITE_CALENDAR};
                break;
            case CONTACT_GROUP:
                strs = new String[]{Manifest.permission.READ_CONTACTS, Manifest.permission.WRITE_CONTACTS, Manifest.permission.GET_ACCOUNTS};
                break;
            case LOCATION_GROUP:
                strs = new String[]{Manifest.permission.ACCESS_FINE_LOCATION, Manifest.permission.ACCESS_COARSE_LOCATION};
                break;
            case MICROPHONE_GROUP:
                strs = new String[]{Manifest.permission.RECORD_AUDIO};
                break;
            case PHONE_GROUP:
                strs = new String[]{
                        Manifest.permission.READ_PHONE_STATE, Manifest.permission.CALL_PHONE, Manifest.permission.READ_CALL_LOG,
                        Manifest.permission.WRITE_CALL_LOG, Manifest.permission.ADD_VOICEMAIL, Manifest.permission.USE_SIP,
                        Manifest.permission.PROCESS_OUTGOING_CALLS};
                break;
            case SENSORS_GROUP:
                strs = new String[]{Manifest.permission.BODY_SENSORS};
                break;
            case SMS_GROUP:
                strs = new String[]{Manifest.permission.SEND_SMS, Manifest.permission.RECEIVE_SMS, Manifest.permission.READ_SMS,
                        Manifest.permission.RECEIVE_WAP_PUSH, Manifest.permission.RECEIVE_MMS};
                break;
            case STORAGE_GROUP:
                strs = new String[]{Manifest.permission.READ_EXTERNAL_STORAGE, Manifest.permission.WRITE_EXTERNAL_STORAGE};
                break;

        }

        return strs;
    }


}
