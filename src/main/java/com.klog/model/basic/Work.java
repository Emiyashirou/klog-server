package com.klog.model.basic;

import java.util.Date;

public class Work {

    private String id;

    private String title;

    private Date create_date;

    private Date modify_date;

    private int status;

    private int priority;

    private String description;

    public Work() {}

    public Work(String id, String title, Date create_date, Date modify_date, int status, int priority, String description){
        this.id = id;
        this.title = title;
        this.create_date = create_date;
        this.modify_date = modify_date;
        this.status = status;
        this.priority = priority;
        this.description = description;
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

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }
}
