package com.klog.model.basic;

import java.util.Date;

public class Comment {

    private String id;

    private String author;

    private Date create_date;

    private Date modify_date;

    private int status;

    private String ref;

    private String content;

    public Comment() {}

    public Comment(String id, String author, Date create_date, Date modify_date, int status, String ref, String content) {
        this.id = id;
        this.author = author;
        this.create_date = create_date;
        this.modify_date = modify_date;
        this.status = status;
        this.ref = ref;
        this.content = content;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getAuthor() {
        return author;
    }

    public void setAuthor(String author) {
        this.author = author;
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

    public String getRef() {
        return ref;
    }

    public void setRef(String ref) {
        this.ref = ref;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }
}
