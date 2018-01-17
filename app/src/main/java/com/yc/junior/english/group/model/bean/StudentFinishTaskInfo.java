package com.yc.junior.english.group.model.bean;

import android.os.Parcel;
import android.os.Parcelable;

import java.util.ArrayList;

/**
 * Created by wanglin  on 2017/8/9 19:57.
 */

public class StudentFinishTaskInfo {


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
        private ArrayList<NoDoneListBean> nodone_list;//未完成
        private ArrayList<NoDoneListBean> done_list;//已查阅


        public ArrayList<NoDoneListBean> getNodone_list() {
            return nodone_list;
        }

        public void setNodone_list(ArrayList<NoDoneListBean> nodone_list) {
            this.nodone_list = nodone_list;
        }

        public ArrayList<NoDoneListBean> getDone_list() {
            return done_list;
        }

        public void setDone_list(ArrayList<NoDoneListBean> done_list) {
            this.done_list = done_list;
        }

        public static class NoDoneListBean implements Parcelable {
            public NoDoneListBean() {
            }

            /**
             * is_read : false
             * nick_name : 默默
             * user_id : 15
             * user_name : 17702719117
             */


            private boolean is_done;
            private String nick_name;
            private String user_id;
            private String user_name;
            private String user_face;
            private String done_time;
            private String done_date;
            private String done_id;

            public NoDoneListBean(Parcel source) {
                nick_name = source.readString();
                user_id = source.readString();
                user_name = source.readString();
                is_done = source.readByte() != 0;
                user_face = source.readString();
                done_date = source.readString();
                done_time = source.readString();
                done_id = source.readString();
            }

            public boolean is_done() {
                return is_done;
            }

            public void setIs_done(boolean is_done) {
                this.is_done = is_done;
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

            public String getDone_time() {
                return done_time;
            }

            public void setDone_time(String done_time) {
                this.done_time = done_time;
            }

            public String getDone_date() {
                return done_date;
            }

            public void setDone_date(String done_date) {
                this.done_date = done_date;
            }

            public String getDone_id() {
                return done_id;
            }

            public void setDone_id(String done_id) {
                this.done_id = done_id;
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
                dest.writeByte((byte) (is_done ? 1 : 0));
                dest.writeString(user_face);
                dest.writeString(done_date);
                dest.writeString(done_time);
                dest.writeString(done_id);

            }

            public static final Creator<NoDoneListBean> CREATOR = new Creator<NoDoneListBean>() {

                @Override
                public NoDoneListBean createFromParcel(Parcel source) {
                    return new NoDoneListBean(source);
                }

                @Override
                public NoDoneListBean[] newArray(int size) {
                    return new NoDoneListBean[size];
                }
            };
        }
    }

}
