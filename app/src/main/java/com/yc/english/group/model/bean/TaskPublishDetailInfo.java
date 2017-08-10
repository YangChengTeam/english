package com.yc.english.group.model.bean;

/**
 * Created by wanglin  on 2017/8/9 10:45.
 * 老师查看发布作业详情
 */

public class TaskPublishDetailInfo {

    /**
     * info : {"add_date":"20170807","add_time":"1502072326","class_id":"45","desp":"测试","id":"1","is_del":"0","publisher":"1","task_id":"1","title":"","type":"0"}
     */

    private InfoBean info;

    public InfoBean getInfo() {
        return info;
    }

    public void setInfo(InfoBean info) {
        this.info = info;
    }

    public class InfoBean {
        /**
         * add_date : 20170807
         * add_time : 1502072326
         * class_id : 45
         * desp : 测试
         * id : 1
         * is_del : 0
         * publisher : 1
         * task_id : 1
         * title :
         * type : 0  0：纯文字，1：图片，2：语音，3：文档，4：综合
         */

        private String add_date;
        private String add_time;
        private String class_id;
        private String desp;
        private String id;
        private String is_del;
        private String publisher;
        private String task_id;
        private String title;
        private String type;
        private String add_week;

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

        public String getPublisher() {
            return publisher;
        }

        public void setPublisher(String publisher) {
            this.publisher = publisher;
        }

        public String getTask_id() {
            return task_id;
        }

        public void setTask_id(String task_id) {
            this.task_id = task_id;
        }

        public String getTitle() {
            return title;
        }

        public void setTitle(String title) {
            this.title = title;
        }

        public String getType() {
            return type;
        }

        public void setType(String type) {
            this.type = type;
        }

        public String getAdd_week() {
            return add_week;
        }

        public void setAdd_week(String add_week) {
            this.add_week = add_week;
        }
    }

}
