package com.android.newsfeedapp.model;

import java.io.Serializable;

public class NewsFeed implements Serializable {

    private String id;
    private String url;
    private String title;
    private String author;

    public NewsFeed(){

    }

    public NewsFeed(String paramId, String paramTitle, String paramAuthor, String paramUrl) {
        id = paramId;
        url = paramUrl;
        title = paramTitle;
        author = paramAuthor;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getAuthor() {
        return author;
    }

    public void setAuthor(String description) {
        this.author = description;
    }


}
