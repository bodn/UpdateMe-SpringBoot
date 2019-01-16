package com.updateme.model.RetrievePostRs;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonPropertyOrder;
import com.updateme.entity.PostInfo;

import java.util.Date;
import java.util.Objects;


@JsonPropertyOrder({"id, postId, platformId, postTitle, postViews, postUrl, postThumbnail, postCreated"})
public class PostInfoResponse {

    @JsonProperty("id")
    private Integer profileId;

    @JsonProperty("postId")
    private Integer postId;

    @JsonProperty("platformId")
    private Integer platformId;

    @JsonProperty("postTitle")
    private String postTitle;

    @JsonProperty("postViews")
    private String postViews;

    @JsonProperty("postUrl")
    private String postUrl;

    @JsonProperty("postThumbnail")
    private String postThumbnail;

    @JsonProperty("postCreated")
    private Date postCreated;

    public Integer getProfileId() {
        return profileId;
    }

    public void setProfileId(Integer profileId) {
        this.profileId = profileId;
    }

    public Integer getPostId() {
        return postId;
    }

    public void setPostId(Integer postId) {
        this.postId = postId;
    }

    public Integer getPlatformId() {
        return platformId;
    }

    public void setPlatformId(Integer platformId) {
        this.platformId = platformId;
    }

    public String getPostTitle() {
        return postTitle;
    }

    public void setPostTitle(String postTitle) {
        this.postTitle = postTitle;
    }

    public String getPostViews() {
        return postViews;
    }

    public void setPostViews(String postViews) {
        this.postViews = postViews;
    }

    public String getPostUrl() {
        return postUrl;
    }

    public void setPostUrl(String postUrl) {
        this.postUrl = postUrl;
    }

    public String getPostThumbnail() {
        return postThumbnail;
    }

    public void setPostThumbnail(String postThumbnail) {
        this.postThumbnail = postThumbnail;
    }

    public Date getPostCreated() {
        return postCreated;
    }

    public void setPostCreated(Date postCreated) {
        this.postCreated = postCreated;
    }


}
