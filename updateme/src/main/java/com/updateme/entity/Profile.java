package com.updateme.entity;

import javax.persistence.*;

@Entity
@Table(name = "Profile")
public class Profile {

    @Id
    @Column(name = "profileId")
    private Integer profileId;

    @Column(name = "name")
    private String name;

    @Column(name = "title")
    private String title;

    @Column(name = "main_platform")
    private Integer mainPlatform;

    @Column(name = "followerCount")
    private String followerCount;

    @Column(name = "last_active")
    private String lastActive;

    @Column(name = "profile_image")
    private String profileImage;

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

    public String getLastActive() {
        return lastActive;
    }

    public void setLastActive(String lastActive) {
        this.lastActive = lastActive;
    }

    public Integer getMainPlatform() {
        return mainPlatform;
    }

    public String getProfileImage() {
        return profileImage;
    }

    public void setProfileImage(String profileImage) {
        this.profileImage = profileImage;
    }

    public void setMainPlatform(Integer mainPlatform) {
        this.mainPlatform = mainPlatform;
    }

    public Integer getProfileId() {
        return profileId;
    }

    public void setProfileId(Integer profileId) {
        this.profileId = profileId;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }



}
