package com.updateme.model;

import java.util.Date;

public class YoutubePostInfo {
    private String postTitle;

    private String postURL;

    private Integer postViewCount;

    private String postThumbnailUrl;

    private Date postCreatedDate;


    public String getPostTitle() {
        return postTitle;
    }

    public void setPostTitle(String postTitle) {
        this.postTitle = postTitle;
    }

    public String getPostURL() {
        return postURL;
    }

    public void setPostURL(String postURL) {
        this.postURL = postURL;
    }

    public Integer getPostViewCount() {
        return postViewCount;
    }

    public void setPostViewCount(Integer postViewCount) {
        this.postViewCount = postViewCount;
    }

    public String getPostThumbnailUrl() {
        return postThumbnailUrl;
    }

    public void setPostThumbnailUrl(String postThumbnailUrl) {
        this.postThumbnailUrl = postThumbnailUrl;
    }

    public Date getPostCreatedDate() {
        return postCreatedDate;
    }

    public void setPostCreatedDate(Date postCreatedDate) {
        this.postCreatedDate = postCreatedDate;
    }

    @Override
    public String toString() {
        return "YoutubePostInfo{" +
                "postTitle='" + postTitle + '\'' +
                ", postURL='" + postURL + '\'' +
                ", postViewCount=" + postViewCount +
                ", postThumbnailUrl='" + postThumbnailUrl + '\'' +
                ", postCreatedDate=" + postCreatedDate +
                '}';
    }
}
