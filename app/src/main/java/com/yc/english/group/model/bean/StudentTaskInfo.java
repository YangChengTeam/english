package com.yc.english.group.model.bean;

import java.util.List;

/**
 * Created by wanglin  on 2017/8/9 19:57.
 */

public class StudentTaskInfo {


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

    public class ListBean {
        private List<NoreadListBean> noread_list;

        public List<NoreadListBean> getNoread_list() {
            return noread_list;
        }

        public void setNoread_list(List<NoreadListBean> noread_list) {
            this.noread_list = noread_list;
        }

        public class NoreadListBean {
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
        }
    }

}
