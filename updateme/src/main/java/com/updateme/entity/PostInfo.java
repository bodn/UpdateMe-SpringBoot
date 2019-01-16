package com.updateme.entity;

import javax.persistence.*;
import java.util.Date;
import java.util.Objects;


@Entity
@Table(name = "Post_Info")
public class PostInfo {

    @Column(name = "profile_id")
    private Integer profileId;

    @Id
    @GeneratedValue(strategy= GenerationType.IDENTITY)
    @Column(name = "post_id")
    private Integer postId;

    @Column(name = "platform_id")
    private Integer platformId;

    @Column(name = "post_title")
    private String postTitle;

    @Column(name = "post_views")
    private String postViews;

    @Column(name = "post_url")
    private String postUrl;

    @Column(name = "post_thumbnail_url")
    private String postThumbnail;

    @Column(name = "post_created_at")
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

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof PostInfo)) return false;
        PostInfo postInfo = (PostInfo) o;
        return Objects.equals(getProfileId(), postInfo.getProfileId()) &&
                Objects.equals(getPlatformId(), postInfo.getPlatformId()) &&
               // Objects.equals(getPostTitle(), postInfo.getPostTitle()) &&
                Objects.equals(getPostUrl(), postInfo.getPostUrl()) &&
                Objects.equals(getPostThumbnail(), postInfo.getPostThumbnail()) &&
                Objects.equals(getPostCreated(), postInfo.getPostCreated());
    }

    @Override
    public int hashCode() {
        return Objects.hash(getProfileId(), getPlatformId(), getPostTitle(), getPostUrl(), getPostThumbnail(), getPostCreated());
    }

    @Override
    public String toString() {
        return "PostInfo{" +
                "profileId=" + profileId +
                ", postId=" + postId +
                ", platformId=" + platformId +
                ", postTitle='" + postTitle + '\'' +
                ", postViews='" + postViews + '\'' +
                ", postUrl='" + postUrl + '\'' +
                ", postThumbnail='" + postThumbnail + '\'' +
                ", postCreated=" + postCreated +
                '}';
    }
}
