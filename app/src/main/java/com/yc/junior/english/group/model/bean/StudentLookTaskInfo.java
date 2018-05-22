package com.yc.junior.english.group.model.bean;

import android.os.Parcel;
import android.os.Parcelable;

import java.util.ArrayList;

/**
 * Created by wanglin  on 2017/8/9 19:57.
 */

public class StudentLookTaskInfo {


    /**
     * list : {"noread_list":[{"is_read":false,"nick_name":"默默","user_id":"15","user_name":"17702719117"},{"is_read":false,"nick_name":"监控","user_id":"16","user_name":"18872922735"}]}
     */

    private ListBean list;

    public ListBean getList() {
        return list;
    }

    public void setList(ListBean list) {
        this.list = list;
    }

    public static class ListBean {
        private ArrayList<NoreadListBean> noread_list;//未查阅
        private ArrayList<NoreadListBean> read_list;//已查阅


        public ArrayList<NoreadListBean> getNoread_list() {
            return noread_list;
        }

        public void setNoread_list(ArrayList<NoreadListBean> noread_list) {
            this.noread_list = noread_list;
        }

        public ArrayList<NoreadListBean> getRead_list() {
            return read_list;
        }

        public void setRead_list(ArrayList<NoreadListBean> read_list) {
            this.read_list = read_list;
        }

        public static class NoreadListBean implements Parcelable {
            public NoreadListBean() {
            }

            /**
             * is_read : false
             * nick_name : 默默
             * user_id : 15
             * user_name : 17702719117
             */


            private boolean is_read;
            private String nick_name;
            private String user_id;
            private String user_name;
            private String user_face;
            private String read_time;


            public NoreadListBean(Parcel source) {
                nick_name = source.readString();
                user_id = source.readString();
                user_name = source.readString();
                is_read = source.readByte() != 0;
                user_face = source.readString();
                read_time = source.readString();
            }

            public boolean isIs_read() {
                return is_read;
            }

            public void setIs_read(boolean is_read) {
                this.is_read = is_read;
            }

            public String getNick_name() {
                return nick_name;
            }

            public void setNick_name(String nick_name) {
                this.nick_name = nick_name;
            }

            public String getUser_id() {
                return user_id;
            }

            public void setUser_id(String user_id) {
                this.user_id = user_id;
            }

            public String getUser_name() {
                return user_name;
            }

            public void setUser_name(String user_name) {
                this.user_name = user_name;
            }

            public String getUser_face() {
                return user_face;
            }

            public void setUser_face(String user_face) {
                this.user_face = user_face;
            }

            public boolean is_read() {
                return is_read;
            }

            public String getRead_time() {
                return read_time;
            }

            public void setRead_time(String read_time) {
                this.read_time = read_time;
            }

            @Override
            public int describeContents() {
                return 0;
            }

            @Override
            public void writeToParcel(Parcel dest, int flags) {
                dest.writeString(nick_name);
                dest.writeString(user_id);
                dest.writeString(user_name);
                dest.writeByte((byte) (is_read ? 1 : 0));
                dest.writeString(user_face);
                dest.writeString(read_time);

            }

            public static final Creator<NoreadListBean> CREATOR = new Creator<NoreadListBean>() {

                @Override
                public NoreadListBean createFromParcel(Parcel source) {
                    return new NoreadListBean(source);
                }

                @Override
                public NoreadListBean[] newArray(int size) {
                    return new NoreadListBean[size];
                }
            };
        }
    }

}
