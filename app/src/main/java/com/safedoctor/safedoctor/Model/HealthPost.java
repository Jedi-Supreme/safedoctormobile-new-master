package com.safedoctor.safedoctor.Model;


import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.List;

public class HealthPost {
    @SerializedName("data")
    @Expose
    private Data data;

    public Data getData() {
        return data;
    }

    public void setData(Data data) {
        this.data = data;
    }

public class Content {

        @SerializedName("libid")
        @Expose
        private Integer libid;
        @SerializedName("links")
        @Expose
        private String links;
        @SerializedName("title")
        @Expose
        private String title;
        @SerializedName("createuserid")
        @Expose
        private String createuserid;
        @SerializedName("createtime")
        @Expose
        private String createtime;
        @SerializedName("updateuserid")
        @Expose
        private Object updateuserid;
        @SerializedName("updatetime")
        @Expose
        private Object updatetime;

        public Integer getLibid() {
            return libid;
        }

        public void setLibid(Integer libid) {
            this.libid = libid;
        }

        public String getLinks() {
            return links;
        }

        public void setLinks(String links) {
            this.links = links;
        }

        public String getTitle() {
            return title;
        }

        public void setTitle(String title) {
            this.title = title;
        }

        public String getCreateuserid() {
            return createuserid;
        }

        public void setCreateuserid(String createuserid) {
            this.createuserid = createuserid;
        }

        public String getCreatetime() {
            return createtime;
        }

        public void setCreatetime(String createtime) {
            this.createtime = createtime;
        }

        public Object getUpdateuserid() {
            return updateuserid;
        }

        public void setUpdateuserid(Object updateuserid) {
            this.updateuserid = updateuserid;
        }

        public Object getUpdatetime() {
            return updatetime;
        }

        public void setUpdatetime(Object updatetime) {
            this.updatetime = updatetime;
        }

    }

    public class Data {

        @SerializedName("content")
        @Expose
        private List<Content> content = null;

        public List<Content> getContent() {
            return content;
        }

        public void setContent(List<Content> content) {
            this.content = content;
        }

    }
}
