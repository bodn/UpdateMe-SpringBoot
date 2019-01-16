package com.updateme.entity;

import javax.persistence.*;
import java.io.Serializable;

@Entity
@Table(name = "Profile_Platform")
public class ProfilePlatform{

    @Column(name = "profileId")
    private Integer profileId;

    @Column(name = "platformId")
    private Integer platformId;

    @Column(name = "platform_key")
    private String platformKey;

    @Id
    @Column(name = "recId")
    private Integer recId;

    public Integer getProfileId() {
        return profileId;
    }

    public void setProfileId(Integer profileId) {
        this.profileId = profileId;
    }

    public Integer getPlatformId() {
        return platformId;
    }

    public void setPlatformId(Integer platformId) {
        this.platformId = platformId;
    }

    public Integer getRecId() {
        return recId;
    }

    public void setRecId(Integer recId) {
        this.recId = recId;
    }

    public String getPlatformKey() {
        return platformKey;
    }

    public void setPlatformKey(String platformKey) {
        this.platformKey = platformKey;
    }
}
