package com.yc.english.group.model.bean;

import java.util.List;

/**
 * Created by wanglin  on 2017/7/28 09:05.
 * 作业信息
 */

public class TaskAllInfoWrapper {

    private List<TaskAllInfo> list;

    public List<TaskAllInfo> getList() {
        return list;
    }

    public void setList(List<TaskAllInfo> list) {
        this.list = list;
    }

    public  class TaskAllInfo {
        /**
         * add_date : 20170810
         * add_time : 1502335878
         * body : {"docs":[""],"imgs":[""],"voices":[""]}
         * class_id : 110
         * desp : com
         * id : 4
         * is_del : 0
         * is_read : 1
         * score :
         * task_id : 59
         * user_id : 16
         */

        private String add_date;
        private String add_time;
        private String add_week;
        private List<BodyBean> body;
        private String class_id;
        private String desp;
        private String id;
        private String is_del;
        private String is_read;
        private String score;
        private String task_id;
        private String user_id;
        private int type;
        private String publisher;

        public String getAdd_date() {
            return add_date;
        }

        public void setAdd_date(String add_date) {
            this.add_date = add_date;
        }

        public String getAdd_time() {
            return add_time;
        }

        public void setAdd_time(String add_time) {
            this.add_time = add_time;
        }

        public List<BodyBean> getBody() {
            return body;
        }

        public void setBody(List<BodyBean> body) {
            this.body = body;
        }

        public String getClass_id() {
            return class_id;
        }

        public void setClass_id(String class_id) {
            this.class_id = class_id;
        }

        public String getDesp() {
            return desp;
        }

        public void setDesp(String desp) {
            this.desp = desp;
        }

        public String getId() {
            return id;
        }

        public void setId(String id) {
            this.id = id;
        }

        public String getIs_del() {
            return is_del;
        }

        public void setIs_del(String is_del) {
            this.is_del = is_del;
        }

        public String getIs_read() {
            return is_read;
        }

        public void setIs_read(String is_read) {
            this.is_read = is_read;
        }

        public String getScore() {
            return score;
        }

        public void setScore(String score) {
            this.score = score;
        }

        public String getTask_id() {
            return task_id;
        }

        public void setTask_id(String task_id) {
            this.task_id = task_id;
        }

        public String getPublisher() {
            return publisher;
        }

        public void setPublisher(String publisher) {
            this.publisher = publisher;
        }

        public String getUser_id() {
            return user_id;
        }

        public void setUser_id(String user_id) {
            this.user_id = user_id;
        }

        public String getAdd_week() {
            return add_week;
        }

        public void setAdd_week(String add_week) {
            this.add_week = add_week;
        }

        public int getType() {
            return type;
        }

        public void setType(int type) {
            this.type = type;
        }

        public  class BodyBean {
            private List<String> docs;
            private List<String> imgs;
            private List<String> voices;

            public List<String> getDocs() {
                return docs;
            }

            public void setDocs(List<String> docs) {
                this.docs = docs;
            }

            public List<String> getImgs() {
                return imgs;
            }

            public void setImgs(List<String> imgs) {
                this.imgs = imgs;
            }

            public List<String> getVoices() {
                return voices;
            }

            public void setVoices(List<String> voices) {
                this.voices = voices;
            }
        }
    }

}
