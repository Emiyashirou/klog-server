package com.klog.model.basic;

import java.util.Date;

public class Post {

    private String id;

    private String workId;

    private String title;

    private Date create_date;

    private Date modify_date;

    private int status;

    private int priority;

    private String content;

    public Post() {}

    public Post(String id, String workId, String title, Date create_date, Date modify_date, int status, int priority, String content) {
        this.id = id;
        this.workId = workId;
        this.title = title;
        this.create_date = create_date;
        this.modify_date = modify_date;
        this.status = status;
        this.priority = priority;
        this.content = content;
    }

    public String getWorkId() {
        return workId;
    }

    public void setWorkId(String workId) {
        this.workId = workId;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public Date getCreate_date() {
        return create_date;
    }

    public void setCreate_date(Date create_date) {
        this.create_date = create_date;
    }

    public Date getModify_date() {
        return modify_date;
    }

    public void setModify_date(Date modify_date) {
        this.modify_date = modify_date;
    }

    public int getStatus() {
        return status;
    }

    public void setStatus(int status) {
        this.status = status;
    }

    public int getPriority() {
        return priority;
    }

    public void setPriority(int priority) {
        this.priority = priority;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }
}
