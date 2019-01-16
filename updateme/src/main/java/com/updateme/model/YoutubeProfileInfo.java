package com.updateme.model;

public class YoutubeProfileInfo {

    private String subscriberCount;

    private String profileImage;

    @Override
    public String toString() {
        return "YoutubeProfileInfo{" +
                "subscriberCount='" + subscriberCount + '\'' +
                ", profileImage='" + profileImage + '\'' +
                '}';
    }

    public String getSubscriberCount() {
        return subscriberCount;
    }

    public void setSubscriberCount(String subscriberCount) {
        this.subscriberCount = subscriberCount;
    }

    public String getProfileImage() {
        return profileImage;
    }

    public void setProfileImage(String profileImage) {
        this.profileImage = profileImage;
    }
}
