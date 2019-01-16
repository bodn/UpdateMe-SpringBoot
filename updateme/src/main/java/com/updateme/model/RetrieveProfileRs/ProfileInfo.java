package com.updateme.model.RetrieveProfileRs;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonPropertyOrder;

import java.util.List;

@JsonPropertyOrder({"id","name", "title","mainPlatform", "followerCount", "platforms","streamStats", "profileImage", "lastActive"})
public class ProfileInfo {

    @JsonProperty("id")
    private Integer id;

    @JsonProperty("name")
    private String name;

    @JsonProperty("title")
    private String title;

    @JsonProperty("mainPlatform")
    private String mPlatform;

    @JsonProperty("followerCount")
    private String followerCount;

    @JsonProperty("lastActive")
    private String lastModified;

    @JsonProperty("profileImage")
    private String profileImage;

    @JsonProperty("platforms")
    private List<String> platforms;

    public List<String> getPlatforms() {
        return platforms;
    }

    public void setPlatforms(List<String> platforms) {
        this.platforms = platforms;
    }

    public String getProfileImage() {
        return profileImage;
    }

    public void setProfileImage(String profileImage) {
        this.profileImage = profileImage;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getFollowerCount() {
        return followerCount;
    }

    public void setFollowerCount(String followerCount) {
        this.followerCount = followerCount;
    }

    public String getLastModified() {
        return lastModified;
    }

    public void setLastModified(String lastModified) {
        this.lastModified = lastModified;
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getmPlatform() {
        return mPlatform;
    }

    public void setmPlatform(String mPlatform) {
        this.mPlatform = mPlatform;
    }
}
